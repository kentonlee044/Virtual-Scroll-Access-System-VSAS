package a3.t10.g09;

public enum MenuTitle {
    MAIN("🔑  Virtual Scroll Access System"),
    SETTINGS("Settings"),
    HELP("Help"),
    EXIT("Exit");

    private final String title;

    MenuTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
