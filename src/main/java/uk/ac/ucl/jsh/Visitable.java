package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public interface Visitable {

    public void accept(BaseVisitor visitor) throws IOException;

    public class Cd implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Cd(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class Pwd implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Pwd(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }

    }

    public class Echo implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Echo(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class Head implements Visitable{

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Head(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class Tail implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Tail(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }

    }

    public class Cat implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Cat(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class Ls implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Ls(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class Grep implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Grep(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class Cut implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Cut(InputStream input, OutputStream output, ArrayList<String> args, String thisDirectory) {

            this.input = input;
            this.output = output;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class Find implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Find(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class Uniq implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Uniq(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class Sort implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Sort(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class Mkdir implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Mkdir(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class Rmdir implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Rmdir(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class DateTime implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public DateTime(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class WordCount implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public WordCount(InputStream inputstream, OutputStream outputstream, ArrayList<String> args, String thisDirectory) {

            this.input = inputstream;
            this.output = outputstream;
            this.currentDirectory = thisDirectory;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class History implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public History(InputStream input, OutputStream output, ArrayList<String> appArgs, String currentDirectory) {

            this.input = input;
            this.output = output;
            this.currentDirectory = currentDirectory;
            this.appArgs = appArgs;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    public class Copy implements Visitable {

        public InputStream input;
        public OutputStream output;
        public String currentDirectory;
        public ArrayList<String> appArgs;

        public Copy(InputStream input, OutputStream output, ArrayList<String> appArgs, String currentDirectory) {

            this.input = input;
            this.output = output;
            this.currentDirectory = currentDirectory;
            this.appArgs = appArgs;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }
}
