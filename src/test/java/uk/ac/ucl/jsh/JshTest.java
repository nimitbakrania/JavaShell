package uk.ac.ucl.jsh;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class JshTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUpStreams() {

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        Jsh.setCurrentDirectory(folder.getRoot().getAbsolutePath());
    }

    @Test
    public void unitTestJshWrongInput() {

        String[] args = new String[1];
        args[0] = "-c";
        Jsh.main(args);
        assertEquals("jsh: wrong number of arguments" + System.getProperty("line.separator"), outContent.toString());
    }

    @Test
    public void unitTestJshWrongInputArg() {

        String[] args = new String[2];
        args[0] = "-b";
        args[1] = "echo foo";
        Jsh.main(args);
        assertEquals("jsh: -b: unexpected arguments" + System.getProperty("line.separator") + "foo" + System.getProperty("line.separator")
                     , outContent.toString());
    }

    @Test
    public void unitTestJsh() {

        String[] args = new String[2];
        args[0] = "-c";
        args[1] = "echo foo";
        Jsh.main(args);
        assertEquals("foo" + System.getProperty("line.separator"), outContent.toString());
    }

    @Test
    public void unitTestFactoryWithInvalidApp() {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream();
            Factory factory = new Factory();
            factory.mkApplication(null, out, "fakeApp", new ArrayList<String>());
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("fakeApp: unknown application"));
        }
    }

    @After
    public void restoreStreams() {

        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
}
