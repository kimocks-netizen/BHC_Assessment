package com.example.bhc_assessment2;
import java.util.*;

class Pallet {
    private String serialNumber;
    private String qualityMark;
    private List<Package> packages;
    private int capacity;

    Pallet(String serialNumber, String qualityMark, int capacity) {
        this.serialNumber = serialNumber;
        this.qualityMark = qualityMark;
        this.capacity = capacity;
        this.packages = new ArrayList<>();
    }

    boolean addPackage(Package pkg) {
        if (packages.size() < capacity && pkg.qualityMark.equals(qualityMark)) {
            packages.add(pkg);
            return true;
        }
        return false;
    }

    double getTotalWeight() {
        return packages.stream().mapToDouble(p -> p.mass).sum();
    }

    List<Package> getPackages() {
        return packages;
    }
}