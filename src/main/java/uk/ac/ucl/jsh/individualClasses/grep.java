package uk.ac.ucl.jsh.individualClasses;

import uk.ac.ucl.jsh.AnirudhAbstract;
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

public class grep extends AnirudhAbstract {

 public void run(ArrayList<String> app_args, OutputStreamWriter writer, String curr_directory) throws IOException {
  if (app_args.size() < 2) {
      throw new RuntimeException("grep: wrong number of arguments");
  }
  Pattern grepPattern = Pattern.compile(app_args.get(0));
  int numOfFiles = app_args.size() - 1;
  Path filePath;
  Path[] filePathArray = new Path[numOfFiles];
  Path currentDir = Paths.get(curr_directory);
  for (int i = 0; i < numOfFiles; i++) {
      filePath = currentDir.resolve(app_args.get(i + 1));
      if (Files.notExists(filePath) || Files.isDirectory(filePath) || 
          !Files.exists(filePath) || !Files.isReadable(filePath)) {
          throw new RuntimeException("grep: wrong file argument");
      }
      filePathArray[i] = filePath;
  }
  for (int j = 0; j < filePathArray.length; j++) {
      Charset encoding = StandardCharsets.UTF_8;
      try (BufferedReader reader = Files.newBufferedReader(filePathArray[j], encoding)) {
          String line = null;
          while ((line = reader.readLine()) != null) {
              Matcher matcher = grepPattern.matcher(line);
              if (matcher.find()) {
                  if (numOfFiles > 1) {
                      writer.write(app_args.get(j+1));
                      writer.write(":");
                  }
                  writer.write(line);
                  writer.write(System.getProperty("line.separator"));
                  writer.flush();
              }
          }
      } catch (IOException e) {
          throw new RuntimeException("grep: cannot open " + app_args.get(j + 1));
      }
}
}
}
