package uk.ac.ucl.jsh;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Factory {

    public Factory() { }

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
                app = new UnsafeDecorator(new Visitable.Sort(in, out, appArgs), flag);
                break;
            case "rmdir":
                app = new UnsafeDecorator(new Visitable.Sort(in, out, appArgs), flag);
                break;
            case "wc":
                app = new UnsafeDecorator(new Visitable.Sort(in, out, appArgs), flag);
                break;
            case "datetime":
                app = new UnsafeDecorator(new Visitable.Sort(in, out, appArgs), flag);
                break;
            default:
                throw new RuntimeException(application + ": unknown application");
            }
		return app;
    }
    
}
