/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jojoland;

import java.util.ArrayList;

/**
 *
 * @author 22004818
 */
public class profile{
    private String name;
    
    public profile(String name){
        this.name = name;
    }
    
    public boolean residentProfile(String residentFilePath, String standFilePath){

        loadFile loadSystemFile = new loadFile();
        ArrayList<resident> resident = loadSystemFile.loadresidentFromFile(residentFilePath);
        ArrayList<stand> stand = loadSystemFile.loadstandFromFile(standFilePath);
        //check name and print profile details
        boolean containName = false;
        for(int i=0;i<resident.size();i++){
            if((resident.get(i).getName()).equals(name)){
                    System.out.println(name + "'s Profile");
                    System.out.println("Name\t: " + resident.get(i).getName() + "\n" 
                        + "Age\t: " + resident.get(i).getAge() + "\n" 
                        + "Gender\t: " + resident.get(i).getGender() + "\n"
                        + "Parents\t: " + resident.get(i).getParents());
                    containName = true;
               }
        }
        
        //check name if have stand
        for(int i=0;i<stand.size();i++){
            if((stand.get(i).getStandUser()).equals(name)){
                    System.out.println("Stand\t\t\t: " + stand.get(i).getStand() + "\n" 
                        + "Destructive Power\t: " + stand.get(i).getDestructivePower() + "\n" 
                        + "Speed\t\t\t: " + stand.get(i).getSpeed() + "\n"
                        + "Range\t\t\t: " + stand.get(i).getRange() + "\n" 
                        + "Stamina\t\t\t: " + stand.get(i).getStamina() + "\n" 
                        + "Precision\t\t: " + stand.get(i).getPrecision() + "\n" 
                        + "Development Potential\t: " + stand.get(i).getDevelopmentPotential());
               }
        }
        return containName;
    }
    
   
}
