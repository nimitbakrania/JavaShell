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
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.*;

public class CutTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private VisitorApplication visitor = new VisitorApplication();

    @Before
    public void setUpDummyData() throws IOException {

        File testFile = folder.newFile("test1.txt");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(testFile), StandardCharsets.UTF_8);
        writer.write("Lorem ipsum dolor sit mi.\n" +
                      "Duis velit enim, maximus quis orci sit amet, sollicitudin bibendum eros.\n" +
                      "Aenean ac lorem et justo malesuada hendrerit.\n" + 
                      "Etiam et erat leo. Ut fringilla quam nisi, at laoreet lacus volutpat sit amet.\n" +
                      "ulla bibendum ornare tortor, in dignissim diam. Vivamus rutrum facilisis nibh eu congue. Pellentesque laoreet leo massa.\n");           // 1 byte per character
        writer.close();

        Jsh.setCurrentDirectory(folder.getRoot().getAbsolutePath());
    }

    // UNIT TESTS

    @Test
    public void cutUnitTestWithInvalidOption() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            ArrayList<String> arr = new ArrayList<>();
            arr.add("-f");
            arr.add("1,2");
            arr.add("test1.txt");
            Visitable.Cut app = new Visitable.Cut(null, out, arr);
            app.accept(visitor);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: incorrect option input -f"));
        }
    }

    @Test
    public void cutUnitTestWithTooManyArguments() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            ArrayList<String> arr = new ArrayList<>();
            arr.add("-b");
            arr.add("1");
            arr.add("2");
            arr.add("3");
            arr.add("test1.txt");
            Visitable.Cut app = new Visitable.Cut(null, out, arr);
            app.accept(visitor);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: too many arguments."));
        }
    }

    @Test
    public void cutUnitTestWithTooFewArguments() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            ArrayList<String> arr = new ArrayList<>();
            arr.add("-b");
            arr.add("test1.txt");
            Visitable.Cut app = new Visitable.Cut(null, out, arr);
            app.accept(visitor);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: too few arguments."));
        }
    }

    @Test
    public void cutUnitTestWithCharacterInput() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            ArrayList<String> arr = new ArrayList<>();
            arr.add("-b");
            arr.add("a-b");
            arr.add("test1.txt");
            Visitable.Cut app = new Visitable.Cut(null, out, arr);
            app.accept(visitor);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: invalid arguments."));
        }
    }

    @Test
    public void cutUnitTestWithInvalidFile() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            ArrayList<String> arr = new ArrayList<>();
            arr.add("-b");
            arr.add("1-3");
            arr.add("fake.txt");
            Visitable.Cut app = new Visitable.Cut(null, out, arr);
            app.accept(visitor);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: file input does not exist."));
        }
    }

    @Test
    public void cutUnitTestWithIndividualBytes() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        ArrayList<String> arr = new ArrayList<>();
        arr.add("-b");
        arr.add("4,5");
        arr.add("test1.txt");
        Visitable.Cut app = new Visitable.Cut(null, out, arr);
        app.accept(visitor);
        assertEquals("em", scan.nextLine());
        assertEquals("s ", scan.nextLine());
        assertEquals("ea", scan.nextLine());
        assertEquals("am", scan.nextLine());
        assertEquals("a ", scan.nextLine());
        scan.close();
    }

    @Test
    public void cutUnitTestWithIntervals() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        ArrayList<String> arr = new ArrayList<>();
        arr.add("-b");
        arr.add("1-2,7-9");
        arr.add("test1.txt");
        Visitable.Cut app = new Visitable.Cut(null, out, arr);
        app.accept(visitor);
        assertEquals("Loips", scan.nextLine());
        assertEquals("Dueli", scan.nextLine());
        assertEquals("Ae ac", scan.nextLine());
        assertEquals("Etet ", scan.nextLine());
        assertEquals("ulibe", scan.nextLine());
        scan.close();
    }

    @Test
    public void cutUnitTestWithHalfIntervals() throws IOException {
    
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        ArrayList<String> arr = new ArrayList<>();
        arr.add("-b");
        arr.add("-4,7-");
        arr.add("test1.txt");
        Visitable.Cut app = new Visitable.Cut(null, out, arr);
        app.accept(visitor);
        assertEquals("Loreipsum dolor sit mi.", scan.nextLine());
        assertEquals("Duiselit enim, maximus quis orci sit amet, sollicitudin bibendum eros.", scan.nextLine());
        assertEquals("Aene ac lorem et justo malesuada hendrerit.", scan.nextLine());
        assertEquals("Etiaet erat leo. Ut fringilla quam nisi, at laoreet lacus volutpat sit amet.", scan.nextLine());
        assertEquals("ullaibendum ornare tortor, in dignissim diam. Vivamus rutrum facilisis nibh eu congue. Pellentesque laoreet leo massa.", scan.nextLine());
        scan.close();
    }

    @Test
    public void cutUnitTestWithInputStream() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        FileInputStream stdin = new FileInputStream(new File(folder.getRoot().getAbsolutePath(), "test1.txt"));
        ArrayList<String> arr = new ArrayList<>();
        arr.add("-b");
        arr.add("-4,7-");
        Visitable.Cut app = new Visitable.Cut(stdin, out, arr);
        app.accept(visitor);
        assertEquals("Loreipsum dolor sit mi.", scan.nextLine());
        assertEquals("Duiselit enim, maximus quis orci sit amet, sollicitudin bibendum eros.", scan.nextLine());
        assertEquals("Aene ac lorem et justo malesuada hendrerit.", scan.nextLine());
        assertEquals("Etiaet erat leo. Ut fringilla quam nisi, at laoreet lacus volutpat sit amet.", scan.nextLine());
        assertEquals("ullaibendum ornare tortor, in dignissim diam. Vivamus rutrum facilisis nibh eu congue. Pellentesque laoreet leo massa.", scan.nextLine());
        scan.close();
    }

    // INTEGRATION TESTS
    @Test 
    public void testCutWithInvalidOption() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_cut -f 1,2 test1.txt", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: incorrect option input -f"));
        }
    }

    @Test
    public void testCutWithTooManyArguments() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_cut -b 1 2 3 test1.txt", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: too many arguments."));
        }
    }

    @Test
    public void testCutWithTooFewArguments() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_cut -b test1.txt", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: too few arguments."));
        }
    }
    
    @Test
    public void testCutWithCharacterInputForBytes() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_cut -b a-b test1.txt", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: invalid arguments."));
        }
    }

    @Test
    public void testCutWithInvalidFile() throws IOException {

        try {
            PipedInputStream in = new PipedInputStream();
            PipedOutputStream out = new PipedOutputStream(in);
            Jsh.eval("_cut -b a-b fake.txt", out);
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().contains("cut: file input does not exist."));
        }

    }

    @Test
    public void testCutWithIndividualBytes() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("cut -b 4,5 test1.txt", out); // get bytes 4,5
        assertEquals("em", scan.nextLine());
        assertEquals("s ", scan.nextLine());
        assertEquals("ea", scan.nextLine());
        assertEquals("am", scan.nextLine());
        assertEquals("a ", scan.nextLine());
        scan.close();
    }

    @Test
    public void testCutWithIntervalsOfBytes() throws IOException {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("cut -b 1-2,7-9 test1.txt", out);    // get bytes 1,7,8
        assertEquals("Loips", scan.nextLine());
        assertEquals("Dueli", scan.nextLine());
        assertEquals("Ae ac", scan.nextLine());
        assertEquals("Etet ", scan.nextLine());
        assertEquals("ulibe", scan.nextLine());
        scan.close();
    }

    @Test
    public void testCutWithIntervalsWithOnlyOneSideGiven() throws IOException {

        // turn into byte arrays
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("cut -b -4,7- test1.txt", out); // get first 4 bytes then from 7th byte to the end.
        assertEquals("Loreipsum dolor sit mi.", scan.nextLine());
        assertEquals("Duiselit enim, maximus quis orci sit amet, sollicitudin bibendum eros.", scan.nextLine());
        assertEquals("Aene ac lorem et justo malesuada hendrerit.", scan.nextLine());
        assertEquals("Etiaet erat leo. Ut fringilla quam nisi, at laoreet lacus volutpat sit amet.", scan.nextLine());
        assertEquals("ullaibendum ornare tortor, in dignissim diam. Vivamus rutrum facilisis nibh eu congue. Pellentesque laoreet leo massa.", scan.nextLine());
        scan.close();
    }
}