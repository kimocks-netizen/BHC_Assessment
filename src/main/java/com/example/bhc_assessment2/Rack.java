package com.example.bhc_assessment2;

import java.util.ArrayList;
import java.util.List;

import java.util.*;

class Rack {
    private String serialNumber;
    private List<Line> lines;

    Rack(String serialNumber) {
        this.serialNumber = serialNumber;
        this.lines = new ArrayList<>();
    }

    void addLine(Line line) {
        lines.add(line);
    }

    double getTotalWeight() {
        return lines.stream().mapToDouble(Line::getTotalWeight).sum();
    }

    List<Package> getAllPackages() {
        List<Package> packages = new ArrayList<>();
        for (Line line : lines) {
            packages.addAll(line.getCartons());
            for (Pallet pallet : line.getPallets()) {
                packages.addAll(pallet.getPackages());
            }
        }
        return packages;
    }

    List<Line> getLines() {
        return lines;
    }

    String getSerialNumber() {
        return serialNumber;
    }
}