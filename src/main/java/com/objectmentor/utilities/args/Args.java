package com.objectmentor.utilities.args;

/**
 * Created by KGuly on 22.12.2016.
 */
@SuppressWarnings("WeakerAccess")
public interface Args {

    boolean has(char arg);

    boolean getBoolean(char arg);

    String getString(char arg);

    int getInt(char arg);
}
