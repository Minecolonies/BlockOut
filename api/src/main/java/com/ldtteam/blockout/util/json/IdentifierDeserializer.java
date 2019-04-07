package com.ldtteam.blockout.util.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;

import java.lang.reflect.Type;

public class IdentifierDeserializer implements JsonDeserializer<IIdentifier>
{
    public static final Type                   CONST_IDENTIFIER_TYPE = new TypeToken<IIdentifier>() {}.getType();
    private static      IdentifierDeserializer ourInstance           = new IdentifierDeserializer();

    private IdentifierDeserializer()
    {
    }

    public static IdentifierDeserializer getInstance()
    {
        return ourInstance;
    }

    @Override
    public IIdentifier deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (!json.isJsonPrimitive())
        {
            throw new JsonParseException("Identifier needs to be string.");
        }

        return IIdentifier.create(json.getAsString());
    }
}
