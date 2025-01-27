package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JSONManager {
    public static final File BUNDLE_SAVE = new File("bundles.json");
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static void saveBundleJson(BundleDTO bundleDTO) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(BUNDLE_SAVE, bundleDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BundleDTO loadBundleJson() {
        try {
            if (!BUNDLE_SAVE.exists()) {
                return new BundleDTO(new ArrayList<>());
            }
            return objectMapper.readValue(BUNDLE_SAVE, BundleDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
