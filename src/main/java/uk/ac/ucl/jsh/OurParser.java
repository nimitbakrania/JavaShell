package uk.ac.ucl.jsh;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.Scanner;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class OurParser {
    
    private String currCmdline;

    public OurParser() { }

    private void setCmdline(String cmdline) {

        this.currCmdline = cmdline;
    }
    
    /* Handles calling relevant functions to parse input. First we seperate the commands up using getCommands.
       Next we check if any commands use command substitution. If they do we execute it and replace the backquoted
       command with the output from it. Finally we loop through each command and split it into tokens and store it as an
       ArrayList<String>. Each ArrayList is added to ret and returned.
       @params = cmdline is the commandline that we need to parse.
                 currentDirectory is the current directory that jsh is in.
       @returns = an arraylist of an arraylist<string>. it contains each command that has been split into tokens.
    */
    public ArrayList<ArrayList<String>> parse(String cmdline, String currentDirectory) throws IOException {

        if (checkCmdSubstitution(cmdline)) {
            cmdline = cmdSubstitution(cmdline);
        }
        setCmdline(cmdline);
        ArrayList<String> rawCommands = getCommands();
        
        for (int i = 0; i != rawCommands.size(); ++i) {
            if (checkCmdSubstitution(rawCommands.get(i))) {
                String newCmd = cmdSubstitution(rawCommands.get(i));
                rawCommands.set(i, newCmd);
            }
        }
        
        ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
        for (String command : rawCommands) {
            ret.add(splitIn2Tokens(command, currentDirectory));
        }

        return ret;
    }

    /* Splits the commands on ";". 
       @returns = arraylist of commands.
    */
    private ArrayList<String> getCommands() {

        CharStream parserInput = CharStreams.fromString(this.currCmdline); 
        JshGrammarLexer lexer = new JshGrammarLexer(parserInput);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);        
        JshGrammarParser parser = new JshGrammarParser(tokenStream);
        ParseTree tree = parser.command();

        ArrayList<String> rawCommands = new ArrayList<String>();
        String lastSubcommand = "";
        for (int i=0; i<tree.getChildCount(); i++) {
            if (!tree.getChild(i).getText().equals(";")) {
                lastSubcommand += tree.getChild(i).getText();
            } else {
                rawCommands.add(lastSubcommand);
                lastSubcommand = "";
            }
        }
        rawCommands.add(lastSubcommand);
        return rawCommands;
    }

    /* Splits the commands into tokens and adds them into an arraylist which it then returns
       @params = rawCommand is the untokenized command. 
                 currentDirectory is the directory that jsh is currently in.
    */
    private ArrayList<String> splitIn2Tokens(String rawCommand, String currentDirectory) throws IOException {

        String spaceRegex = "[^\\s\"']+|\"([^\"]*)\"|'([^']*)'";
        ArrayList<String> tokens = new ArrayList<String>();           // Holds the seperated cmd tokens.
        Pattern regex = Pattern.compile(spaceRegex);
        Matcher regexMatcher = regex.matcher(rawCommand);
        String nonQuote;
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null || regexMatcher.group(2) != null) {
                String quoted = regexMatcher.group(0).trim();
                tokens.add(quoted.substring(1,quoted.length()-1));
            } else {
                nonQuote = regexMatcher.group().trim();
                ArrayList<String> globbingResult = new ArrayList<String>();
                Path dir = Paths.get(currentDirectory);
                DirectoryStream<Path> stream = Files.newDirectoryStream(dir, nonQuote);
                for (Path entry : stream) {
                    globbingResult.add(entry.getFileName().toString());
                }
                if (globbingResult.isEmpty()) {
                    globbingResult.add(nonQuote);
                }
                tokens.addAll(globbingResult);
            }
        }

        ArrayList<String> ret = new ArrayList<String>(tokens.subList(0, tokens.size()));
        return ret;
    }
    
    /* Checks to see if CMD uses command substitution by looping to see 2 backquotes.
       @returns = true if command substitution is found.
    */
    private boolean checkCmdSubstitution(String cmd) {

        int singleQuoteSeen = 0;
        int backQuoteSeen = 0;          // Only work if you see 2 backquotes.
        for (int i = 0; i != cmd.length(); ++i) {
            String charAt = Character.toString(cmd.charAt(i));
            if (charAt.equals("'")) {
                if (singleQuoteSeen == 0) {
                    // left ' seen. 
                    singleQuoteSeen = 1;
                }
                else {
                    // right ' seen.
                    singleQuoteSeen = 0;
                }
            }
            else if (charAt.equals("`")) {
                backQuoteSeen++;
                // ` is seen. We need to check if it is encased in single quotes.
                // we do this by checking singleQuoteSeen. if it is 0 then it isn't encased in single quotes.
                if ((singleQuoteSeen == 0) && (backQuoteSeen % 2 == 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Executes command substitution. It extracts the subcommand encased in `. It first 
       executes the subcommand. It gets the output of the subcommand and returns it. 
    */
    private String cmdSubstitution(String cmd) {

        // first go thru cmd and split on ;
        // next go thru each command and execute command substitution if applicable.
        // concat and return

        ArrayList<String> commands = new ArrayList<>();
        int flagBackQuote = 0;                           // change this to 1 when inside quotes. 0 outside.
        int startIndex = 0;
        int endIndex = -1;
        for (int i = 0; i != cmd.length(); ++i) {
            if (cmd.charAt(i) == '`') {
                if (flagBackQuote == 0) {
                    // going into ` scope.
                    flagBackQuote = 1;
                }
                else {
                    flagBackQuote = 0;
                }
            }
            else if ((cmd.charAt(i) == ';') && (flagBackQuote == 0)) {
                // seen a ; that isnt inside `.
                endIndex = i;
                commands.add(cmd.substring(startIndex, endIndex));
                startIndex = i + 1;
            }
            if (i == cmd.length() - 1) {
                // reached the end of cmd.
                commands.add(cmd.substring(startIndex, i + 1));
            }
        }

        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i != commands.size(); ++i) {
            String curCommand = commands.get(i);
            if (!checkCmdSubstitution(curCommand)) {
                // this command doesnt have substitution.
                ret.add(curCommand);
                continue;
            }
            
            startIndex = -1;                      // now holds index of lhs `.
            endIndex = -1;                        // holds index of rhs `.
            String subCommand = "";
            int counter = 0;

            if (curCommand.charAt(0) == ' ') {
                // remove whitespace at start.
                curCommand = curCommand.substring(1, curCommand.length());
            }

            for (int j = 0; j != curCommand.length(); ++j) {
                // Extracting the subcommand from cmd.
                if (curCommand.charAt(j) == '`') {
                    if (counter == 0) {
                        // seeing the left `.
                        counter = 1;
                        startIndex = j;
                    }
                    else {
                        counter = 0;
                        endIndex = j;
                    }
                }
                else if (counter == 1) {
                    // we are inside ` `.
                    subCommand += curCommand.charAt(j);
                }
            }

            ret.add(executeCmdSubstitution(startIndex, endIndex, curCommand, subCommand));  // replace the backquoted part with the OUTPUT of subCommand.
        }

        return concatArrayList(ret);
    }

    private String executeCmdSubstitution(int startIndex, int endIndex, String cmd, String subCommand) {

        try {
            // write output to file then read that file and return the conents.
            File file = File.createTempFile("temp", null);
            OutputStream out = new FileOutputStream(file);
            Jsh.eval(subCommand, out);
            out.close();

            String cmdOut = "";
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                cmdOut += scan.nextLine() + " ";
            }
            cmdOut = cmdOut.substring(0, cmdOut.length() - 1);
            scan.close();
            file.delete();
            return cmd.substring(0, startIndex) + cmdOut + cmd.substring(endIndex + 1, cmd.length()); 
        } catch (IOException e) {
            throw new RuntimeException("Command substitution: unable to write to file.");
        }
    }

    private String concatArrayList(ArrayList<String> array) {

        String ret = "";
        for (int i = 0; i != array.size(); ++i) {
            ret += array.get(i);
            if (i == array.size() - 1) {
                // reached last element.
                continue;
            }
            else {
                // add semi colon at the end of each command.
                ret += ";";
            }
        }
        return ret;
    }
}
