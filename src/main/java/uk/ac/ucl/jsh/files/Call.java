package uk.ac.ucl.jsh.files;

import java.io.OutputStream;
import java.util.ArrayList;

public class Call implements Command {

    private visitorApplication visitor = new visitorApplication();

    public void eval(String app, ArrayList<String> appArgs) {

        Factory factory = new Factory();
        Visitable application = factory.mkApplication(app);
        application.accept(visitor);
    }
    
}
