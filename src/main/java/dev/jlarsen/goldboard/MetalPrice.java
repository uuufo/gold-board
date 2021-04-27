package dev.jlarsen.goldboard;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class MetalPrice {

    BigDecimal ounce;
    BigDecimal dwt;
    BigDecimal gram;

    public MetalPrice(BigDecimal ounce) {
        this.ounce = ounce;
        this.dwt = ounce.divide(new BigDecimal("20"), RoundingMode.HALF_UP);
        this.gram = ounce.divide(new BigDecimal("31.1035"), RoundingMode.HALF_UP);
    }

    public BigDecimal getOunce(int karat) {
        String percent = getPercent(karat);
        return ounce.multiply(new BigDecimal(percent)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDwt(int karat) {
        String percent = getPercent(karat);
        return dwt.multiply(new BigDecimal(percent)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getGram(int karat) {
        String percent = getPercent(karat);
        return gram.multiply(new BigDecimal(percent)).setScale(2, RoundingMode.HALF_UP);
    }

//    public BigDecimal getPrice(String type, int karat) {
//        double percent = getPercent(karat);
//        if (type.equals("dwt")) {
//            return dwt.multiply(new BigDecimal(percent)).setScale(2, RoundingMode.CEILING);
//        } else if (type.equals("gram")) {
//            return gram.multiply(new BigDecimal(percent)).setScale(2, RoundingMode.CEILING);
//        } else {
//            return ounce.multiply(new BigDecimal(percent)).setScale(2, RoundingMode.CEILING);
//        }
//    }

    private String getPercent(int purity) {
        switch (purity) {
            // gold karat
            case 10: return "0.4167";
            case 14: return "0.5833";
            case 16: return "0.6667";
            case 18: return "0.7500";
            case 21: return "0.8750";
            case 22: return "0.9167";
            // silver & platinum
            case 900: return "0.900";
            case 925: return "0.925";
            case 950: return "0.950";
            case 999: return "0.999";
            default: return "1.0";
        }
    }
}
