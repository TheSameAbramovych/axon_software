package com.bohdan.abramovych.axon_software.service;

import com.bohdan.abramovych.axon_software.entity.Depth;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatesLoggerService {

    final DepthFetcherService depthFetcherService;

    @Value("${depth.query.symbol}")
    String symbol;
    @Value("${depth.query.limit}")
    Integer limit;

    Map<Double, Double> lastBids = new HashMap<>();
    Map<Double, Double> lastAsks = new HashMap<>();
    double bidsSize = 0;
    double asksSize = 0;

    @Scheduled(fixedDelayString = "${log_updates.cron}")
    public void logUpdates() {
        Depth depth = depthFetcherService.getDepth(symbol, limit);
        Map<Double, Double> bidsMap = depth.getBidsMap();
        Map<Double, Double> asksMap = depth.getAsksMap();

        bidsSize = logChangesAndGetSize(bidsMap, lastBids, bidsSize, "bids");
        asksSize = logChangesAndGetSize(asksMap, lastAsks, asksSize, "asks");

        lastBids = bidsMap;
        lastAsks = asksMap;
    }

    private double logChangesAndGetSize(Map<Double, Double> sideMap, Map<Double, Double> lastSide, double sideSize, String side) {
        double sizeSum = sideMap.entrySet().stream()
                .mapToDouble(entry -> {
                    Double key = entry.getKey();
                    Double value = entry.getValue();
                    Double lastSize = lastSide.remove(key);
                    if (lastSize == null) {
                        log.info("new " + side + " ({}, {})", key, value);
                    }
                    else if (!Objects.equals(value, lastSize)) {
                        log.warn("updated " + side + " ({}, {})", key, value);
                    }
                    return value;
                })
                .sum();

        lastSide.forEach((key, value) -> log.error("delete " + side + " ({}, {})", key, value));
        log.info("change " + side + " size: " + (sizeSum - sideSize));
        return sizeSum;
    }
}
