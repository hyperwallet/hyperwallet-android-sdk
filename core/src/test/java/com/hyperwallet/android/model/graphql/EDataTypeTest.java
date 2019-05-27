package com.hyperwallet.android.model.graphql;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import com.hyperwallet.android.model.graphql.field.EDataType;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collection;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class EDataTypeTest {

    @Test
    @Parameters(method = "parametersToTestCorrectDataType")
    public void testGetDataType_returnsCorrectDataType(String dataTypeString, EDataType dataType) {
        assertThat(EDataType.getDataType(dataTypeString), is(dataType));
    }

    @Test
    public void testGetDataType_returnsNullWhenInvalidName() {
        assertThat(EDataType.getDataType("invalid name"), is(nullValue()));
    }

    private Collection<Object[]> parametersToTestCorrectDataType() {
        return Arrays.asList(new Object[][]{
                {"TEXT", EDataType.TEXT},
                {"SELECTION", EDataType.SELECTION},
                {"BOOLEAN", EDataType.BOOLEAN},
                {"NUMBER", EDataType.NUMBER},
                {"RANGE", EDataType.RANGE},
                {"DATE", EDataType.DATE},
                {"DATETIME", EDataType.DATETIME},
                {"PHONE", EDataType.PHONE},
                {"EMAIL", EDataType.EMAIL},
                {"EXPIRY_DATE", EDataType.EXPIRY_DATE},
                {"FILE", EDataType.FILE}
        });
    }
}
