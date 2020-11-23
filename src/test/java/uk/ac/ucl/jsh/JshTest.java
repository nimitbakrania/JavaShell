package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

public class JshTest {

    private PipedInputStream in = new PipedInputStream();
    private PipedOutputStream out = new PipedOutputStream(in);

    public JshTest() {
    }

    @Test
    public void testEchoAppOneArg() throws Exception {

        Jsh.eval("echo foo", this.out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(), "foo");
        scn.close();
    }

    @Test
    public void testEchoAppMultipleArgs() throws Exception {

        
    }

    @Test
    public void testJsh() throws Exception {
        // ANIRHDh - Change this so that its a system test. It tests each application in jsh to make sure they all work.
    }

}
