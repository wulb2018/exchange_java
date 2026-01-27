package com.wulb2018.common.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.google.common.collect.Maps;
import com.wulb2018.common.exception.GlobalExceptionHandler;
import com.wulb2018.common.handler.ApiResponseWrapperReturnValueHandler;
import com.wulb2018.common.model.BaseEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


@Configuration
@Import({GlobalExceptionHandler.class})
@Slf4j
public class WebAutoConfiguration implements WebMvcConfigurer {


    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String TIME_FORMAT = "HH:mm:ss";

    @Bean
    public FilterRegistrationBean<RequestContextFilter> requestContextFilterRegistration() {
        FilterRegistrationBean<RequestContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestContextFilter());
        registration.setUrlPatterns(Arrays.asList("/*"));
        // 设置过滤器优先级，该值越小越优先被执行
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    /**
     * 接收参数为bean时使用jackson解析，需要配置json的转换器
     *
     * @return
     */
    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        //todo 有空看一下 Map<OrderSide, List<OrderFeign>> 的OrderSide为什么不会走这个
        SimpleModule simpleModule = new SimpleModule();
        SimpleDeserializersWrapper deserializers = new SimpleDeserializersWrapper();
        deserializers.addDeserializer(BaseEnum.class, new BaseEnumDeserializer());
        simpleModule.setDeserializers(deserializers);
        simpleModule.addSerializer(BaseEnum.class, new BaseEnumSerializer());
        return builder -> builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer())
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer())
                .serializerByType(Long.class, ToStringSerializer.instance)
                .modulesToInstall(simpleModule);
    }

//    @Bean
//    public Decoder feignDecoder(ObjectProvider<HttpMessageConverters> messageConverters) {
//        return new OptionalDecoder((new ResponseEntityDecoder(new ApiResponseDecoder(new SpringDecoder(messageConverters)))));
//    }

    @Bean
    public ApiResponseWrapperReturnValueHandler apiResponseWrapperReturnValueHandler(
            ObjectProvider<RequestMappingHandlerAdapter> requestMappingHandlerAdapter) {
        RequestMappingHandlerAdapter adapter = requestMappingHandlerAdapter.getIfUnique();
        return new ApiResponseWrapperReturnValueHandler(adapter);
    }

    /**
     * Controller接收非bean类型参数转换器
     */
    @Component
    public static class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

        @Override
        public LocalDateTime convert(@NonNull String source) {
            if (StringUtils.isBlank(source)) {
                return null;
            }
            if (source != null && source.length() == DATETIME_FORMAT.length() && source.contains(" ")) {
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DATETIME_FORMAT));
            }
            Instant instant = Instant.ofEpochMilli(Long.valueOf(source));
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
    }


    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime dateTime, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            if (dateTime != null) {
                ZoneId zone = ZoneId.systemDefault();
                Instant instant = dateTime.atZone(zone).toInstant();
                jsonGenerator.writeNumber(instant.toEpochMilli());
            }
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException, JacksonException {
            long timestamp = jsonParser.getValueAsLong();
            Instant instant = Instant.ofEpochMilli(timestamp);
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
    }

    public static class BaseEnumSerializer extends JsonSerializer<BaseEnum> {
        @Override
        public void serialize(BaseEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            //todo 序列化和反序列化有空重新设计
//            gen.writeStartObject();
//            gen.writeFieldName("code");
            gen.writeObject(value.getCode());
//            gen.writeFieldName("text");
//            gen.writeString(value.getText());
//            gen.writeEndObject();
        }
    }

    public static class SimpleDeserializersWrapper extends SimpleDeserializers {
        @Override
        public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
            JsonDeserializer<?> enumDeserializer = super.findEnumDeserializer(type, config, beanDesc);
            if (enumDeserializer != null) {
                return enumDeserializer;
            }
            for (Class<?> typeInterface : type.getInterfaces()) {
                enumDeserializer = this._classMappings.get(new ClassKey(typeInterface));
                if (enumDeserializer != null) {
                    return enumDeserializer;
                }
            }
            return null;
        }
    }
    public static class BaseEnumDeserializer extends JsonDeserializer<BaseEnum> {
        @Override
        public BaseEnum deserialize(JsonParser jp, DeserializationContext deserializationContext)
                throws IOException, JacksonException {
            try {
                // 前端输入的值
                String inputParameter = jp.getText();
                if (inputParameter == null || inputParameter.length() == 0) {
                    return null;
                }
                JsonStreamContext parsingContext = jp.getParsingContext();
                //前端注入的对象(ResDTO)
                Object currentValue = parsingContext.getCurrentValue();
                //字段名
                String currentName = parsingContext.getCurrentName();

                JsonStreamContext parent = parsingContext.getParent();

                if (currentValue == null) {
                    currentValue = parent.getCurrentValue();
                }
                if (currentName == null) {
                    currentName = parent.getCurrentName();
                    if (currentName == null) {
                        return null;
                    }
                }

                // 通过对象和属性名获取属性的类型
                Field field = ReflectionUtils.findField(currentValue.getClass(), currentName);
                if (field == null) {
                    return null;
                }
                Class enumClass = field.getType();

                Type[] interfaces = enumClass.getGenericInterfaces();

                ParameterizedType baseEnum = Stream.of(interfaces).filter(
                        t -> t instanceof ParameterizedType
                                && ((ParameterizedType) t).getRawType() instanceof Class
                                && BaseEnum.class.isAssignableFrom((Class) ((ParameterizedType) t).getRawType())
                ).map(v -> (ParameterizedType) v).findFirst().orElse(null);
                if (baseEnum == null) {
                    return null;
                }

                final Type parameterizedType = baseEnum.getActualTypeArguments()[0];

                Object enumValue = jp.readValueAs(new TypeReference<Object>() {
                    @Override
                    public Type getType() {
                        return parameterizedType;
                    }
                });
                return BaseEnum.getByCode(enumClass, enumValue);
            } catch (Exception e) {
                log.error("deserialize error", e);
            }
            return null;
        }
    }

    public static class EnumConverter<E, T extends BaseEnum<E>> implements Converter<String, T> {
        private Map<String, T> enumMap = new HashMap<>();

        private Class<T> enumType;

        public EnumConverter(Class<T> enumType) {
            this.enumType = enumType;
            T[] enums = enumType.getEnumConstants();
            for (T e : enums) {
                enumMap.put(e.getCode().toString(), e);
            }
        }

        @Override
        public T convert(String source) {
            T t = enumMap.get(source);
            if (t == null) {
                throw new RuntimeException(this.enumType.getCanonicalName() + " not has value " + source);
            }
            return t;
        }
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new ConverterFactory<String, BaseEnum>() {
            private final Map<Class, Converter> CONVERTERS = Maps.newHashMap();

            @Override
            public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
                Converter<String, T> converter = CONVERTERS.get(targetType);
                if (converter == null) {
                    converter = new EnumConverter<>(targetType);
                    CONVERTERS.put(targetType, converter);
                }
                return converter;
            }
        });
    }
    //这里与网关冲突，导致重复多了一个Access-Control-Allow-Origin: *
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        CorsRegistration registration = registry.addMapping("/**");
//        registration.allowedOrigins("*");
//        registration.allowedMethods("*").allowedHeaders("*");
//    }

}
