package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;


public class findTest {
    /*
    Valid: find -name file, find path -name file, find path -name file with *
    Invalid: 2nd last arg not -name, args size != 2 or 3
    */
    @Test
    public void invalidArgsNumName() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("find src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles -name abc.txt a.txt", out);
        }
        catch(IOException e){
            assertEquals("find: wrong number of arguments", e.toString());
        }
    }

    @Test
    public void noNameArgTest() throws IOException{ //error one
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("find src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles abc.txt", out);
        }
        catch(IOException e){
            assertEquals("head: invalid argument src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles", e.toString());
        }
    }


    @Test
    public void twoArgsTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find -name a.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("C:\\Users\\jpopo\\OneDrive\\Desktop\\Software Engineering\\jsh-team-18\\src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\a.txt", scn.nextLine());
        scn.close();
    }

    @Test
    public void threeArgsTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles -name abbc.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abbc.txt", scn.nextLine());
        scn.close();
    }

    @Test
    public void asteriskTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles -name a*.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\a.txt", scn.nextLine());
        assertEquals("src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abbc.txt", scn.nextLine());
        assertEquals("src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abc.txt", scn.nextLine());
        scn.close();
    }
}
