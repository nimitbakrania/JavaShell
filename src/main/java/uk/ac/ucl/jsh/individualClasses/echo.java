package uk.ac.ucl.jsh.individualClasses;
import uk.ac.ucl.jsh.core.JshCore;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class echo implements App {

  @Override
  public void run(JshCore core, ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        boolean atLeastOnePrinted = !appArgs.isEmpty();
        // arguments printed with space between them, ensuring no space printed after last element
        int count = 0;
        for (String arg : appArgs) {
            writer.write(arg);
            if (count < appArgs.size() - 1) {
                writer.write(" ");
            }
            writer.flush();
            count++;
        }

        // newline only printed if there are arguments called on echo
        if (atLeastOnePrinted) {
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }
}
