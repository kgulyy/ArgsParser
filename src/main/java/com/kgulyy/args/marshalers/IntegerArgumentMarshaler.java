package com.kgulyy.args.marshalers;

import com.kgulyy.args.exception.ParserException;
import org.jetbrains.annotations.Contract;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.kgulyy.args.exception.ErrorCode.INVALID_INTEGER;
import static com.kgulyy.args.exception.ErrorCode.MISSING_INTEGER;

public class IntegerArgumentMarshaler implements ArgumentMarshaler {
    private int intValue = 0;

    @Override
    public void set(Iterator<String> currentArgument) throws ParserException {
        String parameter = null;
        try {
            parameter = currentArgument.next();
            intValue = Integer.parseInt(parameter);
        } catch (NoSuchElementException e) {
            throw new ParserException(MISSING_INTEGER);
        } catch (NumberFormatException e) {
            throw new ParserException(INVALID_INTEGER, parameter);
        }
    }

    @Contract(pure = true)
    public static int getValue(ArgumentMarshaler am) {
        if (am != null && am instanceof IntegerArgumentMarshaler) {
            return ((IntegerArgumentMarshaler) am).intValue;
        }

        return 0;
    }
}
