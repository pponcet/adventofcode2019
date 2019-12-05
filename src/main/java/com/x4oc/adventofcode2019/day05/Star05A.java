package com.x4oc.adventofcode2019.day05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Star05 {
    private static final Logger logger = LoggerFactory.getLogger(Star05.class);

    public enum Operation{
        ADD,
        MULTIPLY;
    }

    public static void execute(){

        String fileName = "./src/main/resources/dataStar05.txt";
        int input = 1;

        int result = 0;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            List<Integer> opcodes = Stream.of(stream.findFirst().get().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            result= calculate(opcodes, input);


        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("result: "+result);

    }

    private static Integer calculate(List<Integer> opcodes, int input) {
        boolean halt = false;
        int position = 0;
        int result = 0;
        do{
            switch (opcodes.get(position) % 10){
                case 1:
                    operate(opcodes, position, Operation.ADD);
                    position += 4;
                    break;
                case 2:
                    operate(opcodes, position, Operation.MULTIPLY);
                    position += 4;
                    break;
                case 3:
                    opcodes.set(getOperand(opcodes, position + 1, 1), input);
                    position += 2;
                    break;
                case 4:
                    result = getOperand(opcodes, position + 1, 0);
                    //logger.info("output: "+result);
                    position += 2;
                    break;
                case 9:
                    halt = true;
                    break;
                default:
                    logger.error("bad opcode");
                    halt = true;
                    break;
            }
        }while(!halt);
        return result;
    }

    private static void operate(List<Integer> opcodes, int position, Operation operation) {
        int parameter1 = (opcodes.get(position) / 100) % 10;
        int parameter2 = (opcodes.get(position) / 1000) % 10;
        switch(operation){
            case ADD:
                opcodes.set(getOperand(opcodes, position + 3, 1), getOperand(opcodes, position + 1, parameter1) + getOperand(opcodes, position + 2, parameter2));
                break;
            case MULTIPLY:
                opcodes.set(getOperand(opcodes, position + 3, 1), getOperand(opcodes, position + 1, parameter1) * getOperand(opcodes, position + 2, parameter2));
        }
    }

    public static int getOperand(List<Integer> opcodes, int position, int parameter){
        if(parameter == 0){
            return opcodes.get(opcodes.get(position));
        }else {
            return opcodes.get(position);
        }
    }

}
