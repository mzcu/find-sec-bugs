/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.spring;


import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.bcel.AnnotationDetector;
import org.apache.bcel.classfile.ArrayElementValue;
import org.apache.bcel.classfile.ElementValue;

import java.util.*;

public class SpringUnvalidatedContentTypeDetector extends AnnotationDetector {
    private static final String BUG_PATTERN_TYPE = "SPRING_CONTENT_TYPE_UNVALIDATED";
    private static final String CONDITIONALLY_MATCHING_ANNOTATION_TYPE =
            "org.springframework.web.bind.annotation.RequestMapping";
    private static final List<String> MATCHING_ANNOTATION_TYPES = Arrays.asList(
            "org.springframework.web.bind.annotation.PostMapping",
            "org.springframework.web.bind.annotation.PutMapping",
            "org.springframework.web.bind.annotation.PatchMapping",
            "org.springframework.web.bind.annotation.DeleteMapping"
    );
    private static final Set<String> MATCHING_HTTP_METHODS =
            new HashSet<>(Arrays.asList("POST", "PUT", "PATCH", "DELETE"));

    private BugReporter reporter;

    public SpringUnvalidatedContentTypeDetector(BugReporter bugReporter) {
        this.reporter = bugReporter;
    }

    @Override
    public void visitAnnotation(String annotationClass, Map<String, ElementValue> map, boolean runtimeVisible) {

        boolean stateChangingMethod = false;

        if (MATCHING_ANNOTATION_TYPES.contains(annotationClass)) {

            stateChangingMethod = true;

        } else if (CONDITIONALLY_MATCHING_ANNOTATION_TYPE.equals(annotationClass)) {

            ElementValue[] methodValue = getElementValues("method", map);
            stateChangingMethod = Arrays.stream(methodValue)
                    .map(ElementValue::toShortString)
                    .anyMatch(MATCHING_HTTP_METHODS::contains);

        }

        if (stateChangingMethod && getElementValues("consumes", map).length == 0) {
            BugInstance bug = new BugInstance(this, BUG_PATTERN_TYPE, Priorities.NORMAL_PRIORITY);
            bug.addClassAndMethod(getThisClass(), getMethod());
            reporter.reportBug(bug);
        }

    }


    private ElementValue[] getElementValues(String param, Map<String, ElementValue> values) {
        Optional<ElementValue> maybeElementValue = Optional.ofNullable(values.get(param));
        return maybeElementValue.map(ArrayElementValue.class::cast)
                .map(ArrayElementValue::getElementValuesArray)
                .orElse(new ElementValue[0]);
    }


}
