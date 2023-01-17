package com.bohdan.abramovych.axon_software.service;

import com.bohdan.abramovych.axon_software.entity.Depth;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DepthFetcherService {

    RestTemplate restTemplate;

    @NonFinal
    @Value("${binance_api_url}")
    String binanceUrl;

    @NonFinal
    @Value("${depth.query}")
    String depthQuery;


    public Depth getDepth(String symbol, Integer limit) {
        Map<String, ?> uriVariables = Map.of("symbol", symbol, "limit", limit);
        return restTemplate.getForObject(binanceUrl + depthQuery, Depth.class, uriVariables);
    }
}
