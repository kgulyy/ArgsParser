package com.kgulyy.args.exception;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ParserExceptionTest {
    private static final char ARG = 'x';
    private static final String ERROR_PARAMETER = "ErrorParameter";

    @Test
    public void getMessage_Ok() {
        ParserException e = new ParserException(ErrorCode.OK);
        String expectedMsg = "TILT: Should not get here.";

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_UnexpectedArgument() {
        ParserException e = new ParserException(ErrorCode.UNEXPECTED_ARGUMENT, ARG);
        String expectedMsg = String.format("Argument -%c unexpected.", ARG);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_MissingString() {
        ParserException e = new ParserException(ErrorCode.MISSING_STRING, ARG);
        String expectedMsg = String.format("Could not find string parameter for -%c.", ARG);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_InvalidInteger() {
        ParserException e = new ParserException(ErrorCode.INVALID_INTEGER, ARG, ERROR_PARAMETER);
        String expectedMsg = String.format("Argument -%c expects an integer but was '%s'.", ARG, ERROR_PARAMETER);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_MissingInteger() {
        ParserException e = new ParserException(ErrorCode.MISSING_INTEGER, ARG);
        String expectedMsg = String.format("Could not find integer parameter for -%c.", ARG);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_InvalidDouble() {
        ParserException e = new ParserException(ErrorCode.INVALID_DOUBLE, ARG, ERROR_PARAMETER);
        String expectedMsg = String.format("Argument -%c expects a double but was '%s'.", ARG, ERROR_PARAMETER);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_MissingDouble() {
        ParserException e = new ParserException(ErrorCode.MISSING_DOUBLE, ARG);
        String expectedMsg = String.format("Could not find double parameter for -%c.", ARG);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_InvalidArgumentName() {
        ParserException e = new ParserException(ErrorCode.INVALID_ARGUMENT_NAME, ARG);
        String expectedMsg = String.format("'%c' is not a valid argument name.", ARG);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }

    @Test
    public void getMessage_InvalidArgumentFormat() {
        ParserException e = new ParserException(ErrorCode.INVALID_ARGUMENT_FORMAT, ERROR_PARAMETER);
        String expectedMsg = String.format("'%s' is not a valid argument format.", ERROR_PARAMETER);

        String actualMsg = e.getMessage();

        assertThat(expectedMsg, is(actualMsg));
    }
}