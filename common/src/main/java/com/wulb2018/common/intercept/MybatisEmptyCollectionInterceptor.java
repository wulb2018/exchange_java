package com.wulb2018.common.intercept;

//import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类说明: mybatis空集合拦截器<br/>
 * 若传入的参数有list，且list为空，则视为没有命中的结果集
 *
 */
//@Intercepts({
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
//        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class,Object.class})})
public class MybatisEmptyCollectionInterceptor implements Interceptor {
    private static final Pattern PATTERN = Pattern.compile("[\"|'](.*?)[\"|']");

    /**
     * 获取()内的内容
     */
    private static final Pattern PATTERN2 = Pattern.compile("(?<=\\()(.+?)(?=\\))");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return null;
        //通过invocation.getArgs()可以得到当前执行方法的参数
        //第一个args[0]是MappedStatement对象，第二个args[1]是参数对象parameterObject。
//        final Object[] args = invocation.getArgs();
//        MappedStatement mappedStatement = (MappedStatement) args[0];
//        Object parameter = args[1];
//        if (parameter == null) {
//            Class<?> parameterType = mappedStatement.getParameterMap().getType();
//            // 实际执行时的参数值为空，但mapper语句上存在输入参数的异常状况，返回默认值
//            if (parameterType != null) {
//                return getDefaultReturnValue(invocation);
//            }
//            return invocation.proceed();
//        }
//        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
//        if (isHaveEmptyList(boundSql.getSql())) {
//            return getDefaultReturnValue(invocation);
//        }
//        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        //只拦截Executor对象，减少目标被代理的次数
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 返回默认的值，list类型的返回空list,数值类型的返回0
     *
     * @param invocation
     * @return
     */
//    private Object getDefaultReturnValue(Invocation invocation) {
//        Class<?> returnType = invocation.getMethod().getReturnType();
//        if (returnType.equals(List.class)) {
//            return Lists.newArrayList();
//        } else if (returnType.equals(Integer.TYPE) || returnType.equals(Long.TYPE)
//                || returnType.equals(Integer.class) || returnType.equals(Long.class)) {
//            return 0;
//        }
//        return null;
//    }

    /**
     * 去除字符中的干扰项，避免字符串中的内容干扰判断。
     *
     * @param sql 完整sql
     * @return
     */
    private static String removeInterference(String sql) {
        Matcher matcher = PATTERN.matcher(sql);
        while (matcher.find()) {
            String replaceWorld = matcher.group();
            sql = sql.replace(replaceWorld, "''");
        }
        return sql;
    }

    /**
     * 判断是否存在空list
     * @param sql 完整sql
     * @return
     */
//    private static Boolean isHaveEmptyList(String sql) {
//        sql = removeInterference(sql);
//        List<String> keyWorldList = Lists.newArrayList("in", "values");
//        Boolean isHaveEmptyList = Boolean.FALSE;
//        for (String keyWorld : keyWorldList) {
//            List<Integer> indexList = Lists.newArrayList();
//            //获取关键词后的index，关键词前必须为空白字符，但以关键词开头的单词也会被匹配到，例如index
//            Pattern pattern = Pattern.compile("\\s(?i)" + keyWorld);
//            Matcher matcher = pattern.matcher(sql);
//            while (matcher.find()) {
//                indexList.add(matcher.end());
//            }
//            if (CollectionUtils.isNotEmpty(indexList)) {
//                isHaveEmptyList = checkHaveEmptyList(sql, indexList);
//                if (isHaveEmptyList) {
//                    break;
//                }
//            }
//        }
//        return isHaveEmptyList;
//    }

    /**
     * 判断sql在indexList的每个index后是否存在存在空列表的情况
     *
     * @param sql 完整sql
     * @param indexList keyWorld在sql中的位置
     * @return
     */
    private static Boolean checkHaveEmptyList(String sql, List<Integer> indexList) {
        boolean isHaveEmptyList = Boolean.FALSE;
        for (Integer index : indexList) {
            if (sql.indexOf(')', index) < 0) {
                continue;
            }
            String subSql = sql.substring(index, sql.indexOf(')', index) +1);
            //如果关键词之后无任何sql语句，则sql语句结尾为关键词，此时判定为空列表
            if (StringUtils.isEmpty(subSql)) {
                isHaveEmptyList = Boolean.TRUE;
                break;
            }
            //关键词后必须是(或者是空字符或者是换行符等才有继续判断的意义，避免sql中存在以关键词in或values开头的单词的情况干扰判断
            boolean flag = subSql.startsWith("(")
                    || subSql.startsWith(" ")
                    || subSql.startsWith("\n")
                    || subSql.startsWith("\r");
            if (!flag) {
                continue;
            }
            subSql = subSql.trim();
            //如果关键词后的sql语句trim后不以(开头，也判定为空列表
            if (!subSql.startsWith("(")) {
                isHaveEmptyList = Boolean.TRUE;
                break;
            }
            if("()".equals(subSql)){
                isHaveEmptyList = Boolean.TRUE;
                break;
            }
            Matcher m2 = PATTERN2.matcher(subSql);
            //如果括号()内的内容trim后为空，则判定为空列表
            if (m2.find()) {
                if (StringUtils.isEmpty(m2.group().trim())) {
                    isHaveEmptyList = Boolean.TRUE;
                    break;
                }
            }
        }
        return isHaveEmptyList;
    }
}