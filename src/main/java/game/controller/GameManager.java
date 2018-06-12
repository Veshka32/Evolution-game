package game.controller;

import services.dataBaseService.GameDAO;

import javax.ejb.*;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameManager {

    @Inject
    private IdGenerator generator;

    @Inject
    private GameDAO gameDAO;

    @Inject
    private Deck deck;

    private Map<Integer, Game> games=new HashMap<>();

    @Schedule(minute="*/2") //every 2 minutes
    public void removeGames(){
        games.values().removeIf(Game::isLeft); //safety removing from map
    }

    public String loadSavedGames(String login){
        List<Game> savedGames=gameDAO.getSavedGames(login);
        if (savedGames.isEmpty()) return " no games";
        return savedGames.stream().map(Game::toString).collect(Collectors.joining("\n"));
    }

    public String getNewGames(String login) {
        //get all the games those are not full or are full but contain this player
        return games.values().stream().filter(game -> !game.onProgress() || game.containsPlayer(login)).map(Game::toString).collect(Collectors.joining("\n"));
    }

    public Integer createGame(String name, Integer number) {
        Game game=new Game();
        game.setId(generator.getGame_next_id());
        game.setNumberOfPlayers(number);
        game.addPlayer(name);
        games.put(game.getId(), game); //not in user
        return game.getId();
    }

    public void joinPlayer(String name, int gameId) {
        Game game = games.get(gameId);
        game.addPlayer(name);
        if (game.isFull()) {
            game.setCardList(deck.getCards());
            game.start();
        }
    }

    public void playerBack(String name,Integer gameId){
        games.get(gameId).playerBack(name);
    }

    public boolean loadGame(Integer gameId,String login){
        Game game=gameDAO.load(gameId,login);
        if (game==null) return false;
        games.put(gameId,game);
        return true;
    }

    public void remove(Integer gameId) {
        games.remove(gameId);
        try {
            gameDAO.remove(gameId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Game getGame(Integer i) {
        return games.get(i);
    }

    public boolean doParticipate(String name, int i) {
        return (games.get(i).containsPlayer(name));
    }

    public boolean isValidId(Integer gameId) {
        return games.containsKey(gameId);
    }

    public void save(Integer gameId){
        Game game=gameDAO.save(games.get(gameId));
        games.put(gameId,game);
    }

}
