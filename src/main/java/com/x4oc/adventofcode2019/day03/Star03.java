package com.x4oc.adventofcode2019.day02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Star02 {
    private static final Logger logger = LoggerFactory.getLogger(Star02.class);

    public static void execute(){

        String fileName = "./src/main/resources/dataStar02.txt";

        int result = 0;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            List<Integer> opcodes = Stream.of(stream.findFirst().get().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            opcodes.set(1, 12);
            opcodes.set(2, 2);
            result= calculate(opcodes);


        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("result: "+result);

    }

    private static Integer calculate(List<Integer> opcodes) {
        boolean halt = false;
        int position = 0;
        do{
            switch (opcodes.get(position)){
                case 1:
                    opcodes.set(opcodes.get(position + 3), opcodes.get(opcodes.get(position + 1)) + opcodes.get(opcodes.get(position + 2)));
                    position = position + 4;
                    break;
                case 2:
                    opcodes.set(opcodes.get(position + 3), opcodes.get(opcodes.get(position + 1)) * opcodes.get(opcodes.get(position + 2)));
                    position = position + 4;
                    break;
                case 99:
                    halt = true;
                    break;
                default:
                    logger.error("bad opcode");
                    halt = true;
                    break;
            }
        }while(!halt);
        return opcodes.get(0);
    }


}
