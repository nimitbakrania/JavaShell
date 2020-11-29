package uk.ac.ucl.jsh;

import java.io.IOException;

public class TEST1 {
    
    public static void main(String[] Args) throws IOException{
        String cmdline = "find -name '*.txt'";
        new Jsh();
        //Scanner input = new Scanner(System.in);
        //String cmdline = input.nextLine();
        try{
            Jsh.eval(cmdline , System.out);
        }
        catch(IOException e){
            System.out.println("\n" + e);
        }
    }
}
