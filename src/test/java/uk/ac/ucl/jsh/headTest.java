package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;


public class headTest {
    /*
    Valid: head -n num file, head file, num>
    Invalid: args > 3, args = 3 and arg[0] not -n, -n non-integer, -n negative, invalid file name
    */
    @Test
    public void invalidFileName() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("head src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abcd.txt", out);
        }
        catch(IOException e){
            assertEquals("head: src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abcd.txt does not exist", e.toString());
        }
    }

    @Test
    public void tooManyArgsTest() throws IOException{ //error one
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("head -n 15 src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abc.txt src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\a.txt", out);
        }
        catch(IOException e){
            assertEquals("head: too many arguments", e.toString());
        }
    }

    @Test
    public void noNThreeArgsTest() throws IOException{ //error one
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("head n 15 src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abc.txt src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\a.txt", out);
        }
        catch(IOException e){
            assertEquals("head: wrong argument n", e.toString());
        }
    }

    @Test
    public void nonIntegerTest() throws IOException{ //error one
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("head -n ab src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abc.txt src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\a.txt", out);
        }
        catch(IOException e){
            assertEquals("head: wrong argument ab", e.toString());
        }
    }

    @Test
    public void negativeTest() throws IOException{ //error one
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        try{
            Jsh.eval("head -n -10 src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\abc.txt src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\a.txt", out);
        }
        catch(IOException e){
            assertEquals("head: wrong argument -10", e.toString());
        }
    }

    @Test
    public void threeArgsTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("head -n 3 src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\a.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. ", scn.nextLine());
        assertEquals("Aenean commodo ligula eget dolor. ", scn.nextLine());
        assertEquals("Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. ", scn.nextLine());
        scn.close();
    }

    @Test
    public void oneArgTest() throws IOException{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("head src\\main\\java\\uk\\ac\\ucl\\jsh\\TestFiles\\a.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. ", scn.nextLine());
        assertEquals("Aenean commodo ligula eget dolor. ", scn.nextLine());
        assertEquals("Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. ", scn.nextLine());
        assertEquals("Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. ", scn.nextLine());
        assertEquals("Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. ", scn.nextLine());
        assertEquals("In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. ", scn.nextLine());
        assertEquals("Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus.", scn.nextLine());
        assertEquals("Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. ", scn.nextLine());
        assertEquals("Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. ", scn.nextLine());
        assertEquals("Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. ", scn.nextLine());
        scn.close();
    }


}
