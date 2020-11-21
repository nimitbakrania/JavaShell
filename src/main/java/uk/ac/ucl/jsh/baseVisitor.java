package uk.ac.ucl.jsh;

import java.io.IOException;

public interface baseVisitor {
    public void visit(Visitable.Cd app) throws IOException;
    public void visit(Visitable.Pwd app) throws IOException;
    public void visit(Visitable.Echo app) throws IOException;
    public void visit(Visitable.Head app) throws IOException;
    public void visit(Visitable.Tail app) throws IOException;
    public void visit(Visitable.Cat app) throws IOException;
    public void visit(Visitable.Ls app) throws IOException;
    public void visit(Visitable.Grep app) throws IOException;
<<<<<<< HEAD
    public void visit(Visitable.Cut app) throws IOException;
=======
    public void visit(Visitable.Find app) throws IOException;
>>>>>>> 32a98dac6366c8024bfb2a77f3eae087cf1ee95b
}
