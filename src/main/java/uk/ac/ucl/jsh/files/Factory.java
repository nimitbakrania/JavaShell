package uk.ac.ucl.jsh.files;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Factory {

    public Factory() { }

    public Visitable mkApplication(InputStream in, OutputStream out, String application, ArrayList<String> appArgs) {
        Visitable app;
        switch (application) {
            case "cd":
                app = new Visitable.Cd(in, out, appArgs);
                break;
            case "pwd":
                app = new Visitable.Pwd(in, out, appArgs);
                break;
            case "find":
                app = new Visitable.Find(in, out, appArgs);
                break;
            case "ls":
                app = new Visitable.Ls(in, out, appArgs);
                break;
            case "cat":
                app = new Visitable.Cat(in, out, appArgs);
                break;
            case "echo":
                app = new Visitable.Echo(in, out, appArgs);
                break;
            case "head":
                app = new Visitable.Head(in, out, appArgs);
                break;
            case "tail":
                app = new Visitable.Tail(in, out, appArgs);
                break;
            case "grep":
                app = new Visitable.Grep(in, out, appArgs);
                break;
            default:
                throw new RuntimeException(application + ": unknown application");
            }
		return app;
}
