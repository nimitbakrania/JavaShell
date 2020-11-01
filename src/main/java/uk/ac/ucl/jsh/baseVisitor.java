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

public interface baseVisitor{
    public void visit(appVisitor.cd app);
    public void visit(appVisitor.pwd app);
    public void visit(appVisitor.echo app);
    public void visit(appVisitor.head app);
    public void visit(appVisitor.tail app);
    public void visit(appVisitor.cat app);
    public void visit(appVisitor.ls app);
    public void visit(appVisitor.grep app);
}

/*
So this is what I've gathered
You have an interface for all the different versions of app which we have, e.g. one for each class. This is in baseVisitor.
You then have the implementations of all these visit functions for all of the versions of app in visitorApplication.
You also have appVisitor, which is where all the classes are implemented. The accept function is a function within all the classes that calls its visit function.

The purpose is to split up the algorithms which are running from their object. So now every object that is called runs the accept function, which runs its own visit function, which runs app for that class.

 */