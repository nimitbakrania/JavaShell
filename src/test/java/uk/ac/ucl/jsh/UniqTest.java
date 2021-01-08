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

public class UniqTest {
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
    public void uniqTestStdin() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("uniq < Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("a", scn.nextLine());
        assertEquals("2", scn.nextLine());
        assertEquals("b", scn.nextLine());
        assertEquals("a", scn.nextLine());
        scn.close();
    }
/*
    @Test
    public void uniqTestStdinWithI() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("uniq -i < Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("a", scn.nextLine());
        assertEquals("2", scn.nextLine());
        assertEquals("b", scn.nextLine());
        assertEquals("a", scn.nextLine());
        scn.close();
    }
*/
    @Test
    public void uniqTestWrongNumOfArgs() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("uniq arg1 arg2", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("uniq: wrong argument arg1"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void uniqTestWrongNumOfArgsUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("arg1");
        appArgs.add("arg2");
        new Visitable.Uniq(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void uniqInvalidOption() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("uniq -t test.txt", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("uniq: wrong argument " + "-t"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void uniqInvalidOptionUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("-t");
        appArgs.add("test.txt");
        new Visitable.Uniq(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void uniqTooManyArg() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("uniq -x arg1 arg2 arg3 arg4", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("uniq: too many arguments"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void uniqTooManyArgUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("-x");
        appArgs.add("arg1");
        appArgs.add("arg2");
        appArgs.add("arg3");
        appArgs.add("arg4");
        new Visitable.Uniq(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void uniqTestFileDoesNotExist() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("uniq test123.txt", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("uniq: file does not exist"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void uniqTestFileDoesNotExistUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("test123.txt");
        new Visitable.Uniq(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void oneArgTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("uniq Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("a", scn.nextLine());
        assertEquals("2", scn.nextLine());
        assertEquals("b", scn.nextLine());
        assertEquals("a", scn.nextLine());
        scn.close();
    }

    @Test
    public void oneArgUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("Test.txt");
        new Visitable.Uniq(null, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals("a", scn.nextLine());
        assertEquals("2", scn.nextLine());
        assertEquals("b", scn.nextLine());
        assertEquals("a", scn.nextLine());
        scn.close();
    }

    @Test
    public void twoArgTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("uniq -i Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("a", scn.nextLine());
        assertEquals("2", scn.nextLine());
        assertEquals("b", scn.nextLine());
        assertEquals("a", scn.nextLine());
        scn.close();
    }
}
