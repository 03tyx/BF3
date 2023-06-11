/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jojoland;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PearlJam {
    private List<orderList> waitingList;
    private List<orderList> orderProcessingList;
    private List<orderList> orders;
    private int dayNum;
    protected String selectedRestaurant;
    protected String[] menu;
    private HashMap<Customer, LocalDate> customerDiningHistory;
    String residentFilePath = "resources/residents.csv";
    loadFile loadSystemFile = new loadFile();
    ArrayList<resident> resident = loadSystemFile.loadresidentFromFile(residentFilePath);
    ArrayList<ArrayList<orderList>> residentOrderLists;
    //private ArrayList<orderList> orderList = new ArrayList<>();

    public PearlJam(String selectedRestaurant, int daynum) {
        waitingList = new ArrayList<>();
        orderProcessingList = new ArrayList<>();
        orders = new ArrayList<>();
        this.selectedRestaurant = selectedRestaurant;
        customerDiningHistory = new HashMap<>();
        randomOrder ro = new randomOrder(daynum);
        residentOrderLists = ro.randomOrderGenerator();
        this.dayNum = daynum;
        sortOrdersWithinRestaurants();
    }
    
    public List<orderList> getOrders() {
    waitingList.clear(); // Clear the waitingList before populating it again

    for (ArrayList<orderList> orderList : residentOrderLists) {
        if (!orderList.isEmpty()) {
            orderList lastOrder = orderList.get(orderList.size() - 1);
            // Check if the last order is for the selected restaurant and current day
            if (lastOrder.getRestaurant().equals(selectedRestaurant) && lastOrder.getDayNum() == dayNum) {
                waitingList.add(lastOrder);
            }
        }
    }
    return waitingList;
}


    // Sort the waiting list by arrival time in ascending order
    public List<orderList> sortWaitingList() {
        waitingList = getOrders();
        Collections.sort(waitingList, Comparator.comparing(orderList::getArrivalTime));
        return waitingList;
    }
    
    
    // Process orders for Jade Garden restaurant
    private void processJadeGardenOrders(List<orderList> restaurantOrders) {
        orderProcessingList.clear();
        waitingList = sortWaitingList();
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
    private void processCafeDeuxMagotsOrders(List<orderList> restaurantOrders) {
        orderProcessingList.clear();
        waitingList = sortWaitingList();
        List<orderList> orderedList = new ArrayList<>(waitingList);

        // Separate orders with known and unknown age
        List<orderList> orderedWithAge = new ArrayList<>();
        List<orderList> orderedWithoutAge = new ArrayList<>();

        for (orderList order : orderedList) {
            if ("N/A".equals(order.getAge())) {
                orderedWithoutAge.add(order);
            } else {
                orderedWithAge.add(order);
            }
        }

        // Sort the orders with known age based on age (oldest to youngest)
        orderedWithAge.sort(Comparator.comparingInt(order -> {
            String age = order.getAge();
            if ("N/A".equals(age)) {
                return Integer.MAX_VALUE; // Assign a high value to unknown ages
            }
            return Integer.parseInt(age);
        }));

        // Select the oldest and youngest customers in turns until everyone is served
        int left = 0;
        int right = orderedWithAge.size() - 1;

        while (left <= right) {
            orderProcessingList.add(orderedWithAge.get(right));
            if (left != right) {
                orderProcessingList.add(orderedWithAge.get(left));
            }
            left++;
            right--;
        }

        // Add the orders with unknown age to the end of the order processing list
        orderProcessingList.addAll(orderedWithoutAge);
    }


    
    // Process orders for Trattoria Trussardi restaurant
    private void processTrattoriaTrussardiOrders(List<orderList> restaurantOrders) {
        orderProcessingList.clear();
        waitingList = sortWaitingList();
        List<orderList> males = new ArrayList<>();
        List<orderList> females = new ArrayList<>();
        List<orderList> unspecified = new ArrayList<>();

        // Categorize customers based on gender and age
        for (orderList customer : waitingList) {
            if (customer.getAge().equalsIgnoreCase("N/A")) {
                unspecified.add(customer);
            } else if (customer.getGender().equalsIgnoreCase("Male")) {
                males.add(customer);
            } else if (customer.getGender().equalsIgnoreCase("Female")) {
                females.add(customer);
            }
        }

        // Sort the categorized lists by age
        males.sort(Comparator.comparing(orderList::getAge, Comparator.nullsLast(Comparator.naturalOrder())));
        females.sort(Comparator.comparing(orderList::getAge, Comparator.nullsLast(Comparator.naturalOrder())));

        // Process orders based on the alternating pattern
        boolean serveYoungestMan = true;

        while (!males.isEmpty() || !females.isEmpty()) {
            if (serveYoungestMan) {
                if (!males.isEmpty()) {
                    orderList youngestMale = males.get(0);
                    orderProcessingList.add(youngestMale);
                    males.remove(0);
                } else if (!females.isEmpty()) {
                    orderList oldestFemale = females.get(females.size() - 1);
                    orderProcessingList.add(oldestFemale);
                    females.remove(females.size() - 1);
                }
            } else {
                if (!females.isEmpty()) {
                    orderList oldestFemale = females.get(females.size() - 1);
                    orderProcessingList.add(oldestFemale);
                    females.remove(females.size() - 1);
                } else if (!males.isEmpty()) {
                    orderList youngestMale = males.get(0);
                    orderProcessingList.add(youngestMale);
                    males.remove(0);
                }
            }

            serveYoungestMan = !serveYoungestMan;
        }

        // Add remaining unspecified customers to the order processing list
        orderProcessingList.addAll(unspecified);
    }





    // Process orders for Libeccio restaurant
//    private void processLibeccioOrders(List<orderList> restaurantOrders) {
//        orderProcessingList.clear();
//        waitingList = sortWaitingList();
//        int index = dayNum - 1;
//
//        while (!waitingList.isEmpty()) {
//            int customerIndex = index % waitingList.size();
//            orderList customer = waitingList.remove(customerIndex);
//            orderProcessingList.add(customer);
//        }
//    }
//    private void processLibeccioOrders(List<orderList> restaurantOrders) {
//        orderProcessingList.clear();
//        List<orderList> waitingListCopy = new ArrayList<>(sortWaitingList());
//
//        int dayNum = 1;
//        int index = 0;
//        int count = 1;
//
//        while (!waitingListCopy.isEmpty()) {
//            int customerIndex = index % waitingListCopy.size();
//            orderList customer = waitingListCopy.get(customerIndex);
//
//            // Check if the customer's count is a multiple of the current day number
//            if (count % dayNum == 0) {
//                // Remove the customer from the waiting list copy and add them to the order processing list
//                orderProcessingList.add(customer);
//                waitingListCopy.remove(customerIndex);
//
//                // Update the index to point to the next customer in the queue
//                if (customerIndex < waitingListCopy.size()) {
//                    index = customerIndex;
//                } else {
//                    index = 0;
//                }
//            } else {
//                // Move to the next customer in the queue
//                index++;
//            }
//
//            count++; // Increment the count
//            dayNum++; // Increment the current day number
//        }
//    }
    private void processLibeccioOrders(List<orderList> restaurantOrders) {
        orderProcessingList.clear();
        List<orderList> waitingListCopy = new ArrayList<>(sortWaitingList());

        int dayNum = 1;
        int index = 0;
        int count = 1;

        while (!waitingListCopy.isEmpty()) {
            int customerIndex = index % waitingListCopy.size();
            orderList customer = waitingListCopy.get(customerIndex);

            // Check if the customer's count is a multiple of the current day number
            if (count % dayNum == 0) {
                // Remove the customer from the waiting list copy and add them to the order processing list
                orderProcessingList.add(customer);
                waitingListCopy.remove(customerIndex);

                // Update the index to point to the next customer in the queue
                if (customerIndex < waitingListCopy.size()) {
                    index = customerIndex;
                } else {
                    index = 0;
                }

                // Reset count and dayNum for the next iteration
                count = 1;
                dayNum = 1;
            } else {
                // Move to the next customer in the queue
                index++;

                // Increment the count
                count++;
            }
        }
    }




    // Process orders for Savage Garden restaurant
//    private void processSavageGardenOrders(List<orderList> restaurantOrders) {
//        orderProcessingList.clear();
//        waitingList = sortWaitingList();
//        int index = dayNum - 1;
//
//        while (!waitingList.isEmpty()) {
//            int customerIndex = index % waitingList.size();
//            orderList customer = waitingList.remove(customerIndex);
//            orderProcessingList.add(customer);
//            index++;
//        }
//    }
    private void processSavageGardenOrders(List<orderList> restaurantOrders) {
        orderProcessingList.clear();
        List<orderList> waitingListCopy = new ArrayList<>(sortWaitingList());

        int dayNum = 1;
        int index = 0;

        while (!waitingListCopy.isEmpty()) {
            int customerIndex = index % waitingListCopy.size();
            orderList customer = waitingListCopy.get(customerIndex);

            // Check if the customer's count matches the current day number
            if (customerIndex + 1 == dayNum) {
                // Remove the customer from the waiting list copy and add them to the order processing list
                orderProcessingList.add(customer);
                waitingListCopy.remove(customerIndex);

                // Reset the index and dayNum for the next iteration
                index = 0;
                dayNum = 1;
            } else {
                // Move to the next customer in the queue
                index++;

                // If we reach the end of the queue, start over from the last person and move in reverse order
                if (index >= waitingListCopy.size()) {
                    index = waitingListCopy.size() - 1;
                    dayNum++;
                }
            }
        }
    }

    // Sort orders within each restaurant based on restaurant logic and arrival time(need to be corrected)
    public void sortOrdersWithinRestaurants() {
        waitingList = sortWaitingList();
        Map<String, List<orderList>> ordersByRestaurant = new HashMap<>();

        for (orderList order : waitingList) {
            String restaurant = order.getRestaurant();
            ordersByRestaurant.putIfAbsent(restaurant, new ArrayList<>());
            ordersByRestaurant.get(restaurant).add(order);
        }

        for (List<orderList> restaurantOrders : ordersByRestaurant.values()) {
            // Sort orders based on arrival time
            restaurantOrders.sort(new Comparator<orderList>() {
                @Override
                public int compare(orderList order1, orderList order2) {
                    if (order1.getArrivalTime() < order2.getArrivalTime()) {
                        return -1;
                    } else if (order1.getArrivalTime() > order2.getArrivalTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            // Sort customers based on restaurant logic
            String restaurantName = restaurantOrders.get(0).getRestaurant();
            switch (restaurantName) {
                case "Jade Garden":
                    // Sort Jade Garden orders based on specific logic
                    restaurantOrders.sort(new Comparator<orderList>() {
                        @Override
                        public int compare(orderList order1, orderList order2) {
                            if (order1.getDayNum() < order2.getDayNum()) {
                                return -1;
                            } else if (order1.getDayNum() > order2.getDayNum()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    processJadeGardenOrders(restaurantOrders);
                    break;
                case "Cafe Deux Magots":
                    // Sort Cafe Deux Magots orders based on specific logic
                    restaurantOrders.sort(new Comparator<orderList>() {
                        @Override
                        public int compare(orderList order1, orderList order2) {
                            if (order1.getDayNum() < order2.getDayNum()) {
                                return -1;
                            } else if (order1.getDayNum() > order2.getDayNum()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    processCafeDeuxMagotsOrders(restaurantOrders);
                    break;
                case "Trattoria Trussardi":
                    // Sort Trattoria Trussardi orders based on specific logic
                    restaurantOrders.sort(new Comparator<orderList>() {
                        @Override
                        public int compare(orderList order1, orderList order2) {
                            if (order1.getDayNum() < order2.getDayNum()) {
                                return -1;
                            } else if (order1.getDayNum() > order2.getDayNum()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    processTrattoriaTrussardiOrders(restaurantOrders);
                    break;
                case "Libeccio":
                    // Sort Libeccio orders based on specific logic
                    restaurantOrders.sort(new Comparator<orderList>() {
                        @Override
                        public int compare(orderList order1, orderList order2) {
                            if (order1.getDayNum() < order2.getDayNum()) {
                                return -1;
                            } else if (order1.getDayNum() > order2.getDayNum()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    processLibeccioOrders(restaurantOrders);
                    break;
                case "Savage Garden":
                    // Sort Savage Garden orders based on specific logic
                    restaurantOrders.sort(new Comparator<orderList>() {
                        @Override
                        public int compare(orderList order1, orderList order2) {
                            if (order1.getDayNum() < order2.getDayNum()) {
                                return -1;
                            } else if (order1.getDayNum() > order2.getDayNum()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    processSavageGardenOrders(restaurantOrders);
                    break;
                default:
                    // No specific sorting logic for unknown restaurants
                    break;
            }
        }
    }


    // Display the waiting list of the selected restaurant
    public void displayWaitingList() {
        waitingList = sortWaitingList();
        System.out.println("Waiting List for " + selectedRestaurant + ":");
        boolean found = false;
        for (orderList customer : waitingList) {
            if (customer.getRestaurant().equals(selectedRestaurant)) {
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("+----+--------------------------+-----+--------+-");
            System.out.println("| No | Name                     | Age | Gender |");
            System.out.println("+----+--------------------------+-----+--------+-");
            int count = 1;
            for (orderList customer : waitingList) {
                if (customer.getRestaurant().equals(selectedRestaurant)) {
                    System.out.printf("| %-2d | %-25s | %-3s | %-6s |\n", count, customer.getName(), customer.getAge(), customer.getGender());
                    count++;
                }
            }
            System.out.println("+----+-------------------------+-----+----------+-");
            System.out.println("-+----------------------------------------+");
            System.out.println("| Order                                   |");
            System.out.println("-+----------------------------------------+");
            for (orderList customer : waitingList) {
                System.out.printf("| %-38s |\n", customer.getFood());
            }
            System.out.println("-+--------------------------------------------+");
        } else {
            System.out.println("No customers in the waiting list for " + selectedRestaurant);
        }
    }

    // Display the order processing list of the selected restaurant
    //orderList customer = new customerPearlJam("John Doe", 30, "Male", 10, "Jade Garden", "Chicken Curry");

    public void displayOrderProcessingList() {
        System.out.println("Order Processing List for " + selectedRestaurant + ":");
        boolean found = false;
        for (orderList customer : orderProcessingList) {
            if (customer.getRestaurant().equals(selectedRestaurant)) {
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("+----+----------------------------+-----+--------+-");
            System.out.println("| No | Name                       | Age | Gender |");
            System.out.println("+----+----------------------------+-----+--------+-");
            int count = 1;
            for (orderList customer : orderProcessingList) {
                if (customer.getRestaurant().equals(selectedRestaurant)) {
                    System.out.printf("| %-2d | %-25s | %-3s | %-6s |\n", count, customer.getName(), customer.getAge(), customer.getGender());
                    count++;
                }
            }
            System.out.println("+----+-----------------------+-----+----------+-");
            System.out.println("-+----------------------------------------+");
            System.out.println("| Order                                   |");
            System.out.println("-+----------------------------------------+");
            for (orderList customer : orderProcessingList) {
                System.out.printf("| %-38s |\n", customer.getFood());
            }
            System.out.println("-+--------------------------------------------+");
        } else {
            System.out.println("No customers in the order processing list for " + selectedRestaurant);
        }
    }

}
