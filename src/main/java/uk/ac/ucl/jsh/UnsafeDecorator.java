package uk.ac.ucl.jsh;

// catch all exceptions here and print instead of throwing, allowing the program to continue instead of terminating
public class UnsafeDecorator implements Visitable {
    private Visitable app;
    private boolean unsafeFlag;

    /**
     * @param application the application we are creating the unsafedecorator object for.
     * @param flag true if it is unsafe mode (has "_" prepended to it).
     */
    public UnsafeDecorator(Visitable application, boolean flag)
    {
        this.app = application;
        this.unsafeFlag = flag;
    }

    /**
     * If unsafeFlag is true then we print onto std.out else Jsh main() will 
     * print it onto std.err.
     * 
     * @param visitor visits the app private variable.
     * 
     * @throws RuntimeException if we are in safe mode.
     */
    public void accept(BaseVisitor visitor) { 
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
            }
        }
    }
}
