package com.objectmentor.utilities.args.marshalers;

import com.objectmentor.utilities.args.exception.ArgsException;

import java.util.Iterator;

/**
 * Created by KGuly on 21.12.2016.
 */
public class BooleanArgumentMarshaler implements ArgumentMarshaler {
    private boolean booleanValue = false;

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
        booleanValue = true;
    }

    public static boolean getValue(ArgumentMarshaler am) {
        if (am != null && am instanceof BooleanArgumentMarshaler) {
            return ((BooleanArgumentMarshaler) am).booleanValue;
        } else
            return false;
    }
}
