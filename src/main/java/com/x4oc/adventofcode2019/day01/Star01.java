package com.x4oc.adventofcode2019.day01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Star01 {
    private static final Logger logger = LoggerFactory.getLogger(Star01.class);

    public static void execute(){

        String fileName = "./src/main/resources/dataStar01.txt";

        long result = 0L;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            result = stream.mapToInt(Integer::valueOf)
                    .map(Star01::calculateFuelRequirement)
                    .sum();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("result: "+result);

    }

    public static int calculateFuelRequirement(int mass){
        int result = (mass / 3) - 2;
        if(result > 8){
            result += calculateFuelRequirement(result);
        }
        return result;
    }
}
