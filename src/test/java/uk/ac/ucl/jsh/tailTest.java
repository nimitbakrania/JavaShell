package uk.ac.ucl.jsh;
import static org.junit.Assert.assertEquals;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.Test;

public class tailTest{
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @After
    public void deleteTempFolder() {
        folder.delete();
    }

    protected void createTestFile(String fileName, String toWrite) throws IOException {
        Charset encoding = StandardCharsets.UTF_8;
        File file = folder.newFile(fileName);
        if (toWrite != null) {
            FileWriter writer = new FileWriter(file, encoding);
            writer.write(toWrite);
            writer.close();
        }
    }
    
    @Test
    public void stdinTest() throws IOException{
        String oldDir = Jsh.getCurrentDirectory();
        Jsh.setCurrentDirectory(folder.getRoot().toString());
        File tempFile = folder.newFile("Test1.txt");
        File tempFile2 = folder.newFile("Test2.txt");
        File tempFile3 = folder.newFile("Test3.txt");
        File tempFile4 = folder.newFile("Test4.txt");
        File tempFile5 = folder.newFile("Test5.txt");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find -name '*.txt' | tail -n 3", out);
        Scanner scn = new Scanner(in);
        System.out.println(scn.hasNextLine());
        assertEquals(tempFile3.getAbsolutePath(), scn.nextLine());
        assertEquals(tempFile4.getAbsolutePath(), scn.nextLine());
        assertEquals(tempFile5.getAbsolutePath(), scn.nextLine());
        scn.close();
        Jsh.setCurrentDirectory(oldDir);
    } 

    @Test
    public void tailTestNoArgs() throws Exception {
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("tail", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("tail: wrong arguments"));
        }
    }


    @Test
    public void tailTestWrongNumOfArgs() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("tail arg1 arg2", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("tail: wrong arguments"));
        }
    }

    @Test
    public void tailInvalidOption() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("tail -x arg1 arg2", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("tail: wrong arguments"));
        }
    }

    @Test
    public void tailTooManyArg() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("tail -x arg1 arg2 arg3 arg4", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("tail: wrong arguments"));
        }
    }

    @Test
    public void tailInvalidNumber() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("tail -n Ten arg1", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("tail: wrong arguments"));
        }
    }


    @Test
    public void tailTestFileDoesNotExist() throws Exception {
        
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("tail -n 2 unexistentFile.txt", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("tail: wrong arguments"));
        }
    }


    @Test
    public void tailTestValidWithArgs() throws Exception {
        String toWrite = "Hello World\n";
        String toWrite2 = "Goodbye World";
        String fileName = "testTextFile.txt";
        createTestFile(fileName, toWrite + toWrite2);

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("tail -n 1 " + fileName, out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.nextLine(), toWrite2);
        scn.close();
    }

    @Test
    // Should not throw error
    public void tailLessLinesInFileThanArg() throws Exception {
        String toWrite = "Hello World\n";
        String toWrite2 = "Goodbye World";
        String fileName = "testTextFile.txt";
        createTestFile(fileName, toWrite + toWrite2);

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("tail " + fileName, out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.nextLine(), toWrite.substring(0, toWrite.length()-1));
        scn.close();
    }

}
