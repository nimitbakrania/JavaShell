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

public class tail extends abstractJSH {

 @Override
 public void run(ArrayList<String> app_args, OutputStreamWriter writer, String curr_directory) throws IOException {
  if (app_args.isEmpty()) {
      throw new RuntimeException("tail: missing arguments");
  }
  if (app_args.size() != 1 && app_args.size() != 3) {
      throw new RuntimeException("tail: wrong arguments");
  }
  if (app_args.size() == 3 && !app_args.get(0).equals("-n")) {
      throw new RuntimeException("tail: wrong argument " + app_args.get(0));
  }
  int tailLines = 10;
  String tailArg;
  if (app_args.size() == 3) {
      try {
          tailLines = Integer.parseInt(app_args.get(1));
      } catch (Exception e) {
          throw new RuntimeException("tail: wrong argument " + app_args.get(1));
      }
      tailArg = app_args.get(2);
  } else {
      tailArg = app_args.get(0);
  }
  File tailFile = new File(curr_directory + File.separator + tailArg);
  if (tailFile.exists()) {
      Charset encoding = StandardCharsets.UTF_8;
      Path filePath = Paths.get((String) curr_directory + File.separator + tailArg);
      ArrayList<String> storage = new ArrayList<>();
      try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
          String line = null;
          while ((line = reader.readLine()) != null) {
              storage.add(line);
          }
          int index = 0;
          if (tailLines > storage.size()) {
              index = 0;
          } else {
              index = storage.size() - tailLines;
          }
          for (int i = index; i < storage.size(); i++) {
              writer.write(storage.get(i) + System.getProperty("line.separator"));
              writer.flush();
          }            
      } catch (IOException e) {
      throw new RuntimeException("tail: cannot open " + tailArg);
      }
  } else {
      throw new RuntimeException("tail: " + tailArg + " does not exist");
  }
}
}
