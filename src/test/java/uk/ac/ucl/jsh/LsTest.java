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
import java.util.ArrayList;

public class LsTest {
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private VisitorApplication visitor = new VisitorApplication();

    @Before
    public void goIntoTempfolder() throws IOException {

        // for lsWithGivenDirectory
        File dir1 = folder.newFolder("dir1");
        File test1 = new File(dir1, "test1.txt");
        folder.newFile("dir1" + System.getProperty("file.separator") + "test1.txt");

        // for testLs
        File file3 = folder.newFile("test3.txt");
        File file4 = folder.newFile("test4.txt");

        Jsh.setCurrentDirectory(folder.getRoot().getAbsolutePath());
    }

    // UNIT TEST

    @Test
    public void lsUnitTest() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        ArrayList<String> arr = new ArrayList<>();
        Visitable.Ls app = new Visitable.Ls(null, out, arr);
        app.accept(visitor);
        String line = scan.nextLine();
        assertTrue(line.contains("test3.txt"));
        assertTrue(line.contains("test4.txt"));
        assertTrue(line.contains("dir1"));
        scan.close();
    }

    @Test
    public void lsUnitTestWithDirectory() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        ArrayList<String> arr = new ArrayList<>();
        arr.add("dir1");
        Visitable.Ls app = new Visitable.Ls(null, out, arr);
        app.accept(visitor);
        assertEquals("test1.txt", scan.next());
        scan.close();

    }

    @Test
    public void lsUnitTestWithAbsolutePath() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        ArrayList<String> arr = new ArrayList<>();
        String path = folder.getRoot().getAbsolutePath() + System.getProperty("file.separator") + "dir1";
        arr.add(path);
        Visitable.Ls app = new Visitable.Ls(null, out, arr);
        app.accept(visitor);
        assertEquals("test1.txt", scan.next());
        scan.close();
    }

    @Test
    public void lsUnitTestWithTooManyArgs() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Scanner scan = new Scanner(in);
            ArrayList<String> arr = new ArrayList<>();
            arr.add("foo");
            arr.add("bar");
            arr.add("foobar");
            Visitable.Ls app = new Visitable.Ls(null, out, arr);
            app.accept(visitor);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("ls: too many arguments"));
        }
    }

    @Test
    public void lsUnitTestWithInvalidDirectory() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Scanner scan = new Scanner(in);
            ArrayList<String> arr = new ArrayList<>();
            arr.add("fakeDirectory");
            Visitable.Ls app = new Visitable.Ls(null, out, arr);
            app.accept(visitor);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("ls: no such directory"));
        }

    }

    // INTEGRATION TESTS

    @Test
    public void testLs() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("ls", out);
        Scanner scan = new Scanner(in);
        String line = scan.nextLine();
        assertTrue(line.contains("test3.txt"));
        assertTrue(line.contains("test4.txt"));
        assertTrue(line.contains("dir1"));
        scan.close();
    }

    @Test
    public void testLsWithGivenDirectory() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("ls dir1", out);
        assertEquals("test1.txt", scan.next());
        scan.close();
    }

    @Test
    public void testLsWithAbsolutePath() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        String path = folder.getRoot().getAbsolutePath() + System.getProperty("file.separator") + "dir1";
        Jsh.eval("ls " + path, out);
        assertEquals("test1.txt", scan.next());
        scan.close();
    }

    @Test
    public void testLsWithTooManyArgs() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_ls foo bar foobar", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("ls: too many arguments"));
        }
    }

    @Test
    public void testLsWithInvalidDirectory() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_ls fakeDirectory", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().equals("ls: no such directory"));
        }
    }

}