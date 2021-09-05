package com.example.viewer.stages.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Assets {
    public static ClassLoader loader() {
        return Assets.class.getClassLoader();
    }


    public static URL getAssetUrl(String name) {
        return Assets.class.getResource(name);
    }

    public static String getAssetPath(String name) {
        return getAssetUrl(name).toExternalForm();
    }

    public static final InputStream getStream(String resourceName) {
        return loader().getResourceAsStream(resourceName);
    }

    public static final String readtextFile(String resourceName) {
        StringBuilder sb = new StringBuilder();

        try (InputStreamReader stream = new InputStreamReader(getStream(resourceName))) {
            try (BufferedReader reader = new BufferedReader(stream)) {
                while(reader.ready()) {
                    sb.append(reader.readLine() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
