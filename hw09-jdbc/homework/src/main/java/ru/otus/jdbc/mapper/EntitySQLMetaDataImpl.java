package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData{

    private final EntityClassMetaData<?> classMeta;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> classMeta){
        this.classMeta = classMeta;
    }

    @Override
    public String getSelectAllSql() {
        return "select " +
                getAllFields() +
                " from " +
                classMeta.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return "select " +
                getAllFields() +
                " from " +
                classMeta.getName() +
                " where " +
                classMeta.getIdField().getName() +
                " = ?";
    }

    @Override
    public String getInsertSql() {
        return "insert into " +
                classMeta.getName() +
                "(" +
                getFieldsWithoutId() +
                ") values (" +
                getInsertBind() +
                ")";
    }

    @Override
    public String getUpdateSql() {
        return "update " +
                classMeta.getName() +
                " set " +
                getUpdateFieldsWithoutIdBind() +
                " where " +
                classMeta.getIdField().getName() +
                " = ?";
    }

    private String getAllFields(){
        return classMeta.getAllFields().stream()
                                        .map (Field::getName)
                                        .collect(Collectors.joining(", "));
    }

    private String getFieldsWithoutId(){
        return classMeta.getFieldsWithoutId().stream()
                .map (Field::getName)
                .collect(Collectors.joining(", "));
    }

    private String getInsertBind(){
        return classMeta.getFieldsWithoutId().stream()
                                        .map( f -> "?")
                                        .collect(Collectors.joining(", "));
    }

    private String getUpdateFieldsWithoutIdBind(){
        return classMeta.getFieldsWithoutId().stream()
                                            .map( f -> f.getName() + " = ?")
                                            .collect(Collectors.joining(", "));
    }
}
