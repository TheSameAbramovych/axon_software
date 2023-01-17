package com.bohdan.abramovych.axon_software.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class Depth implements Serializable {

    Long lastUpdateId;
    List<List<Double>> bids;
    List<List<Double>> asks;

    public Map<Double, Double> getBidsMap() {
        return getMap(bids);
    }

    public Map<Double, Double> getAsksMap() {
        return getMap(asks);
    }

    private Map<Double, Double> getMap(List<List<Double>> list) {
        return bids == null ? null : list.stream()
                .map(doubles -> Map.entry(doubles.get(0), doubles.get(1)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
