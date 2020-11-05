package uk.ac.ucl.jsh.files;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public interface Visitable {
    
    public void accept(baseVisitor visitor);

    public class Cd implements Visitable {

        InputStream input;
        OutputStream output;
        String directory;

        public Cd(InputStream input, OutputStream output, ArrayList<String> appArgs) {

            this.input = input;
            this.output = output;
            this.directory = System.getProperty("user.dir");
        }

        public void accept(baseVisitor visitor){

            visitor.visit(this);
        }
    }

    public class Pwd implements Visitable {

        @Override
        public void accept(baseVisitor visitor) {
            // TODO Auto-generated method stub

        }

    }

    public class Echo implements Visitable {

        InputStream input;
        OutputStream output;
        String directory;
        ArrayList<String> appArgs;

        public Echo(InputStream inputstream, OutputStream outputstream, ArrayList<String> appArgs) {

            this.input = inputstream;
            this.output = outputstream;
            this.directory = System.getProperty("user.dir");
            this.appArgs = appArgs;
        }

        public void accept(baseVisitor visitor) {

            visitor.visit(this);
        }
    }

    public class Head implements Visitable{

        InputStream input;
        OutputStream output;
        String directory;
        ArrayList<String> appArgs;

        public Head(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.directory = System.getProperty("user.dir");
            this.appArgs = args;
        }

        public void accept(baseVisitor visitor){

            visitor.visit(this);
        }
    }

    public class Tail implements Visitable {

        InputStream input;
        OutputStream output;
        String directory;
        ArrayList<String> appArgs;

        public Tail(InputStream input, OutputStream output, ArrayList<String> appArgs) {

            this.input = input;
            this.output = output;
            this.directory = System.getProperty("user.dir");
            this.appArgs = appArgs;
        }

        public void accept(baseVisitor visitor) {

            visitor.visit(this);
        }

    }

    public class Cat implements Visitable {

        InputStream input;
        OutputStream output;
        String directory;

        public Cat(InputStream input,  OutputStream output) {
            this.input = input;
            this.output = output;
            this.directory = System.getProperty("user.dir");
        }

        public void accept(baseVisitor visitor){
            visitor.visit(this);
        }
    }

    public class Ls implements Visitable {

        InputStream input;
        OutputStream output;
        String directory;
        ArrayList<String> appArgs;

        public Ls(InputStream input, OutputStream output, ArrayList<String> appArgs) {

            this.input = input;
            this.output = output;
            this.directory = System.getProperty("user.dir");
            this.appArgs = appArgs;
        }

        public void accept(baseVisitor visitor) {

            visitor.visit(this);
        }
    }

    public class Grep implements Visitable {

        InputStream input;
        OutputStream output;
        String directory;
        ArrayList<String> appArgs;

        public Grep(InputStream inputstream, OutputStream outputstream, ArrayList<String> args) {

            this.input = inputstream;
            this.output = outputstream;
            this.directory = System.getProperty("user.dir");
            this.appArgs = args;
        }

        public void accept(baseVisitor visitor) {

            visitor.visit(this);
        }
    }

}