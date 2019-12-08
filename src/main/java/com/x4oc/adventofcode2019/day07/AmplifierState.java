package com.x4oc.adventofcode2019.day07;

import java.util.List;

public class AmplifierState {
    private final List<Integer> opcodes;
    private final int thrust;
    private final boolean haltLast;

    public AmplifierState(List<Integer> opcodes, int thrust, boolean haltLast) {
        this.opcodes = opcodes;
        this.thrust = thrust;
        this.haltLast = haltLast;
    }

    public List<Integer> getOpcodes() {
        return opcodes;
    }

    public int getThrust() {
        return thrust;
    }

    public boolean isHaltLast() {
        return haltLast;
    }

    @Override
    public String toString() {
        return "AmplifierState{" +
                "opcodes=" + opcodes +
                ", thrust=" + thrust +
                ", haltLast=" + haltLast +
                '}';
    }
}
