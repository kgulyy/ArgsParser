package com.objectmentor.utilities.args;

import com.objectmentor.utilities.args.exception.ArgsException;
import org.junit.Ignore;
import org.junit.Test;

import static com.objectmentor.utilities.args.exception.ErrorCode.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by KGuly on 22.12.2016.
 */
public class ArgsImplTest {
    private static final String TEST_STRING = "TestString";
    private static final int TEST_INT = 124;
    private static final double TEST_DOUBLE= 75.45;

    @Test
    public void constructor_Positive_WithNoSchemaOrArgument() throws ArgsException {
        String schema = "";
        String[] arguments = {};

        Args args = new ArgsImpl(schema, arguments);
        int countArgs = args.cardinality();

        assertEquals(0, countArgs);
    }

    @Test
    public void constructor_Negative_WithNoSchemaButWithOneArgument() {
        String schema = "";
        String[] arguments = {"-a"};

        try {
            new ArgsImpl(schema, arguments);
        } catch (ArgsException e) {
            assertEquals(UNEXPECTED_ARGUMENT, e.getErrorCode());
            assertEquals('a', e.getErrorArgumentId());
        }
    }

    @Test
    public void constructor_Negative_WithNoSchemaButWithMultipleArguments() {
        String schema = "";
        String[] arguments = {"-a", "-b"};

        try {
            new ArgsImpl(schema, arguments);
        } catch (ArgsException e) {
            assertEquals(UNEXPECTED_ARGUMENT, e.getErrorCode());
            assertEquals('a', e.getErrorArgumentId());
        }
    }

    @Test
    public void constructor_Negative_NonLetterSchema() {
        String schema = "*";
        String[] arguments = {};

        try {
            new ArgsImpl(schema, arguments);
        } catch (ArgsException e) {
            assertEquals(INVALID_ARGUMENT_NAME, e.getErrorCode());
            assertEquals('*', e.getErrorArgumentId());
        }
    }

    @Test
    public void constructor_Negative_InvalidArgumentFormat() {
        String schema = "a~";
        String[] arguments = {"-a~"};

        try {
            new ArgsImpl(schema, arguments);
        } catch (ArgsException e) {
            assertEquals(INVALID_ARGUMENT_FORMAT, e.getErrorCode());
            assertEquals('a', e.getErrorArgumentId());
        }
    }

    @Test
    public void constructor_Positive_SpaceInSchema() throws ArgsException {
        String schema = " x , y ";
        String[] arguments = {"-xy"};

        Args args = new ArgsImpl(schema, arguments);
        int countArgs = args.cardinality();
        boolean hasX = args.has('x');
        boolean hasY = args.has('y');

        assertEquals(2, countArgs);
        assertTrue(hasX);
        assertTrue(hasY);
    }

    @Test
    public void constructor_Positive_ValueBeforeArgument() throws ArgsException {
        String schema = "i#";
        String[] arguments = {TEST_STRING, "-i", String.valueOf(TEST_INT)};
        Args args = new ArgsImpl(schema, arguments);

        int argI = args.getInt('i');

        assertThat(argI, is(TEST_INT));
    }

    @Test
    public void has_Positive_TrueReturned() throws ArgsException {
        String schema = "t";
        String[] arguments = {"-t"};
        Args args = new ArgsImpl(schema, arguments);

        boolean hasArg = args.has('t');

        assertTrue(hasArg);
    }

    @Test
    public void has_Positive_FalseReturned() throws ArgsException {
        String schema = "f";
        String[] arguments = {};
        Args args = new ArgsImpl(schema, arguments);

        boolean hasArg = args.has('f');

        assertFalse(hasArg);
    }

    @Test
    public void getBoolean_Positive_TrueReturned() throws ArgsException {
        String schema = "t";
        String[] arguments = {"-t"};
        Args args = new ArgsImpl(schema, arguments);

        boolean argT = args.getBoolean('t');

        assertTrue(argT);
    }

    @Test
    public void getBoolean_Positive_FalseReturned() throws ArgsException {
        String schema = "f";
        String[] arguments = {};
        Args args = new ArgsImpl(schema, arguments);

        boolean argF = args.getBoolean('f');

        assertFalse(argF);
    }

    @Test
    public void getBoolean_Positive_MultipleArgs() throws ArgsException {
        String schema = "a,b";
        String[] arguments = {"-b"};
        Args args = new ArgsImpl(schema, arguments);

        boolean argA = args.getBoolean('a');
        boolean argB = args.getBoolean('b');

        assertFalse(argA);
        assertTrue(argB);
    }

    @Test
    public void getString_Positive_StringPresent() throws ArgsException {
        String schema = "s*";
        String[] arguments = {"-s", TEST_STRING};
        Args args = new ArgsImpl(schema, arguments);

        String argS = args.getString('s');

        assertThat(argS, is(TEST_STRING));
    }

    @Test
    public void getString_Positive_MultipleArgs() throws ArgsException {
        String schema = "a*,b*,c*";
        final String testArgA = "TestArgA";
        final String testArgB = "TestArgB";
        String[] arguments = {"-ba", testArgB, testArgA};
        Args args = new ArgsImpl(schema, arguments);

        String argA = args.getString('a');
        String argB = args.getString('b');
        String argC = args.getString('c');

        assertThat(argA, is(testArgA));
        assertThat(argB, is(testArgB));
        assertThat(argC, is(""));
    }

    @Test
    public void getInt_Positive_IntegerPresent() throws ArgsException {
        String schema = "i#";
        String[] arguments = {"-i", String.valueOf(TEST_INT)};
        Args args = new ArgsImpl(schema, arguments);

        int argI = args.getInt('i');

        assertThat(argI, is(TEST_INT));
    }

    @Test
    public void getInt_Negative_MissingInteger() {
        String schema = "i#";
        String[] arguments = {"-i"};

        try {
            new ArgsImpl(schema, arguments);
        } catch (ArgsException e) {
            assertEquals(MISSING_INTEGER, e.getErrorCode());
            assertEquals('i', e.getErrorArgumentId());
        }
    }

    @Test
    public void getInt_Negative_InvalidInteger() {
        String schema = "i#";
        String[] arguments = {"-i", TEST_STRING};

        try {
            new ArgsImpl(schema, arguments);
        } catch (ArgsException e) {
            assertEquals(INVALID_INTEGER, e.getErrorCode());
            assertEquals('i', e.getErrorArgumentId());
        }
    }

    @Test
    public void getInt_Positive_MultipleArgs() throws ArgsException {
        String schema = "a#,b#,c#";
        final int testArgA = 158;
        final int testArgC = 84;
        String[] arguments = {"-ac", String.valueOf(testArgA), String.valueOf(testArgC)};
        Args args = new ArgsImpl(schema, arguments);

        int argA = args.getInt('a');
        int argB = args.getInt('b');
        int argC = args.getInt('c');

        assertThat(argA, is(testArgA));
        assertThat(argB, is(0));
        assertThat(argC, is(testArgC));
    }

    @Test
    public void getDouble_Positive_DoublePresent() throws ArgsException {
        String schema = "x##";
        String[] arguments = {"-x", String.valueOf(TEST_DOUBLE)};
        Args args = new ArgsImpl(schema, arguments);

        double argD = args.getDouble('x');

        assertThat(TEST_DOUBLE, is(argD));
    }

    @Test
    public void getDouble_Negative_MissingDouble() {
        String schema = "d##";
        String[] arguments = {"-d"};
        try {
            new ArgsImpl(schema, arguments);
        } catch (ArgsException e) {
            assertEquals(MISSING_DOUBLE, e.getErrorCode());
            assertEquals('d', e.getErrorArgumentId());
        }
    }

    @Test
    public void getDouble_Negative_InvalidDouble() {
        String schema = "d##";
        String[] arguments = {"-d", TEST_STRING};

        try {
            new ArgsImpl(schema, arguments);
        } catch (ArgsException e) {
            assertEquals(INVALID_DOUBLE, e.getErrorCode());
            assertEquals('d', e.getErrorArgumentId());
        }
    }

    @Test
    public void getMultiValues_Positive_ComplexTest() throws ArgsException {
        String schema = "a,b*,c#,d,e*,f#";
        String[] arguments = {"-cab", String.valueOf(TEST_INT), TEST_STRING,
                "-e", TEST_STRING, "-f", String.valueOf(TEST_INT)};
        Args args = new ArgsImpl(schema, arguments);

        boolean argA = args.getBoolean('a');
        String argB = args.getString('b');
        int argC = args.getInt('c');
        boolean argD = args.getBoolean('d');
        String argE = args.getString('e');
        int argF = args.getInt('f');

        assertThat(argA, is(true));
        assertThat(argB, is(TEST_STRING));
        assertThat(argC, is(TEST_INT));
        assertThat(argD, is(false));
        assertThat(argE, is(TEST_STRING));
        assertThat(argF, is(TEST_INT));
    }
}
