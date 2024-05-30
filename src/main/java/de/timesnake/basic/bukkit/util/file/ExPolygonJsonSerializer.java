/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.file;

import com.google.gson.*;
import de.timesnake.basic.bukkit.util.world.ExPolygon;

import java.lang.reflect.Type;

public class ExPolygonJsonSerializer implements JsonSerializer<ExPolygon> {

  @Override
  public JsonElement serialize(ExPolygon src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject json = new JsonObject();

    JsonArray xPoints = new JsonArray();
    for (int x : src.getPolygon().xpoints) {
      xPoints.add(x);
    }
    json.add("xPoints", xPoints);

    JsonArray zPoints = new JsonArray();
    for (int z : src.getPolygon().ypoints) {
      zPoints.add(z);
    }
    json.add("zPoints", zPoints);

    json.addProperty("minHeight", src.getMinHeight());
    json.addProperty("maxHeight", src.getMaxHeight());

    return json;
  }
}
