package ru.itis.uno.client;

import java.util.prefs.Preferences;

public class SessionManager {

    private static final Preferences preferences = Preferences.userNodeForPackage(SessionManager.class);

    public static void saveId(int id) {
        preferences.put("id", String.valueOf(id));
    }

    public static int getId() {
        return Integer.parseInt(preferences.get("id", "0"));
    }

    public static void saveUsername(String username) {
        preferences.put("username", username);
    }

    public static String getUsername() {
        return preferences.get("username", "");
    }

    public static void clearSession() {
        preferences.remove("id");
        preferences.remove("username");
    }

}
