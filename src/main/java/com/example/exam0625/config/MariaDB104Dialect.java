package com.example.exam0625.config;

import org.hibernate.dialect.MariaDBDialect;

public class MariaDB104Dialect extends MariaDBDialect {

    @Override
    public String getAlterTableString(String tableName) {
        return "alter table " + tableName;
    }
}
