package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData{

    private final EntityClassMetaData<?> classMeta;
    private final StringBuilder sb;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> classMeta){
        this.classMeta = classMeta;
        sb = new StringBuilder();
    }

    @Override
    public String getSelectAllSql() {
        sb.setLength(0);
        sb.append("select ")
          .append(getAllFields())
          .append(" from ")
          .append(classMeta.getName());

        return sb.toString();
    }

    @Override
    public String getSelectByIdSql() {
        sb.setLength(0);
        sb.append("select ")
          .append(getAllFields())
          .append(" from ")
          .append(classMeta.getName())
          .append(" where ")
          .append(classMeta.getIdField().getName())
          .append(" = ?");

        return sb.toString();
    }

    @Override
    public String getInsertSql() {
        sb.setLength(0);
        sb.append("insert into ")
                .append(classMeta.getName())
                .append("(")
                .append(getFieldsWithoutId())
                .append(") values (")
                .append(getInsertBind())
                .append(")");

        return sb.toString();
    }

    @Override
    public String getUpdateSql() {
        sb.setLength(0);

        sb.append("update ")
                .append(classMeta.getName())
                .append(" set ")
                .append(getUpdateFieldsWithoutIdBind())
                .append(" where ")
                .append(classMeta.getIdField().getName())
                .append(" = ?");

        return sb.toString();
    }

    public EntityClassMetaData<?> getEntityClassMetaData(){
        return classMeta;
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
