import com.example.ai;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonParser {
    public static List<FoodItem> parseJsonFile(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Type foodListType = new TypeToken<List<FoodItem>>() {}.getType();
            return gson.fromJson(reader, foodListType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
