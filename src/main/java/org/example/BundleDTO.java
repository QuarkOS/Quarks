package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Shop.Bundle;

import java.util.List;

public record BundleDTO(@JsonProperty("bundles") List<Bundle> bundles) {
    public BundleDTO(List<Bundle> bundles) {
        this.bundles = List.copyOf(bundles);
    }
}
