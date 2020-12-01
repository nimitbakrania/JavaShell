package uk.ac.ucl.jsh;

// catch all exceptions here and print instead of throwing, allowing the program to continue instead of terminating
public class UnsafeDecorator {
    private Visitable app;
    private boolean unsafeFlag;
    public UnsafeDecorator(Visitable application, boolean flag)
    {
        this.app = application;
        this.unsafeFlag = flag;
    }

    public void accept(baseVisitor visitor) { 
        try{
            this.app.accept(visitor);
        }
        catch(Exception e)
        {
            if (unsafeFlag){
                System.err.println(e.getMessage());
            }
            else{
                throw new RuntimeException(e.toString());
            }
        }
    }
}
