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

public class CopyTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

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

    @Test
    public void testCopyWithOneFile() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("cp test4.txt dir1", out);
        Jsh.eval("ls dir1", out);
        assertEquals("subDir", scan.next());
        assertEquals("test2.txt", scan.next());
        assertEquals("test1.txt", scan.next());
        assertEquals("test4.txt", scan.next());
        scan.close();
    }

    @Test
    public void testCopyWithDirectory() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("cp -r dir1 dir2", out);
        Jsh.eval("ls dir2", out);
        assertEquals("subDir", scan.next());
        assertEquals("test2.txt", scan.next());
        assertEquals("test1.txt", scan.next());
        Jsh.eval("ls dir2" + System.getProperty("file.separator") + "subDir", out);
        assertEquals("test3.txt", scan.next());
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
    
}
