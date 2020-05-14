package com.batic.config;

import com.batic.utils.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 存放转换后的sql语句
 */
public class BoundSql {

    /**
     * 解析后的sql
     */
    private String sqlText;

    /**
     * #{} 里面解析出来的参数名称集合
     */
    private List<ParameterMapping> parameterMappings = new ArrayList<>();

    public BoundSql(String sqlText, List<ParameterMapping> parameterMappings) {
        this.sqlText = sqlText;
        this.parameterMappings = parameterMappings;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }
}
