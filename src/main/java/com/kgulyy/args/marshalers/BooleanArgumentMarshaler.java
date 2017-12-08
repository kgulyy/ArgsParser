package com.kgulyy.args.marshalers;

import org.jetbrains.annotations.Contract;

import java.util.Iterator;

public class BooleanArgumentMarshaler implements ArgumentMarshaler {
    private boolean booleanValue = false;

    @Override
    public void set(Iterator<String> currentArgument) {
        booleanValue = true;
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean getValue(ArgumentMarshaler am) {
        return am != null && am instanceof BooleanArgumentMarshaler && ((BooleanArgumentMarshaler) am).booleanValue;
    }
}
