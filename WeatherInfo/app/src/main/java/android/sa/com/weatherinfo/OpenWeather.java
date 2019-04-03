package android.sa.com.weatherinfo;

public class OpenWeather {
    public String weatherType;
    public String weatherDescription;
    public String temp;
    public String humidity;
    public String tempMin;
    public String tempMax;
    public String windSpeed;
    public String windGust;
    private Double convert(String s){
        Float k = Float.valueOf(s);
        return  ((k -273.15) * (9/5)) + 32;
    }
    public String toString() {
        return String.format("Today weather is %s,%s \n" +
                "Temperature: %.2f, Min: %.2f, Max: %02f \n" +
                "Humidity: %s \n" +
                "Wind: speed %s, gust %s ",weatherType,weatherDescription,
                convert(temp),convert(tempMin),convert(tempMax)
                ,humidity,windSpeed,windGust
        );
    }
}
