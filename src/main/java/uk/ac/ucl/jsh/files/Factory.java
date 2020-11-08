package uk.ac.ucl.jsh.files;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Factory {

    public Factory() { }

    public Visitable mkApplication(InputStream in, OutputStream out, String currentDirectory, String application, ArrayList<String> appArgs) {
        Visitable app;
        switch (application) {
            case "cd":
                app = new Visitable.Cd(in, out, currentDirectory, appArgs);
                break;
            case "pwd":
                app = new Visitable.Pwd(in, out, currentDirectory, appArgs);
                break;
            case "find":
                app = new Visitable.Find(in, out, currentDirectory, appArgs);
                break;
            case "ls":
                app = new Visitable.Ls(in, out, currentDirectory, appArgs);
                break;
            case "cat":
                app = new Visitable.Cat(in, out, currentDirectory, appArgs);
                break;
            case "echo":
                app = new Visitable.Echo(in, out, currentDirectory, appArgs);
                break;
            case "head":
                app = new Visitable.Head(in, out, currentDirectory, appArgs);
                break;
            case "tail":
                app = new Visitable.Tail(in, out, currentDirectory, appArgs);
                break;
            case "grep":
                app = new Visitable.Grep(in, out, currentDirectory, appArgs);
                break;
            default:
                throw new RuntimeException(application + ": unknown application");
            }
		return app;
}
