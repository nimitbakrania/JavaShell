package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

public class JshTest {

    public JshTest() {
    }

    @Test
    public void testEchoAppOneArg() throws Exception {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);        
        Jsh.eval("echo foo", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(), "foo");
        scn.close();
    }

    @Test
    public void testEchoNoArgs() throws Exception {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("echo", out);

    }

    @Test
    public void testEchoAppMultipleArgs() throws Exception {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);    
        Jsh.eval("echo foo bar foobar", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(), "foo bar foobar");
        scn.close();
    }

    @Test 
    public void testLs() throws Exception {

        
    }

    @Test
    public void testJsh() throws Exception {
        // ANIRHDh - Change this so that its a system test. It tests each application in jsh to make sure they all work.
    }

}
