package com.goldenduo.freearthook.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface HookClass {
    String value();
    /**
     * @return min api included
     */
    int minApi() default 0;

    /**
     * @return max api included
     */
    int maxApi() default Integer.MAX_VALUE;
}
