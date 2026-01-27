package com.wulb2018.common.handler;

/**
 * @author wulubin
 * @date 2026/1/24
 * @description TODO
 */
//public class ApiResponseDecoder implements Decoder {
//
//    private final SpringDecoder decoder;
//
//    public ApiResponseDecoder(SpringDecoder decoder) {
//        this.decoder = decoder;
//    }
//
//
//    @Override
//    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
//        Reader reader = response.body().asReader(StandardCharsets.UTF_8);
//        reader.
//        Method method = response.request().requestTemplate().methodMetadata().method();
//        boolean isSame = method.getReturnType() == ApiResponse.class;
//        if (isSame) {
//            //构造一个这个结构类型
//            Type newType =
//                    new ParameterizedType() {
//                        @Override
//                        public Type[] getActualTypeArguments() {
//                            return new Type[]{type};
//                        }
//                        @Override
//                        public Type getRawType() {
//                            return ApiResponse.class;
//                        }
//                        @Override
//                        public Type getOwnerType() {
//                            return null;
//                        }
//                    };
//            ApiResponse<?> result = (ApiResponse<?>) this.decoder.decode(response, newType);
//            //只返回data
//            Object data = result.getData();
//            return result.getData();
//        }
////        try (InputStream is = response.body().asInputStream()) {
////            JavaType javaType = objectMapper.getTypeFactory().constructType(type);
////            return objectMapper.readValue(is, javaType);
////        }
//
//        return this.decoder.decode(response, type);
//    }
//}
