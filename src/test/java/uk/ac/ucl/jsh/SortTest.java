package uk.ac.ucl.jsh;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SortTest {
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
    public void sortTestWrongNumOfArgs() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("sort arg1 arg2", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("sort: wrong argument arg1"));
        }
    }

    @Test
    public void sortInvalidOption() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("sort -t test.txt", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("sort: wrong argument " + "-t"));
        }
    }

    @Test
    public void sortTooManyArg() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("sort -x arg1 arg2 arg3 arg4", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("sort: too many arguments"));
        }
    }



    @Test
    public void sortTestFileDoesNotExist() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("sort test123.txt", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("sort: file does not exist"));
        }
    }


    @Test
    public void oneArgTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort test1.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("2", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("b", scn.nextLine());
        scn.close();
    }
}
