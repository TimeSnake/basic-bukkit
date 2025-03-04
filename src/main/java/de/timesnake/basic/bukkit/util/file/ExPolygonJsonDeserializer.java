/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.file;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import de.timesnake.basic.bukkit.util.world.BlockPolygon;

import java.lang.reflect.Type;
import java.util.List;

public class ExPolygonJsonDeserializer implements JsonDeserializer<BlockPolygon> {

  private static final TypeToken<List<Integer>> POINTS_TYPE = new TypeToken<>() {
  };

  public ExPolygonJsonDeserializer() {

  }

  @Override
  public BlockPolygon deserialize(JsonElement jsonElement, Type type,
                                  JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    return new BlockPolygon(jsonDeserializationContext.<List<Integer>>deserialize(jsonObject.get("xPoints"),
        POINTS_TYPE.getType()),
        jsonDeserializationContext.deserialize(jsonObject.get("zPoints"), POINTS_TYPE.getType()),
        jsonObject.get("minHeight").getAsInt(),
        jsonObject.get("maxHeight").getAsInt());
  }
}
