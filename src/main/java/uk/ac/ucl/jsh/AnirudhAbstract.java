package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AnirudhAbstract {

    public AnirudhAbstract() { }

    public void run(ArrayList<String> app_args, OutputStreamWriter writer, String curr_directory) throws IOException { }
    public String run(ArrayList<String> app_args, OutputStreamWriter writer, String curr_directory) throws IOException { }
    public void run(ArrayList<String> app_args, String curr_directory) throws IOException { }
    public String run(ArrayList<String> app_args, String curr_directory) throws IOException { }
    public void run(OutputStreamWriter writer, String curr_directory) throws IOException { }
    
}
