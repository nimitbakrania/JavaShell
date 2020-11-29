package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

public class EchoTest {
    
    @Test
    public void testEchoAppOneArg() throws Exception {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);        
        Jsh.eval("echo foo", out);
        Scanner scn = new Scanner(in);
        assertEquals("foo", scn.next());
        scn.close();
    }

    @Test
    public void testEchoNoArgs() throws Exception {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("echo", out);
        Scanner scan = new Scanner(in);
        assertNull(scan.next());
        scan.close();
    }

    @Test
    public void testEchoAppMultipleArgs() throws Exception {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);    
        Jsh.eval("echo foo bar foobar", out);
        Scanner scn = new Scanner(in);
        assertEquals("foo", scn.next());
        assertEquals("bar", scn.next());
        assertEquals("foobar", scn.next());
        scn.close();
    }

}
