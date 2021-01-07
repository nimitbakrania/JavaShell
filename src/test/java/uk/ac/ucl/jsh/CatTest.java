package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.Before;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


public class CatTest{
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

    @Test
    public void catDirectory() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("_cat src", out);
    }

    @Test
    public void catValidDirectory() throws Exception {
        String folderName = "src";
        folder.newFolder(folderName);
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("_cat " + folderName, out);
    }

    @Test
    public void catSecondArgInvalid() throws Exception {
        String fileName = "testTextFile.txt";
        createTestFile(fileName, null);
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("_cat " + fileName + " Invalid", out);
    }

    @Test
    public void catEmptyFile() throws Exception {
        String fileName = "testTextFile.txt";
        createTestFile(fileName, null);
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("_cat " + fileName, out);
        out.close();
        String contents = new String(in.readAllBytes());
        assertEquals("file not empty", "", contents);
    }

    @Test
    public void catOneFile() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("_cat test1.txt", out);
        out.close();
        String contents = new String(in.readAllBytes());
        assertEquals("File contents wrong", "a\na\n2\nb\na\n", contents);
    }
}
