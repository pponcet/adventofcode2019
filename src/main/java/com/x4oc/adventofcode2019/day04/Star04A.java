package com.x4oc.adventofcode2019.day04;

import com.x4oc.adventofcode2019.day03.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.x4oc.adventofcode2019.day03.Star03.Direction.*;

public class Star04 {
    private static final Logger logger = LoggerFactory.getLogger(Star04.class);

    public static void execute(){

        long result = 0L;

        int inputMin = 124075;
        int inputMax = 580769;

        result = Stream.iterate(inputMin, n -> n <= inputMax, n -> n + 1)
                .filter(Star04::matchesCriteria)
                .count();

        logger.info("result: "+result);

    }

    public static boolean matchesCriteria(int number){
        return hasAdjacentDigits(number) && areDigitsIncreasing(number);
    }

    public static boolean hasAdjacentDigits(int number){
        String str = Integer.toString(number);
        return Stream.iterate(0, n -> n < str.length() - 1, n -> n + 1)
        .anyMatch(n -> str.charAt(n) == str.charAt(n + 1));
    }

    public static boolean areDigitsIncreasing(int number){
        String str = Integer.toString(number);
        return Stream.iterate(0, n -> n < str.length() - 1, n -> n + 1)
                .allMatch(n -> str.charAt(n) <= str.charAt(n + 1));
    }

}
