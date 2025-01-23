package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Shop.Item;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONManager {
    public static ObjectMapper objectMapper = new ObjectMapper();


    public JSONManager() {
    }

    public static void saveShopData(List<Item> shopInventory) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("jsonoutput.json"), shopInventory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Item> loadShopData(String jsonFilePath) {
        try {
            return objectMapper.readValue(new File(jsonFilePath), new TypeReference<List<Item>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
