package com.kgulyy.args.marshalers;

import com.kgulyy.args.exception.ParserException;

import java.util.Iterator;

public interface ArgumentMarshaler {
    void set(Iterator<String> currentArgument) throws ParserException;
}
