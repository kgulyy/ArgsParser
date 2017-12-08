package com.kgulyy.args;

import com.kgulyy.args.exception.ParserException;
import org.junit.Test;

import static com.kgulyy.args.exception.ErrorCode.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ArgumentsParserTest {
    private static final String TEST_STRING = "TestString";
    private static final int TEST_INT = 124;
    private static final double TEST_DOUBLE = 75.45;

    @Test
    public void constructor_Positive_WithNoSchemaOrArgument() throws ParserException {
        String schema = "";
        String[] arguments = {};

        ArgumentsParser parser = new ArgumentsParser(schema, arguments);
        int countArgs = parser.cardinality();

        assertEquals(0, countArgs);
    }

    @Test
    public void constructor_Negative_WithNoSchemaButWithOneArgument() {
        String schema = "";
        String[] arguments = {"-a"};

        try {
            new ArgumentsParser(schema, arguments);
        } catch (ParserException e) {
            assertEquals(UNEXPECTED_ARGUMENT, e.getErrorCode());
            assertEquals('a', e.getErrorArgumentId());
        }
    }

    @Test
    public void constructor_Negative_WithNoSchemaButWithMultipleArguments() {
        String schema = "";
        String[] arguments = {"-a", "-b"};

        try {
            new ArgumentsParser(schema, arguments);
        } catch (ParserException e) {
            assertEquals(UNEXPECTED_ARGUMENT, e.getErrorCode());
            assertEquals('a', e.getErrorArgumentId());
        }
    }

    @Test
    public void constructor_Negative_NonLetterSchema() {
        String schema = "*";
        String[] arguments = {};

        try {
            new ArgumentsParser(schema, arguments);
        } catch (ParserException e) {
            assertEquals(INVALID_ARGUMENT_NAME, e.getErrorCode());
            assertEquals('*', e.getErrorArgumentId());
        }
    }

    @Test
    public void constructor_Negative_InvalidArgumentFormat() {
        String schema = "a~";
        String[] arguments = {"-a~"};

        try {
            new ArgumentsParser(schema, arguments);
        } catch (ParserException e) {
            assertEquals(INVALID_ARGUMENT_FORMAT, e.getErrorCode());
            assertEquals('a', e.getErrorArgumentId());
        }
    }

    @Test
    public void constructor_Positive_SpaceInSchema() throws ParserException {
        String schema = " x , y ";
        String[] arguments = {"-xy"};

        ArgumentsParser parser = new ArgumentsParser(schema, arguments);
        int countArgs = parser.cardinality();
        boolean hasX = parser.has('x');
        boolean hasY = parser.has('y');

        assertEquals(2, countArgs);
        assertTrue(hasX);
        assertTrue(hasY);
    }

    @Test
    public void constructor_Positive_ValueBeforeArgument() throws ParserException {
        String schema = "i#, a";
        String[] arguments = {TEST_STRING, "-i", String.valueOf(TEST_INT), "-a"};
        ArgumentsParser parser = new ArgumentsParser(schema, arguments);

        int argI = parser.getInt('i');

        assertThat(argI, is(TEST_INT));
    }

    @Test
    public void has_Positive_TrueReturned() throws ParserException {
        String schema = "t";
        String[] arguments = {"-t"};
        ArgumentsParser parser = new ArgumentsParser(schema, arguments);

        boolean hasArg = parser.has('t');

        assertTrue(hasArg);
    }

    @Test
    public void has_Positive_FalseReturned() throws ParserException {
        String schema = "f";
        String[] arguments = {};
        ArgumentsParser parser = new ArgumentsParser(schema, arguments);

        boolean hasArg = parser.has('f');

        assertFalse(hasArg);
    }

    @Test
    public void getBoolean_Positive_TrueReturned() throws ParserException {
        String schema = "t";
        String[] arguments = {"-t"};
        ArgumentsParser parser = new ArgumentsParser(schema, arguments);

        boolean argT = parser.getBoolean('t');

        assertTrue(argT);
    }

    @Test
    public void getBoolean_Positive_FalseReturned() throws ParserException {
        String schema = "f";
        String[] arguments = {};
        ArgumentsParser parser = new ArgumentsParser(schema, arguments);

        boolean argF = parser.getBoolean('f');

        assertFalse(argF);
    }

    @Test
    public void getBoolean_Positive_MultipleArgs() throws ParserException {
        String schema = "a,b";
        String[] arguments = {"-b"};
        ArgumentsParser parser = new ArgumentsParser(schema, arguments);

        boolean argA = parser.getBoolean('a');
        boolean argB = parser.getBoolean('b');

        assertFalse(argA);
        assertTrue(argB);
    }

    @Test
    public void getString_Positive_StringPresent() throws ParserException {
        String schema = "s*";
        String[] arguments = {"-s", TEST_STRING};
        ArgumentsParser parser = new ArgumentsParser(schema, arguments);

        String argS = parser.getString('s');

        assertThat(argS, is(TEST_STRING));
    }

    @Test
    public void getString_Positive_MultipleArgs() throws ParserException {
        String schema = "a*,b*,c*";
        final String testArgA = "TestArgA";
        final String testArgB = "TestArgB";
        String[] arguments = {"-ba", testArgB, testArgA};
        ArgumentsParser parser = new ArgumentsParser(schema, arguments);

        String argA = parser.getString('a');
        String argB = parser.getString('b');
        String argC = parser.getString('c');

        assertThat(argA, is(testArgA));
        assertThat(argB, is(testArgB));
        assertThat(argC, is(""));
    }

    @Test
    public void getInt_Positive_IntegerPresent() throws ParserException {
        String schema = "i#";
        String[] arguments = {"-i", String.valueOf(TEST_INT)};
        ArgumentsParser parser = new ArgumentsParser(schema, arguments);

        int argI = parser.getInt('i');

        assertThat(argI, is(TEST_INT));
    }

    @Test
    public void getInt_Negative_MissingInteger() {
        String schema = "i#";
        String[] arguments = {"-i"};

        try {
            new ArgumentsParser(schema, arguments);
        } catch (ParserException e) {
            assertEquals(MISSING_INTEGER, e.getErrorCode());
            assertEquals('i', e.getErrorArgumentId());
        }
    }

    @Test
    public void getInt_Negative_InvalidInteger() {
        String schema = "i#";
        String[] arguments = {"-i", TEST_STRING};

        try {
            new ArgumentsParser(schema, arguments);
        } catch (ParserException e) {
            assertEquals(INVALID_INTEGER, e.getErrorCode());
            assertEquals('i', e.getErrorArgumentId());
        }
    }

    @Test
    public void getInt_Positive_MultipleArgs() throws ParserException {
        String schema = "a#,b#,c#";
        final int testArgA = 158;
        final int testArgC = 84;
        String[] arguments = {"-ac", String.valueOf(testArgA), String.valueOf(testArgC)};
        ArgumentsParser parser = new ArgumentsParser(schema, arguments);

        int argA = parser.getInt('a');
        int argB = parser.getInt('b');
        int argC = parser.getInt('c');

        assertThat(argA, is(testArgA));
        assertThat(argB, is(0));
        assertThat(argC, is(testArgC));
    }

    @Test
    public void getDouble_Positive_DoublePresent() throws ParserException {
        String schema = "x##";
        String[] arguments = {"-x", String.valueOf(TEST_DOUBLE)};
        ArgumentsParser parser = new ArgumentsParser(schema, arguments);

        double argD = parser.getDouble('x');

        assertThat(TEST_DOUBLE, is(argD));
    }

    @Test
    public void getDouble_Negative_MissingDouble() {
        String schema = "d##";
        String[] arguments = {"-d"};
        try {
            new ArgumentsParser(schema, arguments);
        } catch (ParserException e) {
            assertEquals(MISSING_DOUBLE, e.getErrorCode());
            assertEquals('d', e.getErrorArgumentId());
        }
    }

    @Test
    public void getDouble_Negative_InvalidDouble() {
        String schema = "d##";
        String[] arguments = {"-d", TEST_STRING};

        try {
            new ArgumentsParser(schema, arguments);
        } catch (ParserException e) {
            assertEquals(INVALID_DOUBLE, e.getErrorCode());
            assertEquals('d', e.getErrorArgumentId());
        }
    }

    @Test
    public void getMultiValues_Positive_ComplexTest() throws ParserException {
        String schema = "a,b*,c#,d,e*,f#";
        String[] arguments = {"-cab", String.valueOf(TEST_INT), TEST_STRING,
                "-e", TEST_STRING, "-f", String.valueOf(TEST_INT)};
        ArgumentsParser parser = new ArgumentsParser(schema, arguments);

        boolean argA = parser.getBoolean('a');
        String argB = parser.getString('b');
        int argC = parser.getInt('c');
        boolean argD = parser.getBoolean('d');
        String argE = parser.getString('e');
        int argF = parser.getInt('f');

        assertThat(argA, is(true));
        assertThat(argB, is(TEST_STRING));
        assertThat(argC, is(TEST_INT));
        assertThat(argD, is(false));
        assertThat(argE, is(TEST_STRING));
        assertThat(argF, is(TEST_INT));
    }
}
