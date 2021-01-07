package uk.ac.ucl.jsh;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


public class CatTest{
    @Rule
    public TemporaryFolder TempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException{
        Jsh.setCurrentDirectory(TempFolder.getRoot().toString());

        File tempFile = TempFolder.newFile("Test.txt");
        File tempFile2 = TempFolder.newFile("testTextFile.txt");
        File tempFile3 = TempFolder.newFile("Empty.txt");
        FileWriter tempFileWriter = new FileWriter(tempFile, StandardCharsets.UTF_8);
        tempFileWriter.write("a\na\n2\nb\na");
        tempFileWriter.close();

    }

    @Test
    public void catTestNoArg() throws Exception {
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_cat", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("cat: missing arguments"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void catTestNoArgUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        new Visitable.Cat(null, out, appArgs).accept(new VisitorApplication());
    }

    @Test
    public void catTestInvalidFile() throws Exception {
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_cat Invalid", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("cat: file does not exist"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void catTestInvalidFileUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("Invalid");
        new Visitable.Cat(null, out, appArgs).accept(new VisitorApplication());
    }

    @Test
    public void catDirectory() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("_cat src", out);
    }

    @Test(expected = RuntimeException.class)
    public void catDirectoryUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("src");
        new Visitable.Cat(null, out, appArgs).accept(new VisitorApplication());
    }

    @Test
    public void catValidDirectory() throws Exception {
        String folderName = "src";
        TempFolder.newFolder(folderName);
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("_cat " + folderName, out);
    }

    @Test(expected = RuntimeException.class)
    public void catValidDirectoryUnitTest() throws IOException{
        String folderName = "src";
        TempFolder.newFolder(folderName);
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("src");
        new Visitable.Cat(null, out, appArgs).accept(new VisitorApplication());
    }

    @Test
    public void catSecondArgInvalid() throws Exception {
        String fileName = "testTextFile.txt";
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("_cat " + fileName + " Invalid", out);
    }

    @Test(expected = RuntimeException.class)
    public void ccatSecondArgInvalidUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("testTextFile");
        appArgs.add(" Invalid");
        appArgs.add(out.toString());
        new Visitable.Cat(null, out, appArgs).accept(new VisitorApplication());
    }

    @Test
    public void catEmptyFile() throws Exception {
        String fileName = "Empty.txt";
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("_cat " + fileName, out);
        out.close();
        String contents = new String(in.readAllBytes());
        assertEquals("file not empty", "", contents);
    }


    @Test
    public void oneArgTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cat Test.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("2", scn.nextLine());
        assertEquals("b", scn.nextLine());
        assertEquals("a", scn.nextLine());
        scn.close();
    }

    @Test
    public void oneArgUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("Test.txt");
        new Visitable.Cat(null, out, appArgs).accept(new VisitorApplication());
        Scanner scn = new Scanner(in);
        assertEquals("a", scn.nextLine());
        assertEquals("a", scn.nextLine());
        assertEquals("2", scn.nextLine());
        assertEquals("b", scn.nextLine());
        assertEquals("a", scn.nextLine());
        scn.close();
    }
}
