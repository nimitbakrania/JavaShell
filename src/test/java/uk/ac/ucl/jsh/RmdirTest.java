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

public class RmdirTest {
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

    @Before
    public void EnterTempFolder() throws IOException{
        Jsh.setCurrentDirectory(folder.getRoot().toString());
    }

    @Test(expected = RuntimeException.class)
     public void rmdirNoArg() throws Exception {
         PipedInputStream in = new PipedInputStream();
         PipedOutputStream out;
         out = new PipedOutputStream(in);
         Jsh.eval("rmdir", out);
     }
     
     @Test(expected = RuntimeException.class)
     public void rmdirInvalid() throws Exception {
         PipedInputStream in = new PipedInputStream();
         PipedOutputStream out;
         out = new PipedOutputStream(in);
         Jsh.eval("rmdir invalid", out);
     }
 
     @Test(expected = RuntimeException.class)
     public void rmdirInvalidWithFlag() throws Exception {
         PipedInputStream in = new PipedInputStream();
         PipedOutputStream out;
         out = new PipedOutputStream(in);
         Jsh.eval("rmdir -r invalid", out);
     }
 
     @Test(expected = RuntimeException.class)
     public void rmdirInvalidFlag() throws Exception {
         PipedInputStream in = new PipedInputStream();
         PipedOutputStream out;
         out = new PipedOutputStream(in);
         Jsh.eval("rmdir -x invalid", out);
     }
     
     @Test(expected = RuntimeException.class)
     public void rmdirTxtFile() throws Exception {
         String fileName = "testTextFile.txt";
         createTestFile(fileName, null);
         PipedInputStream in = new PipedInputStream();
         PipedOutputStream out;
         out = new PipedOutputStream(in);
         Jsh.eval("rmdir " + fileName, out);
     }
 
     @Test(expected = RuntimeException.class)
     public void rmdirDirNotExist() throws Exception {
         PipedInputStream in = new PipedInputStream();
         PipedOutputStream out;
         out = new PipedOutputStream(in);
         Jsh.eval("rmdir sauce", out);
     }
 
     @Test(expected = RuntimeException.class)
     public void rmdirDirNotExistWithFlag() throws Exception {
         PipedInputStream in = new PipedInputStream();
         PipedOutputStream out;
         out = new PipedOutputStream(in);
         Jsh.eval("rmdir -r sauce", out);
     }
 
     @Test(expected = RuntimeException.class)
     public void rmdirRmNonEmptyInvalid() throws Exception {
         String folderName = "src";
         folder.newFolder(folderName, "mayonnaise");
         PipedInputStream in = new PipedInputStream();
         PipedOutputStream out;
         out = new PipedOutputStream(in);
         Jsh.eval("rmdir " + folderName, out);
     }

     @Test
    public void mkdirANDrmdir() throws Exception {
        String folderName = "src";
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("mkdir " + folderName, out);
        File dir = new File(Jsh.getCurrentDirectory() + System.getProperty("file.separator") + folderName);
        assertTrue("dir not made", dir.exists());
        Jsh.eval("rmdir -r " + folderName, out);
        assertFalse("dir not deleted", dir.exists());
    }

    @Test
    public void mkdirANDrmdirNoOption() throws Exception {
        String folderName = "src";
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("mkdir " + folderName, out);
        File dir = new File(Jsh.getCurrentDirectory() + System.getProperty("file.separator") + folderName);
        assertTrue("dir not made", dir.exists());
        Jsh.eval("rmdir " + folderName, out);
        assertFalse("dir not deleted", dir.exists());
    }

    @Test
    public void rmdirRmNonEmpty() throws Exception {
        String folderName = "src";
        File newFolder1 = folder.newFolder(folderName);
        File newFolder2 = folder.newFolder(folderName, "textFiles");
        File file1 = new File(newFolder2, "test.txt");
        file1.createNewFile();
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("rmdir -r " + folderName, out);
        assertFalse("entire dir not deleted", file1.exists());
        assertFalse("entire dir not deleted", newFolder2.exists());
        assertFalse("entire dir not deleted", newFolder1.exists());
    }
}
