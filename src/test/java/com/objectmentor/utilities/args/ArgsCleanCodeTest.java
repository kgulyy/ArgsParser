package com.objectmentor.utilities.args;

import com.objectmentor.utilities.args.exception.ArgsException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by KGuly on 23.12.2016.
 */
public class ArgsCleanCodeTest {
    private static final String TEST_STRING = "TestString";
    private static final int TEST_INT = 124;

    @SuppressWarnings("unused")
    public void test_negative_pattern() {
        String schema = "1";
        String[] arguments = {"-1"};

        String errorMessage = "";
        try {
            new ArgsCleanCode(schema, arguments);
        } catch (ArgsException e) {
            errorMessage = e.getMessage();
        }

        assertThat(errorMessage, is("'1' is not a valid argument name."));
    }

    @Test(expected = ArgsException.class)
    public void constructor_Negative_InvalidArgumentName() throws ArgsException {
        String schema = "1";
        String[] arguments = {"-1"};

        new ArgsCleanCode(schema, arguments);
    }

    @Test(expected = ArgsException.class)
    public void constructor_Negative_InvalidArgumentFormat() throws ArgsException {
        String schema = "a@";
        String[] arguments = {"-a@"};

        new ArgsCleanCode(schema, arguments);
    }

    @Test(expected = ArgsException.class)
    public void constructor_Negative_UnexpectedArgument() throws ArgsException {
        String schema = "a";
        String[] arguments = {"-b"};

        new ArgsCleanCode(schema, arguments);
    }

    @Test
    public void constructor_Positive_ValueBeforeArgument() throws ArgsException {
        String schema = "i#";
        String[] arguments = {TEST_STRING, "-i", String.valueOf(TEST_INT)};
        Args args = new ArgsCleanCode(schema, arguments);

        int argI = args.getInt('i');

        assertThat(argI, is(TEST_INT));
    }

    @Test
    public void has_Positive_TrueReturned() throws ArgsException {
        String schema = "t";
        String[] arguments = {"-t"};
        Args args = new ArgsCleanCode(schema, arguments);

        boolean hasArg = args.has('t');

        assertTrue(hasArg);
    }

    @Test
    public void has_Positive_FalseReturned() throws ArgsException {
        String schema = "f";
        String[] arguments = {};
        Args args = new ArgsCleanCode(schema, arguments);

        boolean hasArg = args.has('f');

        assertFalse(hasArg);
    }

    @Test
    public void getBoolean_Positive_TrueReturned() throws ArgsException {
        String schema = "t";
        String[] arguments = {"-t"};
        Args args = new ArgsCleanCode(schema, arguments);

        boolean argT = args.getBoolean('t');

        assertTrue(argT);
    }

    @Test
    public void getBoolean_Positive_FalseReturned() throws ArgsException {
        String schema = "f";
        String[] arguments = {};
        Args args = new ArgsCleanCode(schema, arguments);

        boolean argF = args.getBoolean('f');

        assertFalse(argF);
    }

    @Test
    public void getBoolean_Positive_MultipleArgs() throws ArgsException {
        String schema = "a,b,c,d";
        String[] arguments = {"-db"};
        Args args = new ArgsCleanCode(schema, arguments);

        boolean argA = args.getBoolean('a');
        boolean argB = args.getBoolean('b');
        boolean argC = args.getBoolean('c');
        boolean argD = args.getBoolean('d');

        assertFalse(argA);
        assertTrue(argB);
        assertFalse(argC);
        assertTrue(argD);
    }

    @Test(expected = ArgsException.class)
    public void getString_Negative_MissingString() throws ArgsException {
        String schema = "s*";
        String[] arguments = {"-s"};
        Args args = new ArgsCleanCode(schema, arguments);

        args.getString('s');
    }

    @Test
    public void getString_Positive_TestStringReturned() throws ArgsException {
        String schema = "s*";
        String[] arguments = {"-s", TEST_STRING};
        Args args = new ArgsCleanCode(schema, arguments);

        String argS = args.getString('s');

        assertThat(argS, is(TEST_STRING));
    }

    @Test
    public void getString_Positive_MultipleArgs() throws ArgsException {
        String schema = "a*,b*,c*";
        final String testArgA = "TestArgA";
        final String testArgB = "TestArgB";
        String[] arguments = {"-ba", testArgB, testArgA};
        Args args = new ArgsCleanCode(schema, arguments);

        String argA = args.getString('a');
        String argB = args.getString('b');
        String argC = args.getString('c');

        assertThat(argA, is(testArgA));
        assertThat(argB, is(testArgB));
        assertThat(argC, is(""));
    }

    @Test(expected = ArgsException.class)
    public void getInt_Negative_MissingInteger() throws ArgsException {
        String schema = "i#";
        String[] arguments = {"-i"};
        Args args = new ArgsCleanCode(schema, arguments);

        args.getInt('i');
    }

    @Test(expected = ArgsException.class)
    public void getInt_Negative_InvalidInteger() throws ArgsException {
        String schema = "i#";
        String[] arguments = {"-i", TEST_STRING};
        Args args = new ArgsCleanCode(schema, arguments);

        args.getInt('i');
    }

    @Test
    public void getInt_Positive_TestIntReturned() throws ArgsException {
        String schema = "i#";
        String[] arguments = {"-i", String.valueOf(TEST_INT)};
        Args args = new ArgsCleanCode(schema, arguments);

        int argI = args.getInt('i');

        assertThat(argI, is(TEST_INT));
    }

    @Test
    public void getInt_Positive_MultipleArgs() throws ArgsException {
        String schema = "a#,b#,c#";
        final int testArgA = 158;
        final int testArgC = 84;
        String[] arguments = {"-ac", String.valueOf(testArgA), String.valueOf(testArgC)};
        Args args = new ArgsCleanCode(schema, arguments);

        int argA = args.getInt('a');
        int argB = args.getInt('b');
        int argC = args.getInt('c');

        assertThat(argA, is(testArgA));
        assertThat(argB, is(0));
        assertThat(argC, is(testArgC));
    }

    @Test
    public void getMultiValues_Positive_ComplexTest() throws ArgsException {
        String schema = "a,b*,c#,d,e*,f#";
        String[] arguments = {"-cab", String.valueOf(TEST_INT), TEST_STRING,
                "-e", TEST_STRING, "-f", String.valueOf(TEST_INT)};
        Args args = new ArgsCleanCode(schema, arguments);

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