package ru.otus.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import ru.otus.crm.model.Client;

import java.lang.reflect.Type;

public class ClientSerializer implements JsonSerializer<Client> {

    @Override
    public JsonElement serialize(Client client, Type type, JsonSerializationContext context) {

        JsonObject object = new JsonObject();
/*        String otherValue = obj.b.other;
        object.addProperty("other", otherValue );
        object.addProperty("text", obj.text);*/
        return object;
    }

}
