package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class LsTest {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void goIntoTempfolder() throws IOException {

        // for lsWithGivenDirectory
        File folder1 = folder.newFolder("dir1");
        File file1 = new File(folder1, "test1.txt");
        File file2 = new File(folder1, "test2.txt");

        // for testLs
        File file3 = folder.newFile("test3.txt");
        File file4 = folder.newFile("test4.txt");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cd " + folder.getRoot().getAbsolutePath(), out);
    }

    @Test
    public void testLs() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("ls", out);
        Scanner scan = new Scanner(in);
        assertEquals("dir1", scan.next());
        assertEquals("test3.txt", scan.next());
        assertEquals("test4.txt", scan.next());
        scan.close();
    }

    @Test
    public void testLsWithGivenDirectory() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("ls dir1", out);
        Scanner scan = new Scanner(in);
        assertEquals("test1.txt", scan.next());
        assertEquals("text2.txt", scan.next());
        scan.close();
    }

    @Test
    public void testLsWithTooManyArgs() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("ls foo bar foobar", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("ls: too many arguments"));
        }

    }

    @Test
    public void testLsWithInvalidDirectory() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("ls fakeDirectory", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().equals("ls: no such directory"));
        }
    }

}
