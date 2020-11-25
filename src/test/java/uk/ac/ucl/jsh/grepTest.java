package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;


public class grepTest {
    // Needs to test 0 args 2 args >2 args, invalid file name, valid
    @Test
    public void invalidFileName() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("grep 'hello world' src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abcd.txt", out);
        }
        catch(IOException e){
            assertEquals("grep: wrong file argument", e.toString());
        }
    }

    @Test
    public void twoArgTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("grep 'hello world' src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abc.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.nextLine(), "Nullam dictum felis eu pede mollis pretium. Integer tincidunt. hello world Cras dapibus.");
        scn.close();
    }

    @Test
    public void noArgsTest() throws IOException{ //error one
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("grep", out);
        }
        catch(IOException e){
            assertEquals("grep: no arguments given", e.toString());
        }
    }

    @Test
    public void manyArgsTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("grep 'Nullam dictum felis' 'src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abc.txt' 'src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\a.txt'", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.nextLine(), "src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abc.txt:Nullam dictum felis eu pede mollis pretium. Integer tincidunt. hello world Cras dapibus.");
        assertEquals(scn.nextLine(), "src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\a.txt:Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus.");
        scn.close();
    }

}
