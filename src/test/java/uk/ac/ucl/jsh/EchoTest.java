package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;
import java.util.ArrayList;

public class EchoTest {
    @Rule
    public TemporaryFolder TempFolder = new TemporaryFolder();
    private VisitorApplication visitor = new VisitorApplication();

    @Before
    public void EnterTempFolder() throws IOException{
        Jsh.setCurrentDirectory(TempFolder.getRoot().toString());
    }

    // UNIT TESTS
    @Test
    public void echoUnitTestOneArg() throws IOException {
    
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);    
        ArrayList<String> arr = new ArrayList<>();
        Scanner scan = new Scanner(in);
        arr.add("foo");
        Visitable.Echo app = new Visitable.Echo(null, out, arr);
        app.accept(this.visitor);
        assertEquals("foo", scan.nextLine());
       scan.close();
    }
    
    @Test
    public void echoUnitTestMultipleArgs() throws IOException {
    
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);    
        ArrayList<String> arr = new ArrayList<>();
        Scanner scan = new Scanner(in);
        arr.add("foo");
        arr.add("bar");
        Visitable.Echo app = new Visitable.Echo(null, out, arr);
        app.accept(this.visitor);
        assertEquals("foo bar", scan.nextLine());
        scan.close();
    }
    
    @Test
    public void echoUnitTestNoArgs() throws IOException {
    
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);    
        ArrayList<String> arr = new ArrayList<>();
        Scanner scan = new Scanner(in);
        Visitable.Echo app = new Visitable.Echo(null, out, arr);
        app.accept(this.visitor);
        assertTrue(scan.nextLine().isEmpty());
        scan.close();
    }

    // INTEGRATION TESTS
    @Test
    public void testEchoAppOneArg() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);        
        Jsh.eval("_echo foo", out);
        Scanner scn = new Scanner(in);
        assertEquals("foo", scn.next());
        scn.close();
    }

    @Test
    public void testEchoNoArgs() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("_echo", out);
        Scanner scan = new Scanner(in);
        assertTrue(scan.nextLine().isEmpty());
        scan.close();
    }

    @Test
    public void testEchoAppMultipleArgs() throws IOException {

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
