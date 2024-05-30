/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.file;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import de.timesnake.basic.bukkit.util.world.ExPolygon;

import java.lang.reflect.Type;
import java.util.List;

public class ExPolygonJsonDeserializer implements JsonDeserializer<ExPolygon> {

  private static final TypeToken<List<Integer>> POINTS_TYPE = new TypeToken<>() {
  };

  public ExPolygonJsonDeserializer() {

  }

  @Override
  public ExPolygon deserialize(JsonElement jsonElement, Type type,
                               JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    return new ExPolygon(jsonDeserializationContext.<List<Integer>>deserialize(jsonObject.get("xPoints"),
        POINTS_TYPE.getType()),
        jsonDeserializationContext.deserialize(jsonObject.get("zPoints"), POINTS_TYPE.getType()),
        jsonObject.get("minHeight").getAsInt(),
        jsonObject.get("maxHeight").getAsInt());
  }
}
