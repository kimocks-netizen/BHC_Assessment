package com.example.bhc_assessment2;

class Package {
    String serialNumber;
    String qualityMark;
    double mass;
    boolean isLoose;
    long timestamp; // Track when the package was added

    Package(String serialNumber, String qualityMark, double mass, boolean isLoose) {
        super(serialNumber, qualityMark); // Calls ShipmentItem constructor
        this.serialNumber = serialNumber;
        this.qualityMark = qualityMark;
        this.mass = mass;
        this.isLoose = isLoose;
        this.timestamp = System.currentTimeMillis(); // Set timestamp when package is created
    }
    // Getter for timestamp
    //FIFI - FIRST IN FIRST OUT  AND LIFO - LAST IN LAST OUT
    public long getTimestamp() {
        return timestamp;
    }
}