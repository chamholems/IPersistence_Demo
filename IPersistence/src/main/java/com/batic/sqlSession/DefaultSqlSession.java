package com.batic.sqlSession;

import com.batic.pojo.Configuration;

import java.lang.reflect.*;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        // 将要去完成对simpleExecutor里的query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        List<Object> objectList = simpleExecutor.query(configuration, configuration.getMappedStatementMap().get(statementId), params);
        return (List<E>) objectList;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if (objects.size() == 1) {
            return (T) objects.get(0);
        } else {
            throw new RuntimeException("查询结果为空，或者查询结果过多");
        }
    }

    /**
     * 使用JDK动态代理 来为Dao接口生成代理对象并返回
     * @param mapperClass
     * @param <T>
     * @return
     */
    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            /**
             * 根据不同情况，来调用selectList/selectOne
             * 底层还是去执行JDBC代码
             * @param proxy 当前代理对象的引用
             * @param method 当前被调用方法的引用
             * @param args 传递的参数 1.statemented
             * @return Object
             * @throws Throwable Throwable
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 准备参数1： statementId
                // dao接口名
                String methodName = method.getName();
                // dao包名
                String className = method.getDeclaringClass().getName();

                String statementId = className + "." + methodName;

                // 准备参数2：params：args

                // 获取被调用方法的返回值类型
                Type genericReturnType = method.getGenericReturnType();
                // 判断是否进行了 泛型类型参数化(判断是否为泛型)
                if(genericReturnType instanceof ParameterizedType){
                    return selectList(statementId,args);
                }
                return selectOne(statementId,args);
            }
        });
        return (T) proxyInstance;
    }
}
