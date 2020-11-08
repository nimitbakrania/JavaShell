import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

// catch all exceptions here and print instead of throwing, allowing the program to continue instead of terminating
public class UnsafeDecorator implements App {
    private Visitable app;

    public UnsafeDecorator(Visitable application)
    {
        this.app = application;
    }
    @Override
    public void accept(baseVisitor visitor) throws IOException {
        try{
            this.app.accept(visitor);
        }
        catch(Exception e)
        {
            OutputStreamWriter writer = new OutputStreamWriter(output);
            writer.write(e.getMessage());
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }

    }
}
