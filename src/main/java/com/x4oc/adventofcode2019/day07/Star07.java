package com.x4oc.adventofcode2019.day07;

import org.paukov.combinatorics3.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.x4oc.adventofcode2019.day07.Star07.Comparison.*;
import static com.x4oc.adventofcode2019.day07.Star07.Operation.*;

public class Star07 {
    private static final Logger logger = LoggerFactory.getLogger(Star07.class);

    public enum Operation{
        ADD,
        MULTIPLY
    }

    public enum Comparison{
        LESS_THAN,
        EQUALS
    }

    public static void execute(){

        String fileName = "./src/main/resources/dataStar07.txt";

        int input = 0;

        int result = 0;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            List<Integer> opcodes = Stream.of(stream.findFirst().get().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());


            OptionalInt maxThrust = Generator.permutation(0, 1, 2, 3, 4)
                    .simple()
                    .stream()
                    .mapToInt(phaseSetting -> calculateThrust(opcodes, input, phaseSetting))
                    .max();


            if(maxThrust.isPresent()){
                result = maxThrust.getAsInt();
            }


        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("result: "+result);

    }

    private static int calculate(List<Integer> opcodes, int input, int phase) {
        boolean halt = false;
        int position = 0;
        int result = 0;
        boolean firstInput = false;
        do{
            int opcode = opcodes.get(position);
            int parameter1 = (opcodes.get(position) / 100) % 10;
            int parameter2 = (opcodes.get(position) / 1000) % 10;
            switch (opcode % 10){
                case 1:
                    operate(opcodes, position, ADD, parameter1, parameter2);
                    position += 4;
                    break;
                case 2:
                    operate(opcodes, position, MULTIPLY, parameter1, parameter2);
                    position += 4;
                    break;
                case 3:
                    if(!firstInput){
                        write(opcodes, position + 1, phase);
                        firstInput = true;
                    }else{
                        write(opcodes, position + 1, input);
                    }
                    position += 2;
                    break;
                case 4:
                    result = getOperand(opcodes, position + 1, 0);
                    //logger.info("output: "+result);
                    position += 2;
                    break;
                case 5:
                    if(getOperand(opcodes, position + 1, parameter1) != 0){
                        position = getOperand(opcodes, position + 2, parameter2);
                    }else {
                        position += 3;
                    }
                    break;
                case 6:
                    if(getOperand(opcodes, position + 1, parameter1) == 0){
                        position = getOperand(opcodes, position + 2, parameter2);
                    }else {
                        position += 3;
                    }
                    break;
                case 7:
                    compare(opcodes, position, LESS_THAN, parameter1, parameter2);
                    position += 4;
                    break;
                case 8:
                    compare(opcodes, position, EQUALS, parameter1, parameter2);
                    position += 4;
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

    private static void compare(List<Integer> opcodes, int position, Comparison comparison, int parameter1, int parameter2) {
        int value = 0;
        switch (comparison){
            case LESS_THAN:
                if(getOperand(opcodes, position + 1, parameter1) < getOperand(opcodes, position + 2, parameter2)){
                    value = 1;
                }
                break;
            case EQUALS:
                if(getOperand(opcodes, position + 1, parameter1) == getOperand(opcodes, position + 2, parameter2)){
                    value = 1;
                }
                break;
        }
        write(opcodes, position + 3, value);
    }

    private static void operate(List<Integer> opcodes, int position, Operation operation, int parameter1, int parameter2) {

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

    public static void write(List<Integer> opcodes, int position, int value){
        opcodes.set(getOperand(opcodes, position, 1), value);
    }

    public static int calculateThrust(List<Integer> opcodes, int input, List<Integer> phaseSetting){
        int thrust = input;
        for(int phase : phaseSetting){
            thrust = calculate(opcodes, thrust, phase);
        }
        return thrust;
    }

}
