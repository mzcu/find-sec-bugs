package org.springframework.web.bind.annotation;

public @interface PostMapping {

    String value() default "";

    String[] consumes() default {};

}
