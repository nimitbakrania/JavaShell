package uk.ac.ucl.jsh.individualClasses;

import uk.ac.ucl.jsh.AnirudhAbstract;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class head extends AnirudhAbstract {

 public void run(ArrayList<String> app_args, OutputStreamWriter writer, String curr_directory) throws IOException {
  if (app_args.isEmpty()) {
      throw new RuntimeException("head: missing arguments");
  }
  if (app_args.size() != 1 && app_args.size() != 3) {                      
      throw new RuntimeException("head: wrong arguments");
  }
  if (app_args.size() == 3 && !app_args.get(0).equals("-n")) {
      throw new RuntimeException("head: wrong argument " + app_args.get(0));
  }
  int headLines = 10;
  String headArg;
  if (app_args.size() == 3) {
      try {
      headLines = Integer.parseInt(app_args.get(1));
      } catch (Exception e) {
      throw new RuntimeException("head: wrong argument " + app_args.get(1));
      }
      headArg = app_args.get(2);
  } else {
      headArg = app_args.get(0);
  }
  File headFile = new File(curr_directory + File.separator + headArg);
  if (headFile.exists()) {
      Charset encoding = StandardCharsets.UTF_8;
      Path filePath = Paths.get((String) curr_directory + File.separator + headArg);
      try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
          for (int i = 0; i < headLines; i++) {
              String line = null;
              if ((line = reader.readLine()) != null) {
                  writer.write(line);
                  writer.write(System.getProperty("line.separator"));
                  writer.flush();
              }
          }
      } catch (IOException e) {
          throw new RuntimeException("head: cannot open " + headArg);
      }
  } else {
      throw new RuntimeException("head: " + headArg + " does not exist");
  }
}
}

