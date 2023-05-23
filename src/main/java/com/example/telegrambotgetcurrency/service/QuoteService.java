package com.example.telegrambotgetcurrency.service;

import com.example.telegrambotgetcurrency.model.Quote;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class QuoteService {
    public static String getQuote(Quote quote) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        JsonNode response = new ObjectMapper()
                .readTree(restTemplate.getForObject("http://fucking-great-advice.ru/api/random",String.class));
        quote.setQuote(String.valueOf(response.get("text")));
        return quote.getQuote();
    }
}
