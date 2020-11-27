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

public class CutTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUpDummyData() throws IOException {

        File testFile = folder.newFile("test1.txt");
        FileWriter writer = new FileWriter(testFile);
        writer.write("Lorem ipsum dolor sit mi.\n" +
                      "Duis velit enim, maximus quis orci sit amet, sollicitudin bibendum eros.\n" +
                      "Aenean ac lorem et justo malesuada hendrerit.\n" + 
                      "Etiam et erat leo. Ut fringilla quam nisi, at laoreet lacus volutpat sit amet.\n" +
                      "ulla bibendum ornare tortor, in dignissim diam. Vivamus rutrum facilisis nibh eu congue. Pellentesque laoreet leo massa.\n");           // 1 byte per character
        writer.close();

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cd " + folder.getRoot().getAbsolutePath(), out);
    }

    @Test 
    public void testCutWithInvalidOption() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("cut -f 1,2 test1.txt", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: incorrect option input -f"));
        }
    }

    @Test
    public void testCutWithTooManyArguments() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("cut -b 1 2 3 test1.txt", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: too many arguments."));
        }
    }

    @Test
    public void testCutWithTooFewArguments() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("cut -b test1.txt", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: too few arguments."));
        }
    }
    
    @Test
    public void testCutWithCharacterInputForBytes() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("cut -b a-b test1.txt", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: invalid arguments."));
        }
    }

    @Test
    public void testCutWithInvalidFile() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("cut -b a-b fake.txt", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: file input does not exist."));
        }

    }

    @Test
    public void testCutWithoutDashInInterval() throws IOException {


    }

    @Test
    public void testCutWithIndividualBytes() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("cut -b 4,5 test1.txt", out); // get bytes 4,5
        assertEquals("em", scan.next());
        assertEquals("s ", scan.next());
        assertEquals("ea", scan.next());
        assertEquals("am", scan.next());
        assertEquals("a ", scan.next());
        scan.close();
    }

    @Test
    public void testCutWithIntervalsOfBytes() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("cut -b 1-2,7-9 test1.txt", out);    // get bytes 1,7,8
        assertEquals("Lip", scan.next());
        assertEquals("Del", scan.next());
        assertEquals("A a", scan.next());
        assertEquals("Eet", scan.next());
        assertEquals("ube", scan.next());
        scan.close();
    }

    @Test
    public void testCutWithIntervalsWithOnlyOneSideGiven() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("cut -b -4,7- test1.txt", out); // get first 4 bytes then from 7th byte to the end.
        assertEquals("Lorepsum dolor sit mi", scan.next());
        assertEquals("Duiselit enim, maximus quis orci sit amet, sollicitudin bibendum eros.", scan.next());
        assertEquals("Aene ac lorem et justo malesuada hendrerit.", scan.next());
        assertEquals("Etiaet erat leo. Ut fringilla quam nisi, at laoreet lacus volutpat sit amet.", scan.next());
        assertEquals("ullaibendum ornare tortor, in dignissim diam. Vivamus rutrum facilisis nibh eu congue. Pellentesque laoreet leo massa.", scan.next());
        scan.close();
    }
}