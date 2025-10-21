package com.taoxinyu.mianshibao.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 京东热键缓存注解
 * 自动实现与手动逻辑一致的热键判定、缓存读写
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HotKey {

    /**
     * 热键前缀
     */
    String prefix();
    /**
     * 从请求对象中提取ID的方法名
     */
    String idMethod() default "getId";

    /**
     * 方法参数中请求对象的索引
     */
    int requestParamIndex() default 0;
}
