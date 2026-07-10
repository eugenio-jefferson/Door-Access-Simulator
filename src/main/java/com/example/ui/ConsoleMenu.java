package com.example.ui;

import com.example.controller.SensorController;
import com.example.model.AccessDirection;
import com.example.service.AuthorizationSubscriber;
import com.example.service.StayTimeCalculatorSubscriber;

import java.util.Scanner;

public class ConsoleMenu {

    private final SensorController sensorController;
    private final AuthorizationSubscriber authorizationSubscriber;
    private final StayTimeCalculatorSubscriber stayTimeCalculatorSubscriber;
    private final Scanner scanner;

    public ConsoleMenu(SensorController sensorController,
            AuthorizationSubscriber authorizationSubscriber,
            StayTimeCalculatorSubscriber stayTimeCalculatorSubscriber) {
        this.sensorController = sensorController;
        this.authorizationSubscriber = authorizationSubscriber;
        this.stayTimeCalculatorSubscriber = stayTimeCalculatorSubscriber;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    simulateSwipe();
                    break;
                case "2":
                    addAuthorizedUser();
                    break;
                case "3":
                    removeAuthorizedUser();
                    break;
                case "4":
                    stayTimeCalculatorSubscriber.printReport();
                    break;
                case "5":
                    simulateDoorSensor();
                    break;
                case "6":
                    running = false;
                    System.out.println(" > Exiting simulation...");
                    break;
                default:
                    System.out.println(" > Invalid option. Please try again.");
            }

            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void printMenu() {
        System.out.println("\n-=-=-=-=-= Door Access Simulator Menu =-=-=-=-=-");
        System.out.println(" [ 1 ] Simulate Card Swipe");
        System.out.println(" [ 2 ] Add Authorized User");
        System.out.println(" [ 3 ] Remove Authorized User");
        System.out.println(" [ 4 ] View Stay Time Report");
        System.out.println(" [ 5 ] Simulate Door Sensor Change");
        System.out.println(" [ 6 ] Exit");
        System.out.print("Choose an option: ");
    }

    private void simulateSwipe() {
        System.out.print("\nEnter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter direction (1 for IN, 2 for OUT): ");
        String directionChoice = scanner.nextLine();

        System.out.println();
        AccessDirection direction = "1".equals(directionChoice) ? AccessDirection.IN : AccessDirection.OUT;
        sensorController.simulateCardRead(userId, direction);
    }

    private void addAuthorizedUser() {
        System.out.print("\nEnter User ID to authorize: ");
        String userId = scanner.nextLine();
        authorizationSubscriber.addAuthorizedUser(userId);
        System.out.println("User " + userId + " authorized.");
    }

    private void removeAuthorizedUser() {
        System.out.print("\nEnter User ID to revoke authorization: ");
        String userId = scanner.nextLine();
        authorizationSubscriber.removeAuthorizedUser(userId);
        System.out.println("User " + userId + " authorization revoked.");
    }

    private void simulateDoorSensor() {
        System.out.print("\nEnter door state (1 for OPEN, 2 for CLOSED): ");
        String choice = scanner.nextLine();
        boolean isOpen = "1".equals(choice);
        sensorController.simulateDoorSensor(isOpen);
    }
}
