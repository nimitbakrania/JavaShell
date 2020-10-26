package uk.ac.ucl.jsh.individualClasses;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

public class ls extends abstractJSH{

 @Override
 public void run(ArrayList<String> app_args, OutputStreamWriter writer, String curr_directory) throws IOException {
  File currDir;
  if (app_args.isEmpty()) {
      currDir = new File(curr_directory);
  } else if (app_args.size() == 1) {
      currDir = new File(app_args.get(0));
  } else {
      throw new RuntimeException("ls: too many arguments");
  }
   try {
      File[] listOfFiles = currDir.listFiles();
      boolean atLeastOnePrinted = false;
      for (File file : listOfFiles) {
          if (!file.getName().startsWith(".")) {
              writer.write(file.getName());
               writer.write("\t");
               writer.flush();
               atLeastOnePrinted = true;
          }
      }
      if (atLeastOnePrinted) {
          writer.write(System.getProperty("line.separator"));
          writer.flush();
      }
  } catch (NullPointerException e) {
          throw new RuntimeException("ls: no such directory");
  }
 }
}
