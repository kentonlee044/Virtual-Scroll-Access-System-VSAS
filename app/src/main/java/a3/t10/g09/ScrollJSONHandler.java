package a3.t10.g09;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
            gson.toJson(scrollList.getAllScrolls(), writer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
