package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UniqTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUpDummyData() throws IOException {

        File testFile = folder.newFile("test1.txt");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(testFile), StandardCharsets.UTF_8);
        writer.write("a\na\n2\nb\na");        
        writer.close();

        Jsh.setCurrentDirectory(folder.getRoot().getAbsolutePath());
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
            assertTrue(expected.getMessage().equals("uniq: wrong argument arg1"));
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
            assertTrue(expected.getMessage().equals("uniq: file does not exist"));
        }
    }


    @Test
    public void oneArgTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("uniq test1.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("a", scn.nextLine());
        assertEquals("2", scn.nextLine());
        assertEquals("b", scn.nextLine());
        assertEquals("a", scn.nextLine());
        scn.close();
    }
}
