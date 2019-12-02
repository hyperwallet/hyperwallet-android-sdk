package com.hyperwallet.android.model.graphql.field;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collection;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class DataTypeTest {

    @Test
    @Parameters(method = "parametersToTestCorrectDataType")
    public void testGetDataType_returnsCorrectDataType(String dataTypeString, String dataType) {
        assertThat(DataType.getDataType(dataTypeString), is(dataType));
    }

    @Test
    public void testGetDataType_returnsNullWhenInvalidName() {
        assertThat(DataType.getDataType("invalid name"), is(nullValue()));
    }

    private Collection<Object[]> parametersToTestCorrectDataType() {
        return Arrays.asList(new Object[][]{
                {"TEXT", DataType.TEXT},
                {"SELECTION", DataType.SELECTION},
                {"BOOLEAN", DataType.BOOLEAN},
                {"NUMBER", DataType.NUMBER},
                {"RANGE", DataType.RANGE},
                {"DATE", DataType.DATE},
                {"DATETIME", DataType.DATETIME},
                {"PHONE", DataType.PHONE},
                {"EMAIL", DataType.EMAIL},
                {"EXPIRY_DATE", DataType.EXPIRY_DATE},
                {"FILE", DataType.FILE}
        });
    }
}
