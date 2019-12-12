package com.x4oc.adventofcode2019.day12;

import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Star12 {
    private static final Logger logger = LoggerFactory.getLogger(Star12.class);

    private final static Pattern LINE_PATTERN = Pattern.compile("<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>");

    public static void execute(){

        String fileName = "./src/main/resources/dataStar12.txt";

        long result = 0L;
        try {

            List<Moon> moons = StreamEx.ofLines(Paths.get(fileName))
                    .map(LINE_PATTERN::matcher)
                    .filter(Matcher::matches)
                    .map(matcher -> new Moon(new Point(Long.valueOf(matcher.group(1)), Long.valueOf(matcher.group(2)), Long.valueOf(matcher.group(3))),
                            new Point(0, 0, 0)))
                    .collect(Collectors.toList());
            //logger.info("moons: "+moons);

            int iteration = 0;
            do{
                iteration++;
                List<Moon> finalMoons = moons;
                moons = moons.stream()
                        .map(moon -> new AbstractMap.SimpleEntry<>(moon, applyGravity(finalMoons, moon)))
                        //.peek(e -> logger.info("new velo: "+e.getKey()+" --> "+e.getValue()))
                        .map(e -> new Moon(calculateNewPosition(e.getKey(), e.getValue()), e.getValue()))
                        .collect(Collectors.toList());

                //logger.info("it: "+iteration+", new moons: "+moons);
            }while(iteration < 1000);

            result = moons.stream()
                    .mapToLong(Moon::calculateEnergy)
                    .sum();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("result: "+result);

    }

    public static Point applyGravity(List<Moon> moons, Moon moon){
        //logger.info("moon: "+moon);
        List<Moon> remainingMoons = new ArrayList<>(moons);
        remainingMoons.remove(moon);
        return calculateNewVelocity(remainingMoons, moon);
    }

    public static Point calculateNewVelocity(List<Moon> moons, Moon moon){
        return moons.stream()
                .map(Moon::getPosition)
                //.peek(p -> logger.info("pos: "+p))
                .map(p -> moon.getPosition().gravityPower(p))
                //.peek(p -> logger.info("vel: "+p))
                .reduce(moon.getVelocity(), (p1, p2) ->
                        new Point(p1.getX() + p2.getX(), p1.getY() + p2.getY(), p1.getZ() + p2.getZ()));
    }

    public static Point calculateNewPosition(Moon moon, Point velocity){
        return new Point(moon.getPosition().getX() + velocity.getX(),
                moon.getPosition().getY() + velocity.getY(),
                moon.getPosition().getZ() + velocity.getZ());
    }


}
