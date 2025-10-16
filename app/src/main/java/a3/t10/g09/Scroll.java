package a3.t10.g09;

public class Scroll {
    private String filename;
    private int numberOfUploads;
    private int numberOfDownloads;

    public Scroll(String filename) {
        this.filename = filename;
        this.numberOfUploads = 0;
        this.numberOfDownloads = 0;
    }

    public Scroll(String filename, int numberOfUploads, int numberOfDownloads) {
        this.filename = filename;
        this.numberOfUploads = numberOfUploads;
        this.numberOfDownloads = numberOfDownloads;
    }

    // Getters
    public String getFilename() {
        return filename;
    }

    public int getNumberOfUploads() {
        return numberOfUploads;
    }

    public int getNumberOfDownloads() {
        return numberOfDownloads;
    }

    // Setters
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void incrementUploads() {
        this.numberOfUploads++;
    }

    public void incrementDownloads() {
        this.numberOfDownloads++;
    }
}