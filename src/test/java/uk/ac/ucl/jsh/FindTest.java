package uk.ac.ucl.jsh;

import org.junit.After;
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


public class FindTest {
    /*
    Valid: find -name file, find path -name file, find path -name file with *
    Invalid: 2nd last arg not -name, args size != 2 or 3
    */

    @Rule
    public TemporaryFolder TempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException{
        Jsh.setCurrentDirectory(TempFolder.getRoot().toString());

        File tempFile = TempFolder.newFile("Test.txt");
        FileWriter tempFileWriter = new FileWriter(tempFile, StandardCharsets.UTF_8);
        tempFileWriter.write("Hello");
        tempFileWriter.close();

        File tempFolder2 = TempFolder.newFolder("dir2");
        
        File tempFile2 = new File(tempFolder2, "Test2.txt");
        FileWriter tempFileWriter2 = new FileWriter(tempFile2, StandardCharsets.UTF_8);
        tempFileWriter2.write("Hello");
        tempFileWriter2.close();
    }
    
    @Test
    public void invalidArgsNum() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("_find dir2 -name Test.txt Test2.txt", out);
        }
        catch(IOException e){
            assertEquals("find: Wrong number of arguments", e.toString());
        }
    }
        
    @Test(expected = RuntimeException.class)
    public void invalidArgsNumUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("dir2");
        appArgs.add("-name");
        appArgs.add("Test.txt");
        appArgs.add("Test2.txt");
        new Visitable.Find(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void noNameArgTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("_find name Test.txt", out);
        }
        catch(IOException e){
            assertEquals("find: Wrong argument name", e.toString());
        }
    }

    @Test(expected = RuntimeException.class)
    public void noNameArgUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("name");
        appArgs.add("Test.txt");
        new Visitable.Find(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void twoArgsTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find -name Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("./Test.txt", scn.nextLine());
        scn.close();
    }

    @Test
    public void twoArgsUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("-name");
        appArgs.add("Test.txt");
        new Visitable.Find(null, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals("./Test.txt", scn.nextLine());
        scn.close();
    } 

    @Test
    public void threeArgsAbsoluteTest() throws IOException{
        String cmdline = "find '" + TempFolder.getRoot().getAbsolutePath() + "' -name Test.txt";
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval(cmdline, out);
        Scanner scn = new Scanner(in);
        assertEquals(new File(TempFolder.getRoot().getAbsolutePath(), "Test.txt").getAbsolutePath(), scn.nextLine());
        scn.close();
    }

    @Test
    public void threeArgsAbsoluteUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add(TempFolder.getRoot().getAbsolutePath());
        appArgs.add("-name");
        appArgs.add("Test.txt");
        new Visitable.Find(null, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals(new File(TempFolder.getRoot().getAbsolutePath(), "Test.txt").getAbsolutePath(), scn.nextLine());
        scn.close();
    } 

    @Test
    public void threeArgsRelativeTest() throws IOException{
        String cmdline = "find dir2 -name Test2.txt";
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval(cmdline, out);
        Scanner scn = new Scanner(in);
        assertEquals("dir2/Test2.txt", scn.nextLine());
        scn.close();
    }

    @Test
    public void threeArgsRelativeUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("dir2");
        appArgs.add("-name");
        appArgs.add("Test2.txt");
        new Visitable.Find(null, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals("dir2/Test2.txt", scn.nextLine());
        scn.close();
    } 

    @Test
    public void asteriskTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find -name 'T*.txt'", out);
        Scanner scn = new Scanner(in);
        String next = scn.nextLine();
        if (next.equals("./dir2/Test2.txt")){
            assertEquals("./dir2/Test2.txt", next);
            assertEquals("./Test.txt", scn.nextLine());
        }
        else{
            assertEquals("./dir2/Test2.txt", scn.nextLine());
            assertEquals("./Test.txt", next);
        }
        scn.close();
    }

    @Test
    public void asteriskUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("-name");
        appArgs.add("T*.txt");
        new Visitable.Find(null, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        String next = scn.nextLine();
        if (next.equals("./dir2/Test2.txt")){
            assertEquals("./dir2/Test2.txt", next);
            assertEquals("./Test.txt", scn.nextLine());
        }
        else{
            assertEquals("./dir2/Test2.txt", scn.nextLine());
            assertEquals("./Test.txt", next);
        }
        scn.close();
    } 
}