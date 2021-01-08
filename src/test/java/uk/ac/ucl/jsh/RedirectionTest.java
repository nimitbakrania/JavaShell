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


public class RedirectionTest {

    @Rule
    public TemporaryFolder TempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException{
        Jsh.setCurrentDirectory(TempFolder.getRoot().toString());
        Jsh.eval("echo Hello world > Test1.txt", System.out);
        File tempFile = TempFolder.newFile("Test.txt");
        FileWriter tempFileWriter = new FileWriter(tempFile, StandardCharsets.UTF_8);
        tempFileWriter.write("Hello\nhello world hello everybody\nJava");
        tempFileWriter.close();

    }

    @Test
    public void inFrontTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("< Test.txt cat", out);
        Scanner scn = new Scanner(in);
        assertEquals("Hello", scn.nextLine());
        assertEquals("hello world hello everybody", scn.nextLine());
        assertEquals("Java", scn.nextLine());
        scn.close();
    }

    @Test
    public void complexInFrontTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("< Test.txt > Test2.txt head -n 2; cat Test2.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("Hello", scn.nextLine());
        assertEquals("hello world hello everybody", scn.nextLine());
        scn.close();
    }

    @Test
    public void justInTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat < Test1.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("Hello world", scn.nextLine());
        scn.close();
    } 

    @Test
    public void justOutTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("echo Java > Test1.txt", null);
        Jsh.eval("cat Test1.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("Java", scn.nextLine());
        scn.close();
    } 

    @Test
    public void inAndOutTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat < Test.txt > Test1.txt", null);
        Jsh.eval("cat Test1.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("Hello", scn.nextLine());
        assertEquals("hello world hello everybody", scn.nextLine());
        assertEquals("Java", scn.nextLine());
        scn.close();
    } 

    @Test(expected = RuntimeException.class)
    public void tooManyArgsv1Test() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat < Test1.txt Test2.txt", out);
    } 
    
    @Test(expected = RuntimeException.class)
    public void tooManyArgsv2Test() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat < Test.txt > Test2.txt Test3.txt", out);
    } 

    @Test(expected = RuntimeException.class)
    public void tooManyArgsv3Test() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat Test.txt > Test2.txt Test3.txt", out);
    } 

    @Test
    public void noSpaceTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat <Test1.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("Hello world", scn.nextLine());
        scn.close();
    }

    @Test(expected = RuntimeException.class)
    public void nonExistentInputTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat < abcdef.txt", out);
    } 

    @Test
    public void nonExistentOutputTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("echo 123 >Test10.txt", null);
        Jsh.eval("cat< Test10.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("123", scn.nextLine());
        scn.close();
    }
    
    
    @Test
    public void redirectPipeTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat < Test.txt | head -n 2", out);
        Scanner scn = new Scanner(in);
        assertEquals("Hello", scn.nextLine());
        assertEquals("hello world hello everybody", scn.nextLine());
        scn.close();
    }

    @Test
    public void redirectDoublePipeTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat < Test.txt | head -n 2 | tail -n 1", out);
        Scanner scn = new Scanner(in);
        assertEquals("hello world hello everybody", scn.nextLine());
        scn.close();
    }
    

}
