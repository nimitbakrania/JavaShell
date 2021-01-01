package uk.ac.ucl.jsh;

import java.io.IOException;

public class TEST1 {
    
    public static void main(String[] Args) throws IOException{
        String cmdline = "echo ''";
        new Jsh();
        //Scanner input = new Scanner(System.in);
        //String cmdline = input.nextLine();
        try{
            Jsh.setCurrentDirectory("C:\\Users\\jpopo\\OneDrive\\Desktop\\Software Engineering\\jsh-team-18\\src\\test\\java\\uk\\ac\\ucl\\jsh");
            Jsh.eval(cmdline , System.out);
        }
        catch(IOException e){
            System.out.println("\n" + e);
        }
    }
}
