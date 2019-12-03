package com.x4oc.adventofcode2019.day03;

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

public class Star03 {
    private static final Logger logger = LoggerFactory.getLogger(Star03.class);

    public enum Direction{
        UP(new Point(0, -1)),
        LEFT(new Point(-1, 0)),
        RIGHT(new Point(1, 0)),
        DOWN(new Point(0, 1)),
        NONE(new Point(0, 0));

        private Point point;

        Direction(Point point) {
            this.point = point;
        }

        public Point getPoint() {
            return point;
        }
    }

    public static void execute(){

        String fileName = "./src/main/resources/dataStar03.txt";

        int result = 0;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

        Stream<String> stream2 = Stream.of("R75,D30,R83,U83,L12,D49,R71,U7,L72",
                "U62,R66,U55,R34,D71,R55,D58,R83");

            List<List<Point>> lists = stream.map(Star03::extractPath)
                    //.peek(l -> logger.info("list: "+l))
                    .collect(Collectors.toList());

            Optional<Point> minPoint = lists.get(0).stream()
                    .filter(p -> lists.get(1).contains(p))
                    //.peek(p -> logger.info("point: "+p))
                    .min(Point::compareTo);

            if(minPoint.isPresent()){
                Point min = minPoint.get();
                logger.info("min: "+min);
                result = min.manhattanDistance();
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("result: "+result);

    }

    public static List<Point> extractPath(String wire){
        List<Point> points = new ArrayList<>();
        Point current = new Point(0,0);
        for(String line : wire.split(",")){
            current = extractNextLine(line, points, current);
        }

        return points;
    }

    public static Point extractNextLine(String line, List<Point> points, Point current){
        char directionChar = line.charAt(0);
        int distance = Integer.valueOf(line.substring(1));
        final Direction direction;
        switch (directionChar){
            case 'U':
                direction = UP;
                break;
            case 'L':
                direction = LEFT;
                break;
            case 'R':
                direction = RIGHT;
                break;
            case 'D':
                direction = DOWN;
                break;
            default:
                direction = NONE;
                logger.error("bad direction input");
                break;
        }

        Stream.iterate(1, n -> n + 1).limit(distance)
                .forEach(n -> points.add(new Point(current.getX() + n * direction.getPoint().getX(),
                        current.getY() + n * direction.getPoint().getY())));
        return points.get(points.size() - 1);

    }


}
