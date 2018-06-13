
package game.entities;

import com.google.gson.annotations.Expose;
import game.constants.Property;
import game.controller.Deck;
import game.controller.Game;
import game.controller.GameException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class Animal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long trueId;

    private final int MIN_HUNGRY = 1;
    @ManyToOne
    Player owner;
    boolean attackFlag = false;
    boolean fedFlag = false;
    boolean isPoisoned = false;
    private boolean doPiracy = false;
    private boolean doGrazing = false;
    private int hibernationRound;
    int totalFatSupply;

    //include in json
    @Expose
    private int id;
    @Expose
    @ElementCollection
    List<Property> propertyList = new ArrayList<>();
    @Expose
    @ElementCollection
    List<Integer> cooperateTo = new ArrayList<>();
    @Expose
    @ElementCollection
    private List<Integer> communicateTo = new ArrayList<>();
    @Expose
    @ElementCollection
    private List<Integer> symbiontFor = new ArrayList<>();
    @Expose
    @ElementCollection
    private List<Integer> symbiosisWith = new ArrayList<>();
    @Expose
    int hungry = MIN_HUNGRY;
    @Expose
    private int currentFatSupply;

    public Animal() {
    }

    public Animal(int id, Player player) {
        this.id = id;
        owner = player;
    }

    void setHungry() {
        hungry = calculateHungry();
    }

    public int calculateHungry() {
        int result = MIN_HUNGRY;
        if (hasProperty(Property.PREDATOR)) result++;
        if (hasProperty(Property.BIG)) result++;
        if (hasProperty(Property.PARASITE)) result += 2;
        return result;
    }

    public void deleteFood() {
        hungry++;
    }

    public void poison() {
        isPoisoned = true;
    }

    public void setDoPiracy(boolean bool) {
        doPiracy = bool;
    }

    public void setAttackFlag(boolean bool) {
        attackFlag = bool;
    }

    public void setDoGrazing(boolean bool) {
        doGrazing = bool;
    }

    public boolean isDoPiracy() {
        return doPiracy;
    }

    public boolean isDoGrazing() {
        return doGrazing;
    }

    public void hibernate(int round) throws GameException {
        if (round == -1) throw new GameException("You can't hibernate in last round");
        else if (round != 0 && round == hibernationRound)
            throw new GameException("This animal is already in hibernation");//can hibernate in 0 round
        else if (round - hibernationRound == 1) throw new GameException("You can't hibernate 2 rounds in a row");
        hibernationRound = round;
        hungry = 0;
    }

    public void eatFat() throws GameException {
        if (currentFatSupply < 1) throw new GameException("You have no fat supply");
        if (hungry < 1) throw new GameException("Animal is fed");
        hungry--;
        currentFatSupply--;
    }

    public void attack(Animal victim) throws GameException {
        //exceptions
        if (!hasProperty(Property.PREDATOR)) throw new GameException("This animal is not a predator");
        if (attackFlag) throw new GameException("This predator has been used");

        if (victim.hasProperty(Property.SWIMMING)) {
            if (!hasProperty(Property.SWIMMING)) throw new GameException("Not-swimming predator can't eat swimming animal");
        }

        if (hasProperty(Property.SWIMMING)) {
            if (!victim.hasProperty(Property.SWIMMING))
                throw new GameException("Swimming predator can't eat non-swimming animal");
        }

        if (victim.hasProperty(Property.BIG) && !hasProperty(Property.BIG))
            throw new GameException("You can't eat such a big animal");

        if (!victim.symbiosisWith.isEmpty())
            throw new GameException("You can't eat this animal while its symbiont is alive");
        if (victim.hasProperty(Property.BURROWING) && victim.hungry == 0)
            throw new GameException("This animal is fed and in burrow");
        if (victim.hasProperty(Property.CAMOUFLAGE)) {
            if (!hasProperty(Property.SHARP_VISION)) throw new GameException("This animal is in camouflage");
        }
    }

    public void die() {
        for (int id : cooperateTo) {
            Animal animal = owner.getAnimal(id);
            animal.cooperateTo.remove(Integer.valueOf(this.id));
            if (animal.cooperateTo.isEmpty()) animal.propertyList.remove(Property.COOPERATION);
            owner.increaseUsedCards();
        }

        for (int id : communicateTo) {
            Animal animal = owner.getAnimal(id);
            animal.communicateTo.remove(Integer.valueOf(this.id));
            if (animal.communicateTo.isEmpty()) animal.propertyList.remove(Property.COMMUNICATION);
            owner.increaseUsedCards();
        }

        for (int id : symbiosisWith) {
            Animal animal = owner.getAnimal(id);
            animal.symbiontFor.remove(Integer.valueOf(this.id));
            if (animal.symbiontFor.isEmpty() && animal.symbiosisWith.isEmpty()) animal.propertyList.remove(Property.SYMBIOSIS);
            owner.increaseUsedCards();
        }

        for (int id : symbiontFor) {
            Animal animal = owner.getAnimal(id);
            animal.symbiosisWith.remove(Integer.valueOf(this.id));
            if (animal.symbiontFor.isEmpty() && animal.symbiosisWith.isEmpty()) animal.propertyList.remove(Property.SYMBIOSIS);
            owner.increaseUsedCards();
        }
    }

    public void addProperty(Property property) throws GameException {

        if (property.equals(Property.SCAVENGER) && propertyList.contains(Property.PREDATOR))
            throw new GameException("Predator cannot be a scavenger");

        else if (property.equals(Property.PREDATOR) && propertyList.contains(Property.SCAVENGER))
            throw new GameException("Scavenger cannot be a predator");

        else if (property.equals(Property.FAT)) {
            totalFatSupply++;
        } else if (Deck.isPropertyDouble(property)) {
            //put in list once, but if already has, do not throw exception
            if (hasProperty(property)) return;
        } else if (propertyList.contains(property))
            throw new GameException("This animal already has property: " + property);

        propertyList.add(property);

        if (property.equals(Property.PREDATOR) || property.equals(Property.BIG)) {
            hungry++;
        }
        if (property.equals(Property.PARASITE)) {
            hungry += 2;
        }
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isAttackFlag() {
        return attackFlag;
    }

    public boolean isFedFlag() {
        return fedFlag;
    }

    public void setFedFlag(boolean fedFlag) {
        this.fedFlag = fedFlag;
    }

    public boolean isPoisoned() {
        return isPoisoned;
    }

    public void setPoisoned(boolean poisoned) {
        isPoisoned = poisoned;
    }

    public int getHibernationRound() {
        return hibernationRound;
    }

    public void setHibernationRound(int hibernationRound) {
        this.hibernationRound = hibernationRound;
    }

    public int getTotalFatSupply() {
        return totalFatSupply;
    }

    public void setTotalFatSupply(int totalFatSupply) {
        this.totalFatSupply = totalFatSupply;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    public List<Integer> getCooperateTo() {
        return cooperateTo;
    }

    public void setCooperateTo(ArrayList<Integer> cooperateTo) {
        this.cooperateTo = cooperateTo;
    }

    public List<Integer> getCommunicateTo() {
        return communicateTo;
    }

    public void setCommunicateTo(ArrayList<Integer> communicateTo) {
        this.communicateTo = communicateTo;
    }

    public List<Integer> getSymbiontFor() {
        return symbiontFor;
    }

    public void setSymbiontFor(ArrayList<Integer> symbiontFor) {
        this.symbiontFor = symbiontFor;
    }

    public List<Integer> getSymbiosisWith() {
        return symbiosisWith;
    }

    public void setSymbiosisWith(ArrayList<Integer> symbiosisWith) {
        this.symbiosisWith = symbiosisWith;
    }

    public int getHungry() {
        return hungry;
    }

    public void setHungry(int hungry) {
        this.hungry = hungry;
    }

    public int getCurrentFatSupply() {
        return currentFatSupply;
    }

    public void setCurrentFatSupply(int currentFatSupply) {
        this.currentFatSupply = currentFatSupply;
    }

    public Player getOwner() {
        return owner;
    }

    public void eatMeet(Player player, Game game) throws GameException {

        if (hungry == 0) {
            if (currentFatSupply == totalFatSupply) throw new GameException("This animal is fed!");
            else {
                currentFatSupply++;
                game.deleteFood();
                return;
            }
        }
        if (!checkSymbiosis(player)) throw new GameException("You should feed the symbiont first");

        fedFlag = true;
        hungry--;
        game.deleteFood();
        for (int id : cooperateTo
                ) {
            player.getAnimal(id).eatFish(1);
        }

        for (int id : communicateTo) {
            player.getAnimal(id).eatExtraMeet(player, game);
        }
    }

    private void eatExtraMeet(Player player, Game game) {
        if (game.getFood() == 0) return;
        if (hungry == 0 || fedFlag || !(checkSymbiosis(owner)))
            return; //abort dfs if animal is fed, is already visited or can't get fish

        fedFlag = true;
        hungry--;
        game.deleteFood();
        for (int id : communicateTo) {
            player.getAnimal(id).eatExtraMeet(player, game);
        }

        for (int id : cooperateTo) {
            player.getAnimal(id).eatFish(1);
        }

    }

    public void eatFish(int i) {
        if (hungry == 0 || fedFlag || !(checkSymbiosis(owner)))
            return; //abort dfs if animal is fed, is already visited or can't get fish

        fedFlag = true;
        if (hungry < i) i = 1; // if hungry==1 and fish==2, only one fish goes on
        hungry -= i;
        for (int id : cooperateTo
                ) {
            owner.getAnimal(id).eatFish(i);
        }
    }

    public boolean checkSymbiosis(Player player) { //return false if not all of the symbionts are fed;
        if (!symbiosisWith.isEmpty()) {
            for (int id : symbiosisWith
                    ) {
                if (player.getAnimal(id).hungry != 0)
                    return false;
            }
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public boolean notHungry() {
        return hungry == 0;
    }

    boolean isCommunicate(int id) {
        return communicateTo.contains(id);
    }

    boolean isCooperate(int id) {
        return cooperateTo.contains(id);
    }

    boolean isInSymbiosis(int id) {
        return symbiosisWith.contains(id);
    }

    boolean isSymbiontFor(int id) {
        return symbiontFor.contains(id);
    }

    void setCommunicateTo(int id) {
        communicateTo.add(id);
    }

    void setCooperateTo(int id) {
        cooperateTo.add(id);
    }

    void setSymbiosysWith(int id) {
        symbiosisWith.add(id);
    }

    void setSymbiontFor(int id) {
        symbiontFor.add(id);
    }

    public boolean hasProperty(Property property) {
        return propertyList.contains(property);
    }

    public void removeProperty(Property property) {
        switch (property) {
            case PARASITE:
                hungry -= 2;
                propertyList.remove(property);
                break;
            case BIG:
                hungry--;
                propertyList.remove(property);
                break;
            case FAT:
                totalFatSupply--;
                if (currentFatSupply > totalFatSupply) currentFatSupply = totalFatSupply;
                propertyList.remove(property);
                break;
            case PREDATOR:
                hungry--;
                break;
            case COMMUNICATION:
                Animal partner = owner.getAnimal(communicateTo.get(0));
                communicateTo.remove(Integer.valueOf(partner.id));
                partner.communicateTo.remove(Integer.valueOf(this.id));
                if (communicateTo.isEmpty()) propertyList.remove(property);
                if (partner.communicateTo.isEmpty()) partner.propertyList.remove(property);
                break;
            case COOPERATION:
                partner = owner.getAnimal(cooperateTo.get(0));
                cooperateTo.remove(Integer.valueOf(partner.id));
                partner.cooperateTo.remove(Integer.valueOf(this.id));
                if (cooperateTo.isEmpty()) propertyList.remove(property);
                if (partner.cooperateTo.isEmpty()) partner.propertyList.remove(property);
                break;
            case SYMBIOSIS:
                if (!symbiosisWith.isEmpty()) {
                    partner = owner.getAnimal(symbiosisWith.get(0));
                    symbiosisWith.remove(Integer.valueOf(partner.id));
                    partner.symbiontFor.remove(Integer.valueOf(this.id));
                    if (partner.symbiosisWith.isEmpty() && partner.symbiontFor.isEmpty())
                        partner.propertyList.remove(property);
                } else if (!symbiontFor.isEmpty()) {
                    partner = owner.getAnimal(symbiontFor.get(0));
                    symbiontFor.remove(Integer.valueOf(partner.id));
                    partner.symbiosisWith.remove(Integer.valueOf(this.id));
                    if (partner.symbiosisWith.isEmpty() && partner.symbiontFor.isEmpty())
                        partner.propertyList.remove(property);
                }
                if (symbiosisWith.isEmpty() && symbiontFor.isEmpty()) propertyList.remove(property);
                break;
        }

        if (!Deck.isPropertyDouble(property)) propertyList.remove(property);
        owner.increaseUsedCards();

    }
}
