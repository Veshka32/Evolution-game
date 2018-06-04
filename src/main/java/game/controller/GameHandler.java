package game.controller;

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
    private GameDAO gameDAO;

    @Inject
    private CardHolder cardHolder;

    private Map<Integer,Game> games;

    @PostConstruct
    public void loadGames(){
        List<Game> allGames=gameDAO.getAllGames();
        games=allGames.stream().collect(Collectors.toMap(Game::getId,item->item));
    }

    public int createGame(String name) throws HeuristicMixedException, RollbackException, SystemException, NamingException, HeuristicRollbackException, NotSupportedException {
        Game game=new Game();
        game.setCardList(cardHolder.getCards());
        game.addPlayer(name);
        game=gameDAO.update(game);
        games.put(game.getId(),game);
        return game.getId();
    }

    public void update(int gameId) throws HeuristicMixedException, NotSupportedException, SystemException, NamingException, HeuristicRollbackException, RollbackException {
        gameDAO.update(games.get(gameId));
    }

    public void joinPlayer(String name, int gameId) throws HeuristicMixedException, NotSupportedException, SystemException, NamingException, HeuristicRollbackException, RollbackException {
        Game game=games.get(gameId);
        game.addPlayer(name);
        game=gameDAO.update(games.get(gameId)); //generate id for new player
        if (game.isFull()) {game.start();gameDAO.update(game);}
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
            if (g.containsPlayer(name)) result.add(g.getId());
        return result.toString();
    }

    public String getNewGames(String name){
        List<Integer> notFullGames=new ArrayList<>();
        for (Game g:games.values())
            if (!(g.onProgress() || g.containsPlayer(name))) notFullGames.add(g.getId()); //games that not full and not user's game
        return notFullGames.toString();
    }
}
