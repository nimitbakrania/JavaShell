package uk.ac.ucl.jsh;

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

    @Test
    public void testLs() throws IOException {

        File tempFile = folder.newFile("testFile1.txt");
        File tempFile2 = folder.newFile("testFile2.txt");
        File tempFolder = folder.newFolder("tempFolder");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("ls", out);
        Scanner scan = new Scanner(in);
        assertEquals(scan.next(), "testFile1.txt" + "\t"
                                + "testFile2.txt" + "\t"
                                + "tempFolder" + "\t" + System.getProperty("line.seperator"));
        scan.close();
    }

    @Test
    public void testLsWithGivenDirectory() throws IOException {

        File subFolder = folder.newFolder("subFolder");
        File testFile1 = folder.newFile("subFolder/testFile1.txt");
        File testFile2 = folder.newFile("subfolder/testFile2.txt");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("ls subFolder", out);
        Scanner scan = new Scanner(in);
        assertEquals(scan.next(), "testFile1.txt" + "\t"
                                   + "testFile2.txt" + "\t"
                                   + System.getProperty("line.seperator"));
    }

    @Test
    public void testLsWithTooManyArgs() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("ls foo bar foobar", out);
            fail();
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().equals("ls: too many arguments"));
        }

    }

    @Test
    public void testLsWithInvalidDirectory() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("ls fakeDirectory", out);
            fail();
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().equals("ls: no such directory"));
        }
    }

}
