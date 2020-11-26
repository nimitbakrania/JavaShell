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
import java.util.Scanner;


public class grepTest {
    // Needs to test 0 args 2 args >2 args, invalid file name, valid
    @Rule
    public TemporaryFolder TempFolder = new TemporaryFolder();

    @Before
    public void EnterTempFolder() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cd" + TempFolder.getRoot(), out);
    }
    //piped
    //piped
    //eval cd

    @Test
    public void invalidFileName() throws IOException{
        File tempFile = TempFolder.newFile("Test1.txt");
        FileWriter tempFileWriter = new FileWriter(tempFile, StandardCharsets.UTF_8);
        tempFileWriter.write("Hello\nhello world");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("grep 'hello world' Test.txt", out);
        }
        catch(IOException e){
            tempFileWriter.close();
            assertEquals("grep: wrong file argument", e.toString());
        }
        tempFileWriter.close();
    }

    @Test
    public void twoArgTest() throws IOException{
        File tempFile = TempFolder.newFile("Test.txt");
        FileWriter tempFileWriter = new FileWriter(tempFile, StandardCharsets.UTF_8);
        tempFileWriter.write("Hello\nhello world hello everybody\nJava");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("grep 'hello world' Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.nextLine(), "Nullam dictum felis eu pede mollis pretium. Integer tincidunt. hello world Cras dapibus.");
        scn.close();
        tempFileWriter.close();
    }

    @Test
    public void noArgsTest() throws IOException{ //error one
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("grep", out);
        }
        catch(IOException e){
            assertEquals("grep: no arguments given", e.toString());
        }
    }

    @Test
    public void manyArgsTest() throws IOException{
        File tempFile = TempFolder.newFile("Test.txt");
        FileWriter tempFileWriter = new FileWriter(tempFile, StandardCharsets.UTF_8);
        tempFileWriter.write("Hello\nhello world hello everybody\nJava");
        
        File tempFile2 = TempFolder.newFile("Test2.txt");
        FileWriter tempFileWriter2 = new FileWriter(tempFile2, StandardCharsets.UTF_8);
        tempFileWriter2.write("Hello\nhello world tempfile2\nhello world Java");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("grep 'hello world' Test.txt Test2.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.nextLine(), "Test.txt:hello world hello everybody");
        assertEquals(scn.nextLine(), "Test2.txt:hello world tempfile2");
        assertEquals(scn.nextLine(), "Test2.txt:hello world Java");
        scn.close();
        tempFileWriter.close();
        tempFileWriter2.close();
    }

}
