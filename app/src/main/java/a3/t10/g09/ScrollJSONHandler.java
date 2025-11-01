package a3.t10.g09;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class ScrollJSONHandler {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String DEFAULT_SCROLLS_PATH = "src/main/java/a3/t10/g09/data/scrolls.json";

    public static ScrollList loadFromJson() {
        return loadFromJson(DEFAULT_SCROLLS_PATH);
    }

    public static ScrollList loadFromJson(String filePath) {
        try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
            List<Scroll> scrolls = gson.fromJson(reader, new com.google.gson.reflect.TypeToken<List<Scroll>>(){}.getType());
            ScrollList scrollList = new ScrollList();
            if (scrolls != null) {
                for (Scroll scroll : scrolls) {
                    // Set default owner to 'system' for existing scrolls without an owner
                    if (scroll.getOwnerId() == null) {
                        scroll.setOwnerId("system");
                    }
                    // Ensure uploadDate is non-null for legacy data
                    if (scroll.getUploadDate() == null) {
                        scroll.setUploadDate("");
                    }
                    scrollList.addScroll(scroll);
                }
            }
            return scrollList;
        } catch (IOException e) {
            e.printStackTrace();
            return new ScrollList();
        }
    }

    public static boolean saveToJson(ScrollList scrollList) {
        try (FileWriter writer = new FileWriter(DEFAULT_SCROLLS_PATH)) {
            // Persist all records including soft-deleted ones
            gson.toJson(scrollList.getAllScrollsIncludingDeleted(), writer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String previewScroll(String scrollName){
        // Respect soft-deletion by using the loader and active list
        ScrollList list = loadFromJson();
        for (Scroll scroll : list.getAllScrolls()) {
            if (scroll.getFilename() != null && scroll.getFilename().equals(scrollName)) {
                Path path = Path.of(scrollName).toAbsolutePath().normalize();
                try{
                    String content = Files.readString(path);
                    return content;
                } catch(IOException e){
                    System.out.println("Could not read the scroll file at: " + path);
                    return null;
                }
            }
        }
        System.out.println("Scroll with the name " + scrollName + " not found.");
        return null;
    }
}
