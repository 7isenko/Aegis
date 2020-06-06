package io.github._7isenko.aegis;

import java.util.UUID;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class UUIDGetter {
    private static UUIDGetter instance;

    private UUIDGetter() {
    }

    public UUID getUuid(String nickname) throws NullPointerException {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + nickname;
        try {
            @SuppressWarnings("deprecation")
            String stringJsonUIID = IOUtils.toString(new URL(url));
            if (stringJsonUIID.isEmpty()) throw new NullPointerException();
            JSONObject jsonUUID = (JSONObject) JSONValue.parseWithException(stringJsonUIID);
            return UUID.fromString(addDashes(jsonUUID.get("id").toString()));
        } catch (IOException | ParseException e) {
            // ignore
        }
        throw new NullPointerException();
    }

    private String addDashes(String uuid) {
        return uuid.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5");
    }

    public static UUIDGetter getInstance() {
        if (instance == null) {
            instance = new UUIDGetter();
        }
        return instance;
    }

    public static boolean hasInstance() {
        return instance != null;
    }
}
