package game.entities;

import game.controller.Game;
import game.controller.GameException;

import java.util.*;

public class Animal {
    transient ArrayList<Integer> cooperateTo=new ArrayList<>(); //default 0
    transient ArrayList<Integer> communicateTo=new ArrayList<>();
    transient ArrayList<Integer> symbiontFor=new ArrayList();
    transient ArrayList<Integer> symbiosys=new ArrayList<>();
    transient String owner;
    transient boolean fedFlag=false;

    //go to json
    List<String> propertyList = new ArrayList<>();
    int id;
    int totalHungry = 1;
    int currentHungry=totalHungry;

    int fatSupply;
    int currentFatSupply;
    String cooperateWith;
    String communicateWith;
    String symbiont;
    String symbiosisWith;



    public Animal(int id, String player) {
        this.id = id;
        owner = player;
    }

    public void addProperty(String property) throws GameException {

        if (property.equals("Scavenger") && propertyList.contains("Predator"))
            throw new GameException("Predator cannot be a scavenger");

        else if (property.equals("Predator") && propertyList.contains("Scavenger"))
            throw new GameException("Scavenger cannot be a predator");

        else if (!(property.equals("Fat")) && propertyList.contains(property))
            throw new GameException("This animal already has property: " + property);

        else if (property.equals("Fat")) {fatSupply++; return;} //do not put in property list

        propertyList.add(property);

        if (property.equals("Predator") || property.equals("Big")) totalHungry++;
        if (property.equals("Parasite")) totalHungry += 2;
    }

    public String getOwner() {
        return owner;
    }

    public void eatMeet(Player player) throws GameException {

        if (currentHungry==0) throw new GameException("This animal is fed!");
        if (!checkSymbiosis(player)) throw new GameException("You should feed the symbiont first"); ;
        fedFlag=true;
        currentHungry--;
        for (int id:cooperateTo
             ) {
            player.getAnimal(id).eatFish(player);
        }
    }

    public void eatFish(Player player) {
        if (currentHungry==0 || fedFlag || !(checkSymbiosis(player))) return; //abort dfs if animal is fed, is already visited or can't get fish

        fedFlag=true;
        currentHungry--;
        for (int id:cooperateTo
                ) {
            player.getAnimal(id).eatFish(player);
        }
    }

    public boolean checkSymbiosis(Player player) { //return false if not all of the symbionts are fed;
        if (!symbiosys.isEmpty()) {
            for (int id : symbiosys
                    ) {
                if (player.getAnimal(id).currentHungry != 0)
                    return false;

            }
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public boolean isHungry(){
        return currentHungry!=0;
    }

    public boolean isCommunicate(int id){
        return communicateTo.contains(id);
    }

    public boolean isCooperate(int id){
        return cooperateTo.contains(id);
    }

    public boolean isInSymbiosis(int id){
        return symbiosys.contains(id);
    }

    public boolean isSymbiontFor(int id){
        return symbiontFor.contains(id);
    }

    public void setCommunicateTo(int id){
        communicateTo.add(id);
        communicateWith=communicateTo.toString();
    }

    public void setCooperateTo(int id){
        cooperateTo.add(id);
        cooperateWith=cooperateTo.toString();
    }

    public void setSymbiosysWith(int id){
        symbiosys.add(id);
        symbiosisWith=symbiosys.toString();
    }

    public void setSymbiontFor(int id){
        symbiontFor.add(id);
        symbiont=symbiontFor.toString();
    }

    public boolean hasProperty(String property){
        return propertyList.contains(property);
    }



}
