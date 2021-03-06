package uk.ac.cam.gr3.weather.data.util;

import java.util.HashSet;

public class DangerousWeather {
    private static final HashSet<String> conditions = new HashSet<>();

    //List of dangerous weather conditions from the set the API provides
    static {
        conditions.add("Blizzard");
        conditions.add("CloudRainThunder");
        conditions.add("CloudSleetSnowThunder");
        conditions.add("FreezingDrizzle");
        conditions.add("FreezingFog");
        conditions.add("FreezingRain");
        conditions.add("PartCloudRainThunderDay");
        conditions.add("PartCloudSleetSnowThunderDay");
    }

    public static boolean isDangerous(String condition) {
        return conditions.contains(condition);
    }
}
