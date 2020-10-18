package uk.ac.ucl.jsh;

public class funcFactory {
    public funcInterface mkApplication(String application) {
        funcInterface app;
        switch (application) {
            case "cd":
                app = new Cd();
                break;
            case "pwd":
                app = new Pwd();
                break;
            case "find":
                app = new Find();
                break;
            case "ls":
                app = new Ls();
                break;
            case "cat":
                app = new Cat();
                break;
            case "echo":
                app = new Echo();
                break;
            case "head":
                app = new Head();
                break;
            case "tail":
                app = new Tail();
                break;
            case "grep":
                app = new Grep();
                break;
			
            default:
                throw new RuntimeException(application + ": unknown application");
            }
		return app;
}
