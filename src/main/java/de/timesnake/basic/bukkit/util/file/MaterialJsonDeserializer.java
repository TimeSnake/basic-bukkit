/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.file;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.bukkit.Material;

import java.lang.reflect.Type;

public class MaterialJsonDeserializer implements JsonDeserializer<Material> {

  @Override
  public Material deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
    try {
      return Material.valueOf(json.getAsString());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
