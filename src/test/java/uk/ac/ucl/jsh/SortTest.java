package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class SortTest {
    @Rule
    public TemporaryFolder TempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException{
        Jsh.setCurrentDirectory(TempFolder.getRoot().toString());

        File tempFile = TempFolder.newFile("Test.txt");
        FileWriter tempFileWriter = new FileWriter(tempFile, StandardCharsets.UTF_8);
        tempFileWriter.write("a\na\n2\nb\na");
        tempFileWriter.close();

    }

    @Test
    public void sortTestStdin() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort < Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("2", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("b", scn.nextLine());
        scn.close();
    }
/*
    @Test
    public void sortTestStdinWithI() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort -r < Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("b", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("2", scn.nextLine());
        scn.close();
    }
*/
    @Test
    public void sortTestWrongNumOfArgs() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_sort arg1 arg2", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("sort: wrong argument arg1"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void sortTestWrongNumOfArgsUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("arg1");
        appArgs.add("arg2");
        new Visitable.Sort(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void sortInvalidOption() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_sort -t test.txt", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("sort: wrong argument " + "-t"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void sortInvalidOptionUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("-t");
        appArgs.add("test.txt");
        new Visitable.Sort(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void sortTooManyArg() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_sort -x arg1 arg2 arg3 arg4", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("sort: too many arguments"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void sortTooManyArgUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("arg1");
        appArgs.add("arg2");
        appArgs.add("arg3");
        appArgs.add("arg4");
        new Visitable.Sort(null, out, appArgs).accept(new VisitorApplication());
    } 


    @Test
    public void sortTestFileDoesNotExist() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_sort test123.txt", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("sort: file does not exist"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void sortTestFileDoesNotExistUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("test123.txt");
        new Visitable.Sort(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void oneArgTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("2", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("b", scn.nextLine());
        scn.close();
    }

    @Test
    public void oneArgUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("Test.txt");
        new Visitable.Sort(null, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals("2", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("b", scn.nextLine());
        scn.close();
    }

    @Test
    public void twoArgTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort -r Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("b", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("2", scn.nextLine());
        scn.close();
    }
}
