package game.controller;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameHandler {
    private int currentId=1;
    private Map<Integer,Game> games=new HashMap<>();

    public int createGame(){
        Game game=new Game();
        game.setId(currentId);
        games.put(currentId,game);
        return currentId++;
    }

    public Game getGame(Integer i){
        return games.get(i);
    }

    public String getAllGames(){
        List<Integer> notFullGames=new ArrayList<>();
        for (Game g:games.values()
             ) {
            if (!g.onProgress()) notFullGames.add(g.getId());
        }
        return notFullGames.toString();
        //return games.keySet().stream().map(n->String.valueOf(n)).collect(Collectors.joining(","));

    }
}
