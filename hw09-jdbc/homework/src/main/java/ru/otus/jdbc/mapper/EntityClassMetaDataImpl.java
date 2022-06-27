package ru.otus.jdbc.mapper;

import ru.otus.crm.model.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T>{

    private List<Field> fields;
    private List<Field> fieldsWithoutId;
    private Field fieldId;

    //Generic-тип класса доступен только при компиляции, в runtime он теряется, поэтому
    //класс нужно передавать явно в конструкторе

    private final Class<?> clazz;

    public EntityClassMetaDataImpl(Class<?> clazz){
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<?> getConstructor() {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        if(fieldId == null) {
            fieldId = getAllFields().stream()
                                    .filter(f -> f.isAnnotationPresent(Id.class))
                                    .findFirst()
                                    .orElseThrow();
        }
        return fieldId;
    }

    @Override
    public List<Field> getAllFields() {
        if(fields == null){
            fields = List.of(clazz.getDeclaredFields());
        }
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if(fieldsWithoutId == null) {
            fieldsWithoutId = new ArrayList<>(getAllFields());
            fieldsWithoutId.remove(getIdField());
        }
        return fieldsWithoutId;
    }
}
