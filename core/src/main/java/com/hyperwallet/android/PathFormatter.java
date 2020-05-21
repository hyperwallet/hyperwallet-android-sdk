/*
 * Copyright 2018 Hyperwallet
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package com.hyperwallet.android;

import androidx.annotation.NonNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * {@code PathFormatter} utility that creates api postfix uri for REST
 * based on postfix uri pattern and arguments
 */
class PathFormatter {
    private final String pattern;
    private Object[] arguments;

    /**
     * Construct a {@code PathFormatter} object from pattern provided
     */
    private PathFormatter(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Construct a {@code PathFormatter} object from pattern and list of arguments provided
     *
     * @param pattern postfix uri pattern
     * @param args
     */
    PathFormatter(@NonNull String pattern, Object... args) {
        this(pattern);
        arguments = args;
    }

    /**
     * Create postfix uri path
     * @param token
     * @return String of formatted path
     */
    String format(String token) {
        arguments = prependArgument(token, arguments);
        return MessageFormat.format(pattern, arguments);
    }

    Object[] prependArgument(Object newArgument, Object[] arguments) {
        ArrayList<Object> argumentsList = new ArrayList<>(Arrays.asList(arguments));
        argumentsList.add(0, newArgument);
        return argumentsList.toArray();
    }
}
