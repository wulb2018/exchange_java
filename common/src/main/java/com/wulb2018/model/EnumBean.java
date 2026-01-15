package com.wulb2018.model;

import lombok.Data;


@Data
public class EnumBean<T> implements BaseEnum<T> {
    private final T code;
    private final String text;
}
