package a3.t10.g09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Scroll {
    private String filename;
    private int numberOfUploads;
    private int numberOfDownloads;
    private String categorizationId;
    private String ownerId; // ID of the user who owns this scroll
    private String uploadDate; // ISO 8601 string of when the scroll was uploaded

    public Scroll(String filename, String ownerId) {
        this(filename, 0, 0, "", ownerId);
    }

    public Scroll(String filename, int numberOfUploads, int numberOfDownloads, String ownerId) {
        this(filename, numberOfUploads, numberOfDownloads, "", ownerId);
    }

    public Scroll(String filename, String categorizationId, String ownerId) {
        this(filename, 0, 0, categorizationId, ownerId);
    }

    public Scroll(String filename, int numberOfUploads, int numberOfDownloads, String categorizationId,
            String ownerId) {
        this.filename = filename;
        this.numberOfUploads = numberOfUploads;
        this.numberOfDownloads = numberOfDownloads;
        this.categorizationId = categorizationId;
        this.ownerId = ownerId;
        this.uploadDate = ""; // default empty if not set
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

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
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

    public void download() {
        Path sourcePath = Path.of(this.filename).toAbsolutePath().normalize();
        if (!Files.exists(sourcePath)) {
            System.err.println("Source file not found: " + sourcePath);
            return;
        }
        Path downloadsDir = Path.of("downloads");
        System.out.println("Looking for file at: " + sourcePath);

        try {
            // Ensure the downloads directory exists
            if (!Files.exists(downloadsDir)) {
                Files.createDirectories(downloadsDir);
            }

            // Generate a unique filename in the downloads directory
            Path targetPath = downloadsDir.resolve(this.filename);
            int counter = 1;
            while (Files.exists(targetPath)) {
                String newFilename = this.filename.replaceFirst("(\\.[^.]+$)", "(" + counter + ")$1");
                targetPath = downloadsDir.resolve(newFilename);
                counter++;
            }

            // Copy the file to the downloads directory
            Files.copy(sourcePath, targetPath);
            System.out.println("File downloaded to: " + targetPath);

        } catch (IOException e) {
            System.err.println("Failed to download file: " + e.getMessage());
        }
    }

}