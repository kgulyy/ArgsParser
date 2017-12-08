package com.kgulyy.args;

import com.kgulyy.args.exception.ParserException;
import com.kgulyy.args.marshalers.*;

import java.util.*;

import static com.kgulyy.args.exception.ErrorCode.*;

@SuppressWarnings("WeakerAccess")
public class ArgumentsParser {
    private final Map<Character, ArgumentMarshaler> marshalers;
    private final Set<Character> argsFound;
    private ListIterator<String> currentArgument;

    public ArgumentsParser(String schema, String[] args) throws ParserException {
        marshalers = new HashMap<>();
        argsFound = new HashSet<>();
        parseSchema(schema);
        parseArgumentStrings(Arrays.asList(args));
    }

    private void parseSchema(String schema) throws ParserException {
        for (String element : schema.split(",")) {
            if (element.length() > 0)
                parseSchemaElement(element.trim());
        }
    }

    private void parseSchemaElement(String element) throws ParserException {
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
            throw new ParserException(INVALID_ARGUMENT_FORMAT, elementId, elementTail);

    }

    private void validateSchemaElementId(char elementId) throws ParserException {
        if (!Character.isLetter(elementId))
            throw new ParserException(INVALID_ARGUMENT_NAME, elementId);
    }

    private void parseArgumentStrings(List<String> argList) throws ParserException {
        for (currentArgument = argList.listIterator(); currentArgument.hasNext(); ) {
            String argString = currentArgument.next();
            if (argString.startsWith("-")) {
                parseArgumentCharacters(argString.substring(1));
            }
        }
    }

    private void parseArgumentCharacters(String argChars) throws ParserException {
        for (int i = 0; i < argChars.length(); i++)
            parseArgumentCharacter(argChars.charAt(i));
    }

    private void parseArgumentCharacter(char argChar) throws ParserException {
        ArgumentMarshaler m = marshalers.get(argChar);
        if (m == null) {
            throw new ParserException(UNEXPECTED_ARGUMENT, argChar);
        } else {
            argsFound.add(argChar);
            try {
                m.set(currentArgument);
            } catch (ParserException e) {
                e.setErrorArgumentId(argChar);
                throw e;
            }
        }
    }

    public int cardinality() {
        return argsFound.size();
    }

    public boolean has(char arg) {
        return argsFound.contains(arg);
    }

    public boolean getBoolean(char arg) {
        return BooleanArgumentMarshaler.getValue(marshalers.get(arg));
    }

    public String getString(char arg) {
        return StringArgumentMarshaler.getValue(marshalers.get(arg));
    }

    public int getInt(char arg) {
        return IntegerArgumentMarshaler.getValue(marshalers.get(arg));
    }

    public double getDouble(char arg) {
        return DoubleArgumentMarshaler.getValue(marshalers.get(arg));
    }
}
