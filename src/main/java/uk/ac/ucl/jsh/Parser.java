package uk.ac.ucl.jsh;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import uk.ac.ucl.jsh.JshGrammarLexer;
import uk.ac.ucl.jsh.JshGrammarParser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/* This class is responsible for parsing the input written in the jsh.
*/
public class Parser {
    
    private String currCmdline;

    public Parser() { }

    private void setCmdline(String cmdline) {

        this.currCmdline = cmdline;
    }

    /* This calls get_commands to seperate all the commands in the cmdline. It then goes through each command
       and splits each one into tokens. It puts the tokens in each command into a seperate arraylist and adds them into
       ret.
       @params = cmdline : arguments supplied on commandline
                 current_directory : directory we are currently in.
       @returns = Arraylist containing arraylists of tokenized commands. Each arraylist contains tokens for one command.
                  The value at index 0 in each arraylist is the application name.
       @throws = IOException if ...
 
    */ 
    public ArrayList<ArrayList<String>> parse(String cmdline, String currentDirectory) throws IOException {

        setCmdline(cmdline);
        ArrayList<String> rawCommands = getCommands();
        ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
        
        for (String command : rawCommands) {
            ret.add(splitIn2Tokens(command, currentDirectory));
        }

        return ret;
    }

    /* Run this when parsing a command from cmd. It will return app_args which can be passed
       to ApplicationRunner to run application.
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

    /* Goes through the command given and splits it into tokens. The token at index 0 in ret array
       is app name while rest are app args.
       @params = raw_command : command to tokenize.
                 current_directory : directory that jsh is currently running in.
       @returns = arraylist containing tokens.
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
}
