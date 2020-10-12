package uk.ac.ucl.jsh;

import java.util.ArrayList;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/* This class is responsible for parsing the input written in the jsh.
*/
public class Parser {
    
    private String curr_cmdline;

    public Parser() {
        curr_cmdline = "";
    }

    /* Run this when parsing a command from cmd. It will return app_args which can be passed
       to ApplicationRunner to run application.
    */
    public ArrayList<String> parse(String cmdline) {

        curr_cmdline = cmdline;
        CharStream parserInput = CharStreams.fromString(cmdline); 
        JshGrammarLexer lexer = new JshGrammarLexer(parserInput);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);        
        JshGrammarParser parser = new JshGrammarParser(tokenStream);
        ParseTree tree = parser.command();

    }
}
