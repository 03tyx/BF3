/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JOJOLand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.time.LocalTime;

public class PearlJam {
    private List<Customer> waitingList;
    private List<Customer> orderProcessingList;
    private List<Customer> orders;
    private int dayNum;
    private String selectedRestaurant;
    private String[] menu;

    public PearlJam(String selectedRestaurant) {
        waitingList = new ArrayList<>();
        orderProcessingList = new ArrayList<>();
        orders = new ArrayList<>();
        this.selectedRestaurant = selectedRestaurant;
        setMenu();
        setProcessOrders();
    }
    
    public void setMenu(){
        switch (selectedRestaurant) {
            case "Jade Garden":
                menu = new String[]{
                    "Braised Chicken in Black Bean Sauce ($15.00)",
                    "Braised Goose Web with Vermicelli ($21.00)",
                    "Deep-fried Hiroshima Oysters ($17.00)",
                    "Poached Tofu with Dried Shrimps ($12.00)",
                    "Scrambled Egg White with Milk ($10.00)"
                };
                break;
            case "Cafe Deux Magots":
                menu = new String[]{
                    "Sampling Matured Cheese Platter ($23.00)",
                    "Spring Lobster Salad ($35.00)",
                    "Spring Organic Omelette ($23.00)",
                    "Truffle-flavoured Poultry Supreme ($34.00)",
                    "White Asparagus ($26.00)"
                };
                break;
            case "Trattoria Trussardi":
                menu = new String[]{
                    "Caprese Salad ($10.00)",
                    "Creme caramel ($6.50)",
                    "Lamb Chops with Apple Sauce ($25.00)",
                    "Spaghetti alla Puttanesca ($15.00)"
                };
                break;
            case "Libeccio":
                menu = new String[]{
                    "Formaggio ($12.50)",
                    "Ghiaccio ($1.01))",
                    "Melone ($5.20)",
                    "Prosciutto and Pesci ($20.23)",
                    "Risotto ($13.14)",
                    "Zucchero and Sale ($0.60)"
                };
                break;
            case "Savage Garden":
                menu = new String[]{
                    "Abbacchio’s Tea ($1.00)",
                    "DIO’s Bread ($36.14)",
                    "Giorno’s Donuts ($6.66)",
                    "Joseph’s Tequila ($35.00)",
                    "Kakyoin’s Cherry ($3.50)",
                    "Kakyoin’s Porridge ($4.44)"
                };
                break;
            default:
                break;
        }
                
    }

    public void serveCustomers(Customer[] customers) {
        // Check if it's within the allowed serving time (13 PM to 14)
        LocalTime currentTime = LocalTime.now();
        LocalTime servingStartTime = LocalTime.of(13, 0);
        LocalTime servingEndTime = LocalTime.of(14, 0);

        if (currentTime.isBefore(servingStartTime) || currentTime.isAfter(servingEndTime)) {
            System.out.println("Sorry, the restaurant is currently not serving customers.");
            return;
        } else {
            System.out.println("Welcome!");
        }
            // Serve each customer
        for (Customer customer : customers) {
            // Check if the customer has already dined in today
            boolean hasDinedIn = false;
            for (Customer servedCustomer : orders) {
                if (servedCustomer.getName().equals(customer.getName())) {
                    hasDinedIn = true;
                    break;
                }
            }

            if (hasDinedIn) {
                System.out.println("Customer " + customer.getName() + " has already dined in today. Cannot serve again.");
            } 
        }
    }

    
    public String selectMenu() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select a menu for " + selectedRestaurant + ":");
        displayMenu();

        System.out.print("Select the menu item: ");
        int menuNum = sc.nextInt();

        // Validate the menu index
        if (menuNum < 1 || menuNum > menu.length) {
            System.out.println("Invalid menu number. Please try again.");
            selectMenu();
            return selectMenu();
        }

        // Get the selected menu item
        String selectedMenuItem = menu[menuNum - 1];
        System.out.println("You selected: " + selectedMenuItem);
        return selectedMenuItem;
    }

    // Add a customer to the waiting list
    public void addToWaitingList(Customer[] customers) {
        for(Customer customer : customers){
            waitingList.add(customer);
        }
        
    }

    // Sort the waiting list by arrival time in ascending order
    public void sortWaitingList() {
        Collections.sort(waitingList, Comparator.comparing(Customer::getArrivalTime));
    }

    // Process the orders based on the restaurant's rule
    public void setProcessOrders() {
        orderProcessingList.clear();
        orders.clear();

        switch (selectedRestaurant) {
            case "Jade Garden":
                processJadeGardenOrders();
                break;
            case "Cafe Deux Magots":
                processCafeDeuxMagotsOrders();
                break;
            case "Trattoria Trussardi":
                processTrattoriaTrussardiOrders();
                break;
            case "Libeccio":
                processLibeccioOrders();
                break;
            case "Savage Garden":
                processSavageGardenOrders();
                break;
            default:
                break;
        }
    }

    // Process orders for Jade Garden restaurant
    private void processJadeGardenOrders() {
        int left = 0;
        int right = waitingList.size() - 1;

        while (left <= right) {
            orderProcessingList.add(waitingList.get(left));
            if (left != right) {
                orderProcessingList.add(waitingList.get(right));
            }
            left++;
            right--;
        }
    }

    // Process orders for Cafe Deux Magots restaurant
    private void processCafeDeuxMagotsOrders() {
        List<Customer> orderedList = new ArrayList<>(waitingList);
        Collections.sort(orderedList, Comparator.comparing(Customer::getAge).reversed());

        int left = 0;
        int right = orderedList.size() - 1;

        while (left <= right) {
            orderProcessingList.add(orderedList.get(left));
            if (left != right) {
                orderProcessingList.add(orderedList.get(right));
            }
            left++;
            right--;
        }
    }

    // Process orders for Trattoria Trussardi restaurant
    private void processTrattoriaTrussardiOrders() {
        List<Customer> males = new ArrayList<>();
        List<Customer> females = new ArrayList<>();
        List<Customer> unspecified = new ArrayList<>();

        // Categorize customers based on gender and age
        for (Customer customer : waitingList) {
            if (customer.getGender().equalsIgnoreCase("Male")) {
                males.add(customer);
            } else if (customer.getGender().equalsIgnoreCase("Female")) {
                females.add(customer);
            } else {
                unspecified.add(customer);
            }
        }

        // Process orders based on the rule
        while (!males.isEmpty() || !females.isEmpty()) {
            if (!males.isEmpty()) {
                Customer youngestMale = Collections.min(males, Comparator.comparing(Customer::getAge));
                Customer oldestMale = Collections.max(males, Comparator.comparing(Customer::getAge));

                orderProcessingList.add(youngestMale);
                orderProcessingList.add(oldestMale);

                males.remove(youngestMale);
                males.remove(oldestMale);
            }

            if (!females.isEmpty()) {
                Customer oldestFemale = Collections.max(females, Comparator.comparing(Customer::getAge));
                Customer youngestFemale = Collections.min(females, Comparator.comparing(Customer::getAge));

                orderProcessingList.add(oldestFemale);
                orderProcessingList.add(youngestFemale);

                females.remove(oldestFemale);
                females.remove(youngestFemale);
            }
        }

        // Add remaining unspecified customers to the order processing list
        orderProcessingList.addAll(unspecified);
    }

    // Process orders for Libeccio restaurant
    private void processLibeccioOrders() {
        int index = dayNum - 1;

        while (!waitingList.isEmpty()) {
            int customerIndex = index % waitingList.size();
            Customer customer = waitingList.remove(customerIndex);
            orderProcessingList.add(customer);
        }
    }

    // Process orders for Savage Garden restaurant
    private void processSavageGardenOrders() {
        int index = dayNum - 1;

        while (!waitingList.isEmpty()) {
            int customerIndex = index % waitingList.size();
            Customer customer = waitingList.remove(customerIndex);
            orderProcessingList.add(customer);
            index++;
        }
    }

    // Display the waiting list of the selected restaurant
    public void displayWaitingList() {
        System.out.println("Waiting List for " + selectedRestaurant + ":");
        boolean found = false;
        for (Customer customer : waitingList) {
            if (customer.getRestaurantName().equals(selectedRestaurant)) {
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("+----+--------------------+-----+--------+-");
            System.out.println("| No | Name               | Age | Gender |");
            System.out.println("+----+--------------------+-----+--------+-");
            int count = 1;
            for (Customer customer : waitingList) {
                if (customer.getRestaurantName().equals(selectedRestaurant)) {
                    System.out.printf("| %-2d | %-18s | %-3d | %-6s |\n", count, customer.getName(), customer.getAge(), customer.getGender());
                    count++;
                }
            }
            System.out.println("+----+--------------------+-----+----------+-");
            System.out.println("-+----------------------------------------+");
            System.out.println("| Order                                  |");
            System.out.println("-+----------------------------------------+");
            for (Customer customer : waitingList) {
                System.out.printf("| %-38s |\n", customer.getOrder());
            }
            System.out.println("-+-----------------------------------------+");
        } else {
            System.out.println("No customers in the waiting list for " + selectedRestaurant);
        }
    }

    // Display the order processing list of the selected restaurant
    public void displayOrderProcessingList() {
        System.out.println("Order Processing List for " + selectedRestaurant + ":");
        boolean found = false;
        for (Customer customer : orderProcessingList) {
            if (customer.getRestaurantName().equals(selectedRestaurant)) {
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("+----+--------------------+-----+--------+-");
            System.out.println("| No | Name               | Age | Gender |");
            System.out.println("+----+--------------------+-----+--------+-");
            int count = 1;
            for (Customer customer : orderProcessingList) {
                if (customer.getRestaurantName().equals(selectedRestaurant)) {
                    System.out.printf("| %-2d | %-18s | %-3d | %-6s |\n", count, customer.getName(), customer.getAge(), customer.getGender());
                    count++;
                }
            }
            System.out.println("+----+--------------------+-----+----------+-");
            System.out.println("-+----------------------------------------+");
            System.out.println("| Order                                  |");
            System.out.println("-+----------------------------------------+");
            for (Customer customer : orderProcessingList) {
                System.out.printf("| %-38s |\n", customer.getOrder());
            }
            System.out.println("-+-----------------------------------------+");
        } else {
            System.out.println("No customers in the order processing list for " + selectedRestaurant);
        }
    }

    
    public void displayMenu() {
        if (selectedRestaurant.equals("Jade Garden")) {
            // Display menu for Jade Garden
            System.out.println("Menu for Jade Garden:");
            System.out.println("[1] Braised Chicken in Black Bean Sauce ($15.00)");
            System.out.println("[2] Braised Goose Web with Vermicelli ($21.00)");
            System.out.println("[3] Deep-fried Hiroshima Oysters ($17.00)");
            System.out.println("[4] Poached Tofu with Dried Shrimps ($12.00)");
            System.out.println("[5] Scrambled Egg White with Milk ($10.00)");
        } else if (selectedRestaurant.equals("Trattoria Trussardi")) {
            // Display menu for Trattoria Trussardi
            System.out.println("Menu for Trattoria Trussardi:");
            System.out.println("[1] Caprese Salad ($10.00)");
            System.out.println("[2] Creme caramel ($6.50)");
            System.out.println("[3] Lamb Chops with Apple Sauce ($25.00)");
            System.out.println("[4] Spaghetti alla Puttanesca ($15.00)");
        } else if (selectedRestaurant.equals("Cafe Deux Magots")) {
            // Display menu for Cafe Deux Magots
            System.out.println("Menu for Cafe Deux Magots:");
            System.out.println("[1] Sampling Matured Cheese Platter ($23.00)");
            System.out.println("[2] Spring Lobster Salad ($35.00)");
            System.out.println("[3] Spring Organic Omelette ($23.00)");
            System.out.println("[4] Truffle-flavoured Poultry Supreme ($34.00)");
            System.out.println("[5] White Asparagus ($26.00)");
        } else if (selectedRestaurant.equals("Libeccio")) {
            // Display menu for Libeccio
            System.out.println("Menu for Libeccio:");
            System.out.println("[1] Formaggio ($12.50)");
            System.out.println("[2] Ghiaccio ($1.01)");
            System.out.println("[3] Melone ($5.20)");
            System.out.println("[4] Prosciutto and Pesci ($20.23)");
            System.out.println("[5] Risotto ($13.14)");
            System.out.println("[6] Zucchero and Sale ($0.60)");
        } else if (selectedRestaurant.equals("Savage Garden")) {
            // Display menu for Savage Garden
            System.out.println("Menu for Savage Garden:");
            System.out.println("[1] Abbacchio’s Tea ($1.00)");
            System.out.println("[2] DIO’s Bread ($36.14)");
            System.out.println("[3] Giorno’s Donuts ($6.66)");
            System.out.println("[4] Joseph’s Tequila ($35.00)");
            System.out.println("[5] Kakyoin’s Cherry ($3.50)");
            System.out.println("[6] Kakyoin’s Porridge ($4.44)");
        } else {
            System.out.println("Menu not available for the specified restaurant.");
        }
    }
}
