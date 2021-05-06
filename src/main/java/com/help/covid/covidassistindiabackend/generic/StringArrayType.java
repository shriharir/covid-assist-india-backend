package com.help.covid.covidassistindiabackend.generic;

import java.util.Properties;

import com.vladmihalcea.hibernate.type.array.internal.ArraySqlTypeDescriptor;
import com.vladmihalcea.hibernate.type.array.internal.StringArrayTypeDescriptor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;

public class StringArrayType
        extends AbstractSingleColumnStandardBasicType<String[]>
        implements DynamicParameterizedType {

    private static final long serialVersionUID = 7326419250915613693L;

    public StringArrayType() {
        super(
                ArraySqlTypeDescriptor.INSTANCE,
                StringArrayTypeDescriptor.INSTANCE
        );
    }

    @Override
    public String getName() {
        return "StringArray";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        ((StringArrayTypeDescriptor)
                getJavaTypeDescriptor())
                .setParameterValues(parameters);
    }
}