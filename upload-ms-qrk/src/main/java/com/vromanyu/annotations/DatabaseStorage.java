package com.vromanyu.annotations;

import jakarta.inject.Qualifier;

import java.lang.annotation.*;

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD, ElementType.TYPE})
public @interface DatabaseStorage {
}
