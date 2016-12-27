package com.objectmentor.utilities.args.exception;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by KGuly on 27.12.2016.
 */
public class ArgsExceptionTest {
    private static final char ARG = 'x';
    private static final String ERROR_PARAMETER = "ErrorParameter";

    @Test
    public void getMessage_Ok() {
        ArgsException e = new ArgsException(ErrorCode.OK);
        String expectedMsg = "TILT: Should not get here.";

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_UnexpectedArgument() {
        ArgsException e = new ArgsException(ErrorCode.UNEXPECTED_ARGUMENT, ARG);
        String expectedMsg = String.format("Argument -%c unexpected.", ARG);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_MissingString() {
        ArgsException e = new ArgsException(ErrorCode.MISSING_STRING, ARG);
        String expectedMsg = String.format("Could not find string parameter for -%c.", ARG);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_InvalidInteger() {
        ArgsException e = new ArgsException(ErrorCode.INVALID_INTEGER, ARG, ERROR_PARAMETER);
        String expectedMsg = String.format("Argument -%c expects an integer but was '%s'.", ARG, ERROR_PARAMETER);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_MissingInteger() {
        ArgsException e = new ArgsException(ErrorCode.MISSING_INTEGER, ARG);
        String expectedMsg = String.format("Could not find integer parameter for -%c.", ARG);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_InvalidDouble() {
        ArgsException e = new ArgsException(ErrorCode.INVALID_DOUBLE, ARG, ERROR_PARAMETER);
        String expectedMsg = String.format("Argument -%c expects a double but was '%s'.", ARG, ERROR_PARAMETER);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_MissingDouble() {
        ArgsException e = new ArgsException(ErrorCode.MISSING_DOUBLE, ARG);
        String expectedMsg = String.format("Could not find double parameter for -%c.", ARG);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_InvalidArgumentName() {
        ArgsException e = new ArgsException(ErrorCode.INVALID_ARGUMENT_NAME, ARG);
        String expectedMsg = String.format("'%c' is not a valid argument name.", ARG);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_InvalidArgumentFormat() {
        ArgsException e = new ArgsException(ErrorCode.INVALID_ARGUMENT_FORMAT, ERROR_PARAMETER);
        String expectedMsg = String.format("'%s' is not a valid argument format.", ERROR_PARAMETER);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }
}