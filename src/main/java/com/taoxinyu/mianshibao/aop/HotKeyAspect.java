package com.taoxinyu.mianshibao.aop;

import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import com.taoxinyu.mianshibao.annotation.HotKey;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class HotKeyAspect {
    private static final Logger log = LoggerFactory.getLogger(HotKeyAspect.class);

    // 缓存：参数类型 -> ID方法（减少反射开销）
    private final Map<Class<?>, Method> idMethodCache = new ConcurrentHashMap<>();

    /**
     * 切点：拦截所有标注@HotKey注解的方法
     */
    @Pointcut("@annotation(hotKey)")
    public void hotKeyPointcut(HotKey hotKey) {
    }

    /**
     * 环绕通知：处理热键缓存逻辑
     */
    @Around("hotKeyPointcut(hotKey)")
    public Object around(ProceedingJoinPoint joinPoint, HotKey hotKey) throws Throwable {
        // 获取被拦截的方法信息（用于异常提示）
        String methodInfo = getMethodInfo(joinPoint);

        // 1. 校验参数索引合法性
        Object[] args = joinPoint.getArgs();
        int paramIndex = hotKey.requestParamIndex();
        if (paramIndex < 0 || paramIndex >= args.length) {
            throw new IllegalArgumentException(
                    String.format("方法[%s]的@HotKey注解requestParamIndex无效，参数索引[%d]超出范围（实际参数数量：%d）",
                            methodInfo, paramIndex, args.length)
            );
        }

        // 2. 获取请求对象并校验非空
        Object requestObj = args[paramIndex];
        if (requestObj == null) {
            throw new IllegalArgumentException(
                    String.format("方法[%s]的@HotKey注解指定参数（索引：%d）为null，无法提取ID",
                            methodInfo, paramIndex)
            );
        }

        // 3. 反射获取ID方法（优先从缓存获取）
        String idMethodName = hotKey.idMethod();
        Method idMethod = getCachedIdMethod(requestObj.getClass(), idMethodName, methodInfo);

        // 4. 调用方法获取ID并校验有效性
        Object idObj = invokeIdMethod(idMethod, requestObj, idMethodName, methodInfo);
        Long id = parseIdToLong(idObj, idMethodName, methodInfo);

        // 5. 热键缓存逻辑（带容错处理）
        String key = hotKey.prefix() + id;
        Object result = null;
        try {
            // 检查是否为热键且存在缓存
            if (JdHotKeyStore.isHotKey(key)) {
                Object cachedValue = JdHotKeyStore.get(key);
                if (cachedValue != null) {
                    log.debug("方法[{}]命中热键缓存，key：{}", methodInfo, key);
                    return cachedValue;
                }
            }
        } catch (Exception e) {
            log.warn("方法[{}]热键缓存检查异常，继续执行原方法（key：{}）", methodInfo, key, e);
        }

        // 执行原方法
        result = joinPoint.proceed();

        // 缓存结果（带容错处理）
        try {
            JdHotKeyStore.smartSet(key, result);
            log.debug("方法[{}]执行结果已缓存，key：{}", methodInfo, key);
        } catch (Exception e) {
            log.warn("方法[{}]热键缓存设置异常（key：{}）", methodInfo, key, e);
        }

        return result;
    }

    /**
     * 获取被拦截方法的完整信息（类名.方法名）
     */
    private String getMethodInfo(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
    }

    /**
     * 从缓存获取ID方法，未命中则反射获取并缓存
     */
    private Method getCachedIdMethod(Class<?> paramClass, String idMethodName, String methodInfo) {
        return idMethodCache.computeIfAbsent(paramClass, cls -> {
            try {
                return cls.getMethod(idMethodName);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(
                        String.format("方法[%s]的参数对象[%s]不存在无参方法：%s",
                                methodInfo, cls.getSimpleName(), idMethodName), e
                );
            }
        });
    }

    /**
     * 调用ID方法并处理异常
     */
    private Object invokeIdMethod(Method idMethod, Object requestObj, String idMethodName, String methodInfo) {
        try {
            return idMethod.invoke(requestObj);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("方法[%s]调用参数对象的%s()失败",
                            methodInfo, idMethodName), e
            );
        }
    }

    /**
     * 将ID转换为Long类型（支持Number子类）
     */
    private Long parseIdToLong(Object idObj, String idMethodName, String methodInfo) {
        if (idObj == null) {
            throw new IllegalArgumentException(
                    String.format("方法[%s]的参数对象%s()返回null，无法生成热键",
                            methodInfo, idMethodName)
            );
        }
        if (!(idObj instanceof Number)) {
            throw new ClassCastException(
                    String.format("方法[%s]的参数对象%s()返回值类型错误，需要数字类型（如Long/Integer），实际是：%s",
                            methodInfo, idMethodName, idObj.getClass().getSimpleName())
            );
        }
        // 统一转为Long（支持Integer、Short等Number子类）
        return ((Number) idObj).longValue();
    }
}