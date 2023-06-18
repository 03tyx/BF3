package JOJOLAND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class PearlJam {
    private List<OrderList> waitingList;
    private List<OrderList> orderProcessingList;
    private List<OrderList> orders;
    private int dayNum;
    protected String selectedRestaurant;
    protected String[] menu;
    String residentFilePath = "resources/residents.csv";
    LoadFile loadSystemFile = new LoadFile();
    ArrayList<Resident> resident = loadSystemFile.loadresidentFromFile(residentFilePath);
    ArrayList<ArrayList<OrderList>> residentOrderLists;

    public PearlJam(String selectedRestaurant, int daynum, ArrayList<ArrayList<OrderList>> residentOrderLists) {
        waitingList = new ArrayList<>();
        orderProcessingList = new ArrayList<>();
        orders = new ArrayList<>();
        this.selectedRestaurant = selectedRestaurant;
        this.residentOrderLists = residentOrderLists;
        this.dayNum = daynum;
        sortOrdersWithinRestaurants();
    }
    
    public List<OrderList> getOrders(){
        waitingList.clear(); // Clear the waitingList before populating it again
        for (int i = 0; i < resident.size(); i++) {
            ArrayList<OrderList> orderList = residentOrderLists.get(i);
            for(int j = 0; j < orderList.size(); j++){
                if(orderList.get(j).getDayNum() == dayNum){ //check for orders on current day
                    if(orderList.get(j).getRestaurant().equals(selectedRestaurant)){ //only check for orders at selectedREstaurant
                        waitingList.add(orderList.get(j));
                    }
                }
            }
        }
        return waitingList;
    }


    // Sort the waiting list by arrival time in ascending order
    public List<OrderList> sortWaitingList() {
        waitingList = getOrders();
        Collections.sort(waitingList, Comparator.comparing(OrderList::getArrivalTime));
        return waitingList;
    }
    
    
    // Process orders for Jade Garden restaurant
    private void processJadeGardenOrders() {
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
    private void processCafeDeuxMagotsOrders() {
        orderProcessingList.clear();
        waitingList = sortWaitingList();
        List<OrderList> orderedList = new ArrayList<>(waitingList);

        // Separate orders with known and unknown age
        List<OrderList> orderedWithAge = new ArrayList<>();
        List<OrderList> orderedWithoutAge = new ArrayList<>();

        for (OrderList order : orderedList) {
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
    private void processTrattoriaTrussardiOrders() {
    orderProcessingList.clear();
    waitingList = sortWaitingList();
    List<OrderList> males = new ArrayList<>();
    List<OrderList> females = new ArrayList<>();
    List<OrderList> unspecified = new ArrayList<>();

    // Categorize customers based on gender and age
    for (OrderList customer : waitingList) {
        if (customer.getAge().equalsIgnoreCase("N/A")) {
            unspecified.add(customer);
        } else if (customer.getGender().equalsIgnoreCase("Male")) {
            males.add(customer);
        } else if (customer.getGender().equalsIgnoreCase("Female")) {
            females.add(customer);
        }
    }

    // Sort the categorized lists by age in ascending order
    males.sort(Comparator.comparing(OrderList::getAge, Comparator.nullsLast(Comparator.naturalOrder())));
    females.sort(Comparator.comparing(OrderList::getAge, Comparator.nullsLast(Comparator.naturalOrder())));

    // Process orders based on the specified serving pattern
    while (!males.isEmpty() || !females.isEmpty()) {
        if (!males.isEmpty()) {
            OrderList youngestMale = males.get(0);
            orderProcessingList.add(youngestMale);
            males.remove(0);
        }

        if (!females.isEmpty()) {
            OrderList oldestFemale = females.get(females.size() - 1);
            orderProcessingList.add(oldestFemale);
            females.remove(females.size() - 1);
        }

        if (!males.isEmpty()) {
            OrderList oldestMale = males.get(males.size() - 1);
            orderProcessingList.add(oldestMale);
            males.remove(males.size() - 1);
        }

        if (!females.isEmpty()) {
            OrderList youngestFemale = females.get(0);
            orderProcessingList.add(youngestFemale);
            females.remove(0);
        }
    }

    // Add remaining unspecified customers to the order processing list
    orderProcessingList.addAll(unspecified);
}

    // Process orders for Libeccio restaurant
    public void processLibeccioOrders() {
        orderProcessingList.clear();
        List<OrderList> waitinglist_copy = new ArrayList<>(waitingList);
        Stack<OrderList> reverse = new Stack<>();
        int count = 1;
        while (!waitinglist_copy.isEmpty()) {
            List<OrderList> temp = new ArrayList<>();
            for (int i = 0; i < waitinglist_copy.size(); i++) {
                if (count % dayNum == 0) {
                    reverse.push(waitinglist_copy.get(i));
                    temp.add(waitinglist_copy.get(i));
                }
                count++;
            }
            for (OrderList removed : temp) {
                waitinglist_copy.remove(removed);
            }
            temp.clear();
        }

        while (!reverse.isEmpty()) {
            orderProcessingList.add(reverse.pop());
        }
    }
    
    // Process orders for Savage Garden restaurant
    public void processSavageGardenOrders() {
        orderProcessingList.clear();
        List<OrderList> waitingListCopy = new ArrayList<>(waitingList);
        int queueSize = waitingListCopy.size();
        int count = 1;
        int forwardIndex = 0;
        int reverseIndex = queueSize - 1;

        while (!waitingListCopy.isEmpty()) {
            if (count == dayNum) {
                if (forwardIndex <= reverseIndex) {
                    OrderList matched = waitingListCopy.get(forwardIndex);
                    orderProcessingList.add(matched);
                    waitingListCopy.remove(forwardIndex);
                    reverseIndex--;
                } else {
                    OrderList matched = waitingListCopy.get(reverseIndex);
                    orderProcessingList.add(matched);
                    waitingListCopy.remove(reverseIndex);
                    forwardIndex++;
                }
                count = 1;
                queueSize--;
            } else {
                count++;
                forwardIndex = (forwardIndex + 1) % queueSize;
                reverseIndex = (reverseIndex - 1 + queueSize) % queueSize;
            }
        }
    }

    // Sort orders within each restaurant based on restaurant logic and arrival time(need to be corrected)
    public void sortOrdersWithinRestaurants() {
        waitingList = sortWaitingList();
        Map<String, List<OrderList>> ordersByRestaurant = new HashMap<>();

        for (OrderList order : waitingList) {
            String restaurant = order.getRestaurant();
            ordersByRestaurant.putIfAbsent(restaurant, new ArrayList<>());
            ordersByRestaurant.get(restaurant).add(order);
        }

        for (List<OrderList> restaurantOrders : ordersByRestaurant.values()) {
            // Sort orders based on arrival time
            restaurantOrders.sort(new Comparator<OrderList>() {
                @Override
                public int compare(OrderList order1, OrderList order2) {
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
                    restaurantOrders.sort(new Comparator<OrderList>() {
                        @Override
                        public int compare(OrderList order1, OrderList order2) {
                            if (order1.getDayNum() < order2.getDayNum()) {
                                return -1;
                            } else if (order1.getDayNum() > order2.getDayNum()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    processJadeGardenOrders();
                    break;
                case "Cafe Deux Magots":
                    // Sort Cafe Deux Magots orders based on specific logic
                    restaurantOrders.sort(new Comparator<OrderList>() {
                        @Override
                        public int compare(OrderList order1, OrderList order2) {
                            if (order1.getDayNum() < order2.getDayNum()) {
                                return -1;
                            } else if (order1.getDayNum() > order2.getDayNum()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    processCafeDeuxMagotsOrders();
                    break;
                case "Trattoria Trussardi":
                    // Sort Trattoria Trussardi orders based on specific logic
                    restaurantOrders.sort(new Comparator<OrderList>() {
                        @Override
                        public int compare(OrderList order1, OrderList order2) {
                            if (order1.getDayNum() < order2.getDayNum()) {
                                return -1;
                            } else if (order1.getDayNum() > order2.getDayNum()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    processTrattoriaTrussardiOrders();
                    break;
                case "Libeccio":
                    // Sort Libeccio orders based on specific logic
                    restaurantOrders.sort(new Comparator<OrderList>() {
                        @Override
                        public int compare(OrderList order1, OrderList order2) {
                            if (order1.getDayNum() < order2.getDayNum()) {
                                return -1;
                            } else if (order1.getDayNum() > order2.getDayNum()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    processLibeccioOrders();
                    break;
                case "Savage Garden":
                    // Sort Savage Garden orders based on specific logic
                    restaurantOrders.sort(new Comparator<OrderList>() {
                        @Override
                        public int compare(OrderList order1, OrderList order2) {
                            if (order1.getDayNum() < order2.getDayNum()) {
                                return -1;
                            } else if (order1.getDayNum() > order2.getDayNum()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    processSavageGardenOrders();
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
        for (OrderList customer : waitingList) {
            if (customer.getRestaurant().equals(selectedRestaurant)) {
                found = true;
                break;
            }
        }
        
        if (found) {
            System.out.println("+----+--------------------------+-----+--------+----------------------------------------+");
            System.out.println("| No | Name                     | Age | Gender | Order                                  |");
            System.out.println("+----+--------------------------+-----+--------+----------------------------------------+");
            int count = 1;
            for (OrderList customer : waitingList) {
                if (customer.getRestaurant().equals(selectedRestaurant)) {
                    System.out.printf("| %-2d | %-24s | %-3s | %-6s | %-38s |\n", count, customer.getName(), customer.getAge(), customer.getGender(), customer.getFood());
                    count++;
                }
            }
            System.out.println("+---------------------------------------------------------------------------------------+");
            System.out.println();
        } else {
            System.out.println("No customers in the waiting list for " + selectedRestaurant);
        }
    }

    // Display the order processing list of the selected restaurant

    public void displayOrderProcessingList() {
        System.out.println("Order Processing List for " + selectedRestaurant + ":");
        boolean found = false;
        for (OrderList customer : orderProcessingList) {
            if (customer.getRestaurant().equals(selectedRestaurant)) {
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("+----+--------------------------+-----+--------+----------------------------------------+");
            System.out.println("| No | Name                     | Age | Gender | Order                                  |");
            System.out.println("+----+--------------------------+-----+--------+----------------------------------------+");
            int count = 1;
            for (OrderList customer : orderProcessingList) {
                if (customer.getRestaurant().equals(selectedRestaurant)) {
                    System.out.printf("| %-2d | %-24s | %-3s | %-6s | %-38s |\n", count, customer.getName(), customer.getAge(), customer.getGender(), customer.getFood());
                    count++;
                }
            }
            System.out.println("+---------------------------------------------------------------------------------------+");
            System.out.println("=".repeat(200));
        } else {
            System.out.println("No customers in the order processing list for " + selectedRestaurant);
            System.out.println("=".repeat(200));
        }
    }

}