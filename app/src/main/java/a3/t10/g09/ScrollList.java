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

    // Get a scroll by filename (only active)
    public Scroll getScroll(String filename) {
        for (Scroll scroll : scrolls) {
            if (!scroll.isDeleted() && scroll.getFilename().equalsIgnoreCase(filename)) { // Use equalsIgnoreCase
                return scroll;
            }
        }
        return null;
    }

    // Get all scrolls by filename (only active)
    public ScrollList getScrolls(String filename) {
        ScrollList matches = new ScrollList();
        for (Scroll scroll : scrolls) {
            if (!scroll.isDeleted() && scroll.getFilename().equals(filename)) {
                matches.addScroll(scroll);
            }
        }
        return matches;
    }

    // Upload a new binary file to replace a scroll (only if target is active)
    public boolean replaceExistingScroll(String oldFileName, String newFileName, String ownerID) {
        for (Scroll scroll : scrolls) {
            if (!scroll.isDeleted() && scroll.getFilename().equals(oldFileName)) {
                scroll.setFilename(newFileName);
                scroll.resetDownloads();
                scroll.resetUploads();
                return true;
            }
        }
        return false;
    }

    // Get all active scrolls
    public List<Scroll> getAllScrolls() {
        List<Scroll> active = new ArrayList<>();
        for (Scroll s : scrolls) {
            if (!s.isDeleted()) {
                active.add(s);
            }
        }
        return active; // Return active copy to preserve encapsulation
    }

    // Get all scrolls including deleted (for persistence/internal logic)
    public List<Scroll> getAllScrollsIncludingDeleted() {
        return new ArrayList<>(scrolls);
    }

    // Get scrolls owned by a specific user
    public List<Scroll> getScrollsByOwner(String ownerId) {
        List<Scroll> ownedScrolls = new ArrayList<>();
        for (Scroll scroll : scrolls) {
            if (!scroll.isDeleted() && scroll.isOwnedBy(ownerId)) {
                ownedScrolls.add(scroll);
            }
        }
        return ownedScrolls;
    }

    // Soft-remove a scroll by filename (mark deleted)
    public boolean removeScroll(String filename) {
        for (Scroll scroll : scrolls) {
            if (!scroll.isDeleted() && scroll.getFilename().equals(filename)) {
                scroll.setDeleted(true);
                return true;
            }
        }
        return false;
    }

    // Get the number of active scrolls in the list
    public int getScrollCount() {
        int count = 0;
        for (Scroll s : scrolls) {
            if (!s.isDeleted()) count++;
        }
        return count;
    }

    // Get the total number of downloads across all active scrolls
    public int getTotalDownloads() {
        int sum = 0;
        for (Scroll s : scrolls) {
            if (!s.isDeleted()) sum += s.getNumberOfDownloads();
        }
        return sum;
    }

    // Get the total number of uploads across all active scrolls
    public int getTotalUploads() {
        int sum = 0;
        for (Scroll s : scrolls) {
            if (!s.isDeleted()) sum += s.getNumberOfUploads();
        }
        return sum;
    }

    // Get a list of active scrolls with at least a certain number of downloads
    public List<Scroll> getPopularScrolls(int minDownloads) {
        List<Scroll> popularScrolls = new ArrayList<>();
        for (Scroll scroll : scrolls) {
            if (!scroll.isDeleted() && scroll.getNumberOfDownloads() >= minDownloads) {
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