package com.objectmentor.utilities.args;

import com.objectmentor.utilities.args.exception.ArgsException;

import java.util.*;

import static com.objectmentor.utilities.args.exception.ErrorCode.*;

/**
 * Created by KGuly on 22.12.2016.
 */
public class ArgsCleanCode implements Args {
    private String schema;
    private Map<Character, ArgumentMarshaler> marshalers = new HashMap<>();
    private Set<Character> argsFound = new HashSet<>();
    private Iterator<String> currentArgument;
    private List<String> argsList;

    @SuppressWarnings("WeakerAccess")
    public ArgsCleanCode(String schema, String[] args) throws ArgsException {
        this.schema = schema;
        argsList = Arrays.asList(args);
        parse();
    }

    private void parse() throws ArgsException {
        parseSchema();
        parseArguments();
    }

    private boolean parseSchema() throws ArgsException {
        for (String element : schema.split(",")) {
            if (element.length() > 0) {
                parseSchemaElement(element.trim());
            }
        }
        return true;
    }

    private void parseSchemaElement(String element) throws ArgsException {
        char elementId = element.charAt(0);
        validateSchemaElementId(elementId);
        String elementTail = element.substring(1);
        if (elementTail.isEmpty())
            marshalers.put(elementId, new BooleanArgumentMarshaler());
        else if (elementTail.equals("*"))
            marshalers.put(elementId, new StringArgumentMarshaler());
        else if (elementTail.equals("#"))
            marshalers.put(elementId, new IntegerArgumentMarshaler());
        else if (elementTail.equals("##"))
            marshalers.put(elementId, new DoubleArgumentMarshaler());
        else
            throw new ArgsException(INVALID_ARGUMENT_FORMAT, elementId, elementTail);
    }

    private void validateSchemaElementId(char elementId) throws ArgsException {
        if (!Character.isLetter(elementId)) {
            throw new ArgsException(INVALID_ARGUMENT_NAME, elementId);
        }
    }

    private void parseArguments() throws ArgsException {
        for (currentArgument = argsList.iterator(); currentArgument.hasNext();) {
            String arg = currentArgument.next();
            parseArgument(arg);
        }
    }

    private void parseArgument(String arg) throws ArgsException {
        if (arg.startsWith("-")) {
            parseElements(arg);
        }
    }

    private void parseElements(String arg) throws ArgsException {
        for (int i = 1; i < arg.length(); i++) {
            parseElement(arg.charAt(i));
        }
    }

    private void parseElement(char argChar) throws ArgsException {
        if (setArgument(argChar)) {
            argsFound.add(argChar);
        } else {
            throw new ArgsException(UNEXPECTED_ARGUMENT, argChar);
        }
    }

    private boolean setArgument(char argChar) throws ArgsException {
        ArgumentMarshaler m = marshalers.get(argChar);
        if (m == null)
            return false;
        try {
            m.set(currentArgument);
        } catch (ArgsException e) {
            e.setErrorArgumentId(argChar);
            throw e;
        }

        return true;
    }

    @Override
    public int cardinality() {
        return argsFound.size();
    }

    @SuppressWarnings("unused")
    public String usage() {
        if (schema.length() > 0)
            return "-[" + schema + "]";
        else
            return "";
    }

    @Override
    public boolean has(char arg) {
        return argsFound.contains(arg);
    }

    @Override
    public boolean getBoolean(char arg) {
        ArgumentMarshaler am = marshalers.get(arg);
        boolean b;
        try {
            b = am != null && (Boolean) am.get();
        } catch (ClassCastException e) {
            b = false;
        }

        return b;
    }

    @Override
    public String getString(char arg) {
        ArgumentMarshaler am = marshalers.get(arg);
        try {
            return am == null ? "" : (String) am.get();
        } catch (ClassCastException e) {
            return "";
        }
    }

    @Override
    public int getInt(char arg) {
        ArgumentMarshaler am = marshalers.get(arg);
        try {
            return am == null ? 0 : (Integer) am.get();
        } catch (ClassCastException e) {
            return 0;
        }
    }

    public double getDouble(char arg) {
        ArgumentMarshaler am = marshalers.get(arg);
        try {
            return am == null ? 0 : (Double) am.get();
        } catch (ClassCastException e) {
            return 0;
        }
    }

    private interface ArgumentMarshaler {
        void set(Iterator<String> currentArgument) throws ArgsException;
        Object get();
    }

    private class BooleanArgumentMarshaler implements ArgumentMarshaler {
        private boolean booleanValue = false;

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            booleanValue = true;
        }

        @Override
        public Object get() {
            return booleanValue;
        }
    }

    private class StringArgumentMarshaler implements ArgumentMarshaler {
        private String stringValue = "";

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            try {
                stringValue = currentArgument.next();
            } catch (NoSuchElementException e) {
                throw new ArgsException(MISSING_STRING);
            }
        }

        @Override
        public Object get() {
            return stringValue;
        }
    }

    private class IntegerArgumentMarshaler implements ArgumentMarshaler {
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

        @Override
        public Object get() {
            return intValue;
        }
    }

    private class DoubleArgumentMarshaler implements ArgumentMarshaler {
        private double doubleValue = 0;

        @Override
        public void set(Iterator<String> currentArgument) throws ArgsException {
            String parameter = null;
            try {
                parameter = currentArgument.next();
                doubleValue = Double.parseDouble(parameter);
            } catch (NoSuchElementException e) {
                throw new ArgsException(MISSING_DOUBLE);
            } catch (NumberFormatException e) {
                throw new ArgsException(INVALID_DOUBLE, parameter);
            }
        }

        @Override
        public Object get() {
            return doubleValue;
        }
    }
}
