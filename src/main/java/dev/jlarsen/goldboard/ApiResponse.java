package dev.jlarsen.goldboard;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ApiResponse {

    private Instant timestamp;
    private String metal;
    private String currency;
    private String exchange;
    private String symbol;
    private BigDecimal prev_close_price;
    private BigDecimal open_price;
    private BigDecimal low_price;
    private BigDecimal high_price;
    private Instant open_time;
    private BigDecimal price;
    private BigDecimal ch;
    private BigDecimal chp;
    private BigDecimal ask;
    private BigDecimal bid;

}
