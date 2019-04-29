package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class TypeReferenceTest {

    @Test
    public void testGetType_returnsInstanceType() {
        TypeReference typeReference = new MyType();
        assertThat(typeReference.getType().getTypeName(), is(Runnable.class.getTypeName()));
    }

    @Test
    public void testGetType_returnsClassType() {
        TypeReference typeReference = new TypeReference<Runnable>();
        assertThat(typeReference.getType(), isA(Object.class));
    }

    private class MyType extends TypeReference<Runnable> {

    }
}
