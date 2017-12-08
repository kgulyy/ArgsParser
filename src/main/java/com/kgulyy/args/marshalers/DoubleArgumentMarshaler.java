package com.kgulyy.args.marshalers;

import com.kgulyy.args.exception.ParserException;
import org.jetbrains.annotations.Contract;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.kgulyy.args.exception.ErrorCode.INVALID_DOUBLE;
import static com.kgulyy.args.exception.ErrorCode.MISSING_DOUBLE;

public class DoubleArgumentMarshaler implements ArgumentMarshaler {
    private double doubleValue = 0.0;

    @Override
    public void set(Iterator<String> currentArgument) throws ParserException {
        String parameter = null;
        try {
            parameter = currentArgument.next();
            doubleValue = Double.parseDouble(parameter);
        } catch (NoSuchElementException e) {
            throw new ParserException(MISSING_DOUBLE);
        } catch (NumberFormatException e) {
            throw new ParserException(INVALID_DOUBLE, parameter);
        }
    }

    @Contract(pure = true)
    public static double getValue(ArgumentMarshaler am) {
        if (am != null && am instanceof DoubleArgumentMarshaler) {
            return ((DoubleArgumentMarshaler) am).doubleValue;
        }

        return 0;
    }
}
