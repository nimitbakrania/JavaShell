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
                System.out.println(e.getMessage()); // cat dir1; echo BBB
            }
            else {
                throw new RuntimeException(e.getMessage());
                // somehow stop running here by printing nothing and waiting for next command.
            }
        }
    }
}
