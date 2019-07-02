package org.elastoscarrier.dposprofit.Utils;

import java.util.*;

public class Utils {

    public static Map<String, Long> sortMapByValue(Map<String, Long> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Long> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<String, Long>> list = new ArrayList<>(oriMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> mapping1, Map.Entry<String, Long> mapping2) {
                return mapping2.getValue().compareTo(mapping1.getValue());
            }
        });

        Iterator<Map.Entry<String, Long>> iter = list.iterator();
        Map.Entry<String, Long> tmpEntry;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }
}
