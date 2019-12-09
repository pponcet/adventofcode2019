package com.x4oc.adventofcode2019.day08;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Star08 {
    private static final Logger logger = LoggerFactory.getLogger(Star08.class);

    public static void execute(){

        String fileName = "./src/main/resources/dataStar08.txt";

        int width = 25;
        int height = 6;

        int area = width * height;

        long result = 0L;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            final AtomicInteger counter = new AtomicInteger();

            Optional<List<Integer>> minLayer = stream.findFirst().get()
                    .chars()
                    .mapToObj(i -> i - 48)
                    .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / area))
                    .values()
                    .stream()
                    .sequential()
                    .min(Comparator.comparing(list -> Star08.calculateNumberOfDigit(list, 0)));

            if(minLayer.isPresent()){
                List<Integer> min = minLayer.get();
                result = Star08.calculateNumberOfDigit(min, 1)
                * Star08.calculateNumberOfDigit(min, 2);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("result: "+result);

    }

    public static long calculateNumberOfDigit(List<Integer> digits, int digit){
        return digits.stream()
                .filter(i -> i == digit)
                .count();
    }


}
