package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;


public class findTest {
    /*
    Valid: find -name file, find path -name file, find path -name file with *
    Invalid: 2nd last arg not -name, args size != 2 or 3
    */

    @Rule
    public TemporaryFolder TempFolder = new TemporaryFolder();

    @Before
    public void EnterTempFolder() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cd" + TempFolder.getRoot(), out);
    }
    @Test
    public void invalidArgsNumName() throws IOException{
        File tempFile = TempFolder.newFile("Test.txt");
        
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("find Test.txt -name abc.txt a.txt", out);
        }
        catch(IOException e){
            assertEquals("find: wrong number of arguments", e.toString());
        }
    }

    @Test
    public void noNameArgTest() throws IOException{
        File tempFile = TempFolder.newFile("Test.txt");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("find name Test.txt", out);
        }
        catch(IOException e){
            assertEquals("head: invalid argument name", e.toString());
        }
    }


    @Test
    public void twoArgsTest() throws IOException{
        File tempFile = TempFolder.newFile("Test.txt");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find -name Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(tempFile.getAbsolutePath(), scn.nextLine());
        scn.close();
    }

    @Test
    public void threeArgsTest() throws IOException{
        File tempFolder2 = TempFolder.newFolder("Test");
        File tempFile = new File(tempFolder2, "Test.txt");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find \\Test -name Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(tempFile.getAbsolutePath(), scn.nextLine());
        scn.close();
    }

    @Test
    public void asteriskTest() throws IOException{
        File tempFile = TempFolder.newFile("Test.txt");
        File tempFolder2 = TempFolder.newFolder("Test");
        File tempFile2 = new File(tempFolder2, "Test2.txt");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find -name T*.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(tempFile.getAbsolutePath(), scn.nextLine());
        assertEquals(tempFile2.getAbsolutePath(), scn.nextLine());
        scn.close();
    }
}
