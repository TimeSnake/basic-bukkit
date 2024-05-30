/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.Material;

import java.lang.reflect.Type;

public class MaterialJsonSerializer implements JsonSerializer<Material> {

  @Override
  public JsonElement serialize(Material material, Type type, JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive(material.name());
  }
}
