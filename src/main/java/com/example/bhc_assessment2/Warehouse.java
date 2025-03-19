package com.example.bhc_assessment2;
import java.util.*;

class Warehouse {
    private String name;
    private double maxCapacity;
    private double currentCapacity; // Track current capacity used
    List<Rack> racks; // List of racks in the warehouse
    private List<Package> allPackages; // Track all packages for historical records
    private List<String> logs; // Log package movements

    Warehouse(String name, double maxCapacity) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
        this.racks = new ArrayList<>();
        this.allPackages = new ArrayList<>();
        this.logs = new ArrayList<>();
    }

    // Add a rack to the warehouse
    boolean addRack(Rack rack) {
        double rackWeight = rack.getTotalWeight();
        if (currentCapacity + rackWeight <= maxCapacity) {
            racks.add(rack);
            currentCapacity += rackWeight;
            allPackages.addAll(rack.getAllPackages());
            logs.add("Rack " + rack.getSerialNumber() + " added to warehouse.");
            return true;
        } else {
            logs.add("Failed to add rack " + rack.getSerialNumber() + ": Warehouse capacity exceeded.");
            return false;
        }
    }

    // Update warehouse capacity when packages are added or removed
    void updateCapacity(double weight) {
        if (currentCapacity + weight <= maxCapacity) {
            currentCapacity += weight;
            logs.add("Warehouse capacity updated by " + weight + " kgs. New capacity: " + currentCapacity + " kgs.");
        } else {
            logs.add("Warning: Attempted to update warehouse capacity by " + weight + " kgs, but capacity would be exceeded.");
        }
    }
    //FIFI - FIRST IN FIRST OUT  AND LIFO - LAST IN LAST OUT
    // Get all packages in the warehouse
    List<Package> getAllPackages() {
        return allPackages;
    }

    // Sort packages by timestamp (oldest first)
    List<Package> getPackagesOldestFirst() {
        List<Package> sortedPackages = new ArrayList<>(allPackages);
        sortedPackages.sort(Comparator.comparingLong(Package::getTimestamp));
        return sortedPackages;
    }

    // Sort packages by timestamp (newest first)
    List<Package> getPackagesNewestFirst() {
        List<Package> sortedPackages = new ArrayList<>(allPackages);
        sortedPackages.sort(Comparator.comparingLong(Package::getTimestamp).reversed());
        return sortedPackages;
    }
    // Offload the oldest package
    boolean offloadOldestPackage() {
        if (allPackages.isEmpty()) {
            System.out.println("No packages to offload.");
            return false;
        }

        // Get the oldest package
        Package oldestPackage = getPackagesOldestFirst().get(0);

        // Remove the package from the warehouse
        if (removePackage(oldestPackage.serialNumber)) {
            System.out.println("Offloaded oldest package: " + oldestPackage.serialNumber);
            return true;
        } else {
            System.out.println("Failed to offload oldest package.");
            return false;
        }
    }
    // Offload the newest package
    boolean offloadNewestPackage() {
        if (allPackages.isEmpty()) {
            System.out.println("No packages to offload.");
            return false;
        }

        // Get the newest package
        Package newestPackage = getPackagesNewestFirst().get(0);

        // Remove the package from the warehouse
        if (removePackage(newestPackage.serialNumber)) {
            System.out.println("Offloaded newest package: " + newestPackage.serialNumber);
            return true;
        } else {
            System.out.println("Failed to offload newest package.");
            return false;
        }
    }

    // Remove a package from the warehouse by serial number
    boolean removePackage(String serialNumber) {
        for (Rack rack : racks) {
            for (Line line : rack.getLines()) {
                // Remove from cartons
                Package carton = line.getCartons().stream()
                        .filter(p -> p.serialNumber.equals(serialNumber))
                        .findFirst()
                        .orElse(null);
                if (carton != null) {
                    line.getCartons().remove(carton);
                    currentCapacity -= carton.mass;
                    logs.add("Carton " + serialNumber + " removed from warehouse.");
                    return true;
                }
                // Remove from pallets
                for (Pallet pallet : line.getPallets()) {
                    Package pkg = pallet.getPackages().stream()
                            .filter(p -> p.serialNumber.equals(serialNumber))
                            .findFirst()
                            .orElse(null);
                    if (pkg != null) {
                        pallet.getPackages().remove(pkg);
                        currentCapacity -= pkg.mass;
                        logs.add("Package " + serialNumber + " removed from warehouse.");
                        return true;
                    }
                }
            }
        }
        logs.add("Package " + serialNumber + " not found in warehouse.");
        return false;
    }

    // Discard a package (mark as bad and remove)
    boolean discardPackage(String serialNumber) {
        if (removePackage(serialNumber)) {
            logs.add("Package " + serialNumber + " discarded.");
            return true;
        }
        return false;
    }

    // Get remaining capacity
    double getRemainingCapacity() {
        return maxCapacity - currentCapacity;
    }

    // Get all packages in the warehouse
    List<Package> getAllPackages() {
        return allPackages;
    }

    // Get all logs
    List<String> getLogs() {
        return logs;
    }

    // Generate a warehouse snapshot
    String getSnapshot() {
        StringBuilder snapshot = new StringBuilder();
        snapshot.append("Warehouse Snapshot:\n");
        snapshot.append("Total Capacity: ").append(maxCapacity).append(" kgs\n");
        snapshot.append("Current Capacity: ").append(currentCapacity).append(" kgs\n");
        snapshot.append("Remaining Capacity: ").append(getRemainingCapacity()).append(" kgs\n");
        snapshot.append("Packages:\n");
        for (Rack rack : racks) {
            for (Line line : rack.getLines()) {
                for (Package carton : line.getCartons()) {
                    snapshot.append("Carton ").append(carton.serialNumber)
                            .append(" (Mass: ").append(carton.mass).append(" kgs) in Rack ")
                            .append(rack.getSerialNumber()).append(", Line ").append(line.getLineNumber()).append("\n");
                }
                for (Pallet pallet : line.getPallets()) {
                    for (Package pkg : pallet.getPackages()) {
                        snapshot.append("Package ").append(pkg.serialNumber)
                                .append(" (Mass: ").append(pkg.mass).append(" kgs) in Rack ")
                                .append(rack.getSerialNumber()).append(", Line ").append(line.getLineNumber()).append("\n");
                    }
                }
            }
        }
        return snapshot.toString();
    }

    // Search for a package or rack by serial number
    String searchPackageOrRack(String serialNumber) {
        // Search for package
        for (Rack rack : racks) {
            for (Line line : rack.getLines()) {
                for (Package carton : line.getCartons()) {
                    if (carton.serialNumber.equals(serialNumber)) {
                        return "Package " + serialNumber + " found in Rack " + rack.getSerialNumber() +
                                ", Line " + line.getLineNumber() + ", Warehouse " + name;
                    }
                }
                for (Pallet pallet : line.getPallets()) {
                    for (Package pkg : pallet.getPackages()) {
                        if (pkg.serialNumber.equals(serialNumber)) {
                            return "Package " + serialNumber + " found in Rack " + rack.getSerialNumber() +
                                    ", Line " + line.getLineNumber() + ", Warehouse " + name;
                        }
                    }
                }
            }
        }

        // Search for rack
        for (Rack rack : racks) {
            if (rack.getSerialNumber().equals(serialNumber)) {
                StringBuilder result = new StringBuilder("Rack " + serialNumber + " found in Warehouse " + name + ".\nContents:\n");
                for (Line line : rack.getLines()) {
                    for (Package carton : line.getCartons()) {
                        result.append("Carton ").append(carton.serialNumber)
                                .append(" (Mass: ").append(carton.mass).append(" kgs) in Line ")
                                .append(line.getLineNumber()).append("\n");
                    }
                    for (Pallet pallet : line.getPallets()) {
                        for (Package pkg : pallet.getPackages()) {
                            result.append("Package ").append(pkg.serialNumber)
                                    .append(" (Mass: ").append(pkg.mass).append(" kgs) in Line ")
                                    .append(line.getLineNumber()).append("\n");
                        }
                    }
                }
                return result.toString();
            }
        }

        // If nothing is found, provide suggestions
        StringBuilder suggestions = new StringBuilder("No package or rack found with serial number: " + serialNumber + ".\n");
        suggestions.append("Try searching for:\n");

        // Add suggestions for cartons
        suggestions.append("Cartons: ");
        for (Rack rack : racks) {
            for (Line line : rack.getLines()) {
                for (Package carton : line.getCartons()) {
                    suggestions.append(carton.serialNumber).append(", ");
                }
            }
        }

        // Add suggestions for pallets
        suggestions.append("\nPallets: ");
        for (Rack rack : racks) {
            for (Line line : rack.getLines()) {
                for (Pallet pallet : line.getPallets()) {
                    for (Package pkg : pallet.getPackages()) {
                        suggestions.append(pkg.serialNumber).append(", ");
                    }
                }
            }
        }

        // Remove the trailing comma and space
        if (suggestions.length() > 0) {
            suggestions.setLength(suggestions.length() - 2);
        }

        return suggestions.toString();
    }
}