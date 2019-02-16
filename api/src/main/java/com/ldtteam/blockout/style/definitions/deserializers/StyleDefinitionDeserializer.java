package com.ldtteam.blockout.style.definitions.deserializers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.ldtteam.blockout.style.definitions.StyleDefinition;
import com.ldtteam.blockout.util.json.IdentifierDeserializer;
import com.ldtteam.blockout.util.stream.StreamHelper;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Collectors;

public class StyleDefinitionDeserializer implements JsonDeserializer<StyleDefinition>
{
    public static final  Type                        CONST_STYLE_DEFINTION_TYPE = new TypeToken<StyleDefinition>() {}.getType();
    private static final Gson                        CONST_GSON                 = new GsonBuilder()
                                                                                    .registerTypeAdapter(IdentifierDeserializer.CONST_IDENTIFIER_TYPE,
                                                                                      IdentifierDeserializer.getInstance())
                                                                                    .create();
    private static       StyleDefinitionDeserializer ourInstance                = new StyleDefinitionDeserializer();

    private StyleDefinitionDeserializer()
    {
    }

    public static StyleDefinitionDeserializer getInstance()
    {
        return ourInstance;
    }

    @Override
    public StyleDefinition deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonObject())
        {
            throw new JsonParseException("Style Definition needs an id and a collection of resource type definition locations");
        }

        final JsonObject object = json.getAsJsonObject();

        if (!object.has("id"))
        {
            throw new JsonParseException("Style Definition needs an id.");
        }

        final IIdentifier id = CONST_GSON.fromJson(object.get("id"), IdentifierDeserializer.CONST_IDENTIFIER_TYPE);

        if (!object.has("types") || !object.get("types").isJsonArray())
        {
            throw new JsonParseException("Style Definition needs a types array.");
        }

        final Collection<IIdentifier> resourceTypeLocations =
          StreamHelper.getJsonArrayAsStream(object.get("types").getAsJsonArray())
            .map(e -> (IIdentifier) CONST_GSON.fromJson(e, IdentifierDeserializer.CONST_IDENTIFIER_TYPE))
            .collect(Collectors.toList());

        return new StyleDefinition(id, resourceTypeLocations);
    }
}
