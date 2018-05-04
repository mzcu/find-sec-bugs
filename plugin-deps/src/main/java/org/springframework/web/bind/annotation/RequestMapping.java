package org.springframework.web.bind.annotation;


public @interface RequestMapping {
    String value() default "";

    RequestMethod[] method() default {};

    String[] consumes() default {};
}
