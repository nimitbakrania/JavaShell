package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

public class JshTest {

    public JshTest() {
    }

    @Test
    public void testJsh() throws Exception {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in); 
        Scanner scan = new Scanner(in);
        Jsh.eval("echo \"\\\"Hello World\\\"\"", out);
        assertEquals("\"Hello World\"", scan.nextLine());
        scan.close();
    }

    @Test
    public void testKeywords() throws Exception {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Scanner scan = new Scanner(in);
        Jsh.eval("echo `cat anirudh.txt`", out);
        assertEquals("''", scan.nextLine());
        scan.close();
    }

    @Test
    public void testGlobbingDir() throws Exception {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream();
        Scanner scan = new Scanner(in);
        Jsh.eval("echo dir1/*.txt", out);
        assertEquals("file1.txt", scan.nextLine());
        scan.close();
    }

    @Test
    public void test() throws Exception {

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream();
        Scanner scan = new Scanner(in);
        Jsh.eval("uniq anirudh.txt", out);
        scan.close();
    }
}
