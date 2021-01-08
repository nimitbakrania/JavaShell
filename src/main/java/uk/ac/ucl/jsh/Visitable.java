package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public interface Visitable {
    /**
     * An interface for the generalised accept function which each app uses.
     * 
     * @param visitor is the app which is being called.
    */
    void accept(BaseVisitor visitor) throws IOException;

    class Cd implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Cd(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    class Pwd implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Pwd(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }

    }

    class Echo implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Echo(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    class Head implements Visitable{

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Head(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    class Tail implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Tail(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }

    }

    class Cat implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Cat(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    class Ls implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Ls(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    class Grep implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Grep(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    class Cut implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Cut(InputStream input, OutputStream output, ArrayList<String> args) {

            this.input = input;
            this.output = output;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    class Find implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Find(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    class Uniq implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Uniq(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    class Sort implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Sort(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    class Mkdir implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Mkdir(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    class Rmdir implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Rmdir(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.appArgs = args;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }

    class Copy implements Visitable {

        public InputStream input;
        public OutputStream output;
        public ArrayList<String> appArgs;

        public Copy(InputStream input, OutputStream output, ArrayList<String> appArgs) {

            this.input = input;
            this.output = output;
            this.appArgs = appArgs;
        }

        public void accept(BaseVisitor visitor) throws IOException {

            visitor.visit(this);
        }
    }
}
