package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;
import java.io.IOException;

public class HistoryTest {

    @Before 
    public void setUp() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("echo foo", out);
        Jsh.eval("ls", out);
        Jsh.eval("echo `echo foobar`", out);
    }

    @Test
    public void testHistory() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("history", out);
        assertEquals("echo foo", scan.nextLine());
        assertEquals("ls", scan.nextLine());
        assertEquals("echo `echo foobar`", scan.nextLine());
        scan.close();
    }

    @Test 
    public void testHistoryWithInvalidArgs() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_history foo", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("history: too many arguments supplied"));
        }
    }
    
}
