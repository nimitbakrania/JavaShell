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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class uniqTest {
    @Rule
    public TemporaryFolder TempFolder = new TemporaryFolder();

    @Before
    public void EnterTempFolder() throws IOException{
        Jsh.setCurrentDirectory(TempFolder.getRoot().toString());
    }


    @Test
    public void uniqTestNoArgs() throws Exception {
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("uniq", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("uniq: missing arguments"));
        }
    }


    @Test
    public void uniqTestWrongNumOfArgs() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("uniq arg1 arg2", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("uniq: wrong argument " + "args"));
        }
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



    @Test
    public void uniqTestFileDoesNotExist() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("uniq test123.txt", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("uniq: "+ "test123.txt" + " does not exist"));
        }
    }


    @Test
    public void oneArgTest() throws IOException{
        File tempFile = TempFolder.newFile("Testuniq.txt");
        FileWriter tempFileWriter = new FileWriter(tempFile, StandardCharsets.UTF_8);
        tempFileWriter.write("a\na\n2\nb\nb\na");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("uniq Testsort.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("a", scn.nextLine());
        assertEquals("2", scn.nextLine());
        assertEquals("b", scn.nextLine());
        assertEquals("a", scn.nextLine());
        scn.close();
        tempFileWriter.close();
    }
}
