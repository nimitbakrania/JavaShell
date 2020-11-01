package uk.ac.ucl.jsh;

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
        public int accept(baseVisitor visitor);
    }

    public class cd implements ourInterface{
        inputStream input;
        outputStream output;
        dir directory;
        public cd(inputstream outputstream thisdirectory){
            this.input = inputstream;
            this.output = outputstream;
            this.directory = thisdirectory;
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

    public class tail implements ourInterface{

    }

    public class app implements ourInterface{

    }

    public class ls implements ourInterface{

    }

    public class grep implements ourInterface{

    }

}