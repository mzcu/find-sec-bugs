package org.springframework.web.bind.annotation;

public @interface PatchMapping {

    String value() default "";

    String[] consumes() default {};
}
