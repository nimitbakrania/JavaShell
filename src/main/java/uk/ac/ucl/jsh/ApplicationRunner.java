package uk.ac.ucl.jsh;

import java.util.ArrayList;

/* This class is responsible for running the applications. It should receive
   a parsed input from Parser class and executes the relevant application.
*/
public class ApplicationRunner {
    
    private String curr_directory;         // Directory may be important in running some applications.

    /* Constructor for ApplicationRunner. Call this at the start so that the
       directory can be initialized.
    */
    public ApplicationRunner() {
        // Call this constructor when starting up jsh.
        curr_directory = System.getProperty("user.dir");
    }

    /* Changes the working directory to the one specified in app_Args.
       @params = app_args : Arraylist that contains the arguments passed to cd. Should either be a path
                 or a directory. If not throw an exception.
    */
    public void cd(ArrayList<String> app_args) {

    }

    /* Prints the current working directory in jsh.*/
    public void pwd() {

    }

    /* Lists all files in the current directory or given argument.
       @params = app_args : arraylist containing directory to print files in or should be empty 
                            in the case where you want to print the current directory contents.
    */
    public void ls(ArrayList<String> app_args) {

    }

    /* Concatenates all contents of the files given as arguments and prints them in jsh.
       @params = app_args : contains files that need to be read.
    */
    public void cat(ArrayList<String> app_args) {

    }

    /* Outputs whatever argument it is given. If output is directed towards a file it is written there else
       it is printed in jsh.
       @params = app_args : contains arguments to print. 
    */
    public void echo(ArrayList<String> app_args) {

    }

    /* Outputs the first N lines in given file where N is specified in app_args. If output isnt redirected 
    it should be printed in jsh.
       @params = app_args : contains arguments to head. Should contain N and also the file to read.
    */
    public void head(ArrayList<String> app_args) {

    }

    /* Outputs the last N lines in a given file where N is specified in app_args. If file is < N, print all lines
       without throwing an error. If output isnt redirected, print in jsh.
       @params = app_args : contains arguments to tail. Should contain N and also the file to read.
    */
    public void tail(ArrayList<String> app_args) {

    }

    /* Pattern matches the (CHECK IDK IF IT IS REGEX OR STRING) in a given file/directory. Outputs all the lines
       that contain the pattern and if the output isn't redircted, print in jsh.
       @params = app_args : contains arguments to grep. Should have a pattern and directory/file.
    */
    public void grep(ArrayList<String> app_args) {

    }
}
