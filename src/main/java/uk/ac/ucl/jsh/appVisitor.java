package uk.ac.ucl.jsh;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public interface appVisitor{

    public interface ourInterface{
        public void accept(baseVisitor visitor);
    }

    public class cd implements ourInterface{
        InputStream input;
        OutputStream output;
        String directory;
        public cd(InputStream input, OutputStream output) {
            this.input = input;
            this.output = output;
            this.directory = System.getProperty("user.dir");
        }
        public void accept(baseVisitor visitor){
            visitor.visit(this);
        }
    }

    public class pwd implements ourInterface{

    }

    public class echo implements ourInterface{

    }

    public class head implements ourInterface{

    }

    public class tail implements ourInterface {

        InputStream input;
        OutputStream output;
        String directory;

        public tail(InputStream input, OutputStream output) {

            this.input = input;
            this.output = output;
            this.directory = System.getProperty("user.dir");

        }

        public void accept(baseVisitor visitor) {

            visitor.visit(this);
        }

    }

    public class cat implements ourInterface{
        inputStream input;
        outputStream output;
        dir directory;

        public cat(inputstream outputstream thisdirectory){
            this.input = inputstream;
            this.output = outputstream;
            this.directory = thisdirectory;
        }

        public void accept(baseVisitor visitor){
            visitor.visit(this);
        }
    }

    public class ls implements ourInterface {

        InputStream input;
        OutputStream output;
        String directory;

        public ls(InputStream input, OutputStream output) {

            this.input = input;
            this.output = output;
            this.directory = System.getProperty("user.dir");
        }

        public void accept(baseVisitor visitor) {

            visitor.visit(this);
        }

    }

    public class grep implements ourInterface{

    }

}