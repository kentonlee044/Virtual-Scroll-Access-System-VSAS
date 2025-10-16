package a3.t10.g09;

public class Scroll {
    private String filename;
    private int numberOfUploads;
    private int numberOfDownloads;
    private String categorizationId;

    public Scroll(String filename) {
        this(filename, 0, 0, "");
    }

    public Scroll(String filename, int numberOfUploads, int numberOfDownloads) {
        this(filename, numberOfUploads, numberOfDownloads, "");
    }
    
    public Scroll(String filename, String categorizationId) {
        this(filename, 0, 0, categorizationId);
    }
    
    public Scroll(String filename, int numberOfUploads, int numberOfDownloads, String categorizationId) {
        this.filename = filename;
        this.numberOfUploads = numberOfUploads;
        this.numberOfDownloads = numberOfDownloads;
        this.categorizationId = categorizationId;
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
    
    public String getCategorizationId() {
        return categorizationId;
    }
    
    public void setCategorizationId(String categorizationId) {
        this.categorizationId = categorizationId;
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