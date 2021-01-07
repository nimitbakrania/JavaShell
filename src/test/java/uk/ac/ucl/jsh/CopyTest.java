package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class CopyTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private VisitorApplication visitor = new VisitorApplication();

    @Before 
    public void setUp() throws IOException {

        File dirToCopy = folder.newFolder("dir1");
        File subDir = folder.newFolder("dir1", "subDir");
        File test1 = folder.newFile("dir1" + System.getProperty("file.separator") + "test1.txt"); // dir1/test1.txt
        File test2 = folder.newFile("dir1" + System.getProperty("file.separator") + "test2.txt"); // dir1/test2.txt
        File test3 = folder.newFile("dir1" + System.getProperty("file.separator") + 
                                    "subDir" + System.getProperty("file.separator") + "test3.txt"); // dir1/dir2/test3.txt
        File test4 = folder.newFile("test4.txt");

        File dirDest = folder.newFolder("dir2"); // copy to here.

        Jsh.setCurrentDirectory(folder.getRoot().getAbsolutePath());

    }

    // UNIT TESTS

    @Test
    public void copyUnitTestWithOneFile() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> arr = new ArrayList<>();
        Scanner scan = new Scanner(in);
        arr.add("test4.txt");
        arr.add("dir1");
        Visitable.Copy app = new Visitable.Copy(null, out, arr);
        app.accept(visitor);
        Jsh.eval("ls dir1", out);
        String line = scan.nextLine();
        assertTrue(line.contains("subDir"));
        assertTrue(line.contains("test2.txt"));
        assertTrue(line.contains("test1.txt"));
        assertTrue(line.contains("test4.txt"));
        scan.close();
    }

    @Test
    public void copyUnitTestWithDirectory() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> arr = new ArrayList<>();
        Scanner scan = new Scanner(in);
        arr.add("-r");
        arr.add("dir1");
        arr.add("dir2");
        Visitable.Copy app = new Visitable.Copy(null, out, arr);
        app.accept(visitor);
        Jsh.eval("ls dir2", out);
        String line = scan.nextLine();
        assertTrue(line.contains("subDir"));
        assertTrue(line.contains("test2.txt"));
        assertTrue(line.contains("test1.txt"));
        Jsh.eval("ls dir2" + System.getProperty("file.separator") + "subDir", out);
        String line2 = scan.nextLine();
        assertTrue(line2.contains("test3.txt"));
        scan.close();
    }

    @Test
    public void copyUnitTestWithInvalidFile() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            ArrayList<String> arr = new ArrayList<>();
            arr.add("fakeFile");
            arr.add("dir2");
            Visitable.Copy app = new Visitable.Copy(null, out, arr);
            app.accept(visitor);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cp: unable to copy file."));
        }
    }

    @Test
    public void copyUnitTestWithInvalidDirectory() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            ArrayList<String> arr = new ArrayList<>();
            arr.add("-r");
            arr.add("fakeDir");
            arr.add("dir2");
            Visitable.Copy app = new Visitable.Copy(null, out, arr);
            app.accept(visitor);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cp: unable to copy directory."));
        }
    }

    @Test
    public void copyUnitTestCopyDirtWithTooFewArgs() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            ArrayList<String> arr = new ArrayList<>();
            arr.add("-r");
            arr.add("fakeDir");
            Visitable.Copy app = new Visitable.Copy(null, out, arr);
            app.accept(visitor);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("copy: too few arguments"));
        }
    }

    @Test
    public void copyUnitTestCopyFileWithTooFewArgs() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            ArrayList<String> arr = new ArrayList<>();
            arr.add("dir1");
            Visitable.Copy app = new Visitable.Copy(null, out, arr);
            app.accept(visitor);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("copy: too few arguments"));
        }
    }

    // INTEGRATION TESTS

    @Test
    public void testCopyWithOneFile() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("cp test4.txt dir1", out);
        Jsh.eval("ls dir1", out);
        String line = scan.nextLine();
        assertTrue(line.contains("subDir"));
        assertTrue(line.contains("test2.txt"));
        assertTrue(line.contains("test1.txt"));
        assertTrue(line.contains("test4.txt"));
        scan.close();
    }

    @Test
    public void testCopyWithDirectory() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("cp -r dir1 dir2", out);
        Jsh.eval("ls dir2", out);
        String line = scan.nextLine();
        assertTrue(line.contains("subDir"));
        assertTrue(line.contains("test2.txt"));
        assertTrue(line.contains("test1.txt"));
        Jsh.eval("ls dir2" + System.getProperty("file.separator") + "subDir", out);
        String line2 = scan.nextLine();
        assertTrue(line2.contains("test3.txt"));
        scan.close();
    }

    @Test
    public void testCopyWithInvalidFile() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_cp fakeFile dir2", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cp: unable to copy file."));
        }
    }

    @Test
    public void testCopyWithInvalidDirectory() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_cp -r fakeDir dir2", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cp: unable to copy directory."));
        }
    }

    @Test
    public void testCopyDirWithtooFewArgs() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_cp -r fakeDir", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("copy: too few arguments"));
        }
    }

    @Test
    public void testCopyFileWithTooFewArgs() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_cp dir1", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("copy: too few arguments"));
        }

    }
    
}
