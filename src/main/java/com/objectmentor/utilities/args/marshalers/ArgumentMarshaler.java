package com.objectmentor.utilities.args.marshalers;

import com.objectmentor.utilities.args.exception.ArgsException;

import java.util.Iterator;

/**
 * Created by KGuly on 21.12.2016.
 */
public interface ArgumentMarshaler {
    void set(Iterator<String> currentArgument) throws ArgsException;
}
