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

public class cd extends abstractJSH{

 @Override
 public String run(ArrayList<String> app_args, String curr_directory) throws IOException {
  if (app_args.isEmpty()) {
   throw new RuntimeException("cd: missing argument");
  } else if (app_args.size() > 1) {
   throw new RuntimeException("cd: too many arguments");
  }
  String dirString = app_args.get(0);
  File dir = new File(curr_directory, dirString);
  if (!dir.exists() || !dir.isDirectory()) {
   throw new RuntimeException("cd: " + dirString + " is not an existing directory");
  }
  curr_directory = dir.getCanonicalPath();
  return curr_directory;
 }
}
