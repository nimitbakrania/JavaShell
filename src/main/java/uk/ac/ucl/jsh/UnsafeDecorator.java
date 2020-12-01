package uk.ac.ucl.jsh;

// catch all exceptions here and print instead of throwing, allowing the program to continue instead of terminating
public class UnsafeDecorator {
    private Visitable app;

    public UnsafeDecorator(Visitable application)
    {
        this.app = application;
    }

    public void accept(baseVisitor visitor) { 
        try{
            this.app.accept(visitor);
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
}
