package uk.ac.ucl.jsh;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Factory {

    public Factory() { }

    public UnsafeDecorator mkApplication(InputStream in, OutputStream out, String currentDirectory, String application, ArrayList<String> appArgs) {
        UnsafeDecorator app;
        switch (application) {
            case "cd":
                app = new UnsafeDecorator(new Visitable.Cd(in, out, appArgs, currentDirectory));
                break;
            case "pwd":
                app = new UnsafeDecorator(new Visitable.Pwd(in, out, appArgs, currentDirectory));
                break;
            case "ls":
                app = new UnsafeDecorator(new Visitable.Ls(in, out, appArgs, currentDirectory));
                break;
            case "cat":
                app = new UnsafeDecorator(new Visitable.Cat(in, out, appArgs, currentDirectory));
                break;
            case "echo":
                app = new UnsafeDecorator(new Visitable.Echo(in, out, appArgs, currentDirectory));
                break;
            case "head":
                app = new UnsafeDecorator(new Visitable.Head(in, out, appArgs, currentDirectory));
                break;
            case "tail":
                app = new UnsafeDecorator(new Visitable.Tail(in, out, appArgs, currentDirectory));
                break;
            case "grep":
                app = new UnsafeDecorator(new Visitable.Grep(in, out, appArgs, currentDirectory));
                break;
            case "cut":
                app = new UnsafeDecorator(new Visitable.Cut(in, out, appArgs, currentDirectory));
                break;
            case "find":
                app = new UnsafeDecorator(new Visitable.Find(in, out, appArgs, currentDirectory));
                break;
            case "uniq":
                app = new UnsafeDecorator(new Visitable.Uniq(in, out, appArgs, currentDirectory));
                break;
            case "sort":
                app = new UnsafeDecorator(new Visitable.Sort(in, out, appArgs, currentDirectory));
                break;
            default:
                throw new RuntimeException(application + ": unknown application");
            }
		return app;
    }
}
