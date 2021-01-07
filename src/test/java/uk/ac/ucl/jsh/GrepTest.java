package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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


public class GrepTest {
    // Needs to test 0 args 2 args >2 args, invalid file name, valid
    @Rule
    public TemporaryFolder TempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException{
        Jsh.setCurrentDirectory(TempFolder.getRoot().toString());

        File tempFile = TempFolder.newFile("Test.txt");
        FileWriter tempFileWriter = new FileWriter(tempFile, StandardCharsets.UTF_8);
        tempFileWriter.write("Hello\nhello world hello everybody\nJava");
        tempFileWriter.close();

        File tempFile2 = TempFolder.newFile("Test2.txt");
        FileWriter tempFileWriter2 = new FileWriter(tempFile2, StandardCharsets.UTF_8);
        tempFileWriter2.write("Hello\nhello world tempfile2\nhello world Java");
        tempFileWriter2.close();
    }

    @Test
    public void stdinTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("head -n 2 Test.txt | grep world", out);
        Scanner scn = new Scanner(in);
        assertEquals("hello world hello everybody", scn.nextLine());
        scn.close();
    } 
    
    @Test
    public void stdinUnitTest() throws IOException{
        FileInputStream stdin = new FileInputStream(new File(TempFolder.getRoot().toString(), "Test.txt"));
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("world");
        new Visitable.Grep(stdin, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals("hello world hello everybody", scn.nextLine());
        scn.close();
    } 


    @Test
    public void invalidFileName() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("_grep 'hello world' Test1.txt", out);
        }
        catch(IOException e){
            assertEquals("grep: wrong file argument", e.toString());
        }
    }
    
    @Test(expected = RuntimeException.class)
    public void invalidFileNameUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("hello world");
        appArgs.add("Test1.txt");
        new Visitable.Grep(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void twoArgTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("_grep 'hello world' Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.nextLine(), "hello world hello everybody");
        scn.close();
    }
        
    @Test
    public void twoArgUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("hello world");
        appArgs.add("Test.txt");
        new Visitable.Grep(null, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals(scn.nextLine(), "hello world hello everybody");
        scn.close();
    } 


    @Test
    public void noArgsTest() throws IOException{ //error one
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("_grep", out);
        }
        catch(IOException e){
            assertEquals("grep: no arguments given", e.toString());
        }
    }
        
    @Test(expected = RuntimeException.class)
    public void noArgsUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        new Visitable.Grep(null, out, appArgs).accept(new VisitorApplication());
    } 


    @Test
    public void manyArgsTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("_grep 'hello world' Test.txt Test2.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.nextLine(), "Test.txt:hello world hello everybody");
        assertEquals(scn.nextLine(), "Test2.txt:hello world tempfile2");
        assertEquals(scn.nextLine(), "Test2.txt:hello world Java");
        scn.close();
    }
   
    @Test
    public void manyArgsUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("hello world");
        appArgs.add("Test.txt");
        appArgs.add("Test2.txt");
        new Visitable.Grep(null, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals(scn.nextLine(), "Test.txt:hello world hello everybody");
        assertEquals(scn.nextLine(), "Test2.txt:hello world tempfile2");
        assertEquals(scn.nextLine(), "Test2.txt:hello world Java");
        scn.close();
    } 
}
