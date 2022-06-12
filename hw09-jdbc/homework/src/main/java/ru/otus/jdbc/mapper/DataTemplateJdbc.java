package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.mapper.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                var clazz = entitySQLMetaData.getEntityClassMetaData().getConstructor().getDeclaringClass();
                if (rs.next()) {
                    List<Object> objParamList = getObjParamList(rs);
                    return (T)ReflectionUtil.instantiate(clazz, objParamList.toArray());
                }
                return null;
            } catch (SQLException | ClassNotFoundException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(Connection connection){
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var objList = new ArrayList<T>();
            try {
                var clazz = entitySQLMetaData.getEntityClassMetaData().getConstructor().getDeclaringClass();
                while (rs.next()) {
                    List<Object> objParamList = getObjParamList(rs);
                    objList.add((T)ReflectionUtil.instantiate(clazz, objParamList.toArray()));
                }
                return objList;
            } catch (SQLException | ClassNotFoundException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            List<Object> objList = getFieldsValues(client, entitySQLMetaData.getEntityClassMetaData().getFieldsWithoutId());
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), objList);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> objList = getFieldsValues(client, entitySQLMetaData.getEntityClassMetaData().getFieldsWithoutId());
            Field idField = entitySQLMetaData.getEntityClassMetaData().getIdField();
            idField.setAccessible(true);
            objList.add(idField.get(client));
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), objList);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private static List<Object> getObjParamList(ResultSet rs) throws SQLException, ClassNotFoundException {
        List<Object> objParamList = new ArrayList<>();
        for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
            Class<?> clazz = Class.forName(rs.getMetaData().getColumnClassName(i));
            Object objParam = rs.getObject(i, clazz);
            if(objParam == null){
                objParam = ReflectionUtil.instantiate(clazz);
            }
            objParamList.add(objParam);
        }
        return objParamList;
    }

    private static List<Object> getFieldsValues(Object obj, List<Field> fieldList) throws IllegalAccessException {
        List<Object> objList = new ArrayList<>();
        for(Field f : fieldList){
            f.setAccessible(true);
            objList.add(f.get(obj));
        };

        return objList;
    }

}
