package com.x4oc.adventofcode2019.day11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.x4oc.adventofcode2019.day11.Star11.Comparison.EQUALS;
import static com.x4oc.adventofcode2019.day11.Star11.Comparison.LESS_THAN;
import static com.x4oc.adventofcode2019.day11.Star11.Operation.ADD;
import static com.x4oc.adventofcode2019.day11.Star11.Operation.MULTIPLY;

public class Star11 {
    private static final Logger logger = LoggerFactory.getLogger(Star11.class);

    public enum Operation{
        ADD,
        MULTIPLY
    }

    public enum Comparison{
        LESS_THAN,
        EQUALS
    }

    public static void execute(){

        String fileName = "./src/main/resources/dataStar11.txt";

        long input = 0L;

        int result = 0;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            List<Long> opcodes = Stream.of(stream.findFirst().get().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            result = calculate(opcodes, input);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("result: "+result);

    }

    private static int calculate(List<Long> opcodes, long input) {
        boolean halt = false;
        int position = 0;
        long result = 0L;
        long relativeBaseOffset = 0L;

        boolean coloredTile = false;
        Robot robot = new Robot(new Point(0, 0), Robot.Direction.UP);
        Map<Point, Long> colors = new HashMap<>();
        int a = 0;
        do{
            a++;
            long opcode = opcodes.get(position);
            long parameter1 = (opcode / 100) % 10;
            long parameter2 = (opcode / 1000) % 10;
            long parameter3 = (opcode / 10000) % 10;

            Long lastDigit = opcode % 10;
            switch (lastDigit.intValue()){
                case 1:
                    operate(opcodes, position, ADD, parameter1, parameter2, parameter3, relativeBaseOffset);
                    position += 4;
                    break;
                case 2:
                    operate(opcodes, position, MULTIPLY, parameter1, parameter2, parameter3, relativeBaseOffset);
                    position += 4;
                    break;
                case 3:
                    write(opcodes, position + 1, parameter1, input, relativeBaseOffset);
                    position += 2;
                    break;
                case 4:
                    result = getOperand(opcodes, position + 1, parameter1, relativeBaseOffset);
                    if(!coloredTile){
                        colors.put(robot.getPosition(), result);
                        coloredTile = true;
                    }else{
                        robot.move(result);
                        coloredTile = false;
                        Point newPosition = robot.getPosition();
                        if(colors.containsKey(newPosition)){
                            input = colors.get(robot.getPosition());
                        }else{
                            input = 0;
                        }
                    }
                    position += 2;
                    break;
                case 5:
                    if(getOperand(opcodes, position + 1, parameter1, relativeBaseOffset) != 0){
                        position = getOperand(opcodes, position + 2, parameter2, relativeBaseOffset).intValue();
                    }else {
                        position += 3;
                    }
                    break;
                case 6:
                    if(getOperand(opcodes, position + 1, parameter1, relativeBaseOffset) == 0){
                        position = getOperand(opcodes, position + 2, parameter2, relativeBaseOffset).intValue();
                    }else {
                        position += 3;
                    }
                    break;
                case 7:
                    compare(opcodes, position, LESS_THAN, parameter1, parameter2, parameter3, relativeBaseOffset);
                    position += 4;
                    break;
                case 8:
                    compare(opcodes, position, EQUALS, parameter1, parameter2, parameter3, relativeBaseOffset);
                    position += 4;
                    break;
                case 9:
                    if((opcode / 10) % 10 == 9){
                        halt = true;
                    }else{
                        relativeBaseOffset += getOperand(opcodes, position + 1, parameter1, relativeBaseOffset);
                        position += 2;
                    }
                    break;
                default:
                    logger.error("bad opcode");
                    halt = true;
                    break;
            }
        }while(!halt);
        return colors.size();
    }

    private static void compare(List<Long> opcodes, int position, Comparison comparison, long parameter1, long parameter2, long parameter3, long relativeBaseOffset) {
        long value = 0L;
        switch (comparison){
            case LESS_THAN:
                if(getOperand(opcodes, position + 1, parameter1, relativeBaseOffset) < getOperand(opcodes, position + 2, parameter2, relativeBaseOffset)){
                    value = 1L;
                }
                break;
            case EQUALS:
                if(getOperand(opcodes, position + 1, parameter1, relativeBaseOffset).equals(getOperand(opcodes, position + 2, parameter2, relativeBaseOffset))){
                    value = 1L;
                }
                break;
        }
        write(opcodes, position + 3, parameter3, value, relativeBaseOffset);
    }

    private static void operate(List<Long> opcodes, int position, Operation operation, long parameter1, long parameter2, long parameter3, long relativeBaseOffset) {

        switch(operation){
            case ADD:
                write(opcodes, position + 3, parameter3, getOperand(opcodes, position + 1, parameter1, relativeBaseOffset) + getOperand(opcodes, position + 2, parameter2, relativeBaseOffset),relativeBaseOffset);
                break;
            case MULTIPLY:
                write(opcodes, position + 3, parameter3, getOperand(opcodes, position + 1, parameter1, relativeBaseOffset) * getOperand(opcodes, position + 2, parameter2, relativeBaseOffset),relativeBaseOffset);
                break;
        }
    }

    public static Long getOperand(List<Long> opcodes, int position, Long parameter, Long relativeBaseOffset){
        if(position >= opcodes.size()){
            fillOpcodes(opcodes, position);
        }

        switch (parameter.intValue()){
            case 0:
                return getOperand(opcodes, opcodes.get(position).intValue(), 1L, relativeBaseOffset);
            case 1:
                return opcodes.get(position);
            case 2:
                Long pos = opcodes.get(position) + relativeBaseOffset;
                return getOperand(opcodes, pos.intValue(), 1L, relativeBaseOffset);
            default:
                logger.info("bad parameter mode");
                return 0L;
        }
    }

    public static int getPositionToWrite(List<Long> opcodes, int position, Long parameter, Long relativeBaseOffset){
        if(position >= opcodes.size()){
            fillOpcodes(opcodes, position);
        }
        switch (parameter.intValue()){
            case 0:
            case 1:
                return opcodes.get(position).intValue();
            case 2:
                return opcodes.get(position).intValue() + relativeBaseOffset.intValue();
            default:
                logger.info("bad parameter mode");
                return 0;
        }

    }

    public static void write(List<Long> opcodes, int position, long parameter, long value, long relativeBaseOffset){
        int pos = getPositionToWrite(opcodes, position, parameter, relativeBaseOffset);
        if(pos >= opcodes.size()){
            fillOpcodes(opcodes, pos);
        }
        opcodes.set(pos, value);
    }

    public static void fillOpcodes(List<Long> opcodes, int position){
        int size = opcodes.size();
        for(int i = 0; i <= position - size; i++){
            opcodes.add(0L);
        }
    }


}
