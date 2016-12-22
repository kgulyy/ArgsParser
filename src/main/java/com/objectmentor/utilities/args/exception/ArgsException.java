package com.objectmentor.utilities.args.exception;

import static com.objectmentor.utilities.args.exception.ErrorCode.OK;

/**
 * Created by KGuly on 21.12.2016.
 */
public class ArgsException extends Exception {
    private ErrorCode errorCode = OK;
    private char errorArgumentId = '\0';
    private String errorParameter = null;

    @SuppressWarnings("unused")
    public ArgsException() {
    }

    @SuppressWarnings("unused")
    public ArgsException(String message) {
        super(message);
    }

    public ArgsException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ArgsException(ErrorCode errorCode, String errorParameter) {
        this.errorParameter = errorParameter;
        this.errorCode = errorCode;
    }

    public ArgsException(ErrorCode errorCode, char errorArgumentId) {
        this.errorCode = errorCode;
        this.errorArgumentId = errorArgumentId;
    }

    public ArgsException(ErrorCode errorCode, char errorArgumentId, String errorParameter) {
        this.errorCode = errorCode;
        this.errorArgumentId = errorArgumentId;
        this.errorParameter = errorParameter;
    }

    @SuppressWarnings("unused")
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @SuppressWarnings("unused")
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @SuppressWarnings("unused")
    public char getErrorArgumentId() {
        return errorArgumentId;
    }


    public void setErrorArgumentId(char errorArgumentId) {
        this.errorArgumentId = errorArgumentId;
    }

    @SuppressWarnings("unused")
    public String getErrorParameter() {
        return errorParameter;
    }

    @SuppressWarnings("unused")
    public void setErrorParameter(String errorParameter) {
        this.errorParameter = errorParameter;
    }

    public String getMessage() {
        switch (errorCode) {
            case OK:
                return "TILT: Should not get here.";
            case UNEXPECTED_ARGUMENT:
                return String.format("Argument -%c unexpected.", errorArgumentId);
            case MISSING_STRING:
                return String.format("Could not find string parameter for -%c.", errorArgumentId);
            case INVALID_INTEGER:
                return String.format("Argument -%c expects an integer but was '%s'.", errorArgumentId, errorParameter);
            case MISSING_INTEGER:
                return String.format("Could not find integer parameter for -%c.", errorArgumentId);
            case INVALID_ARGUMENT_NAME:
                return String.format("'%s' is not a valid argument name.", errorArgumentId);
            case INVALID_ARGUMENT_FORMAT:
                return String.format("'%s' is not a valid argument format.", errorParameter);
            default:
                return "";
        }
    }
}
