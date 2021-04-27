package dev.jlarsen.goldboard;

public enum Metal {
    GOLD("https://www.goldapi.io/api/XAU/USD"),
    SILVER("https://www.goldapi.io/api/XAG/USD"),
    PLATINUM("https://www.goldapi.io/api/XPT/USD");

    public final String apiUrl;

    Metal(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
