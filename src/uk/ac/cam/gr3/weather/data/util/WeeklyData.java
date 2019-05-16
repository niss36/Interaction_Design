package uk.ac.cam.gr3.weather.data.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//This provides data for the weekly report
public class WeeklyData {
    //Use the getter methods below to access the data
    private ArrayList<Day> week;
    private int numFutureDays;

    public WeeklyData (JSONArray weekForecast) {
        numFutureDays = 7;
        week = new ArrayList<>();
        for (int i = 0; i < numFutureDays; i++) {
            JSONObject dayForecast = weekForecast.getJSONObject(i);
            String date = dayForecast.getString("date");
            int lowestTemperature = dayForecast.getInt("temp_min_c");
            int highestTemperature = dayForecast.getInt("temp_max_c");
            String weatherIcon =  "WeatherIcons/" + dayForecast.getJSONArray("Timeframes").getJSONObject(0).getString("wx_icon");
            Day day = new Day(date, lowestTemperature, highestTemperature, weatherIcon);
            week.add(day);
        }
    }

    //getters
    public ArrayList<Day> getWeek() {
        return week;
    }
}
