/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JOJOLand;

public class Customer {
    private int number;
    private String name;
    private int age;
    private String gender;
    private int arrivalTime;
    private String restaurantName;
    private String order;
    public boolean hasDinedIn;

    public Customer(int number, String name, int age, String gender, int arrivalTime, String restaurantName, String order) {
        this.number = number;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.arrivalTime = arrivalTime;
        this.restaurantName = restaurantName;
        this.order = order;
        this.hasDinedIn = false;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
    
    public String getRestaurantName() {
        return restaurantName;
    }
    
    public String getOrder() {
        return order;
    }
    
    public boolean hasDinedIn(){
        return hasDinedIn;
    }
    
    public void setHasDinedIn(boolean hasDinedIn){
        this.hasDinedIn = hasDinedIn;
    }
}
