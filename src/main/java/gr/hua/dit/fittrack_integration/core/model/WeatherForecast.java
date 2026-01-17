package gr.hua.dit.fittrack_integration.core.model;

import java.time.LocalDate;
import java.util.List;

public class WeatherForecast {

    public static class Daily {
        private LocalDate date;
        private Double tempMin;
        private Double tempMax;
        private Double precipitationSum;
        private Integer weatherCode;

        public Daily(LocalDate date, Double tempMin, Double tempMax, Double precipitationSum, Integer weatherCode) {
            this.date = date;
            this.tempMin = tempMin;
            this.tempMax = tempMax;
            this.precipitationSum = precipitationSum;
            this.weatherCode = weatherCode;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public Double getTempMin() {
            return tempMin;
        }

        public void setTempMin(Double tempMin) {
            this.tempMin = tempMin;
        }

        public Double getTempMax() {
            return tempMax;
        }

        public void setTempMax(Double tempMax) {
            this.tempMax = tempMax;
        }

        public Double getPrecipitationSum() {
            return precipitationSum;
        }

        public void setPrecipitationSum(Double precipitationSum) {
            this.precipitationSum = precipitationSum;
        }

        public Integer getWeatherCode() {
            return weatherCode;
        }

        public void setWeatherCode(Integer weatherCode) {
            this.weatherCode = weatherCode;
        }
    }

    private double latitude;
    private double longitude;
    private List<Daily> days;

    public WeatherForecast(double latitude, double longitude, List<Daily> days) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.days = days;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<Daily> getDays() {
        return days;
    }

    public void setDays(List<Daily> days) {
        this.days = days;
    }
}
