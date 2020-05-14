package com.batic.sqlSession;

import com.batic.pojo.Configuration;

/**
 * SqlSession工厂类：生产SqlSession：会话对象
 * @author Administrator
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
