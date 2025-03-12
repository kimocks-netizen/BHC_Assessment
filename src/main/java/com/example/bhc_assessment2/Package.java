package com.example.bhc_assessment2;

class Package {
    String serialNumber;
    String qualityMark;
    double mass;
    boolean isLoose;
    long timestamp; // Track when the package was added

    Package(String serialNumber, String qualityMark, double mass, boolean isLoose) {
        this.serialNumber = serialNumber;
        this.qualityMark = qualityMark;
        this.mass = mass;
        this.isLoose = isLoose;
        this.timestamp = System.currentTimeMillis(); // Set timestamp when package is created
    }
}