package dev.jlarsen.goldboard;

import lombok.Data;

@Data
public class CurrentProperties {

    private String apiKey;
    private String goldPayRate;
    private String silverPayRate;
    private String platinumPayRate;

    public CurrentProperties(String apiKey, String goldPayRate,
                             String silverPayRate, String platinumPayRate) {
        this.apiKey = apiKey;
        this.goldPayRate = goldPayRate;
        this.silverPayRate = silverPayRate;
        this.platinumPayRate = platinumPayRate;
    }

    public String getPayRate(Metal metal) {
        switch (metal) {
            case GOLD:
                return goldPayRate;
            case SILVER:
                return silverPayRate;
            case PLATINUM:
                return platinumPayRate;
            default:
                return null;
        }
    }
}
