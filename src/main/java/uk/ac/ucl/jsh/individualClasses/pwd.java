package uk.ac.ucl.jsh.individualClasses;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class pwd extends abstractJSH{

 @Override
 public void run(OutputStreamWriter writer, String curr_directory) throws IOException {
  writer.write(curr_directory);
  writer.write(System.getProperty("line.separator"));
  writer.flush();
 }
}