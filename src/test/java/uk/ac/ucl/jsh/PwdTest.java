package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Scanner;


public class PwdTest {
    /*
    Valid: no args
    Invalid: any args
    */

    @Rule
    public TemporaryFolder TempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException{
        Jsh.setCurrentDirectory(TempFolder.getRoot().toString());
    }

    @Test
    public void regularTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);

        Jsh.eval("pwd", out);
        Scanner scn = new Scanner(in);
        assertEquals(TempFolder.getRoot().toString(), scn.nextLine());
        scn.close();
    }

    @Test
    public void regularUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        new Visitable.Pwd(null, out, appArgs).accept(new VisitorApplication());
    } 

    @Test
    public void tooManyArgsTest() throws IOException{ //error one
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        
        try{
            Jsh.eval("_pwd dir2", out);
        }
        catch(IOException e){
            assertEquals("pwd: too many arguments", e.toString());
        }
    }
    
    @Test(expected = RuntimeException.class)
    public void tooManyArgsUnitTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        ArrayList<String> appArgs = new ArrayList<String>();
        appArgs.add("dir2");
        new Visitable.Pwd(null, out, appArgs).accept(new VisitorApplication());
    } 

}