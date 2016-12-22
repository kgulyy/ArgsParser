package com.objectmentor.utilities.args.marshalers;

import com.objectmentor.utilities.args.exception.ArgsException;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.objectmentor.utilities.args.exception.ErrorCode.MISSING_STRING;

/**
 * Created by KGuly on 21.12.2016.
 */
public class StringArgumentMarshaler implements ArgumentMarshaler {
    private String stringValue = "";

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
        try {
            stringValue = currentArgument.next();
        } catch (NoSuchElementException e) {
            throw new ArgsException(MISSING_STRING);
        }
    }

    public static String getValue(ArgumentMarshaler am) {
        if (am != null && am instanceof StringArgumentMarshaler)
            return ((StringArgumentMarshaler) am).stringValue;
        else
            return "";
    }
}
