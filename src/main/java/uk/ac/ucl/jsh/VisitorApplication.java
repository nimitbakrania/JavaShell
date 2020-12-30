package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.*;


import java.io.InputStream;

public class VisitorApplication implements BaseVisitor {

    public VisitorApplication() {
    }

    public void visit(Visitable.Cd app) throws IOException {
        if (app.appArgs.isEmpty()) {
            // takes user to home directory when 'cd' is called alone
            Jsh.setCurrentDirectory(Jsh.getHomeDirectory());
        } else if (app.appArgs.size() > 1) {
            throw new RuntimeException("cd: too many arguments");
        } else {
            // takes user to directory specified in arg
            String currentDirectory = Jsh.getCurrentDirectory();
            String dirString = app.appArgs.get(0);
            File dir = new File(currentDirectory, dirString);
            if (dirString.charAt(0) == '/') {
                dir = new File(dirString);
            }
            if (!dir.exists() || !dir.isDirectory()) {
                throw new RuntimeException("cd: " + dirString + " is not an existing directory");
            }
            currentDirectory = dir.getCanonicalPath();
            Jsh.setCurrentDirectory(currentDirectory);
        }
    }

    /**
     * Prints the current directory which the user is working in
     * 
     * @param app takes in a generalised form app which has the properties
     *            InputStream input, OutputStream output, String currDirectory,
     *            ArrayList<String> appArgs.
     * @return nothing, as any values obtained are written to the OutputStream
     *         specified by the user.
     * @exception RuntimeException if the number of arguments are wrong.
     */
    public void visit(Visitable.Pwd app) throws IOException {

        if (!app.appArgs.isEmpty()) {
            throw new RuntimeException("pwd: too many arguments");
        }
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        lineOutputWriter(Jsh.getCurrentDirectory(), writer, "pwd");
    }

    /**
     * Prints the arguments to the command line.
     * 
     * @param  APP, contains the app arguments as public variables.
     * 
     */
    public void visit(Visitable.Echo app) throws IOException {

        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        Stream<String> args = app.appArgs.stream();
        int size = app.appArgs.size();
        String lastArg = app.appArgs.get(size - 1);

        args.forEach(arg -> echoPrint(writer, arg, size, lastArg));

        if (size > 0) {
            // check if anything was printed if so print a newline.
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }

    // Auxiliary method for ECHO to print arg onto outputstream.
    private void echoPrint(OutputStreamWriter writer, String arg, int size, String lastArg) {

        try {
            writer.write(arg);
            if ((!arg.equals(lastArg))) {
                // if you printing more than 1 or arg isnt the last arg print whitespace after.
                writer.write(" ");
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("echo: unable to print args.");
        }
    }

    /**
     * Head prints the first n lines of the given input, if no number is specified
     * with the -n marker it defaults to 10. If there are fewer lines than n it
     * prints every line. There are two cases for the input method. If 0 args or 2
     * args (-n number), it uses the InputStream. If 1 arg (file) or 3 args (-n
     * number file), it uses the file specified.
     * 
     * @param app takes in a generalised form app which has the properties
     *            InputStream input, OutputStream output, String currDirectory,
     *            ArrayList<String> appArgs.
     * @return nothing, as any values obtained are written to the OutputStream
     *         specified by the user.
     * @exception RuntimeException if the number of arguments are wrong, the input
     *                             stream is null, or the files specified dont
     *                             exist, cant open, or are folders. Also if the arg
     *                             before the number is not -n, or the number arg is
     *                             negative or not an integer.
     */
    public void visit(Visitable.Head app) throws IOException {

        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        if (app.appArgs.size() > 3) {
            throw new RuntimeException("head: too many arguments");
        }
        if ((app.appArgs.size() == 3 || app.appArgs.size() == 2) && !app.appArgs.get(0).equals("-n")) {
            throw new RuntimeException("head: wrong argument " + app.appArgs.get(0));
        }
        int headLines = 10;
        String headArg = "";
        if (app.appArgs.size() == 2 || app.appArgs.size() == 3) {
            try {
                headLines = Integer.parseInt(app.appArgs.get(1));
            } catch (Exception e) {
                throw new RuntimeException("head: wrong argument " + app.appArgs.get(1));
            }
            if (app.appArgs.size() == 3) {
                headArg = app.appArgs.get(2);
            }
        } else if (app.appArgs.size() == 1) {
            headArg = app.appArgs.get(0);
        }
        if (headLines < 0) {
            throw new RuntimeException("head: wrong argument " + String.valueOf(headLines));
        }
        if (app.appArgs.size() == 2 || app.appArgs.isEmpty()) {
            if (app.input == null) {
                throw new RuntimeException("head: no input stream given");
            }
            BufferedReader standardInputBuffer = new BufferedReader(new InputStreamReader(app.input));
            standardInputBuffer.lines().limit(headLines).forEach((line) -> lineOutputWriter(line, writer, "head"));
        } else if (app.appArgs.size() == 3 || app.appArgs.size() == 1) {
            File headFile = new File(app.currentDirectory + File.separator + headArg);
            if (headFile.exists()) {
                Charset encoding = StandardCharsets.UTF_8;
                Path filePath = Paths.get((String) app.currentDirectory + File.separator + headArg);
                try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                    reader.lines().limit(headLines).forEach((line) -> lineOutputWriter(line, writer, "head"));
                } catch (IOException e) {
                    throw new RuntimeException("head: cannot open " + headArg);
                }
            } else {
                throw new RuntimeException("head: " + headArg + " does not exist");
            }
        }
    }

    /**
     * Tail prints the last n lines of the given input, if no number is specified
     * with the -n marker it defaults to 10. If there are fewer lines than n it
     * prints every line. There are two cases for the input method. If 0 args or 2
     * args (-n number), it uses the InputStream. If 1 arg (file) or 3 args (-n
     * number file), it uses the file specified.
     * 
     * @param app takes in a generalised form app which has the properties
     *            InputStream input, OutputStream output, String currDirectory,
     *            ArrayList<String> appArgs.
     * @return nothing, as any values obtained are written to the OutputStream
     *         specified by the user.
     * @exception RuntimeException if the number of arguments are wrong, the input
     *                             stream is null, or the files specified dont
     *                             exist, cant open, or are folders. Also if the arg
     *                             before the number is not -n, or the number arg is
     *                             negative or not an integer.
     */
    public void visit(Visitable.Tail app) throws IOException {

        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        if (app.appArgs.size() > 3) {
            throw new RuntimeException("tail: wrong arguments");
        }
        if ((app.appArgs.size() == 3 || app.appArgs.size() == 2) && !app.appArgs.get(0).equals("-n")) {
            throw new RuntimeException("tail: wrong argument " + app.appArgs.get(0));
        }
        int tailLines = 10;
        String tailArg = "";
        if (app.appArgs.size() == 2 || app.appArgs.size() == 3) {
            try {
                tailLines = Integer.parseInt(app.appArgs.get(1));
            } catch (Exception e) {
                throw new RuntimeException("tail: wrong argument " + app.appArgs.get(1));
            }
            if (app.appArgs.size() == 3) {
                tailArg = app.appArgs.get(2);
            }
        } else if (app.appArgs.size() == 1) {
            tailArg = app.appArgs.get(0);
        }
        if (tailLines < 0) {
            throw new RuntimeException("tail: wrong argument " + String.valueOf(tailLines));
        }
        if (app.appArgs.size() == 2 || app.appArgs.isEmpty()) {
            if (app.input == null) {
                throw new RuntimeException("tail: no input stream given");
            }
            BufferedReader standardInputBuffer = new BufferedReader(new InputStreamReader(app.input));
            List<String> readerList = standardInputBuffer.lines().collect(Collectors.toList());
            readerList.stream().skip(((readerList.size() - tailLines) < 0) ? 0 : (readerList.size() - tailLines))
                    .forEach((line) -> lineOutputWriter(line, writer, "tail"));
        } else if (app.appArgs.size() == 3 || app.appArgs.size() == 1) {
            File tailFile = new File(app.currentDirectory + File.separator + tailArg);
            if (tailFile.exists()) {
                Charset encoding = StandardCharsets.UTF_8;
                Path filePath = Paths.get((String) app.currentDirectory + File.separator + tailArg);
                try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                    List<String> readerList = reader.lines().collect(Collectors.toList());
                    readerList.stream()
                            .skip(((readerList.size() - tailLines) < 0) ? 0 : (readerList.size() - tailLines))
                            .forEach((line) -> lineOutputWriter(line, writer, "tail"));
                } catch (IOException e) {
                    throw new RuntimeException("tail: cannot open " + tailArg);
                }
            } else {
                throw new RuntimeException("tail: " + tailArg + " does not exist");
            }
        }
    }

    public void visit(Visitable.Cat app) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        if (app.appArgs.isEmpty()) {
            if (app.input == null) {
                throw new RuntimeException("cat: no input stream given");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(app.input));
            reader.lines().forEach(line -> lineOutputWriter(line, writer, "cat"));
        } else {
            Charset encoding = StandardCharsets.UTF_8;
            for (String arg : app.appArgs) {
                File currFile = new File(app.currentDirectory, arg);
                if (currFile.exists()) {
                    Path filePath = currFile.toPath();
                    try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                        reader.lines().forEach(line -> lineOutputWriter(line, writer, "cat"));
                    } catch (IOException e) {
                        writer.close();
                        throw new RuntimeException("cat: cannot open " + arg);
                    }
                } else {
                    writer.close();
                    throw new RuntimeException("cat: file does not exist");
                }
            }
        }
    }

    /**
     * Prints all the files and folders in the current directory if no argument is
     * given. If argument is given then it prints all files/folders in given
     * directory.
     * 
     * @param  APP which contains information such as arguments and current
     *          directory
     * 
     * @throws RuntimeException if 1) There is more than 1 argument supplied.
     *                             2) The directory supplied doesnt exist.
     */
    public void visit(Visitable.Ls app) throws IOException {

        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        File currDir;

        if (app.appArgs.isEmpty()) {
            currDir = new File(app.currentDirectory);
        } else if (app.appArgs.size() == 1) {
            if (app.appArgs.get(0).charAt(0) == '/') {
                currDir = new File(app.appArgs.get(0));
            } else {
                currDir = new File(app.currentDirectory, app.appArgs.get(0));
            }
        } else {
            throw new RuntimeException("ls: too many arguments");
        }

        try {
            // filter in all the files/folders that dont start with ".". Then print each one
            // of them.
            long size = Stream.of(currDir.listFiles()).filter(f -> !f.getName().startsWith(".")).count();
            Stream<File> streamOfFiles = Stream.of(currDir.listFiles()).filter(f -> !f.getName().startsWith("."));
            streamOfFiles.forEach(f -> lsWriteFile(writer, f));

            if (size > 0) {
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("ls: no such directory");
        }
    }

    /**
     * Auxiliary method for LS to print argument f onto outputstream.
     * @param writer writes the filenames to the outputstream.
     * @param f file whos name we need to write.
     *  
     * @throws RuntimeException if it is unable to write to file.
     */
    private void lsWriteFile(OutputStreamWriter writer, File f) {

        try {
            writer.write(f.getName());
            writer.write("\t");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("ls: unable to write files.");
        }
    }

    /**
     * Grep takes either 1 appArg and an InputStream, or 2 appArgs, the second being
     * a file name. Goes through the method of input and gets all lines containing
     * the text in the first arg, outputs to specified OutputStream.
     * 
     * @param app takes in a generalised form app which has the properties
     *            InputStream input, OutputStream output, String currDirectory,
     *            ArrayList<String> appArgs.
     * @return nothing, as any values obtained are written to the OutputStream
     *         specified by the user.
     * @exception RuntimeException if the number of arguments are wrong, the input
     *                             stream is null, or the files specified dont
     *                             exist, cant open, or are folders.
     */
    public void visit(Visitable.Grep app) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        if (app.appArgs.isEmpty()) {
            throw new RuntimeException("grep: no arguments given");
        }
        Pattern grepPattern = Pattern.compile(app.appArgs.get(0));
        if (app.appArgs.size() == 1) {
            if (app.input == null) {
                throw new RuntimeException("grep: no input stream given");
            }
            BufferedReader standardInputBuffer = new BufferedReader(new InputStreamReader(app.input));
            standardInputBuffer.lines().filter(line -> grepPattern.matcher(line).find())
                    .forEach(line -> lineOutputWriter(line, writer, "grep"));
        } else {
            int numOfFiles = app.appArgs.size() - 1;
            Path filePath;
            Path[] filePathArray = new Path[numOfFiles];
            Path currentDir = Paths.get(app.currentDirectory);

            for (int i = 0; i < numOfFiles; i++) {
                filePath = currentDir.resolve(app.appArgs.get(i + 1));
                if (Files.notExists(filePath) || Files.isDirectory(filePath) || !Files.exists(filePath)
                        || !Files.isReadable(filePath)) {
                    writer.close();
                    throw new RuntimeException("grep: wrong file argument");
                }
                filePathArray[i] = filePath;
            }

            for (int j = 0; j < filePathArray.length; j++) {
                Charset encoding = StandardCharsets.UTF_8;
                try (BufferedReader reader = Files.newBufferedReader(filePathArray[j], encoding)) {
                    int k = j + 1;
                    reader.lines().filter(line -> grepPattern.matcher(line).find()).forEach(line -> {
                        if (numOfFiles > 1) {
                            line = (app.appArgs.get(k) + ":" + line);
                        }
                        lineOutputWriter(line, writer, "grep");
                    });
                } catch (IOException e) {
                    writer.close();
                    throw new RuntimeException("grep: cannot open " + app.appArgs.get(j + 1));
                }
            }
        }
    }

    /**
     * Find takes either 3 appArgs and uses a specified directory, or 2 appArgs and
     * uses the current directory. It goes through the directory, and all
     * sub-directories, and finds any files with the name given in arg 2. If there
     * is a * in the name, it will match any string to that *. Outputs the relative
     * path to all matching files. The visit function for find is used to work out
     * the arguments for the recursive function, which works out which files to
     * output.
     * 
     * @param app takes in a generalised form app which has the properties
     *            InputStream input, OutputStream output, String currDirectory,
     *            ArrayList<String> appArgs.
     * @return nothing, as any values obtained are written to the OutputStream
     *         specified by the user.
     * @exception RuntimeException if the number of arguments are wrong, -name is
     *                             not supplied before the search string, or the
     *                             directory given doesn't exist.
     */
    public void visit(Visitable.Find app) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        Path rootDirectory;
        Pattern findPattern;
        String callCommand = null;
        if (app.appArgs.size() != 2 && app.appArgs.size() != 3) {
            throw new RuntimeException("find: Wrong number of arguments");
        }
        if (!((app.appArgs.get(app.appArgs.size() - 2)).equals("-name"))) {
            throw new RuntimeException("find: Wrong argument " + app.appArgs.get(app.appArgs.size() - 2));
        }
        if (app.appArgs.size() == 2) {
            rootDirectory = Path.of(app.currentDirectory);
        } else {
            callCommand = app.appArgs.get(0);
            try {
                if (Path.of(callCommand).isAbsolute()) {
                    rootDirectory = Path.of(app.appArgs.get(0));
                } else {
                    rootDirectory = Path.of(app.currentDirectory, app.appArgs.get(0));
                }
            } catch (Exception e) {
                throw new RuntimeException("find: specified path does not exist");
            }
        }
        String regexString = app.appArgs.get(app.appArgs.size() - 1).replaceAll("\\*", ".*");
        findPattern = Pattern.compile(regexString);
        findRecurse(writer, rootDirectory, rootDirectory, callCommand, findPattern);
    }

    /**
     * findRecurse is the main function for find, which matches and outputs the
     * relative paths of all files which match the input string. It recursively
     * looks through all the subfolders to match any file to the string.
     * 
     * @param writer        is the area to write any outputs to.
     * @param currDirectory tells the recursive function which directory it is
     *                      currently in.
     * @param rootDirectory tells the function where it came from in order to make
     *                      the paths relative.
     * @param callCommand   is the original command used to specify which directory
     *                      to look in. If null it knows to prefix the output with
     *                      "./", if char 0 is '/' it knows to use absolute pathing.
     * @param findPattern   is the regex pattern specified in the appArgs which we
     *                      are matching all the files to.
     * @return nothing, as any values obtained are written to the OutputStream
     *         specified by the user.
     */
    private void findRecurse(OutputStreamWriter writer, Path currDirectory, Path rootDirectory, String callCommand,
            Pattern findPattern) throws IOException {
        File[] listOfFiles = currDirectory.toFile().listFiles();
        Stream<File> FileStream = Stream.of(listOfFiles);
        FileStream.forEach(file -> {
            if (file.isDirectory()) {
                try {
                    findRecurse(writer, file.toPath(), rootDirectory, callCommand, findPattern);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (findPattern.matcher(file.getName()).matches()) {
                if (callCommand == null) {
                    lineOutputWriter("./".concat(rootDirectory.toUri().relativize(file.toURI()).toString()), writer,
                            "find");
                } else if (Path.of(callCommand).isAbsolute()) {
                    lineOutputWriter(file.getAbsolutePath().toString(), writer, "find");
                } else {
                    lineOutputWriter(Path.of(Jsh.getCurrentDirectory()).toUri().relativize(file.toURI()).toString(),
                            writer, "find");
                }
            }
        });
    }

    /**
     * Takes specified bytes from each line in a text file then outputs it. This
     * function assumes you cant have multiple different options. Eg 1,2,3 OR
     * 5-6,7-8 OR -5,6-. Not 1,2,4-5. It firstly parses the string and checks if its in
     * the correct format. Next it decides which format is it in and calls the relevant auxiliary
     * function to extract the bytes we want from each line in the supplied file.
     * 
     * @param APP contains info about arguments, currentDirectory, appArgs, inputstream and outputstream.
     * 
     * @throws RuntimeException if 1) There are too many or too few arguments.
     *                             2) Argument uses something other than "-b".
     *                             3) Mixed up intervals for example "-3,4-5". Can only have 1 type of interval.
     *                             4) If it cannot open the file to read.
     */
    public void visit(Visitable.Cut app) {

        int inputStreamUsed = 0; // Set this to 1 if file isnt supplied and input stream is not null
        if (app.appArgs.size() < 3) {
            if (app.input != null) {
                inputStreamUsed = 1;
            } else {
                // input stream is null and no file specified. Throw exception.
                throw new RuntimeException("cut: too few arguments.");
            }
        }
        if (!app.appArgs.get(0).equals("-b")) {
            throw new RuntimeException("cut: incorrect option input " + app.appArgs.get(0));
        }
        if (app.appArgs.size() > 3) {
            throw new RuntimeException("cut: too many arguments.");
        }

        Charset charset = StandardCharsets.UTF_8;
        OutputStreamWriter writer = new OutputStreamWriter(app.output, charset);
        String[] args = app.appArgs.get(1).split(",");

        for (int i = 0; i != args.length; ++i) {
            if (!Pattern.matches("[0-9]*-*[0-9]*", args[i])) {
                // Check arguments are 1, 4-7, -5 or 6-.
                throw new RuntimeException("cut: invalid arguments.");
            }
        }

        File file = null;
        Path filePath = null;
        if (inputStreamUsed == 0) {
            // we are using app args
            file = new File(app.currentDirectory + File.separator + app.appArgs.get(2));
            filePath = Paths.get(app.currentDirectory + File.separator + app.appArgs.get(2));
        }
        if (!args[0].contains("-")) {
            // of the format 1,2,3.
            try {
                BufferedReader reader = initReader(inputStreamUsed, charset, filePath, file, app.input);
                reader.lines().forEach(line -> cutSingleBytes(line, writer, args, charset));
            } catch (IOException e) {
                throw new RuntimeException("cut: cannot open " + app.appArgs.get(2));
            }
        }

        else {
            if (Pattern.matches("[0-9]+-[0-9]+", args[0])) {
                // option is of format [0-9]+-[0-9]+.
                // calculate length of bytesToPrint
                int length = 0;
                for (int i = 0; i != args.length; ++i) {
                    int index = args[i].indexOf("-");
                    length += Integer.parseInt(args[i].substring(index + 1, args[i].length()))
                            - Integer.parseInt(args[i].substring(0, index)) + 1;
                }
                final int lengthFinal = length; // to make it work with streams. length isnt updated after this point
                                                // anyway.

                try {
                    BufferedReader reader = initReader(inputStreamUsed, charset, filePath, file, app.input);
                    reader.lines().forEach(line -> cutIntervals(line, writer, args, charset, lengthFinal));
                } catch (IOException e) {
                    throw new RuntimeException("cut: cannot open " + app.appArgs.get(2));
                }
            }

            else {
                // options are of the form -3,6- etc
                ArrayList<Integer> to = new ArrayList<>();
                ArrayList<Integer> from = new ArrayList<>();
                for (int i = 0; i != args.length; ++i) {
                    if (args[i].charAt(0) == '-') {
                        // of the form -[0-9].
                        to.add(Integer.parseInt(args[i].substring(1, args[i].length())));
                    } else {
                        from.add(Integer.parseInt(args[i].substring(0, args[i].length() - 1)));
                    }
                }

                try {
                    BufferedReader reader = initReader(inputStreamUsed, charset, filePath, file, app.input);
                    reader.lines().forEach(line -> cutHalfIntervals(line, writer, to, from, charset));
                } catch (IOException e) {
                    throw new RuntimeException("cut: cannot open " + app.appArgs.get(2));
                }
            }
        }
    }

    /**
     * Auxiliary method for Cut. It was made to allow cut with work with both
     * standard input and supplied file argument. It checks if the flag specifing
     * that appArgs < 3 && input != null is raised. If it isn't then we are using
     * the supplied File file. Else we make a BufferedReader object using the
     * inputstream and return it.
     * 
     * @param inputStreamUsed is a flag. It is 1 if we are using inputstream else it is 0.
     * @param charset is UTF-8 
     * @param filePath is Path object specifing path to file. If we are using inputstream it is null.
     * @param file is File object. it is null if we are using input stream. 
     * @param input is null if we are using File file otherwise it is initialized.
     * 
     * @return = bufferedreader that will be used to read each line.
     * 
     * @throws IOException if it is unable to create a bufferedreader object.
     * @throws RuntimeException if it is unable to find the file to create the bufferedreader object for.
     */
    private BufferedReader initReader(int inputStreamUsed, Charset charset, Path filePath, File file, InputStream input)
            throws IOException {

        if (inputStreamUsed == 0) {
            // using appArgs
            if (file.exists()) {
                return Files.newBufferedReader(filePath, charset);
            } else {
                throw new RuntimeException("cut: file input does not exist.");
            }
        }

        return new BufferedReader(new InputStreamReader(input));
    }

    /**
     * Auxiliary method for Cut. It works for input of the format 1,2,3 etc. The
     * algorithm takes each line, converts it to bytes then extracts the bytes that
     * we need and stores them in BYTESTOPRINT. Finally it outputs BYTESTOPRINT as a
     * string.
     * 
     * @param line is the line we are looking at in file. 
     * @param writer is used to write to output.
     * @param args contains the bytes to extract. e.g. it may be ["1","3"]
     * @param charset is the charset of the file we are reading it. It is UTF_8.
     * 
     * @throws RuntimeException if the byte interval is out of bounds.
     */
    private void cutSingleBytes(String line, OutputStreamWriter writer, String[] args, Charset charset) {

        byte[] bytes = line.getBytes(charset);
        byte[] bytesToPrint = new byte[args.length];

        if (bytes.length == 0) {
            // empty line
            return;
        }

        try {
            for (int i = 0; i != args.length; ++i) {
                // bytes is offset by 1. Eg first byte is at index 0 in bytes. So -1 to get
                // first byte.
                bytesToPrint[i] = bytes[Integer.parseInt(args[i]) - 1];
            }
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("cut: byte index given is too large. Lines have less bytes.");
        }

        lineOutputWriter(new String(bytesToPrint, charset), writer, "cut");
    }

    /**
     * Auxiliary method for Cut. It works for input of the format 1-3,6-7.. etc. The
     * algorithm takes each line, converts it to bytes then extracts the bytes that
     * we need and stores them in BYTESTOPRINT. Finally it outputs BYTESTOPRINT as a
     * string.
     * 
     * @param line is the line we are looking at in file. 
     * @param writer is used to write to output 
     * @param args contains the bytes to extract. e.g. it may be ["1","3"]
     * @param charset is the charset of the file we are reading it. It is UTF_8. 
     * @param length is the length of the BYTESTOPRINT array that is precalculated.
     * 
     * @throws RuntimeException if the byte interval is out of bounds.
     */
    private void cutIntervals(String line, OutputStreamWriter writer, String[] args, Charset charset, int length) {

        byte[] bytes = line.getBytes(charset);
        byte[] bytesToPrint = new byte[length];

        if (bytes.length == 0) {
            // empty line.
            return;
        }

        int counter = 0;
        try {
            for (int i = 0; i != args.length; ++i) {
                // for each interval in args
                int index = args[i].indexOf("-");
                for (int j = Integer.parseInt(args[i].substring(0, index)); j != (Integer
                        .parseInt(args[i].substring(index + 1, args[i].length())) + 1); ++j) {
                    // loop from start to end of interval
                    bytesToPrint[counter++] = bytes[j - 1];
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("cut: byte index given is too large. Lines have less bytes.");
        }

        lineOutputWriter(new String(bytesToPrint, charset), writer, "cut");
    }

    /** 
     * Auxiliary method for Cut. It works for input of the format 1,2,3 etc. The
     * algorithm takes each line, converts it to bytes then extracts the bytes that
     * we need and stores them in BYTESTOPRINT. Finally it outputs BYTESTOPRINT as a
     * string.
     * 
     * @param line is the line we are looking at in file. 
     * @param writer is used to write to output 
     * @param to contains integers that upper bound the interval. Each index corresponds 
     *           to integer at the same index in from. E.g. from[0] and to[0] form 1 interval,
     *           from[1] and to[1] form another and so on.
     * @param from contains integers that lower bound the interval.
     * @param charset is the charset of the file we are reading it. It is UTF_8.
     * 
     * @throws RuntimeException if the byte interval is out of bounds.
     */
    private void cutHalfIntervals(String line, OutputStreamWriter writer, ArrayList<Integer> to,
            ArrayList<Integer> from, Charset charset) {

        byte[] bytes = line.getBytes(charset);
        byte[] bytesToPrint = new byte[bytes.length]; // Assume at most we print all bytes in line.
        if (bytes.length == 0) {
            return;
        }
        
        // This section deals with overlapping parts.
        int highest = -1;
        int indexOfHighest = -1;
        if (to.size() > 1) {
            // you have overlapping ranges take the highest element.
            for (int i = 0; i != to.size(); ++i) {
                if (highest <= to.get(i)) {
                    highest = to.get(i);
                    indexOfHighest = i;
                }
            }
            // make it so the only element is the highest.
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(to.get(indexOfHighest));
            to = temp;
        }

        int lowest = 1000000000; // large number so it always is reset to lwoest at start.
        int indexOfLowest = -1;
        if (from.size() > 1) {
            // you have overlapping ranges. Take the lowest to.
            for (int i = 0; i != from.size(); ++i) {
                if (lowest >= from.get(i)) {
                    lowest = from.get(i);
                    indexOfLowest = i;
                }
            }
            // make it so the only element is the lowest.
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(from.get(indexOfLowest));
            from = temp;
        }
        if ((to.size() == 1) && (from.size() == 1)) {
            if (to.get(0) > from.get(0)) {
                // check if ranges from to and from overlap. E.g. -5,3- overlap so you would
                // just output entire line.
                lineOutputWriter(new String(bytes, charset), writer, "cut");
            }
        }

        // Extract and print relevant bytes.
        int counter = 0;
        try {
            for (int i = 0; i != to.size(); ++i) {
                // Extract bytes to. Eg -5 will get first 4 bytes
                for (int j = 0; j != to.get(i); ++j) {
                    // dont offset this since it starts at same point.
                    bytesToPrint[counter++] = bytes[j];
                }
            }

            for (int i = 0; i != from.size(); ++i) {
                // Extract bytes from. Eg 5- will get bytes 5 to end.
                for (int j = from.get(i) - 1; j != bytes.length; ++j) {
                    // offset this since 5th byte would be at index 4.
                    bytesToPrint[counter++] = bytes[j];
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("cut: byte index given is too large. Lines have less bytes.");
        }

        bytesToPrint = removeEmptyBytes(bytesToPrint);
        lineOutputWriter(new String(bytesToPrint, charset), writer, "cut");
    }

    /** 
     * Auxiliary method for cut. It removes any leftover null elements in
     * bytesToPrint.
     * 
     * @param = bytesToPrint, byte array that contains all the bytes that we want
     * to output.
     * 
     * @return = ret, contains all the bytes in bytesToPrint but removed any null
     * entries.
     */
    private byte[] removeEmptyBytes(byte[] bytesToPrint) {

        List<Byte> temp = new ArrayList<Byte>();
        for (byte elem : bytesToPrint) {
            if (elem != 0) {
                // not null
                temp.add(elem);
            }
        }

        byte[] ret = new byte[temp.size()];
        for (int i = 0; i != temp.size(); ++i) {
            ret[i] = temp.get(i);
        }

        return ret;
    }

    /**
     * lineOutputWriter is an abstracted function which uses the generalised form of
     * how the visit functions usually write to the OutputStream.
     * 
     * @param line    is the line which is being written to the OutputStream.
     * @param writer  is the OutputStreamWriter which we use to write to the
     *                OutputStream.
     * @param appname is the name of the function we are writing for, so that if an
     *                error occurs writing the data then the error message will be
     *                correct.
     * @return nothing, as all inputs are written to the OutputStream specified by
     *         the user.
     * @exception RuntimeException if the line cannot be written to the
     *                             OutputStream.
     */
    private void lineOutputWriter(String line, OutputStreamWriter writer, String appname) {
        try {
            writer.write(line);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(appname + ": error with given inputs");
        }
    }

    /*
     * This function uses the text file provided as an argument in order to sort the
     * contents and write out as the outputstream. The sorting is done trivially
     * using streams api. If -r is provided, the the output will be in reverse
     * order. Furthermore, if the app args is empty or contains no file name, then
     * we use stdin as our input stream.
     * 
     * @Params = APP contains info about arguments and currentDirectory.
     */
    public void visit(Visitable.Sort app) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(app.output);
        if (app.appArgs.size() > 2) {
            throw new RuntimeException("sort: too many arguments");
        }
        String reverse = "";
        String headArg = "";
        if (app.appArgs.size() == 2) {
            try {
                reverse = app.appArgs.get(0);
                headArg = app.appArgs.get(1);
            } catch (Exception e) {
                throw new RuntimeException("sort: wrong argument here1" + app.appArgs.get(1));
            }
        } else if (app.appArgs.size() == 1) {
            headArg = app.appArgs.get(0);
        }
        if ((app.appArgs.size() == 2) && !app.appArgs.get(0).equals("-r")) {
            throw new RuntimeException("sort: wrong argument " + app.appArgs.get(0));
        }
        String sortFile = app.currentDirectory + File.separator + headArg;
        try (Stream<String> lines = Files.lines(Paths.get(sortFile))) {
            if (app.appArgs.isEmpty() || (app.appArgs.size() == 1 && app.appArgs.get(0).equals("-r"))) {
                if (app.input != null) {

                    // if the app args are empty and only -r is provided, we are using stdin

                    BufferedReader standardInputBuffer = new BufferedReader(new InputStreamReader(app.input));
                    standardInputBuffer.lines().sorted().forEach((line) -> lineOutputWriter(line, writer, "sort"));
                } else {
                    throw new RuntimeException("sort: error with stdin");
                }

            }
            if (app.appArgs.size() == 1) {
                lines.sorted().forEach(s -> {
                    try {
                        writer.write(s);
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    } catch (IOException e) {
                        throw new RuntimeException("sort: cannot open " + app.appArgs.get(0));
                    }
                });
            } else if (app.appArgs.size() == 2) {
                // using the comparator to reverse the order if -r is provided in the args

                lines.sorted(Comparator.reverseOrder()).forEach(s -> {
                    try {
                        writer.write(s);
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    } catch (IOException e) {
                        throw new RuntimeException("sort: cannot open " + app.appArgs.get(0));
                    }
                });
            }
        }
    }

    /*
     * This function uses the text file provided as an argument in order to apply
     * the uniq linux command. This command deletes all lines that are not unique,
     * in this case defined as not the same as the previous line. It then writes
     * these changes not only to the file, but the output stream. If -i is passed,
     * then we do not consider case when doing the comp- arisons. If the app_args
     * are empty or only contain -i, we use stdin as our input stream.
     * 
     * @Params = APP contains info about arguments and currentDirectory.
     */
    public void visit(Visitable.Uniq app) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(app.output);
        if (app.appArgs.size() > 2) {
            throw new RuntimeException("uniq: too many arguments");
        }
        String reverse = "";
        String headArg = "";
        if (app.appArgs.size() == 2) {
            try {
                reverse = app.appArgs.get(0);
                headArg = app.appArgs.get(1);
            } catch (Exception e) {
                throw new RuntimeException("uniq: wrong argument here1" + app.appArgs.get(1));
            }
        } else if (app.appArgs.size() == 1) {
            headArg = app.appArgs.get(0);
        }
        if ((app.appArgs.size() == 2) && !app.appArgs.get(0).equals("-i")) {
            throw new RuntimeException("uniq: wrong argument " + app.appArgs.get(0));
        }
        String sortFile = app.currentDirectory + File.separator + headArg;
        try (Stream<String> lines = Files.lines(Paths.get(sortFile))) {
            if (app.appArgs.isEmpty() || (app.appArgs.size() == 1 && app.appArgs.get(0).equals("-i"))) {
                if (app.input != null) {

                    /*
                     * It is worth noting why we have chosen a linkedlist. Mostly due to its
                     * simplicity. While is may not be the most efficient for searching, it is
                     * elegant for keeping track of the previous line in text file, and its
                     * 'getLast()' method has shown to be effective.
                     */

                    LinkedList<String> previous = new LinkedList<String>();
                    previous.add("");
                    BufferedReader standardInputBuffer = new BufferedReader(new InputStreamReader(app.input));
                    if (app.appArgs.size() == 1 && app.appArgs.get(0).equals("-i")) {

                        // we are using stdin here as our input stream as no correct text file for
                        // applying
                        // the linux uniq command has been given.

                        standardInputBuffer.lines().forEach((line) -> {
                            if (!line.toLowerCase().equals(previous.getLast().toLowerCase())) {
                                lineOutputWriter(line, writer, "uniq");
                                previous.add(line);
                            }
                        });
                    } else {
                        standardInputBuffer.lines().forEach((line) -> {
                            if (!line.equals(previous.getLast())) {
                                lineOutputWriter(line, writer, "uniq");
                                previous.add(line);
                            }
                        });
                    }
                } else {
                    throw new RuntimeException("uniq: error with stdin");
                }
            }
            if (app.appArgs.size() == 1) {
                File file = File.createTempFile("temp", ".txt");
                File headFile = new File(app.currentDirectory + File.separator + headArg);
                FileWriter fw = new FileWriter(file.getName(), false);
                LinkedList<String> previous = new LinkedList<String>();
                previous.add("");
                lines.forEach(s -> {
                    try {
                        if (!s.equals(previous.getLast())) {
                            writer.write(s);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                            fw.write(s);
                            fw.write(System.getProperty("line.separator"));
                            fw.flush();
                            previous.add(s);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("uniq: cannot open " + app.appArgs.get(0));
                    }
                });
                fw.close();
                headFile.delete();
                Boolean t = file.renameTo(headFile);
            } else if (app.appArgs.size() == 2) {
                File file = File.createTempFile("temp", ".txt");
                File headFile = new File(app.currentDirectory + File.separator + headArg);
                FileWriter fw = new FileWriter(file.getName(), false);
                LinkedList<String> previous = new LinkedList<String>();
                previous.add("");
                lines.forEach(s -> {
                    try {
                        if (!s.toLowerCase().equals(previous.getLast().toLowerCase())) {
                            writer.write(s);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                            fw.write(s);
                            fw.write(System.getProperty("line.separator"));
                            fw.flush();
                            previous.add(s);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("uniq: cannot open " + app.appArgs.get(0));
                    }
                });
                fw.close();
                headFile.delete();
                Boolean t = file.renameTo(headFile);
            }
        }
    }

    /**
        * Will create a directory inside the current directory with the name provided assuing the name is valid
        * Does not create directory within directories e.g. mkdir one/two will not work.
        * 
        * @param commandLineArgs Holds nothing or the requested path
        * @param input           The InputStream from which to read from if a pipe or redirection is occuring
        * @param output          The OutputStream to write the result of the application to.
        * @throws IOException    Exception thrown by BufferedWriter because of something like a closed pipe
    */
    @Override
    public void visit(Visitable.Mkdir app) throws IOException {
        if (app.appArgs.size() != 1) {
            throw new RuntimeException("mkdir: only one argument allowed");
        }
        String dirName = app.appArgs.get(0);
        File newDir = new File(Jsh.getCurrentDirectory() + System.getProperty("file.separator") + dirName);
        if (newDir.isFile()) {
            throw new RuntimeException("mkdir: cannot create directory '" + dirName + "': File Exists");
        } else if (newDir.isDirectory()) {
            throw new RuntimeException("mkdir: cannot create directory '" + dirName + "': Directory Exists");
        }
        newDir.mkdir();
    }

     /**
     * Will remove an empty directory within the current directory or if the -r tag is appllied will remove
     * all subdirectories and files from the named directory passed as an argument.
     * 
     * @param commandLineArgs Holds nothing or the requested path
     * @param input           The InputStream from which to read from if a pipe or redirection is occuring
     * @param output          The OutputStream to write the result of the application to.
     * @throws IOException    Exception thrown by BufferedWriter because of an issue such as a closed pipe
     */
    @Override
    public void visit(Visitable.Rmdir app) throws IOException {
        checkArgs(app.appArgs);
        File directory;
        if (app.appArgs.size() == 2) {
            directory = new File(Jsh.getCurrentDirectory() + System.getProperty("file.separator") + app.appArgs.get(1));
            if (directory.isDirectory()) {
                removeEntireDirectory(directory);
            } else {
                throw new RuntimeException("Directory does not exist");
            }
        } else {
            directory = new File(Jsh.getCurrentDirectory() + System.getProperty("file.separator") + app.appArgs.get(0));
            removeEmptyDirectory(directory);
        }
    }

    /**
     * If -r flag applied the the method will recursively call itself and delete all folders and
     * files inside the folder passed as an argument.
     * 
     * @param directory The File or directory to be deleted.
     */
    private void removeEntireDirectory(File directory) {
        if (directory.isDirectory()) {
            if (directory.list().length == 0) {
                directory.delete();
            } else {
                String files[] = directory.list();
                for (String fileName : files) {
                    File deleteFile = new File(directory, fileName);
                    removeEntireDirectory(deleteFile);
                }
                directory.delete();
            }
        } else {
            directory.delete();
        }
    }

    /**
     * If no flag is given then checks to see if the directory exists and is empty, if so then the 
     * directory is deleted.
     * 
     * @param directory The name of the directory to be deleted.
     */
    private void removeEmptyDirectory(File directory) {
        if (!directory.exists()) {
            throw new RuntimeException("Directory does not exist");
        } else if (!directory.isDirectory()) {
            throw new RuntimeException(directory.getName() + " is not a directory");
        }
        if (directory.list().length == 0) {
            directory.delete();
        } else {
            throw new RuntimeException("Directory is not empty");
        }
    }

    /**
     * Performs sanity checks of the number of arguments passed to the application
     * 
     * @param commandLineArgs List of arguments passed to the application
     */
    private void checkArgs(ArrayList<String> commandLineArgs) {
        if (commandLineArgs.size() != 1 && commandLineArgs.size() != 2) {
            throw new RuntimeException("rmdir: Wrong number of arguments");
        }
        if (commandLineArgs.size() == 2 && !commandLineArgs.get(0).equals("-r")) {
            throw new RuntimeException("rmdir: " + commandLineArgs.get(0) + " is not a valid flag");
        }
    }

    //Run application by typing in date, application named Datetime to avoid name conflict with imported Date package
    /**
     * Outputs to the OutputStream the current date and time in the following format.
     * E M d H:m:s z Y
     * according to date and time patterns described in the oracle docs for SimpleDateFormat.
     *
     * @param commandLineArgs Holds the new dirctory that is being requested
     * @param input           The InputStream from which to read from if a pipe or redirection is occuring
     * @param output          The OutputStream to write the result of the application to.
     * @throws IOException    Exception thrown by BufferedWriter because of something like a closed pipe
     */
    @Override
    public void visit(Visitable.DateTime app) throws IOException {
        Charset encoding = StandardCharsets.UTF_8;
        Date date = new Date(System.currentTimeMillis());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(app.output, encoding));
        writer.write(date.toString());
        writer.write(System.getProperty("line.separator"));
        writer.flush();
    }

    public void visit(Visitable.WordCount app) {
        
    }

    /**
     * Prints out each command that has been entered before. Simply gets an arraylist of 
     * previous commands and converts it to a stream and prints them one by one.
     * 
     * @param app object contains relevant variables such as outputstream as app.output and 
     *        app arguments as app.appArgs.
     * @throws RuntimeException if you supply any arguments to it.
     */
    public void visit(Visitable.History app) {

        if (app.appArgs.size() != 0) {
            throw new RuntimeException("history: too many arguments supplied");
        }

        Stream<String> history = Jsh.getHistory().stream();
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        history.forEach(elem -> lineOutputWriter(elem, writer, "history"));
    } 


    /**
     * Of the format "cp (-r) source1 source2 source3 ... dest". 
     * "-r" is optional and if it is supplied it means copy directories.
     * There can be any number of source files to copy.
     * Doesnt work with input stream yet.
     * 
     * @param app object contains relevant information such as currentDirectory and appArgs.
     * 
     * @throws IOException if it is unable to copy the files.
     */
    public void visit(Visitable.Copy app) throws IOException {

        String dest = app.appArgs.get(app.appArgs.size() - 1);   // last element is destination.
        app.appArgs.remove(app.appArgs.size() - 1);
        
        if (app.appArgs.get(0).equals("-r")) {
            // copying directories
            app.appArgs.remove(0);               // remove -r.
            Stream<String> stream = app.appArgs.stream();
            stream.forEach(directory -> {
                String srcDir = app.currentDirectory + System.getProperty("file.separator") + directory;
                String destDir = app.currentDirectory + System.getProperty("file.separator") + dest;
                copyDirectory(srcDir, destDir);});
        }
        else {
            // copying files.
            Stream<String> stream = app.appArgs.stream();
            stream.forEach(file -> {
                try {
                    String src = app.currentDirectory + System.getProperty("file.separator") + file;
                    String destination = app.currentDirectory + System.getProperty("file.separator") + dest + System.getProperty("file.separator") + file;
                    copyFile(src, destination);
                } catch (IOException e) {
                    throw new RuntimeException("cp: unable to copy file.");
                }
            });

        }
    }

    /**
     * This copies the file argument into the dest directory. 
     * 
     * @param file is a string that indicates the path to the directory that src is in.
     * @param dest is a string that indicates the path to the directory that dest is in.
     * 
     * @throws IOException if it is unable to copy the file.
     */
    private void copyFile(String src, String dest) throws IOException {

        Path source = Paths.get(src);
        Path destination = Paths.get(dest);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Copies the directory specified by src into dest.
     * 
     * @param src is a string that indicates the path to src directory
     * @param dest is a string that indicates the path to dest directory
     * 
     * @throws IOException if it is unable to copy files.
     */
    private void copyDirectory(String src, String dest) {

        Path source = Paths.get(src);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    // copying subdirectory. First we need to create subdirectory in dest then copy all the files from subdirectory into the
                    // newly created dest/subdir.
                    String path = dest + System.getProperty("file.separator") + entry.getFileName().toString();
                    new File(path).mkdir();
                    copyDirectory(entry.toString(), path);
                }
                else {
                    copyFile(entry.toString(), dest + System.getProperty("file.separator") + entry.getFileName().toString());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("cp: unable to copy directory.");
        }
    }
}