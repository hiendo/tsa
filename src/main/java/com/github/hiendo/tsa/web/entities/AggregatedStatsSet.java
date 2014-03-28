package com.github.hiendo.tsa.web.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility= JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class AggregatedStatsSet {
    private List<AggregatedStats> aggregatedStatsList;

    // Json serialization
    private AggregatedStatsSet(){}

    public AggregatedStatsSet(List<AggregatedStats> aggregatedStatsList) {
        this.aggregatedStatsList = aggregatedStatsList;
    }

    public List<AggregatedStats> getAggregatedStatsList() {
        return aggregatedStatsList;
    }

    public int size() {
        return aggregatedStatsList == null ? 0 : aggregatedStatsList.size();
    }

    @JsonIgnore
    public AggregatedStats getAggregatedStats(int index) {
        return aggregatedStatsList.get(index);
    }
}
