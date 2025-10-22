package a3.t10.g09;

public class Scroll {
    private String filename;
    private int numberOfUploads;
    private int numberOfDownloads;
    private String categorizationId;
    private String ownerId;  // ID of the user who owns this scroll

    public Scroll(String filename, String ownerId) {
        this(filename, 0, 0, "", ownerId);
    }

    public Scroll(String filename, int numberOfUploads, int numberOfDownloads, String ownerId) {
        this(filename, numberOfUploads, numberOfDownloads, "", ownerId);
    }
    
    public Scroll(String filename, String categorizationId, String ownerId) {
        this(filename, 0, 0, categorizationId, ownerId);
    }
    
    public Scroll(String filename, int numberOfUploads, int numberOfDownloads, String categorizationId, String ownerId) {
        this.filename = filename;
        this.numberOfUploads = numberOfUploads;
        this.numberOfDownloads = numberOfDownloads;
        this.categorizationId = categorizationId;
        this.ownerId = ownerId;
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

    public void resetUploads() {
        this.numberOfUploads = 0;
    }

    public void resetDownloads() {
        this.numberOfDownloads = 0;
    }
    
    // Owner-related methods
    public String getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    
    public boolean isOwnedBy(String userId) {
        return this.ownerId != null && this.ownerId.equals(userId);
    }
}