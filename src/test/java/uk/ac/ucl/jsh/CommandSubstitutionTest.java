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
import java.io.FileWriter;

public class CommandSubstitutionTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private OurParser parser = new OurParser();

    @Before
    public void setUp() throws IOException {

        File testFile = folder.newFile("test1.txt");
        FileWriter writer = new FileWriter(testFile);
        writer.write("this is a string to test with\nfoobar");
        writer.close();

        Jsh.setCurrentDirectory(folder.getRoot().getAbsolutePath());
    }

    // UNIT TESTS

    @Test
    public void unitTestCmdSub() throws IOException {

        String cmd = "echo `cat test1.txt`";
        assertEquals("echo this is a string to test with foobar", parser.cmdSubstitution(cmd));
    }

    @Test
    public void unitTestCmdSubSeq() throws IOException {

        String cmd = "echo `cat test1.txt`; echo foo";
        assertEquals("echo this is a string to test with foobar; echo foo", parser.cmdSubstitution(cmd));
    }

    @Test
    public void unitTestCmdSubWithSemicolonInsideQuotes() throws IOException {

        String cmd = "echo `echo foo; cat test1.txt`";
        assertEquals("echo foo this is a string to test with foobar", parser.cmdSubstitution(cmd));
    }

    @Test
    public void unitTestCmdSubWithSingleQuotes() throws IOException {

        String cmd = "echo '`echo foo`'";
        assertEquals("echo '`echo foo`'", parser.cmdSubstitution(cmd));
    }

    // INTEGRATION TESTS
    @Test
    public void testCommandSubstitution() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("echo `cat test1.txt`", out);
        assertEquals("this is a string to test with foobar", scan.nextLine());
        scan.close();
    }
    
    @Test
    public void testCommandSubstitutionWithSeq() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("echo `cat test1.txt`; echo foo", out);
        assertEquals("this is a string to test with foobar", scan.nextLine());
        assertEquals("foo", scan.nextLine());
        scan.close();
    }

    @Test
    public void testCommandSubstitutionWithSemicolonInsideQuotes() throws IOException {
        
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("echo `echo foo; cat test1.txt`", out);
        assertEquals("foo this is a string to test with foobar", scan.nextLine());
        scan.close();
    }

 
    @Test
    public void testCommandSubstitutionWithSingleQuotes() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("echo '`echo foo`'", out);
        assertEquals("`echo foo`", scan.nextLine());
        scan.close();
    }
}