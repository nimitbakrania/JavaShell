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

public class echo extends abstractJSH{

 @Override
 public void run(ArrayList<String> app_args, OutputStreamWriter writer, String curr_directory) throws IOException {
  boolean atLeastOnePrinted = false;
  for (String arg : app_args) {
  writer.write(arg);
  writer.write(" ");
  writer.flush();
  atLeastOnePrinted = true;
  }
  if (atLeastOnePrinted) {
      writer.write(System.getProperty("line.separator"));
      writer.flush();
  }
}
