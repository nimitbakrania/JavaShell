package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class RedirectionTest{
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @After
    public void deleteTempFolder() {
        folder.delete();
    }

    Charset encoding = StandardCharsets.UTF_8;

    protected void createTestFile(String fileName, String toWrite) throws IOException {
        File file = folder.newFile(fileName);
        if (toWrite != null) {
            FileWriter writer = new FileWriter(file, encoding);
            writer.write(toWrite);
            writer.close();
        }
    }

  @Test(expected = RuntimeException.class)
    public void redirectionTooManyFiles() throws Exception {
        String fileName1 = "test1.txt";
        String fileName2 = "test2.txt";
        String toWrite1 = "hello ";
        String toWrite2 = "world";
        createTestFile(fileName1,toWrite1);
        createTestFile(fileName2,toWrite2);

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat > *.txt", out);
    }

    @Test
    public void redirectionLs() throws Exception {
        String folder1 = "Folder1";
        String folder2 = "Folder2";
        String file1 = "file1.txt";
        String file2 = "file2.txt";

        folder.newFolder(folder1);
        folder.newFolder(folder2);
        folder.newFolder(".hidden");
        folder.newFile(file1);
        folder.newFile(file2);
        folder.newFile(".hiddenTxt.txt");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        String outputFileName = "output.txt";
        Jsh.eval("ls > " + outputFileName, out);
        Path path = Paths.get(Jsh.getCurrentDirectory() + System.getProperty("file.separator") + outputFileName);
        String contents = Files.readString(path);
        assertTrue("contents printed does not print '" + folder1 + "' (no args given)", contents.contains(folder1));
        contents = contents.replaceFirst(folder1, "");
        assertTrue("contents printed does not print '" + folder2 + "' (no args given)", contents.contains(folder2));
        contents = contents.replaceFirst(folder2, "");
        assertTrue("contents printed does not print '" + file1 + "' (no args given)", contents.contains(file1));
        contents = contents.replaceFirst(file1, "");
        assertTrue("contents printed does not print '" + outputFileName + "' (no args given)", contents.contains(outputFileName));
        contents = contents.replaceFirst(outputFileName, "");
        assertTrue("contents printed does not print '" + file2 + "' (no args given)", contents.contains(file2));
        contents = contents.replaceFirst(file2, "").replace("\t","").replace("\n","");
        assertEquals("contents contains extra characters", contents, "");
    }

    @Test
    // valid command should work as usual
    public void redirectionCat() throws Exception {
        String toWrite = "Hello World";
        String fileName = "testTextFile.txt";
        createTestFile(fileName, toWrite); 

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat < " + fileName, out);
        out.close();
        String contents = new String(in.readAllBytes());
        assertEquals("File contents wrong", toWrite, contents);
    }

    @Test(expected = RuntimeException.class)
    public void multipleOutputFiles() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat < test.txt < test.txt", out);

    }

    @Test(expected = RuntimeException.class)
    public void multipleInputFiles() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat >test.txt >test.txt", out);

    }


    @Test(expected = RuntimeException.class)
    public void AmbigousRedict() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        folder.newFile(); folder.newFile();
        Jsh.eval("echo < *", out);
        out.close();
    }

    @Test
    // valid command should work as usual
    public void redirectionCatSingleQuotes() throws Exception {
        String toWrite = "Hello World";
        String fileName = "testTextFile.txt";
        createTestFile(fileName, toWrite); 

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cat < " +"'"+ fileName + "'", out);
        out.close();
        String contents = new String(in.readAllBytes());
        assertEquals("File contents wrong", toWrite, contents);
    }
    
}