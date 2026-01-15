package com.wulb2018.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public interface BaseEnum<T> {

    default T getCode() {
        return EnumPool.getEnum(this).getCode();
    }

    default String getText() {
        return EnumPool.getEnum(this).getText();
    }

    default void init(T code, String text) {
        EnumPool.putEnum(this, code, text);
    }

    static <T, R extends BaseEnum<T>> R getByCode(Class<? extends BaseEnum<T>> clazz, T code) {
        return Stream.of(clazz.getEnumConstants())
                .filter((BaseEnum<T> e) -> (e.getCode().equals(code)))
                .map(v -> (R) v)
                .findAny()
                .orElse(null);
    }

    static <T> List<BaseEnum<T>> getAll(Class<? extends BaseEnum<T>> clazz) {
        Map<String, Field> fieldCache = Arrays.stream(clazz.getDeclaredFields())
                .filter(Field::isEnumConstant)
                .collect(Collectors.toMap(Field::getName, Function.identity()));
        BaseEnum<T>[] allEnum = clazz.getEnumConstants();
        return Stream.of(allEnum)
                .filter(e -> !fieldCache.get(((Enum<?>) e).name()).isAnnotationPresent(Deprecated.class))
                .map(EnumPool::getEnum)
                .collect(Collectors.toList());
    }


    @SuppressWarnings("all")
    class EnumPool {
        private static final Map<BaseEnum, EnumBean> ENUM_MAP = new ConcurrentHashMap<>();

        static <T> void putEnum(BaseEnum<T> baseEnum, T code, String text) {
            ENUM_MAP.put(baseEnum, new EnumBean<>(code, text));
        }

        static <K extends BaseEnum<T>, T> EnumBean<T> getEnum(K baseEnum) {
            return ENUM_MAP.get(baseEnum);
        }

    }

}
