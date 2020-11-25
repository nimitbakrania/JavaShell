package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;


public class pwdTest {
    /*
    Valid: no args
    Invalid: any args
    */
    @Test
    public void regularTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("pwd", out);
        Scanner scn = new Scanner(in);
        assertEquals("C:\\Users\\jpopo\\OneDrive\\Desktop\\Software Engineering\\jsh-team-18", scn.nextLine());
        scn.close();
    }

    @Test
    public void tooManyArgsTest() throws IOException{ //error one
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("pwd src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abc.txt src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\a.txt", out);
        }
        catch(IOException e){
            assertEquals("pwd: too many arguments", e.toString());
        }
    }

}
