package com.wulb2018.common.handler;



import com.wulb2018.common.model.BaseEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

@MappedTypes(BaseEnum.class)
@SuppressWarnings("all")
public class BaseEnumTypeHandler<E, T extends Enum<?> & BaseEnum<E>> extends BaseTypeHandler<T> {

    private Class<T> enumClass;
    private Class<E> codeClass;

    public BaseEnumTypeHandler(Class<T> tClass) {
        if (tClass == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.enumClass = tClass;
        Type[] interfaces = tClass.getGenericInterfaces();
        if (interfaces == null || interfaces.length == 0) {
            return;
        }
        ParameterizedType baseEnum = Stream.of(interfaces).filter(
                t -> t instanceof ParameterizedType
                        && ((ParameterizedType) t).getRawType() instanceof Class
                        && BaseEnum.class.isAssignableFrom((Class<E>) ((ParameterizedType) t).getRawType())
        ).map(v -> (ParameterizedType) v).findFirst().orElse(null);
        if (baseEnum == null) {
            return;
        }
        Type parameterizedType = baseEnum.getActualTypeArguments()[0];
        this.codeClass = parameterizedType instanceof Class ? (Class<E>) parameterizedType : null;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter == null ? null : parameter.getCode());
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        E code = this.codeClass == null ? (E) rs.getObject(columnName) : rs.getObject(columnName, codeClass);
        return rs.wasNull() ? null : BaseEnum.getByCode(enumClass, code);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        E code = this.codeClass == null ? (E) rs.getObject(columnIndex) : rs.getObject(columnIndex, codeClass);
        return rs.wasNull() ? null : BaseEnum.getByCode(enumClass, code);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        E code = this.codeClass == null ? (E) cs.getObject(columnIndex) : cs.getObject(columnIndex, codeClass);

        return cs.wasNull() ? null : BaseEnum.getByCode(enumClass, code);
    }
}
