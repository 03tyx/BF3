/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JOJOLand;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Map extends Graph {
    public static Graph<String, Integer> map;
    public static Scanner sc = new Scanner(System.in);
    
    public Map(){
        map = new Graph();
    }
    
    public static void chooseMap(int num){
        switch (num) {
            case 1:
                {
                    //creating default map
                    
                    String[] location = {"Town Hall", "Cafe Deux Magots", "Polnareff Land",
                        "Savage Garden", "Vineyard", "Joestar Mansion", "Jade Garden",
                        "Morioh Grand Hotel", "San Giorgio Maggiore", "Trattoria Trussardi",
                        "Libeccio", "DIO's Mansion", "Angelo Rock", "Green Dolphin Street Prison"};
                    
                    //adding vertices into the map
                    for(String i : location)
                        map.addVertex(i);
                    
                    //adding edges between the vertices
                    try{
                        Scanner sc = new Scanner(new FileInputStream("resources/DefaultMap.txt"));
                        while(sc.hasNextLine()){
                            String s = sc.nextLine();
                            String[] edge = s.split(",");
                            map.addEdge(edge[0], edge[1], Integer.valueOf(edge[2]));
                        }
                    }catch(FileNotFoundException e){
                        System.out.println("File Not Found");
                    }       break;
                }
            case 2:
                {
                    //creating parallel map
                    
                    String[] location = {"Town Hall", "Cafe Deux Magots", "Polnareff Land",
                        "Savage Garden", "Vineyard", "Joestar Mansion", "Jade Garden",
                        "Morioh Grand Hotel", "San Giorgio Maggiore", "Trattoria Trussardi",
                        "Libeccio", "DIO's Mansion", "Angelo Rock", "Green Dolphin Street Prison"};
                    
                    //adding vertices into the map
                    for(String i : location)
                        map.addVertex(i);
                    
                    //adding edges between the vertices
                    try{
                        Scanner sc = new Scanner(new FileInputStream("resources/ParallelMap.txt"));
                        while(sc.hasNextLine()){
                            String s = sc.nextLine();
                            String[] edge = s.split(",");
                            map.addEdge(edge[0], edge[1], Integer.valueOf(edge[2]));
                        }
                    }catch(FileNotFoundException e){
                        System.out.println("File not found");
                    }       break;
                }
            case 3:
                {
                    //creating alternate map
                    
                    String[] location = {"Town Hall", "Cafe Deux Magots", "Polnareff Land",
                        "Savage Garden", "Vineyard", "Joestar Mansion", "Jade Garden",
                        "Morioh Grand Hotel", "San Giorgio Maggiore", "Trattoria Trussardi",
                        "Passione Restaurant", "DIO's Mansion", "Angelo Rock", "Green Dolphin Street Prison"};
                    
                    //adding vertices into the map
                    for(String i : location)
                        map.addVertex(i);
                    
                    //adding edges between the vertices
                    try{
                        Scanner sc = new Scanner(new FileInputStream("resources/AlternateMap.txt"));
                        while(sc.hasNextLine()){
                            String s = sc.nextLine();
                            String[] edge = s.split(",");
                            map.addEdge(edge[0], edge[1], Integer.valueOf(edge[2]));
                        }
                    }catch(FileNotFoundException e){
                        System.out.println("File not found");
                    }       break;
                }
            default:
                break;
        }
    }
    
    //method to move to adjacent location
    public static void move(){
        start.history.add(start.currentLocation);
        System.out.println("Moving to: ");
        List<String> neighbours = map.getNeighbours(start.currentLocation);
        for(int i = 0; i < neighbours.size(); i++){
            System.out.printf("[%d] %s\n", i, neighbours.get(i));
        }
        System.out.print("\nSelect: ");
        int input = sc.nextInt();
        start.currentLocation = neighbours.get(input);
        System.out.print("======================================================================");

        //to check if forward location is same as the location before backtrack
        if(!start.forward.empty() && !start.forward.pop().equals(start.currentLocation)){
            start.history.pop();
            start.history.pop();
            start.forward.clear();
        }else if(!start.forward.empty() && start.forward.pop().equals(start.currentLocation)){
            start.history.pop();
            start.forward.clear();
        }
        
    }

}
