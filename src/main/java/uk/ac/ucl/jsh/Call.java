package uk.ac.ucl.jsh;

import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class Call implements Command {

    private visitorApplication visitor = new visitorApplication();

    public void eval(InputStream in, OutputStream out, String currentDirectory, String app, ArrayList<String> appArgs) {

        Factory factory = new Factory();
        UnsafeDecorator application = factory.mkApplication(in, out, currentDirectory, app, appArgs);
        application.accept(visitor);
    }
    
}
