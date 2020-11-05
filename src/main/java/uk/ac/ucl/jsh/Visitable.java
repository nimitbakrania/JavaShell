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

public interface Visitable {
    
    public void accept(baseVisitor visitor);

    public class cd implements Visitable {
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

    public class pwd implements Visitable {

        @Override
        public void accept(baseVisitor visitor) {
            // TODO Auto-generated method stub

        }

    }

    public class echo implements Visitable{
        InputStream input;
        OutputStream output;
        String directory;
        ArrayList<String> appArgs;
        public echo(InputStream inputstream, OutputStream outputstream, ArrayList<String> appArgs){
            this.input = inputstream;
            this.output = outputstream;
            this.directory = System.getProperty("user.dir");
            this.appArgs = appArgs;
        }
        public void accept(baseVisitor visitor){
            visitor.visit(this);
        }
    }

    public class head implements Visitable{
        inputStream input;
        outputStream output;
        String directory;
        String[] appArgs;
        public head(inputStream inputstream, outputStream outputstream, String[] args){
            this.input = inputstream;
            this.output = outputstream;
            this.directory = System.getProperty("user.dir");
            this.appArgs = args;
        }
        public void accept(baseVisitor visitor){
            visitor.visit(this);
        }
    }

    public class tail implements Visitable {

        InputStream input;
        OutputStream output;
        String directory;
        ArrayList<String> appArgs;

        public tail(InputStream input, OutputStream output, ArrayList<String> appArgs) {

            this.input = input;
            this.output = output;
            this.directory = System.getProperty("user.dir");
            this.appArgs = appArgs;
        }

        public void accept(baseVisitor visitor) {

            visitor.visit(this);
        }

    }

    public class cat implements Visitable{
        InputStream input;
        OutputStream output;
        String directory;

        public cat(InputStream input,  OutputStream output, thisdirectory){
            this.input = input;
            this.output = output;
            this.directory = thisdirectory;
        }
        public void accept(baseVisitor visitor){
            visitor.visit(this);
        }
    }

    public class ls implements Visitable {

        InputStream input;
        OutputStream output;
        String directory;
        ArrayList<String> appArgs;

        public ls(InputStream input, OutputStream output, ArrayList<String> appArgs) {

            this.input = input;
            this.output = output;
            this.directory = System.getProperty("user.dir");
            this.appArgs = appArgs;
        }

        public void accept(baseVisitor visitor) {

            visitor.visit(this);
        }
    }

    public class grep implements Visitable{
        inputStream input;
        outputStream output;
        String directory;
        String[] appArgs;
        public grep(inputStream inputstream, outputStream outputstream, String[] args){
            this.input = inputstream;
            this.output = outputstream;
            this.directory = System.getProperty("user.dir");
            this.appArgs = args;
        }
        public void accept(baseVisitor visitor){
            visitor.visit(this);
        }
    }

}