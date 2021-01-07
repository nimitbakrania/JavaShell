package uk.ac.ucl.jsh;

import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class Call implements Command {

    
    private VisitorApplication visitor = new VisitorApplication();
    private Factory factory = new Factory();

    /* Uses factory design pattern to create the object of the app that we are interested in.
      Then uses visitor design pattern to call accept on that type of app object. 
      @Params = in is inputstream
                out is where we are writign to
                app is the name of the application that is being evaluated.
                appArgs contains the arguments that were given to APP.
    */
    public void eval(InputStream in, OutputStream out, String app, ArrayList<String> appArgs) {

        UnsafeDecorator application = this.factory.mkApplication(in, out, app, appArgs);
        application.accept(visitor);
    }
    
}
