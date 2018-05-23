
package game.entities;

import game.constants.Constants;
import game.controller.Game;
import game.controller.GameException;

import java.util.*;

public class Animal {
    transient ArrayList<Integer> cooperateTo = new ArrayList<>(); //default 0
    transient ArrayList<Integer> communicateTo = new ArrayList<>();
    transient ArrayList<Integer> symbiontFor = new ArrayList<>();
    transient ArrayList<Integer> symbiosis = new ArrayList<>();
    transient Player owner;
    transient boolean attackFlag = false;
    transient boolean fedFlag = false;
    transient boolean isPoisoned = false;

    //go to json
    List<String> propertyList = new ArrayList<>();
    int id;
    int totalHungry = Constants.MIN_HUNGRY.getValue();
    int currentHungry = totalHungry;
    int totalFatSupply;
    int currentFatSupply;
    String cooperateWith;
    String communicateWith;
    String symbiont;
    String symbiosisWith;

    public Animal(int id, Player player) {
        this.id = id;
        owner = player;
    }

    public void poison() {
        isPoisoned = true;
    }

    public boolean attack(Animal victim) throws GameException {
        //exceptions
        if (attackFlag) throw new GameException("This predator has been used");

        if (victim.hasProperty("Swimming")) {
            if (!hasProperty("Swimming")) throw new GameException("Not-swimming predator can't eat swimming animal");
        }

        if (hasProperty("Swimming")) {
            if (!victim.hasProperty("Swimming"))
                throw new GameException("Swimming predator can't eat non-swimming animal");
        }

        if (victim.hasProperty("Big")) {
            if (!hasProperty("Big")) throw new GameException("You can't eat such a big animal");
        }

        if (!victim.symbiosis.isEmpty())
            throw new GameException("You can't eat this animal while its symbiont is alive");
        if (victim.hasProperty("Burrowing") && victim.currentHungry == 0)
            throw new GameException("This animal is fed and in burrow");
        if (victim.hasProperty("Camouflage")) {
            if (!hasProperty("Sharp Vision")) throw new GameException("This animal is in camouflage");
        }

        //extra info
        if (victim.hasProperty("Mimicry")) {
            boolean canAttack = false;
            for (Animal an : victim.owner.animals.values()) {
                try {
                    canAttack = attack(an);
                } catch (GameException e) {
                }
                if (canAttack) break;
            }

            if (canAttack) {
                //{"pick animal for reattacking";}
            }
        }

        if (victim.hasProperty("Tail loss")) {
        } //what to do?

        attackFlag = true;
        return true;
    }

    public void die() {
        for (int id : cooperateTo) {
            owner.getAnimal(id).cooperateTo.remove(Integer.valueOf(this.id));
            owner.usedCards++;
        }

        for (int id : communicateTo) {
            owner.getAnimal(id).communicateTo.remove(Integer.valueOf(this.id));
            owner.usedCards++;
        }

        for (int id : symbiosis) {
            owner.getAnimal(id).symbiontFor.remove(Integer.valueOf(this.id));
            owner.usedCards++;
        }

        for (int id : symbiontFor) {
            owner.getAnimal(id).symbiosis.remove(Integer.valueOf(this.id));
            owner.usedCards++;
        }

        owner.animals.remove(Integer.valueOf(this.id));
        owner.usedCards += this.totalHungry;
    }

    public void addProperty(String property) throws GameException {

        if (property.equals("Scavenger") && propertyList.contains("Predator"))
            throw new GameException("Predator cannot be a scavenger");

        else if (property.equals("Predator") && propertyList.contains("Scavenger"))
            throw new GameException("Scavenger cannot be a predator");

        else if (property.equals("Fat")) {
            totalFatSupply++;
            //put in list once, but if already has, do not throw exception
            if (hasProperty(property)) return;
        } else if (property.equals("Communication") || property.equals("Cooperation") || property.equals("Symbiosis")) {
            //put in list once, but if already has, do not throw exception
            if (hasProperty(property)) return;
        } else if (propertyList.contains(property))
            throw new GameException("This animal already has property: " + property);

        propertyList.add(property);

        if (property.equals("Predator") || property.equals("Big")) totalHungry += Constants.PREDATOR_POINTS.getValue();
        if (property.equals("Parasite")) totalHungry += Constants.PARASITE_POINTS.getValue();
    }

    public Player getOwner() {
        return owner;
    }

    public void eatMeet(Player player, Game game) throws GameException {

        if (currentHungry == 0) throw new GameException("This animal is fed!");
        if (!checkSymbiosis(player)) throw new GameException("You should feed the symbiont first");

        fedFlag = true;
        currentHungry--;
        game.deleteFood();
        for (int id : cooperateTo
                ) {
            player.getAnimal(id).eatFish(1);
        }

        for (int id : communicateTo) {
            player.getAnimal(id).eatExtraMeet(player, game);
        }
    }

    public void eatExtraMeet(Player player, Game game) {
        if (game.getFood() == 0) return;
        if (currentHungry == 0 || fedFlag || !(checkSymbiosis(owner)))
            return; //abort dfs if animal is fed, is already visited or can't get fish

        fedFlag = true;
        currentHungry--;
        game.deleteFood();
        for (int id : communicateTo) {
            player.getAnimal(id).eatExtraMeet(player, game);
        }

        for (int id : cooperateTo) {
            player.getAnimal(id).eatFish(1);
        }

    }

    public void eatFish(int i) {
        if (currentHungry == 0 || fedFlag || !(checkSymbiosis(owner)))
            return; //abort dfs if animal is fed, is already visited or can't get fish

        fedFlag = true;
        if (currentHungry < i) i = 1; // if hungry==1 and fish==2, only one fish goes on
        currentHungry -= i;
        for (int id : cooperateTo
                ) {
            owner.getAnimal(id).eatFish(i);
        }
    }

    public boolean checkSymbiosis(Player player) { //return false if not all of the symbionts are fed;
        if (!symbiosis.isEmpty()) {
            for (int id : symbiosis
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

    public boolean isHungry() {
        return currentHungry != 0;
    }

    public boolean isCommunicate(int id) {
        return communicateTo.contains(id);
    }

    public boolean isCooperate(int id) {
        return cooperateTo.contains(id);
    }

    public boolean isInSymbiosis(int id) {
        return symbiosis.contains(id);
    }

    public boolean isSymbiontFor(int id) {
        return symbiontFor.contains(id);
    }

    public void setCommunicateTo(int id) {
        communicateTo.add(id);
        communicateWith = communicateTo.toString();
    }

    public void setCooperateTo(int id) {
        cooperateTo.add(id);
        cooperateWith = cooperateTo.toString();
    }

    public void setSymbiosysWith(int id) {
        symbiosis.add(id);
        symbiosisWith = symbiosis.toString();
    }

    public void setSymbiontFor(int id) {
        symbiontFor.add(id);
        symbiont = symbiontFor.toString();
    }

    public boolean hasProperty(String property) {
        return propertyList.contains(property);
    }

    public void removeProperty(String property, int id) throws GameException {
        if (!propertyList.contains(property)) throw new GameException("Animal has no property " + property);
        switch (property) {
            case "Parasite":
                totalHungry -= 2;
                break;
            case "Big":
                totalHungry--;
                break;
            case "Fat":
                totalFatSupply--;
                if (currentFatSupply > totalFatSupply) currentFatSupply = totalFatSupply;
                if (totalFatSupply == 0) propertyList.remove(property); //remove only there is no more fat supply
                break;
            case "Predator":
                totalHungry--;
                break;
            case "Cooperation":
                cooperateTo.remove(Integer.valueOf(id));
                cooperateWith = cooperateTo.toString();
                Animal an = owner.getAnimal(id);
                an.cooperateTo.remove(Integer.valueOf(this.id));
                an.cooperateWith = an.cooperateTo.toString();
                if (cooperateTo.isEmpty()) propertyList.remove(property);
                if (an.cooperateTo.isEmpty()) an.propertyList.remove(property);
                return;
            case "Communication":
                communicateTo.remove(Integer.valueOf(id));
                communicateWith = communicateTo.toString();
                an = owner.getAnimal(id);
                an.communicateTo.remove(Integer.valueOf(this.id));
                an.communicateWith = an.communicateTo.toString();
                if (communicateTo.isEmpty()) propertyList.remove(property);
                if (an.communicateTo.isEmpty()) an.propertyList.remove(property);
                return;
            case "Symbiosis":
                return;

        }
        propertyList.remove(property);
        owner.usedCards++;
    }

    public void addFat() throws GameException {
        if (currentFatSupply == totalFatSupply) throw new GameException("This animal can't get more fat");
        currentFatSupply++;
    }


}
