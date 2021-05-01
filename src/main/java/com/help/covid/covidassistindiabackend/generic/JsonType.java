package com.help.covid.covidassistindiabackend.generic;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

public class JsonType implements UserType, DynamicParameterizedType {

    public static final String JSON_TYPE = "JsonType";
    private static final int[] SQL_TYPES = {Types.JAVA_OBJECT};
    private Class<?> returnedClass;

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        } else if (x == null || y == null) {
            return false;
        } else {
            return x.equals(y);
        }
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return null == x ? 0 : x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index,
                            SharedSessionContractImplementor session) throws HibernateException, SQLException {
        PGobject dataObject = new PGobject();
        dataObject.setType("json");

        if (value != null) {
            dataObject.setValue(convertObjectToJson(value));
        }

        st.setObject(index, dataObject);
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names,
                              SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {
        Object result = resultSet.getObject(names[0]);
        if (result instanceof PGobject) {
            return convertJsonToObject(((PGobject) result).getValue());
        }

        return null;
    }

    private Object convertJsonToObject(String content) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
            mapper.registerModule(new JavaTimeModule());
            JavaType type = createJavaType(mapper);
            if (type == null) {
                return mapper.readValue(content, returnedClass);
            }

            return mapper.readValue(content, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String convertObjectToJson(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        String json = convertObjectToJson(value);
        return convertJsonToObject(json);
    }

    /**
     * Optionnal
     */
    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

    /**
     * (optional operation)
     *
     * @param value
     * @throws HibernateException
     */
    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    /**
     * (optional operation)
     *
     * @param cached
     * @param owner
     * @return the instance cached
     * @throws HibernateException
     */
    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    /**
     * By default we are expecting to use a simple object / not a collection (Set,
     * List)
     *
     * @param mapper : instance jackson object mapper
     * @return A jackson JavaType to specify wich object represent the json string
     * representation
     */
    @SuppressWarnings("deprecation")
    public JavaType createJavaType(ObjectMapper mapper) {
        try {
            return SimpleType.construct(returnedClass());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        final ParameterType reader = (ParameterType) parameters.get(PARAMETER_TYPE);
        if (reader != null) {
            this.returnedClass = reader.getReturnedClass();
        }

    }

    @Override
    public Class<?> returnedClass() {
        return this.returnedClass;
    }

}
