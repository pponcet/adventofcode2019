package com.x4oc.adventofcode2019.day10;

import one.util.streamex.MoreCollectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.AbstractMap.*;

public class Star10 {
    private static final Logger logger = LoggerFactory.getLogger(Star10.class);

    public static void execute(){

        String fileName = "./src/main/resources/dataStar10.txt";

        int result = 0;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            List<String> lines = stream.collect(Collectors.toList());
            Map<Point, Boolean> asteroids = IntStream.range(0, lines.size())
                    .mapToObj(i -> extractAsteroids(lines.get(i), i))
                    .flatMap(Function.identity())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            Optional<Entry<Point, Integer>> maxOrigin = asteroids.keySet().stream()
                    .flatMap(origin -> calculateVisibleAsteroids(asteroids, origin))
                    .max(Comparator.comparing(Entry::getValue));

            if(maxOrigin.isPresent()){
                result = maxOrigin.get().getValue();
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("result: "+result);

    }

    public static Stream<Map.Entry<Point, Boolean>> extractAsteroids(String line, int row){
        return Stream.iterate(0, n -> n < line.length(), n -> n + 1)
                .filter(n -> line.charAt(n) == '#')
                .map(n -> new SimpleEntry<>(new Point(n, row), true));

    }

    public static Stream<Map.Entry<Point, Integer>> calculateVisibleAsteroids(Map<Point, Boolean> asteroids, Point origin){
        Map<Point, Boolean> map = new HashMap<>(asteroids);
        map.remove(origin);
        Map<Point, Boolean> updatedAsteroids = map.keySet().stream()
                .flatMap(obstacle -> findValidAsteroids(map, origin, obstacle))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Boolean::logicalAnd));

        //show(updatedAsteroids, origin);
        Long countVisibleAsteroids = map.keySet().stream()
                .filter(updatedAsteroids::get)
                .count();

        return Stream.of(new SimpleEntry<>(origin, countVisibleAsteroids.intValue()));

    }

    public static Stream<Map.Entry<Point, Boolean>> findValidAsteroids(Map<Point, Boolean> obstacles, Point origin, Point obstacle){
        Map<Point, Boolean> map = new HashMap<>(obstacles);
        map.remove(obstacle);
        map.keySet().stream()
                .filter(destination -> areAlignedInThatOrder(origin, obstacle, destination))
                .forEach(p -> map.put(p, false));
        return map.entrySet().stream();
    }

    public static boolean areAlignedInThatOrder(Point origin, Point obstacle, Point destination){
        boolean aligned = false;
        double deltaObstacleX = obstacle.getX() - origin.getX();
        double deltaObstacleY = obstacle.getY() - origin.getY();
        double deltaDestinationX = destination.getX() - origin.getX();
        double deltaDestinationY = destination.getY() - origin.getY();
        double deltaX = destination.getX() - obstacle.getX();
        double deltaY = destination.getY() - obstacle.getY();

        if((deltaObstacleX * deltaX >= 0 && deltaObstacleY * deltaY >= 0)){

            if ((deltaDestinationX == 0 && deltaObstacleX == 0) || (deltaDestinationY == 0 && deltaObstacleY == 0)) {
                aligned = true;
            }else{
                if(deltaObstacleX/deltaDestinationX == deltaObstacleY/deltaDestinationY){
                    aligned = true;
                }
            }
        }

        return aligned;
    }

    public static void show(Map<Point, Boolean> updatedAsteroids, Point origin){
        for(int j = 0; j <= 20; j++){
            for(int i = 0; i <= 20; i++){
                Point point = new Point(i, j);
                if (point.equals(origin)) {
                    System.out.print("O");
                }else{
                    if(updatedAsteroids.containsKey(point)){
                        if(updatedAsteroids.get(point)){
                            System.out.print("T");
                        }else{
                            System.out.print("F");
                        }
                    }else{
                        System.out.print(".");
                    }
                }

            }
            System.out.print("\n");
        }
    }

}
