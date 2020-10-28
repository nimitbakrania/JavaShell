import java.io.IOException;
import java.util.ArrayList;
import uk.ac.ucl.jsh.core.JshCore;

public interface App {

    public void run(JshCore core, ArrayList<String> appArgs) throws IOException ;
}
