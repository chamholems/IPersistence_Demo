package com.batic.sqlSession;

import com.batic.config.BoundSql;
import com.batic.pojo.Configuration;
import com.batic.pojo.MappedStatement;
import com.batic.utils.GenericTokenParser;
import com.batic.utils.ParameterMapping;
import com.batic.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor{

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception{
        // 注册驱动，获取数据库连接
        Connection connection = configuration.getDataSource().getConnection();
        // 获取sql语句  select * from use where id = #{id} and username = #{username}
        String sql = mappedStatement.getSql();
        // 转换sql语句  select * from use where id = ? and username = ? 转换的过程中，还需要对#{}立面的值进行解析存储
        BoundSql boundSql = getBoundSql(sql);
        // 获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        // 设置参数
        // 获取参数的全路径
        String paramType = mappedStatement.getParamType();
        // 获取参数的Class类
        Class<?> paramterTypeClass = getClassType(paramType);

        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();
            // 使用反射获取实体类的对应属性值
            Field declaredField = paramterTypeClass.getDeclaredField(content);
            // 暴力访问
            declaredField.setAccessible(true);
            // 属性的类型
            Object o = declaredField.get(params[0]);

            preparedStatement.setObject(i+1,o);

        }
        // 执行sql
        ResultSet resultSet = preparedStatement.executeQuery();

        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);
        ArrayList<Object> objects = new ArrayList<>();
        // 封装返回结果集
        while (resultSet.next()){
            Object o = resultTypeClass.newInstance();
            // 元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                // 获取字段名
                String columnName = metaData.getColumnName(i);
                // 获取字段值
                Object object = resultSet.getObject(columnName);
                // 使用反射或者内省，根据数据库表和实体的对应关系，完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,object);
            }
            objects.add(o);
        }

        return (List<E>) objects;
    }

    /**
     * 根据类的全路径 获取类的Class对象
     * @return
     */
    private Class<?> getClassType(String paramType) throws ClassNotFoundException {
        if(paramType != null) {
            return Class.forName(paramType);
        }
        return null;
    }

    /**
     * 完成对#{}的解析工作：
     *  1.将#{}使用?代替
     *  2.解析出#{}里面的值进行存储
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        // 标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler  parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        // 标记解析器：
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        // 解析后的sql
        String parseSql = genericTokenParser.parse(sql);
        // #{} 里面解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        return new BoundSql(parseSql,parameterMappings);

    }
}
