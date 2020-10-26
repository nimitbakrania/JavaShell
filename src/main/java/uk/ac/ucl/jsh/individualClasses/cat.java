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

public class cat extends AnirudhAbstract {

 public void run(ArrayList<String> app_args, OutputStreamWriter writer, String curr_directory) throws IOException {
  if (app_args.isEmpty()) {
   throw new RuntimeException("cat: missing arguments");
  } else {
   for (String arg : app_args) {
    Charset encoding = StandardCharsets.UTF_8;
    File currFile = new File(curr_directory + File.separator + arg);
    if (currFile.exists()) {
     Path filePath = Paths.get(curr_directory + File.separator + arg);
     try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
      String line = null;
     while ((line = reader.readLine()) != null) {
      writer.write(String.valueOf(line));
      writer.write(System.getProperty("line.separator"));
      writer.flush();
     }
    } catch (IOException e) {
     throw new RuntimeException("cat: cannot open " + arg);
    }
    } else {
     throw new RuntimeException("cat: file does not exist");
    }
   } 
  } 
 }
}

