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


public class visitorApplication implements baseVisitor{
    public void visit(appVisitor.cd app){
        //app function for cd
    }
    public void visit(appVisitor.pwd app);
    public void visit(appVisitor.echo app);
    public void visit(appVisitor.head app);
    public void visit(appVisitor.tail app);
    public void visit(appVisitor.cat app);
    public void visit(appVisitor.ls app);
    public void visit(appVisitor.grep app);
}