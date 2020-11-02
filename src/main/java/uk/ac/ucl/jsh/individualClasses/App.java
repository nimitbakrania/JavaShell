import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import uk.ac.ucl.jsh.core.JshCore;

public interface App {

    public void run(ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException;
}
