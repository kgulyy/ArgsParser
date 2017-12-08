package com.kgulyy.args.marshalers;

import com.kgulyy.args.exception.ParserException;
import org.jetbrains.annotations.Contract;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.kgulyy.args.exception.ErrorCode.MISSING_STRING;

public class StringArgumentMarshaler implements ArgumentMarshaler {
    private String stringValue = "";

    @Override
    public void set(Iterator<String> currentArgument) throws ParserException {
        try {
            stringValue = currentArgument.next();
        } catch (NoSuchElementException e) {
            throw new ParserException(MISSING_STRING);
        }
    }

    @Contract(value = "null -> !null", pure = true)
    public static String getValue(ArgumentMarshaler am) {
        if (am != null && am instanceof StringArgumentMarshaler) {
            return ((StringArgumentMarshaler) am).stringValue;
        }

        return "";
    }
}
