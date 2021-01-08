package uk.ac.ucl.jsh;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Factory {

    public Factory() { }

    /**
     * Handles creation of UnsafeDecorator object. First it checks if "_" is prepended then it uses a switch statement to create the 
     * relevant Visitable object wrapped with an unsafedecorator object.
     * 
     * @param in inputstream that is null if we arent using it. If we are using pipe or redirection then it is not null
     * @param out outputstream that we want to print the results of running the application onto.
     * @param currentDirectory the directory that Jsh is currently operating in.
     * @param application a string that has the application command. E.g. "echo", "cut", "grep", "head" ...
     * @param appArgs contains all of the arguments supplied on the cmdline for the application.
     * 
     * @returns an UnsafeDecorator object wrapping a Visitable object for the parameter "applciation".
     */
    public UnsafeDecorator mkApplication(InputStream in, OutputStream out, String application, ArrayList<String> appArgs) {
        UnsafeDecorator app;
        boolean flag = false;
        if (application.charAt(0) == '_'){
            flag = true;
            application = application.substring(1);
        }
        switch (application) {
            case "cd":
                app = new UnsafeDecorator(new Visitable.Cd(in, out, appArgs), flag);
                break;
            case "pwd":
                app = new UnsafeDecorator(new Visitable.Pwd(in, out, appArgs), flag);
                break;
            case "ls":
                app = new UnsafeDecorator(new Visitable.Ls(in, out, appArgs), flag);
                break;
            case "cat":
                app = new UnsafeDecorator(new Visitable.Cat(in, out, appArgs), flag);
                break;
            case "echo":
                app = new UnsafeDecorator(new Visitable.Echo(in, out, appArgs), flag);
                break;
            case "head":
                app = new UnsafeDecorator(new Visitable.Head(in, out, appArgs), flag);
                break;
            case "tail":
                app = new UnsafeDecorator(new Visitable.Tail(in, out, appArgs), flag);
                break;
            case "grep":
                app = new UnsafeDecorator(new Visitable.Grep(in, out, appArgs), flag);
                break;
            case "cut":
                app = new UnsafeDecorator(new Visitable.Cut(in, out, appArgs), flag);
                break;
            case "find":
                app = new UnsafeDecorator(new Visitable.Find(in, out, appArgs), flag);
                break;
            case "uniq":
                app = new UnsafeDecorator(new Visitable.Uniq(in, out, appArgs), flag);
                break;
            case "sort":
                app = new UnsafeDecorator(new Visitable.Sort(in, out, appArgs), flag);
                break;
            case "mkdir":
                app = new UnsafeDecorator(new Visitable.Mkdir(in, out, appArgs), flag);
                break;
            case "rmdir":
                app = new UnsafeDecorator(new Visitable.Rmdir(in, out, appArgs), flag);
                break;
            case "cp":
                app = new UnsafeDecorator(new Visitable.Copy(in, out, appArgs), flag);
                break;
            default:
                throw new RuntimeException(application + ": unknown application");
            }
		return app;
    }
    
}
