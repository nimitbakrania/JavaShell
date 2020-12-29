package uk.ac.ucl.jsh;
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

public class CdTest{
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
    public void cdNoArgs() throws Exception {
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("cd", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("cd: missing argument"));
        }
        
    }

    @Test
    public void cdTooManyArgs() throws Exception {
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("cd arg1 arg2", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("cd: too many arguments"));
        }
    }

    @Test
    public void cdDirectoryNotExists() throws Exception {
        try{
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("cd unexistent", out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("cd: " + "unexistent" + " is not an existing directory"));
        }
    }

    @Test
    public void cdToFileNotDirectory() throws Exception {
        
        try{
            String fileName = "testTextFile.txt";
            createTestFile(fileName, null);
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("cd " + fileName, out);
        }
        catch(RuntimeException expected)
        {
            assertTrue(expected.getMessage().equals("cd: " + "testTextFile.txt" + " is not an existing directory"));
        }
    }

    @Test
    //given a valid directory cd should not produce any error
    public void cdValidInput() throws Exception {
        String folderName = "testFolder";
        folder.newFolder(folderName);
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cd " + folderName, out);
    }

}
