package job.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import job.persistence.ExchangeRateRepository;
import job.persistence.entity.ExchangeRate;
import job.service.serviceInterface.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    @PostConstruct
    public void checkAndAddRecords() {
        Iterator<Map.Entry<String, JsonNode>> fields = getExchangeRatesJson();
        Map<String, Double> rates = new HashMap<>();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            rates.put(entry.getKey(), entry.getValue().asDouble());
        }
        List<ExchangeRate> exchangeRatesFromDB = exchangeRateRepository.findAll();
        List<ExchangeRate> notExistExchangeRates = new ArrayList<>();
        for (ExchangeRate exchangeRate : exchangeRatesFromDB) {
            if (!(rates.containsKey(exchangeRate.getCurrencyCode()))) {
                notExistExchangeRates.add(exchangeRate);
            }
        }
        exchangeRateRepository.saveAll(notExistExchangeRates);
    }

    @Override
    @Transactional
    public void updateRecords() {
        Iterator<Map.Entry<String, JsonNode>> fields = getExchangeRatesJson();
        List<ExchangeRate> exchangeRatesFromDB = exchangeRateRepository.findAll();
        Map<String, ExchangeRate> existingRatesMap = new HashMap<>();
        for (ExchangeRate exchangeRate : exchangeRatesFromDB) {
            existingRatesMap.put(exchangeRate.getCurrencyCode(), exchangeRate);
        }
        List<ExchangeRate> exchangeRatesToSave = new ArrayList<>();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String currencyCode = entry.getKey();
            double rate = entry.getValue().asDouble();
            if (existingRatesMap.containsKey(currencyCode)) {
                ExchangeRate exchangeRate = existingRatesMap.get(currencyCode);
                exchangeRate.setRate(rate);
                exchangeRatesToSave.add(exchangeRate);
            } else {
                ExchangeRate newRate = new ExchangeRate();
                newRate.setCurrencyCode(currencyCode);
                newRate.setRate(rate);
                exchangeRatesToSave.add(newRate);
            }
        }
        exchangeRateRepository.saveAll(exchangeRatesToSave);
    }

    private Iterator<Map.Entry<String, JsonNode>> getExchangeRatesJson() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.exchangerate-api.com/v4/latest/AMD";
        String jsonString;
        try {
            jsonString = restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode ratesNode = root.path("rates");
        return ratesNode.fields();
    }
}