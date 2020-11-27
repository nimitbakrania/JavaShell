package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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

    @Rule
    public TemporaryFolder TempFolder = new TemporaryFolder();

    @Before
    public void EnterTempFolder() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        Jsh.eval("cd" + TempFolder.getRoot(), out);
    }

    @Test
    public void regularTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("pwd", out);
        Scanner scn = new Scanner(in);
        assertEquals(TempFolder.getRoot().getAbsolutePath(), scn.nextLine());
        scn.close();
    }

    @Test
    public void tooManyArgsTest() throws IOException{ //error one
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("pwd src", out);
        }
        catch(IOException e){
            assertEquals("pwd: too many arguments", e.toString());
        }
    }

}
