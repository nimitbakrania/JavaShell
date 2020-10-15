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
      if (appArgs.isEmpty()) {
         throw new RuntimeException("cd: missing argument");
      } else if (appArgs.size() > 1) {
         throw new RuntimeException("cd: too many arguments");
      }
      String dirString = appArgs.get(0);
      File dir = new File(currentDirectory, dirString);
      if (!dir.exists() || !dir.isDirectory()) {
         throw new RuntimeException("cd: " + dirString + " is not an existing directory");
      }
      currentDirectory = dir.getCanonicalPath();
      break;
    }

    /* Prints the current working directory in jsh.*/
    public void pwd() {
      writer.write(currentDirectory);
      writer.write(System.getProperty("line.separator"));
      writer.flush();
      break;
    }

    /* Lists all files in the current directory or given argument.
       @params = app_args : arraylist containing directory to print files in or should be empty 
                            in the case where you want to print the current directory contents.
    */
    public void ls(ArrayList<String> app_args) {
      File currDir;
      if (appArgs.isEmpty()) {
         currDir = new File(currentDirectory);
         } else if (appArgs.size() == 1) {
            currDir = new File(appArgs.get(0));
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
         break;
    }

    /* Concatenates all contents of the files given as arguments and prints them in jsh.
       @params = app_args : contains files that need to be read.
    */
    public void cat(ArrayList<String> app_args) {
      if (appArgs.isEmpty()) {
         throw new RuntimeException("cat: missing arguments");
         } else {
            for (String arg : appArgs) {
               Charset encoding = StandardCharsets.UTF_8;
               File currFile = new File(currentDirectory + File.separator + arg);
               if (currFile.exists()) {
                  Path filePath = Paths.get(currentDirectory + File.separator + arg);
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
      break;
    }

    /* Outputs whatever argument it is given. If output is directed towards a file it is written there else
       it is printed in jsh.
       @params = app_args : contains arguments to print. 
    */
    public void echo(ArrayList<String> app_args) {
      boolean atLeastOnePrinted = false;
      for (String arg : appArgs) {
         writer.write(arg);
         writer.write(" ");
         writer.flush();
         atLeastOnePrinted = true;
      }
      if (atLeastOnePrinted) {
         writer.write(System.getProperty("line.separator"));
         writer.flush();
      }
      break;
    }

    /* Outputs the first N lines in given file where N is specified in app_args. If output isnt redirected 
    it should be printed in jsh.
       @params = app_args : contains arguments to head. Should contain N and also the file to read.
    */
    public void head(ArrayList<String> app_args) {
      if (appArgs.isEmpty()) {
         throw new RuntimeException("head: missing arguments");
      }
      if (appArgs.size() != 1 && appArgs.size() != 3) {                      
         throw new RuntimeException("head: wrong arguments");
      }
      if (appArgs.size() == 3 && !appArgs.get(0).equals("-n")) {
         throw new RuntimeException("head: wrong argument " + appArgs.get(0));
      }
      int headLines = 10;
      String headArg;
      if (appArgs.size() == 3) {
         try {
            headLines = Integer.parseInt(appArgs.get(1));
         } catch (Exception e) {
            throw new RuntimeException("head: wrong argument " + appArgs.get(1));
         }
         headArg = appArgs.get(2);
      } else {
         headArg = appArgs.get(0);
      }
      File headFile = new File(currentDirectory + File.separator + headArg);
      if (headFile.exists()) {
         Charset encoding = StandardCharsets.UTF_8;
         Path filePath = Paths.get((String) currentDirectory + File.separator + headArg);
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
      break;
    }

    /* Outputs the last N lines in a given file where N is specified in app_args. If file is < N, print all lines
       without throwing an error. If output isnt redirected, print in jsh.
       @params = app_args : contains arguments to tail. Should contain N and also the file to read.
    */
    public void tail(ArrayList<String> app_args) {
      if (appArgs.isEmpty()) {
         throw new RuntimeException("tail: missing arguments");
      }
      if (appArgs.size() != 1 && appArgs.size() != 3) {
         throw new RuntimeException("tail: wrong arguments");
      }
      if (appArgs.size() == 3 && !appArgs.get(0).equals("-n")) {
         throw new RuntimeException("tail: wrong argument " + appArgs.get(0));
      }
      int tailLines = 10;
      String tailArg;
      if (appArgs.size() == 3) {
         try {
            tailLines = Integer.parseInt(appArgs.get(1));
         } catch (Exception e) {
            throw new RuntimeException("tail: wrong argument " + appArgs.get(1));
         }
         tailArg = appArgs.get(2);
      } else {
         tailArg = appArgs.get(0);
      }
      File tailFile = new File(currentDirectory + File.separator + tailArg);
      if (tailFile.exists()) {
         Charset encoding = StandardCharsets.UTF_8;
         Path filePath = Paths.get((String) currentDirectory + File.separator + tailArg);
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
      break;
    }

    /* Pattern matches the (CHECK IDK IF IT IS REGEX OR STRING) in a given file/directory. Outputs all the lines
       that contain the pattern and if the output isn't redircted, print in jsh.
       @params = app_args : contains arguments to grep. Should have a pattern and directory/file.
    */
    public void grep(ArrayList<String> app_args) {
      if (appArgs.size() < 2) {
         throw new RuntimeException("grep: wrong number of arguments");
      }
      Pattern grepPattern = Pattern.compile(appArgs.get(0));
      int numOfFiles = appArgs.size() - 1;
      Path filePath;
      Path[] filePathArray = new Path[numOfFiles];
      Path currentDir = Paths.get(currentDirectory);
      for (int i = 0; i < numOfFiles; i++) {
         filePath = currentDir.resolve(appArgs.get(i + 1));
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
                     writer.write(appArgs.get(j+1));
                     writer.write(":");
                  }
                  writer.write(line);
                  writer.write(System.getProperty("line.separator"));
                  writer.flush();
               }
            }
         } catch (IOException e) {
            throw new RuntimeException("grep: cannot open " + appArgs.get(j + 1));
         }
      }
      break;
      }
    } 
}
