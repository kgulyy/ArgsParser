package com.objectmentor.utilities.args.marshalers;

import com.objectmentor.utilities.args.exception.ArgsException;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.objectmentor.utilities.args.exception.ErrorCode.INVALID_INTEGER;
import static com.objectmentor.utilities.args.exception.ErrorCode.MISSING_INTEGER;

/**
 * Created by KGuly on 21.12.2016.
 */
public class IntegerArgumentMarshaler implements ArgumentMarshaler {
    private int intValue = 0;

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
        String parameter = null;
        try {
            parameter = currentArgument.next();
            intValue = Integer.parseInt(parameter);
        } catch (NoSuchElementException e) {
            throw new ArgsException(MISSING_INTEGER);
        } catch (NumberFormatException e) {
            throw new ArgsException(INVALID_INTEGER, parameter);
        }
    }

    public static int getValue(ArgumentMarshaler am) {
        if (am != null && am instanceof IntegerArgumentMarshaler)
            return ((IntegerArgumentMarshaler) am).intValue;
        else
            return 0;
    }
}
