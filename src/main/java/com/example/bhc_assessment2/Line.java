package com.example.bhc_assessment2;

import java.util.*;

class Line {
    private int lineNumber;
    private double maxCapacity;
    private List<Package> cartons;
    private List<Pallet> pallets;

    Line(int lineNumber, double maxCapacity, boolean b) {
        this.lineNumber = lineNumber;
        this.maxCapacity = maxCapacity;
        this.cartons = new ArrayList<>();
        this.pallets = new ArrayList<>();
    }

    boolean addCarton(Package carton) {
        if (getTotalWeight() + carton.mass <= maxCapacity) {
            cartons.add(carton);
            return true;
        }
        return false;
    }

    boolean addPallet(Pallet pallet) {
        if (getTotalWeight() + pallet.getTotalWeight() <= maxCapacity) {
            pallets.add(pallet);
            return true;
        }
        return false;
    }

    double getTotalWeight() {
        double totalWeight = 0;
        for (Package carton : cartons) {
            totalWeight += carton.mass;
        }
        for (Pallet pallet : pallets) {
            totalWeight += pallet.getTotalWeight();
        }
        return totalWeight;
    }

    List<Package> getCartons() {
        return cartons;
    }

    List<Pallet> getPallets() {
        return pallets;
    }

    int getLineNumber() {
        return lineNumber;
    }
}
