package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Shop.Item;

import java.io.File;
import java.util.List;

public class JSONManager {
    public static ObjectMapper objectMapper = new ObjectMapper();


    public JSONManager() {
    }

    public static void saveShopData(List<Item> shopInventory) {
        for (Item item : shopInventory) {
            try {
                objectMapper.writeValueAsString(item);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
