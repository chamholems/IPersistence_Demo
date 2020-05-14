package com.batic.sqlSession;

import com.batic.pojo.Configuration;
import com.batic.pojo.MappedStatement;

import java.util.List;

public interface Executor {

    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object...params) throws Exception;

}
