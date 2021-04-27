package dev.jlarsen.goldboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

@Service
public class GoldBoardService {

    @Autowired
    RestTemplate restTemplate;

    public boolean checkApiStatus(CurrentProperties properties) {
        String apiUrl = "https://www.goldapi.io/api/status";
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-access-token", properties.getApiKey());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<ApiStatus> response = restTemplate.exchange
                    (apiUrl, HttpMethod.GET, httpEntity, ApiStatus.class);
            if (response.getBody() != null) {
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

    public CurrentProperties readPropertiesFile() {
        CurrentProperties currentProperties = null;
        try (InputStream input = new FileInputStream("src/main/resources/gold-config")) {
            Properties properties = new Properties();
            properties.load(input);
            String apiKey = properties.getProperty("api.key");
            String prop1 = properties.getProperty("price.gold");
            String prop2 = properties.getProperty("price.silver");
            String prop3 = properties.getProperty("price.platinum");
            currentProperties = new CurrentProperties(apiKey, prop1, prop2, prop3);
        } catch (IOException ex) {
            System.out.println("gold-config file not found!! is it in the same folder as this program?");
            ex.printStackTrace();
        }
        return currentProperties;
    }

    public MetalPrice getCurrentPrice(CurrentProperties properties, Metal metal) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-access-token", properties.getApiKey());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ApiResponse> response = null;
        try {
            response = restTemplate.exchange(metal.apiUrl, HttpMethod.GET, httpEntity, ApiResponse.class);
        } catch (HttpClientErrorException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        if (response != null && response.getBody() != null) {
            BigDecimal currentPrice = response.getBody().getPrice();
            return adjustPrice(currentPrice, properties, metal);
        } else {
            return getBackupPrice(properties, metal);
        }
    }

    public MetalPrice getBackupPrice(CurrentProperties properties, Metal metal) {
        BigDecimal price = null;
        try (InputStream input = new FileInputStream("./gold-prices-backup")) {
            Properties prices = new Properties();
            prices.load(input);
            price = new BigDecimal(prices.getProperty(metal.name()));
        } catch (IOException ex) {
            System.out.println("gold-prices-backup file could not be found!!");
            ex.printStackTrace();
        }
        if (price != null) {
            return adjustPrice(price, properties, metal);
        } else {
            // todo - throw visual exception
            return null;
        }
    }

    public MetalPrice adjustPrice(BigDecimal currentPrice, CurrentProperties properties, Metal metal) {
        return new MetalPrice(currentPrice.multiply(new BigDecimal(properties.getPayRate(metal))
                .setScale(4, RoundingMode.HALF_UP)));
//        return new MetalPrice((currentPrice));
    }
}
