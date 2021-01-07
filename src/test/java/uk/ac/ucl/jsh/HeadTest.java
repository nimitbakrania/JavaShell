package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import uk.ac.ucl.jsh.Visitable.Find;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;


public class HeadTest {
    /*
    Valid: head -n num file, head file, num>
    Invalid: args > 3, args = 3 and arg[0] not -n, -n non-integer, -n negative, invalid file name
    */

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
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("tail -n 4 Test.txt" + " | head -n 2", out);
        Scanner scn = new Scanner(in);
        assertEquals("amet", scn.nextLine());
        assertEquals("consectetuer", scn.nextLine());
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
        new Visitable.Head(stdin, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals("Hello", scn.nextLine());
        assertEquals("hello world", scn.nextLine());
        scn.close();
    } 

    @Test
    public void invalidFileName() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("_head Test1.txt", out);
        }
        catch(IOException e){
            assertEquals("head: Test1.txt does not exist", e.toString());
        }
    }

    @Test(expected = RuntimeException.class)
    public void invalidFileNameUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("Test1.txt");
        new Visitable.Head(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void tooManyArgsTest() throws IOException{ 
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("_head -n 15 Test.txt Test2.txt", out);
        }
        catch(IOException e){
            assertEquals("head: too many arguments", e.toString());
        }
    }
        
    @Test(expected = RuntimeException.class)
    public void tooManyArgsUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("Test.txt");
        appArgs.add("Test2.txt");
        new Visitable.Head(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void noNThreeArgsTest() throws IOException{ 
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("_head n 15 Test.txt", out);
        }
        catch(IOException e){
            assertEquals("head: wrong argument n", e.toString());
        }
    }
    
    @Test(expected = RuntimeException.class)
    public void noNThreeArgsUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("n");
        appArgs.add("Test.txt");
        new Visitable.Head(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void nonIntegerTest() throws IOException{ 
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("_head -n ab Test.txt", out);
        }
        catch(IOException e){
            assertEquals("head: wrong argument ab", e.toString());
        }
    }
    
    @Test(expected = RuntimeException.class)
    public void nonIntegerUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("-n");
        appArgs.add("ab");
        appArgs.add("Test.txt");
        new Visitable.Head(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void negativeTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("_head -n -10 Test.txt", out);
        }
        catch(IOException e){
            assertEquals("head: wrong argument -10", e.toString());
        }
    }
        
    @Test(expected = RuntimeException.class)
    public void negativeUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("-n");
        appArgs.add("-10");
        appArgs.add("Test.txt");
        new Visitable.Head(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void threeArgsTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("head -n 3 Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("Hello", scn.nextLine());
        assertEquals("hello world", scn.nextLine());
        assertEquals("Java", scn.nextLine());
        scn.close();
    }

    @Test
    public void threeArgsUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("-n");
        appArgs.add("3");
        appArgs.add("Test.txt");
        new Visitable.Head(null, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals("Hello", scn.nextLine());
        assertEquals("hello world", scn.nextLine());
        assertEquals("Java", scn.nextLine());
        scn.close();
    } 

    @Test
    public void oneArgTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("head Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("Hello", scn.nextLine());
        assertEquals("hello world", scn.nextLine());
        assertEquals("Java", scn.nextLine());
        assertEquals("Tests", scn.nextLine());
        assertEquals("Example", scn.nextLine());
        assertEquals("Testing Head", scn.nextLine());
        assertEquals("Lorem", scn.nextLine());
        assertEquals("ipsum", scn.nextLine());
        assertEquals("dolor", scn.nextLine());
        assertEquals("sit", scn.nextLine());
        scn.close();
    }

    @Test
    public void oneArgUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("Test.txt");
        new Visitable.Head(null, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals("Hello", scn.nextLine());
        assertEquals("hello world", scn.nextLine());
        assertEquals("Java", scn.nextLine());
        assertEquals("Tests", scn.nextLine());
        assertEquals("Example", scn.nextLine());
        assertEquals("Testing Head", scn.nextLine());
        assertEquals("Lorem", scn.nextLine());
        assertEquals("ipsum", scn.nextLine());
        assertEquals("dolor", scn.nextLine());
        assertEquals("sit", scn.nextLine());
        scn.close();
    } 

}
