
package game.entities;

import com.google.gson.annotations.Expose;
import game.constants.CardHolder;
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
    boolean doPiracy = false;
    boolean doGrazing = false;
    int hibernationRound;
    int totalFatSupply;

    //include in json
    int id;
    @Expose
    @ElementCollection
    List<String> propertyList = new ArrayList<>();
    @Expose
    @ElementCollection
    List<Integer> cooperateTo = new ArrayList<>();
    @Expose
    @ElementCollection
    List<Integer> communicateTo = new ArrayList<>();
    @Expose
    @ElementCollection
    List<Integer> symbiontFor = new ArrayList<>();
    @Expose
    @ElementCollection
    List<Integer> symbiosisWith = new ArrayList<>();
    @Expose
    int hungry = MIN_HUNGRY;
    @Expose
    int currentFatSupply;

    public Animal() {
    }

    public Animal(int id, Player player) {
        this.id = id;
        owner = player;
    }

    public void setHungry() {
        hungry = MIN_HUNGRY;
        if (hasProperty("Predator")) hungry++;
        if (hasProperty("Big")) hungry++;
        if (hasProperty("Parasite")) hungry += 2;
    }

    public int calculateHungry() {
        int result = MIN_HUNGRY;
        if (hasProperty("Predator")) result++;
        if (hasProperty("Big")) result++;
        if (hasProperty("Parasite")) result += 2;
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
        if (!hasProperty("Predator")) throw new GameException("This animal is not a predator");
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

        if (!victim.symbiosisWith.isEmpty())
            throw new GameException("You can't eat this animal while its symbiont is alive");
        if (victim.hasProperty("Burrowing") && victim.hungry == 0)
            throw new GameException("This animal is fed and in burrow");
        if (victim.hasProperty("Camouflage")) {
            if (!hasProperty("Sharp Vision")) throw new GameException("This animal is in camouflage");
        }
    }

    public void die() {
        for (int id : cooperateTo) {
            Animal animal = owner.getAnimal(id);
            animal.cooperateTo.remove(Integer.valueOf(this.id));
            if (animal.cooperateTo.isEmpty()) animal.propertyList.remove("Cooperation");
            owner.increaseUsedCards();
        }

        for (int id : communicateTo) {
            Animal animal = owner.getAnimal(id);
            animal.communicateTo.remove(Integer.valueOf(this.id));
            if (animal.communicateTo.isEmpty()) animal.propertyList.remove("Communication");
            owner.increaseUsedCards();
        }

        for (int id : symbiosisWith) {
            Animal animal = owner.getAnimal(id);
            animal.symbiontFor.remove(Integer.valueOf(this.id));
            if (animal.symbiontFor.isEmpty() && animal.symbiosisWith.isEmpty()) animal.propertyList.remove("Symbiosis");
            owner.increaseUsedCards();
        }

        for (int id : symbiontFor) {
            Animal animal = owner.getAnimal(id);
            animal.symbiosisWith.remove(Integer.valueOf(this.id));
            if (animal.symbiontFor.isEmpty() && animal.symbiosisWith.isEmpty()) animal.propertyList.remove("Symbiosis");
            owner.increaseUsedCards();
        }
    }

    public void addProperty(String property) throws GameException {

        if (property.equals("Scavenger") && propertyList.contains("Predator"))
            throw new GameException("Predator cannot be a scavenger");

        else if (property.equals("Predator") && propertyList.contains("Scavenger"))
            throw new GameException("Scavenger cannot be a predator");

        else if (property.equals("Fat")) {
            totalFatSupply++;
        } else if (property.equals("Communication") || property.equals("Cooperation") || property.equals("Symbiosis")) {
            //put in list once, but if already has, do not throw exception
            if (hasProperty(property)) return;
        } else if (propertyList.contains(property))
            throw new GameException("This animal already has property: " + property);

        propertyList.add(property);

        if (property.equals("Predator") || property.equals("Big")) {
            hungry++;
        }
        if (property.equals("Parasite")) {
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

    public List<String> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<String> propertyList) {
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

    public void eatExtraMeet(Player player, Game game) {
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

    public boolean isCommunicate(int id) {
        return communicateTo.contains(id);
    }

    public boolean isCooperate(int id) {
        return cooperateTo.contains(id);
    }

    public boolean isInSymbiosis(int id) {
        return symbiosisWith.contains(id);
    }

    public boolean isSymbiontFor(int id) {
        return symbiontFor.contains(id);
    }

    public void setCommunicateTo(int id) {
        communicateTo.add(id);
    }

    public void setCooperateTo(int id) {
        cooperateTo.add(id);
    }

    public void setSymbiosysWith(int id) {
        symbiosisWith.add(id);
    }

    public void setSymbiontFor(int id) {
        symbiontFor.add(id);
    }

    public boolean hasProperty(String property) {
        return propertyList.contains(property);
    }

    public void removeProperty(String property) {
        switch (property) {
            case "Parasite":
                hungry -= 2;
                propertyList.remove(property);
                break;
            case "Big":
                hungry--;
                propertyList.remove(property);
                break;
            case "Fat":
                totalFatSupply--;
                if (currentFatSupply > totalFatSupply) currentFatSupply = totalFatSupply;
                propertyList.remove(property);
                break;
            case "Predator":
                hungry--;
                break;
            case "Communication":
                Animal partner = owner.getAnimal(communicateTo.get(0));
                communicateTo.remove(Integer.valueOf(partner.id));
                partner.communicateTo.remove(Integer.valueOf(this.id));
                if (communicateTo.isEmpty()) propertyList.remove(property);
                if (partner.communicateTo.isEmpty()) partner.propertyList.remove(property);
                break;
            case "Cooperation":
                partner = owner.getAnimal(cooperateTo.get(0));
                cooperateTo.remove(Integer.valueOf(partner.id));
                partner.cooperateTo.remove(Integer.valueOf(this.id));
                if (cooperateTo.isEmpty()) propertyList.remove(property);
                if (partner.cooperateTo.isEmpty()) partner.propertyList.remove(property);
                break;
            case "Symbiosis":
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

        if (!CardHolder.isDouble(property)) propertyList.remove(property);
        owner.increaseUsedCards();

    }
}
