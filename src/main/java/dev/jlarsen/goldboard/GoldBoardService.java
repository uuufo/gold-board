package dev.jlarsen.goldboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

@Service
public class GoldBoardService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${status.url}")
    private String statusUrl;

    @Value("${properties.file}")
    private String propertiesFile;

    @Value("${backup.file}")
    private String backupFile;

    private final String workingDir = System.getProperty("user.dir");

    private static final Logger logger = LoggerFactory.getLogger(GoldBoardService.class);

    public boolean checkApiStatus(CurrentProperties properties) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-access-token", properties.getApiKey());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        logger.debug("Checking API status..");
        try {
            ResponseEntity<ApiStatus> response = restTemplate.exchange
                    (statusUrl, HttpMethod.GET, httpEntity, ApiStatus.class);
            if (response.getBody() != null) {
                logger.debug("Got API status response:");
                logger.debug("{}", response.getBody());
                if (response.getBody().getResult()) {
                    return true;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            ex.printStackTrace();
        }
        logger.error("API status check failed, maybe server is down?");
        return false;
    }

    public CurrentProperties readPropertiesFile() {
        CurrentProperties currentProperties = null;
        logger.debug("Attempting to read properties file..");
        try (InputStream input = new FileInputStream(workingDir + propertiesFile)) {
            Properties properties = new Properties();
            properties.load(input);
            String apiKey = properties.getProperty("api.key");
            String prop1 = properties.getProperty("price.gold");
            String prop2 = properties.getProperty("price.silver");
            String prop3 = properties.getProperty("price.platinum");
            currentProperties = new CurrentProperties(apiKey, prop1, prop2, prop3);
            logger.debug("Successfully read properties file..");
        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            logger.error(propertiesFile + " file not found!! is it in the same folder as this program?");
            ex.printStackTrace();
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            logger.error("Couldn't read properties file");
        }
        return currentProperties;
    }

    public MetalPrice getCurrentPrice(CurrentProperties properties, Metal metal) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-access-token", properties.getApiKey());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ApiResponse> response = null;
        logger.debug("Attempting to get price for: " + metal.name());
        try {
            response = restTemplate.exchange(metal.apiUrl, HttpMethod.GET, httpEntity, ApiResponse.class);
        } catch (HttpClientErrorException ex) {
            logger.error(ex.getMessage(), ex);
            logger.error("Couldn't reach API endpoint (are we authorized?)");
        }
        if (response != null && response.getBody() != null) {
            BigDecimal currentPrice = response.getBody().getPrice();
            logger.debug("Received price response for: " + metal.name());
            saveBackupPrice(currentPrice, metal);
            return adjustPrice(currentPrice, properties, metal);
        } else {
            return getBackupPrice(properties, metal);
        }
    }

    public void saveBackupPrice(BigDecimal currentPrice, Metal metal) {
        logger.debug("Writing " + metal.name() + " price to backup file.");
        Properties prices = new Properties();
        try {
            prices.load(new FileInputStream(workingDir + backupFile));
        } catch (FileNotFoundException ex) {
            logger.error("Backup file: " + backupFile + " doesn't exist, will create new file.");
            logger.error(ex.getMessage(), ex);
        } catch (IOException ex) {
            logger.error("Error reading backup file: " + backupFile);
            logger.error(ex.getMessage(), ex);
        }
        try (OutputStream output = new FileOutputStream(workingDir + backupFile)) {
            prices.put(metal.name(), currentPrice.toString());
            prices.store(output, "Backup Prices");
        } catch (IOException ex) {
            logger.error("Error writing backup file: " + backupFile);
            logger.error(ex.getMessage(), ex);
        }
    }

    public MetalPrice getBackupPrice(CurrentProperties properties, Metal metal) {
        logger.debug("Getting " + metal.name() + " price from backup file.");
        BigDecimal price = null;
        try (InputStream input = new FileInputStream(workingDir + backupFile)) {
            Properties prices = new Properties();
            prices.load(input);
            price = new BigDecimal(prices.getProperty(metal.name()));
            logger.debug("Got " + metal.name() + " price.");
        } catch (IOException ex) {
            logger.error(backupFile + " could not be read (not found?)");
            logger.error(ex.getMessage(), ex);
        }
        if (price != null) {
            return adjustPrice(price, properties, metal);
        } else {
            logger.error("Problem with price from backup file.");
            return null;
        }
    }

    public MetalPrice adjustPrice(BigDecimal currentPrice, CurrentProperties properties, Metal metal) {
        logger.debug("Adjusting " + metal.name() + " price using pay rate: " + properties.getPayRate(metal));
        return new MetalPrice(currentPrice.multiply(new BigDecimal(properties.getPayRate(metal))
                .setScale(4, RoundingMode.HALF_UP)));
    }
}
