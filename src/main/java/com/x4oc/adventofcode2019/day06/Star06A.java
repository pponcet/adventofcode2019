package com.x4oc.adventofcode2019.day06;

import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Star06 {
    private static final Logger logger = LoggerFactory.getLogger(Star06.class);

    private final static Pattern LINE_PATTERN = Pattern.compile("([A-Z0-9]+)\\)([A-Z0-9]+)");


    public static void execute(){

        String fileName = "./src/main/resources/dataStar06.txt";

        int result = 0;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            Map<String, String> orbits = StreamEx.ofLines(Paths.get(fileName))
                    .map(LINE_PATTERN::matcher)
                    .filter(Matcher::matches)
                    .map(matcher -> new AbstractMap.SimpleEntry<>(matcher.group(2), matcher.group(1)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            logger.info("orbits: "+orbits);

            result = calculateAllNumberOrbits(orbits).values().stream()
            .mapToInt(i -> i)
            .sum();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("result: "+result);

    }

    public static Map<String, Integer> calculateAllNumberOrbits(Map<String, String> orbits){

        Map<String, Integer> numberOfOrbits = new HashMap<>();
        numberOfOrbits.put("COM", 0);
        for(String objectInSpace : orbits.keySet()){
            numberOfOrbits.put(objectInSpace, calculateNumberOrbit(orbits, numberOfOrbits, objectInSpace));
        }
        return numberOfOrbits;
    }

    public static int calculateNumberOrbit(Map<String, String> orbits, Map<String, Integer> numberOfOrbits, String objectInSpace){
        logger.info("ois: "+objectInSpace);
        String orbitFrom = orbits.get(objectInSpace);
        logger.info("orbitFrom: "+orbitFrom);
        if(numberOfOrbits.containsKey(orbitFrom)){
            logger.info("numberOfOrbits: "+numberOfOrbits.get(orbitFrom));
            return numberOfOrbits.get(orbitFrom) + 1;
        }else{
            logger.info("else");
            return calculateNumberOrbit(orbits, numberOfOrbits, orbitFrom) + 1;
        }
    }

}
