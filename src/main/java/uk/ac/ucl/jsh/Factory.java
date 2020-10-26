package uk.ac.ucl.jsh;

import uk.ac.ucl.jsh.individualClasses.*;

public class Factory {

    public Factory() { }

    public App mkApplication(String application) {
        App app;
        switch (application) {
            case "cd":
                app = new cd();
                break;
            case "pwd":
                app = new pwd();
                break;
            case "find":
                app = new find();
                break;
            case "ls":
                app = new ls();
                break;
            case "cat":
                app = new cat();
                break;
            case "echo":
                app = new echo();
                break;
            case "head":
                app = new head();
                break;
            case "tail":
                app = new tail();
                break;
            case "grep":
                app = new grep();
                break;
			
            default:
                throw new RuntimeException(application + ": unknown application");
            }
		return app;
}
