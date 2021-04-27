package dev.jlarsen.goldboard;

import lombok.Data;

@Data
public class ApiStatus {

    private boolean result;

    public boolean getResult() {
        return result;
    }
}
