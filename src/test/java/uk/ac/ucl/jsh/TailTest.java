package uk.ac.ucl.jsh;
import static org.junit.Assert.assertEquals;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TailTest{
    @Rule
    public TemporaryFolder TempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException{
        Jsh.setCurrentDirectory(TempFolder.getRoot().toString());

        File tempFile = TempFolder.newFile("Test.txt");
        FileWriter tempFileWriter = new FileWriter(tempFile, StandardCharsets.UTF_8);
        tempFileWriter.write("Hello\nhello world\nJava\nTests\nExample\nTesting Head\nLorem\nipsum\ndolor\nsit\namet\nconsectetuer\nadipiscing\nelit");
        tempFileWriter.close();

        File tempFile2 = TempFolder.newFile("Test2.txt");
        FileWriter tempFileWriter2 = new FileWriter(tempFile2, StandardCharsets.UTF_8);
        tempFileWriter2.write("Hello\nhello");
        tempFileWriter2.close();
    }
    
    @Test
    public void stdinTest() throws IOException{
        File tempFile = TempFolder.newFile("Test1.txt");
        FileWriter w = new FileWriter(tempFile);
        w.write("Hello Test\nhello world test\n Test Test test \n lorem\nipsum\ndolor");;
        w.close();
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("head -n 5 " + tempFile.getName() + " | tail -n 2", out);
        Scanner scn = new Scanner(in);
        assertEquals(" lorem", scn.nextLine());
        assertEquals("ipsum", scn.nextLine());
        scn.close();
    } 

    @Test
    public void stdinUnitTest() throws IOException{
        FileInputStream stdin = new FileInputStream(new File(TempFolder.getRoot().toString(), "Test.txt"));
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("-n");
        appArgs.add("2");
        new Visitable.Tail(stdin, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals("adipiscing", scn.nextLine());
        assertEquals("elit", scn.nextLine());
        scn.close();
    } 

    @Test
    public void tailTestNoArgs() throws Exception {
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_tail", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("tail: wrong arguments"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void tailTestNoArgsUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        new Visitable.Tail(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void tailTestWrongNumOfArgs() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_tail arg1 arg2", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("tail: wrong arguments"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void tailTestWrongNumOfArgsUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("arg1");
        appArgs.add("arg2");
        new Visitable.Tail(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void tailInvalidOption() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_tail -x arg1 arg2", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("tail: wrong arguments"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void tailInvalidOptionUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("-x");
        appArgs.add("arg1");
        new Visitable.Tail(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void tailTooManyArg() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_tail -x arg1 arg2 arg3 arg4", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("tail: wrong arguments"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void tailTooManyArgUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("arg1");
        appArgs.add("arg2");
        appArgs.add("arg3");
        appArgs.add("arg4");
        new Visitable.Tail(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void tailInvalidNumber() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_tail -n Ten arg1", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("tail: wrong arguments"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void tailInvalidNumberUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("-n");
        appArgs.add("Ten");
        appArgs.add("arg1");
        new Visitable.Tail(null, out, appArgs).accept(new VisitorApplication());
    } 



    @Test
    public void tailTestFileDoesNotExist() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_tail -n 2 unexistentFile.txt", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("tail: wrong arguments"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void tailTestFileDoesNotExistUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("-n");
        appArgs.add("2");
        appArgs.add("unexistentFile.txt");
        new Visitable.Tail(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void negativeTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("_tail -n -10 Test.txt", out);
        }
        catch(IOException e){
            assertEquals("tail: wrong argument -10", e.toString());
        }
    }

    @Test
    public void absoluteTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        File file = new File(TempFolder.getRoot().toString(), "Test2.txt");
        Jsh.eval("_tail -n 2 '" + file.toString() + "'", out);
        Scanner scn = new Scanner(in);
        assertEquals("Hello", scn.nextLine());
        assertEquals("hello", scn.nextLine());
        scn.close();
    }
}
