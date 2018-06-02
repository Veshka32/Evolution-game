package game.controller;

import game.constants.CardHolder;
import services.dataBaseService.GameDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.transaction.*;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameHandler {
    @Inject
    CardHolder cardHolder;

    @Inject
    GameDAO gameDAO;

    private int currentId=1;

    private Map<Integer,Game> games;

    @PostConstruct
    public void loadGames(){
        List<Game> allGames=gameDAO.getAllGames();
        games=allGames.stream().collect(Collectors.toMap(Game::getId,item->item));
    }

    public int createGame(String name) throws HeuristicMixedException, RollbackException, SystemException, NamingException, HeuristicRollbackException, NotSupportedException {
        Game game=new Game(cardHolder);
        game.setId(currentId);
        games.put(currentId,game);
        game.addPlayer(name);
        gameDAO.create(game);
        return currentId++;
    }

    public void update(int gameId) throws HeuristicMixedException, NotSupportedException, SystemException, NamingException, HeuristicRollbackException, RollbackException {
        gameDAO.update(games.get(gameId));
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

    public String getUserGames(String name){
        List<Integer> result=new ArrayList<>();
        for (Game g:games.values())
            if (g.onProgress() && g.containsPlayer(name)) result.add(g.getId());
        return result.toString();
    }

    public String getNewGames(){
        List<Integer> notFullGames=new ArrayList<>();
        for (Game g:games.values())
            if (!g.onProgress()) notFullGames.add(g.getId());
        return notFullGames.toString();
    }
}
