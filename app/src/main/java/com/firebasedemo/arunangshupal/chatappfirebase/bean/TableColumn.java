package com.firebasedemo.arunangshupal.chatappfirebase.bean;

/**
 * Created by Arunangshu Pal on 5/17/2016.
 */
public class TableColumn {
    String colName,dataType;

    public TableColumn(String dataType, String colName) {
        this.dataType = dataType;
        this.colName = colName;
    }

    public String getColName() {
        return colName;
    }

    public String getDataType() {
        return dataType;
    }
}
