package dev.jlarsen.goldboard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BackupPrices {

    private BigDecimal gold;
    private BigDecimal silver;
    private BigDecimal platinum;

    public BackupPrices(BigDecimal gold, BigDecimal silver, BigDecimal platinum) {
        this.gold = gold;
        this.silver = silver;
        this.platinum = platinum;
    }
}
