package game.entities;

import game.controller.Game;
import game.controller.GameException;

import java.util.*;

public class Animal {
    transient ArrayList<Integer> cooperateTo=new ArrayList<>(); //default 0
    transient ArrayList<Integer> communicateTo=new ArrayList<>();
    transient ArrayList<Integer> symbiontFor=new ArrayList<>();
    transient ArrayList<Integer> symbiosys=new ArrayList<>();
    transient Player owner;
    transient boolean attackFlag=false;
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

    public Animal(int id, Player player) {
        this.id = id;
        owner = player;
    }

    public boolean attack(Animal victim) throws GameException{
        if (attackFlag) throw new GameException("This predator has been used");

        if (victim.hasProperty("Swimming")){
            if (!hasProperty("Swimming")) throw new GameException("Not-swimming predator can't eat swimming animal");
        }

        if (hasProperty("Swimming")){
            if (!victim.hasProperty("Swimming")) throw new GameException("Swimming predator can't eat non-swimming animal");
        }

        if (victim.hasProperty("Poisonous")) {}
        if (victim.hasProperty("Mimicry")) {}

        if (victim.hasProperty("Big")){
            if (!hasProperty("Big")) throw new GameException("You can't eat such a big animal");
        }

        if (!victim.symbiosys.isEmpty()) throw new GameException("You can't eat this animal while its symbiont is alive");
        if (victim.hasProperty("Burrowing") && victim.currentHungry==0) throw new GameException("This animal is fed and in burrow");
        if (victim.hasProperty("Camouflage")){
            if (!hasProperty("Sharp Vision")) throw new GameException("This animal is in camouflage");
        }

        if (victim.hasProperty("Running")){
            boolean flag=new Random().nextBoolean();
            return flag;
        }

        if (victim.hasProperty("Tail loss")){} //what to do?

        if (currentHungry>=2){
            fedFlag=true;
            currentHungry-=2;
            for (int id:cooperateTo
                    ) {
                owner.getAnimal(id).eatFish(2);
            }
        } else if (currentHungry==1){
            fedFlag=true;
            currentHungry--;
            for (int id:cooperateTo
                    ) {
                owner.getAnimal(id).eatFish(1);
            }
        }

        attackFlag=true;
        return true;

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

    public Player getOwner() {
        return owner;
    }

    public void eatMeet(Player player, Game game) throws GameException {

        if (currentHungry==0) throw new GameException("This animal is fed!");
        if (!checkSymbiosis(player)) throw new GameException("You should feed the symbiont first");

        fedFlag=true;
        currentHungry--;
        game.deleteFood();
        for (int id:cooperateTo
             ) {
            player.getAnimal(id).eatFish(1);
        }

        for (int id:communicateTo){
            player.getAnimal(id).eatExtraMeet(player,game);
        }
    }

    public void eatExtraMeet(Player player, Game game){
        if (game.getFood()==0) return;
        if (currentHungry==0 || fedFlag || !(checkSymbiosis(owner))) return; //abort dfs if animal is fed, is already visited or can't get fish

        fedFlag=true;
        currentHungry--;
        game.deleteFood();
        for (int id:communicateTo){
            player.getAnimal(id).eatExtraMeet(player,game);
        }

        for (int id:cooperateTo){
            player.getAnimal(id).eatFish(1);
        }

    }

    public void eatFish(int i) {
        if (currentHungry==0 || fedFlag || !(checkSymbiosis(owner))) return; //abort dfs if animal is fed, is already visited or can't get fish

        fedFlag=true;
        if (currentHungry<i) i=1; // if hungry==1 and fish==2, only one fish goes on
        currentHungry-=i;
        for (int id:cooperateTo
                ) {
            owner.getAnimal(id).eatFish(i);
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
