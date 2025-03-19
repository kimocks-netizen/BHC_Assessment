package com.example.bhc_assessment2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class HelloApplication extends Application {
        private Warehouse warehouse = new Warehouse("Main Warehouse", 1000); // Warehouse object
        private int cartonsAdded = 0; // Track cartons added
        private double cartonsWeight = 0; // Track total weight of cartons
        private int palletsAdded = 0; // Track pallets added
        private double palletsWeight = 0; // Track total weight of pallets

        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("Warehouse Management System");

            VBox root = new VBox(10);
            root.setPadding(new javafx.geometry.Insets(10));

            // Warehouse Capacity
            Label capacityLabel = new Label("Warehouse Capacity: 1000 kgs");
            Label remainingCapacityLabel = new Label("Remaining Capacity: 1000 kgs");
            root.getChildren().addAll(capacityLabel, remainingCapacityLabel);

            // Adding Cartons
            Label cartonLabel = new Label("Adding Cartons Into Warehouse");
            root.getChildren().add(cartonLabel);

            HBox cartonButtons = new HBox(10);
            Button addCartonButton = new Button("Add Carton");
            Button removeCartonButton = new Button("Remove Carton");
            Button sendToWarehouseButton = new Button("Send to Warehouse");
            cartonButtons.getChildren().addAll(addCartonButton, removeCartonButton, sendToWarehouseButton);
            root.getChildren().add(cartonButtons);

            // Display Cartons Added
            Label cartonsAddedLabel = new Label("0 cartons added, 0 kgs");
            root.getChildren().add(cartonsAddedLabel);

            // Adding Pallets
            Label palletLabel = new Label("Adding Pallets Into Warehouse");
            root.getChildren().add(palletLabel);

            HBox palletButtons = new HBox(10);
            Button addPalletButton = new Button("Add Pallet");
            Button removePalletButton = new Button("Remove Pallet");
            Button sendPalletToWarehouseButton = new Button("Send Pallet to Warehouse");
            palletButtons.getChildren().addAll(addPalletButton, removePalletButton, sendPalletToWarehouseButton);
            root.getChildren().add(palletButtons);

            // Display Pallets Added
            Label palletsAddedLabel = new Label("0 pallets added, 0 kgs");
            root.getChildren().add(palletsAddedLabel);

            // Special Line
            Label specialLineLabel = new Label("Special Line");
            root.getChildren().add(specialLineLabel);

            Button specialLineButton = new Button("Add to Special Line");
            specialLineButton.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Manager approval required for special line. Proceed?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        openSpecialLineWindow(primaryStage, remainingCapacityLabel);
                    }
                });
            });
            root.getChildren().add(specialLineButton);

            // Offloading Packages
            Label offloadLabel = new Label("Offload Packages");
            root.getChildren().add(offloadLabel);

            TextField offloadField = new TextField();
            Button offloadButton = new Button("Offload Package");
            HBox offloadBox = new HBox(10, offloadField, offloadButton);
            root.getChildren().add(offloadBox);

            // Discard Packages
            Label discardLabel = new Label("Discard Packages");
            root.getChildren().add(discardLabel);

            TextField discardField = new TextField();
            Button discardButton = new Button("Discard Package");
            HBox discardBox = new HBox(10, discardField, discardButton);
            root.getChildren().add(discardBox);

            // Warehouse Snapshot
            Button snapshotButton = new Button("View Warehouse Snapshot");
            root.getChildren().add(snapshotButton);

            // Historical Records
            Button logsButton = new Button("View Historical Records");
            root.getChildren().add(logsButton);

            // Filter and Search
            Label filterLabel = new Label("Filter and Search");
            root.getChildren().add(filterLabel);

            TextField searchField = new TextField();
            Button searchButton = new Button("Search");
            HBox searchBox = new HBox(10, searchField, searchButton);
            root.getChildren().add(searchBox);

            // Display Search Results
            Label searchResultsLabel = new Label();
            root.getChildren().add(searchResultsLabel);

            // Event Handlers for Carton Buttons
            addCartonButton.setOnAction(e -> {
                if (warehouse.getRemainingCapacity() >= 10) { // Check warehouse capacity
                    cartonsAdded++;
                    cartonsWeight += 10; // Assuming each carton weighs 10 kgs
                    cartonsAddedLabel.setText(cartonsAdded + " cartons added, " + cartonsWeight*2 + " kgs");
                } else {
                    System.out.println("Warehouse is full. Cannot add more cartons.");
                }
            });

            removeCartonButton.setOnAction(e -> {
                if (cartonsAdded > 0) {
                    cartonsAdded--;
                    cartonsWeight -= 10; // Assuming each carton weighs 10 kgs
                    cartonsAddedLabel.setText(cartonsAdded + " cartons added, " + cartonsWeight *2 + " kgs");
                } else {
                    System.out.println("No cartons to remove.");
                }
            });
            //events for 2 added butons:
            // Event Handlers for Offloading Buttons
            offloadOldestButton.setOnAction(e -> {
                if (warehouse.offloadOldestPackage()) {
                    System.out.println("Oldest package offloaded successfully.");
                } else {
                    System.out.println("Failed to offload oldest package.");
                }
            });

            offloadNewestButton.setOnAction(e -> {
                if (warehouse.offloadNewestPackage()) {
                    System.out.println("Newest package offloaded successfully.");
                } else {
                    System.out.println("Failed to offload newest package.");
                }
            });
            sendToWarehouseButton.setOnAction(e -> {
                if (cartonsAdded > 0) {
                    double totalWeight = cartonsAdded * 10; // Calculate total weight dynamically
                    if (warehouse.getRemainingCapacity() >= totalWeight) {
                        // Add cartons to a line in the warehouse
                        Line line = new Line(1, 100, false);
                        for (int i = 0; i < cartonsAdded; i++) {
                            Package carton = new Package("C" + i, "QualityA", 10, false);
                            line.addCarton(carton);
                        }
                        Rack rack = new Rack("R1");
                        rack.addLine(line);
                        warehouse.addRack(rack);
                        warehouse.updateCapacity(totalWeight); // Update warehouse capacity with totalWeight
                        remainingCapacityLabel.setText("Remaining Capacity: " + warehouse.getRemainingCapacity()+ " kgs");
                        System.out.println(cartonsAdded + " cartons sent to warehouse.");

                        // Reset cartonsAdded and cartonsWeight after sending
                        cartonsAdded = 0;
                        cartonsWeight = 0;
                        cartonsAddedLabel.setText("0 cartons added, 0 kgs");
                    } else {
                        System.out.println("Warehouse does not have enough capacity for these cartons.");
                    }
                } else {
                    System.out.println("No cartons to send.");
                }
            });

            // Event Handlers for Pallet Buttons
            addPalletButton.setOnAction(e -> {
                if (warehouse.getRemainingCapacity() >= 100) { // Check warehouse capacity
                    palletsAdded++;
                    palletsWeight += 100; // Assuming each pallet weighs 100 kgs
                    palletsAddedLabel.setText(palletsAdded + " pallets added, " + palletsWeight * 2+ " kgs");
                } else {
                    System.out.println("Warehouse is full. Cannot add more pallets.");
                }
            });

            removePalletButton.setOnAction(e -> {
                if (palletsAdded > 0) {
                    palletsAdded--;
                    palletsWeight -= 100; // Assuming each pallet weighs 100 kgs
                    palletsAddedLabel.setText(palletsAdded + " pallets added, " + palletsWeight *2  + " kgs");
                } else {
                    System.out.println("No pallets to remove.");
                }
            });

            sendPalletToWarehouseButton.setOnAction(e -> {
                if (palletsAdded > 0) {
                    if (warehouse.getRemainingCapacity() >= palletsWeight) {
                        // Add pallets to a line in the warehouse
                        Line line = new Line(2, 200, true);
                        Pallet pallet = new Pallet("P1", "QualityB", 10);
                        for (int i = 0; i < 10; i++) { // Assuming 10 packages per pallet
                            Package pkg = new Package("P" + i, "QualityB", 10, true);
                            pallet.addPackage(pkg);
                        }
                        line.addPallet(pallet);
                        Rack rack = new Rack("R2");
                        rack.addLine(line);
                        warehouse.addRack(rack);
                        warehouse.updateCapacity(palletsWeight); // Update warehouse capacity
                        remainingCapacityLabel.setText("Remaining Capacity: " + warehouse.getRemainingCapacity() + " kgs");
                        System.out.println(palletsAdded + " pallets sent to warehouse.");
                        palletsAdded = 0;
                        palletsWeight = 0;
                        palletsAddedLabel.setText("0 pallets added, 0 kgs");
                    } else {
                        System.out.println("Warehouse does not have enough capacity for these pallets.");
                    }
                } else {
                    System.out.println("No pallets to send.");
                }
            });

            // Event Handler for Offload Button
            // Event Handler for Offload Button
            offloadButton.setOnAction(e -> {
                String serialNumber = offloadField.getText();

                // Check if the package exists
                if (warehouse.removePackage(serialNumber)) {
                    // Package offloaded successfully
                    remainingCapacityLabel.setText("Remaining Capacity: " + warehouse.getRemainingCapacity() + " kgs");
                    System.out.println("Package " + serialNumber + " offloaded.");
                } else {
                    // Package not found, show alert with available packages
                    offloadAndDiscard(serialNumber);

                }
            });

        // Event Handler for Discard Button
            discardButton.setOnAction(e -> {
                String serialNumber = discardField.getText();

                // Check if the package exists
                if (warehouse.discardPackage(serialNumber)) {
                    // Package discarded successfully
                    remainingCapacityLabel.setText("Remaining Capacity: " + warehouse.getRemainingCapacity() + " kgs");
                    System.out.println("Package " + serialNumber + " discarded.");
                } else {
                    // Package not found, show alert with available packages
                    offloadAndDiscard(serialNumber);
                }
            });

            // Event Handler for Snapshot Button
            snapshotButton.setOnAction(e -> {
                String snapshot = warehouse.getSnapshot();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warehouse Snapshot");
                alert.setHeaderText(null);
                alert.setContentText(snapshot);
                alert.showAndWait();
            });

            // Event Handler for Logs Button
            logsButton.setOnAction(e -> {
                StringBuilder logs = new StringBuilder();
                for (String log : warehouse.getLogs()) {
                    logs.append(log).append("\n");
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Historical Records");
                alert.setHeaderText(null);
                alert.setContentText(logs.toString());
                alert.showAndWait();
            });

            // Event Handler for Search Button
            searchButton.setOnAction(e -> {
                String serialNumber = searchField.getText();
                String result = warehouse.searchPackageOrRack(serialNumber);
                searchResultsLabel.setText(result);
            });

            // Set up the scene
            Scene scene = new Scene(root, 400, 700);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    private void offloadAndDiscard(String serialNumber) {
        StringBuilder availablePackages = new StringBuilder("Package " + serialNumber + " not found.\nAvailable packages:\n");

        // Reuse the suggestion logic from searchPackageOrRack
        availablePackages.append("Cartons: ");
        for (Rack rack : warehouse.racks) { // Access racks directly
            for (Line line : rack.getLines()) {
                for (Package carton : line.getCartons()) {
                    availablePackages.append(carton.serialNumber).append(", ");
                }
            }
        }

        availablePackages.append("\nPallets: ");
        for (Rack rack : warehouse.racks) { // Access racks directly
            for (Line line : rack.getLines()) {
                for (Pallet pallet : line.getPallets()) {
                    for (Package pkg : pallet.getPackages()) {
                        availablePackages.append(pkg.serialNumber).append(", ");
                    }
                }
            }
        }

        // Remove the trailing comma and space
        if (availablePackages.length() > 0) {
            availablePackages.setLength(availablePackages.length() - 2);
        }

        // Show alert with available packages
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Package Not Found");
        alert.setHeaderText(null);
        alert.setContentText(availablePackages.toString());
        alert.showAndWait();
    }

    // Method to open the Special Line window
        // Track cartons and pallets added to the special line
        int specialCartonsAdded = 0;
        double specialCartonsWeight = 0;
        int specialPalletsAdded = 0;
        double specialPalletsWeight = 0;
        private void openSpecialLineWindow(Stage primaryStage, Label remainingCapacityLabel) {
            Stage specialLineStage = new Stage();
            specialLineStage.setTitle("Special Line");

            VBox specialLineRoot = new VBox(10);
            specialLineRoot.setPadding(new javafx.geometry.Insets(10));



            // Add Cartons to Special Line
            Label specialCartonLabel = new Label("Add Cartons to Special Line");
            Button addSpecialCartonButton = new Button("Add Carton");
            Label specialCartonsAddedLabel = new Label("0 cartons added, 0 kgs");
            specialLineRoot.getChildren().addAll(specialCartonLabel, addSpecialCartonButton, specialCartonsAddedLabel);

            // Add Pallets to Special Line
            Label specialPalletLabel = new Label("Add Pallets to Special Line");
            Button addSpecialPalletButton = new Button("Add Pallet");
            Label specialPalletsAddedLabel = new Label("0 pallets added, 0 kgs");
            specialLineRoot.getChildren().addAll(specialPalletLabel, addSpecialPalletButton, specialPalletsAddedLabel);

            // Send Special Line to Warehouse
            Button sendSpecialLineButton = new Button("Send Special Line to Warehouse");
            specialLineRoot.getChildren().add(sendSpecialLineButton);

            // Event Handlers for Special Line Buttons
            addSpecialCartonButton.setOnAction(e -> {
                if (warehouse.getRemainingCapacity() >= 10) { // Check warehouse capacity
                    specialCartonsAdded++;
                    specialCartonsWeight += 10; // Assuming each carton weighs 10 kgs
                    specialCartonsAddedLabel.setText(specialCartonsAdded + " cartons added, " + specialCartonsWeight*2 + " kgs");
                } else {
                    System.out.println("Warehouse is full. Cannot add more cartons.");
                }
            });

            addSpecialPalletButton.setOnAction(e -> {
                if (warehouse.getRemainingCapacity() >= 100) { // Check warehouse capacity
                    specialPalletsAdded++;
                    specialPalletsWeight += 100; // Assuming each pallet weighs 100 kgs
                    specialPalletsAddedLabel.setText(specialPalletsAdded + " pallets added, " + specialPalletsWeight*2 + " kgs");
                } else {
                    System.out.println("Warehouse is full. Cannot add more pallets.");
                }
            });

            sendSpecialLineButton.setOnAction(e -> {
                if (specialCartonsAdded > 0 || specialPalletsAdded > 0) {
                    double totalWeight = specialCartonsWeight + specialPalletsWeight;
                    if (warehouse.getRemainingCapacity() >= totalWeight) {
                        // Add cartons and pallets to a special line in the warehouse
                        Line specialLine = new Line(3, 300, true); // Special line
                        for (int i = 0; i < specialCartonsAdded; i++) {
                            Package carton = new Package("SC" + i, "QualityA", 10, false);
                            specialLine.addCarton(carton);
                        }
                        for (int i = 0; i < specialPalletsAdded; i++) {
                            Pallet pallet = new Pallet("SP" + i, "QualityB", 10);
                            for (int j = 0; j < 10; j++) { // Assuming 10 packages per pallet
                                Package pkg = new Package("SP" + i + "-" + j, "QualityB", 10, true);
                                pallet.addPackage(pkg);
                            }
                            specialLine.addPallet(pallet);
                        }
                        Rack rack = new Rack("R3");
                        rack.addLine(specialLine);
                        warehouse.addRack(rack);
                        warehouse.updateCapacity(totalWeight); // Update warehouse capacity
                        remainingCapacityLabel.setText("Remaining Capacity: " + warehouse.getRemainingCapacity() + " kgs");
                        System.out.println("Special line sent to warehouse.");

                        // Reset special line counters
                        specialCartonsAdded = 0;
                        specialCartonsWeight = 0;
                        specialPalletsAdded = 0;
                        specialPalletsWeight = 0;
                        specialCartonsAddedLabel.setText("0 cartons added, 0 kgs");
                        specialPalletsAddedLabel.setText("0 pallets added, 0 kgs");

                        // Close the special line window
                        specialLineStage.close();
                    } else {
                        System.out.println("Warehouse does not have enough capacity for the special line.");
                    }
                } else {
                    System.out.println("No cartons or pallets to send.");
                }
            });

            // Set up the scene
            Scene specialLineScene = new Scene(specialLineRoot, 300, 300);
            specialLineStage.setScene(specialLineScene);
            specialLineStage.show();
        }
}