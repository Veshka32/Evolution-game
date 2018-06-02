package game.controller;

import game.constants.CardHolder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameHandler {

    @Inject
    CardHolder cardHolder;

    private int currentId=1;
    private Map<Integer,Game> games=new HashMap<>();

    public int createGame(String name){
        Game game=new Game(cardHolder);
        game.setId(currentId);
        games.put(currentId,game);
        game.addPlayer(name);
        return currentId++;
    }

    public void joinPlayer(String name, int gameId){
        games.get(gameId).addPlayer(name);
    }

    public Game getGame(Integer i){
        return games.get(i);
    }

    public boolean isCurrentGame(String name,int i){
        return  (games.get(i).containsPlayer(name));
    }

    public boolean isValidId(Integer i){
        return games.containsKey(i);
    }

    public String getAvailableGames(){
        List<Integer> notFullGames=new ArrayList<>();
        for (Game g:games.values())
            if (!g.onProgress()) notFullGames.add(g.getId());
        return notFullGames.toString();
    }
}
