package uk.ac.ucl.jsh;

import java.io.IOException;
// An interface for the VisitorApplication class
public interface BaseVisitor {
    void visit(Visitable.Cd app) throws IOException;
    void visit(Visitable.Pwd app) throws IOException;
    void visit(Visitable.Echo app) throws IOException;
    void visit(Visitable.Head app) throws IOException;
    void visit(Visitable.Tail app) throws IOException;
    void visit(Visitable.Cat app) throws IOException;
    void visit(Visitable.Ls app) throws IOException;
    void visit(Visitable.Grep app) throws IOException;
    void visit(Visitable.Cut app) throws IOException;
    void visit(Visitable.Find app) throws IOException;
    void visit(Visitable.Uniq app) throws IOException;
    void visit(Visitable.Sort app) throws IOException;
    void visit(Visitable.Mkdir app) throws IOException;
    void visit(Visitable.Rmdir app) throws IOException;
    void visit(Visitable.Copy app) throws IOException;
}
