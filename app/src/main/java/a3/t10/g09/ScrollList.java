package a3.t10.g09;

import java.util.ArrayList;
import java.util.List;

public class ScrollList {
    private List<Scroll> scrolls;

    public ScrollList() {
        this.scrolls = new ArrayList<>();
    }

    // Add a new scroll to the list
    public void addScroll(Scroll scroll) {
        scrolls.add(scroll);
    }

    // Get a scroll by filename
    public Scroll getScroll(String filename) {
        for (Scroll scroll : scrolls) {
            if (scroll.getFilename().equals(filename)) {
                return scroll;
            }
        }
        return null;
    }

    // Upload a new binary file to replace a scroll
    public boolean replaceExistingScroll(String oldFileName,String newFileName, String ownerID) {
        for (Scroll scroll : scrolls) {
            if (scroll.getFilename().equals(oldFileName)) {
                scroll.setFilename(newFileName);
                scroll.resetDownloads();
                scroll.resetUploads();
                return true;
            }
        }
        return false;
    }

    // Get all scrolls
    public List<Scroll> getAllScrolls() {
        return new ArrayList<>(scrolls); // Return a copy to preserve encapsulation
    }
    
    // Get scrolls owned by a specific user
    public List<Scroll> getScrollsByOwner(String ownerId) {
        List<Scroll> ownedScrolls = new ArrayList<>();
        for (Scroll scroll : scrolls) {
            if (scroll.isOwnedBy(ownerId)) {
                ownedScrolls.add(scroll);
            }
        }
        return ownedScrolls;
    }

    // Remove a scroll by filename
    public boolean removeScroll(String filename) {
        return scrolls.removeIf(scroll -> scroll.getFilename().equals(filename));
    }

    // Get the number of scrolls in the list
    public int getScrollCount() {
        return scrolls.size();
    }

    // Get the total number of downloads across all scrolls
    public int getTotalDownloads() {
        return scrolls.stream()
                     .mapToInt(Scroll::getNumberOfDownloads)
                     .sum();
    }

    // Get the total number of uploads across all scrolls
    public int getTotalUploads() {
        return scrolls.stream()
                     .mapToInt(Scroll::getNumberOfUploads)
                     .sum();
    }

    // Get a list of scrolls with at least a certain number of downloads
    public List<Scroll> getPopularScrolls(int minDownloads) {
        List<Scroll> popularScrolls = new ArrayList<>();
        for (Scroll scroll : scrolls) {
            if (scroll.getNumberOfDownloads() >= minDownloads) {
                popularScrolls.add(scroll);
            }
        }
        return popularScrolls;
    }

    // Clear all scrolls from the list
    public void clear() {
        scrolls.clear();
    }
}