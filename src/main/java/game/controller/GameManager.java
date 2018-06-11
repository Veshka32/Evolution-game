package game.controller;

import game.entities.Users;
import services.dataBaseService.GameDAO;
import services.dataBaseService.UsersDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.transaction.*;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameManager {

    @Inject
    private GameDAO gameDAO;

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private Deck deck;

    private Map<Integer, Game> games=new HashMap<>();

    public String loadSavedGames(String login){
        List<Game> savedGames=gameDAO.getSavedGames(login);
        if (savedGames.isEmpty()) return " no games";
        return savedGames.stream().map(Game::toString).collect(Collectors.joining("/n"));
    }

    public String getNewGames(String login) {
        //get all the games those are not full or are full but contain this player
        return games.values().stream().filter(game -> !game.onProgress() || game.containsPlayer(login)).map(Game::toString).collect(Collectors.joining("/n"));
    }

    public int createGame(String name, Integer number) {
        Game game=new Game();
        game.setNumberOfPlayers(number);
        game.addUser(usersDAO.get(name));
        game=gameDAO.save(game);
        games.put(game.getId(), game); //not in user
        return game.getId();
    }

    public void joinPlayer(String name, int gameId) {
        Game game = games.get(gameId);
        game.addPlayer(name);
        Users user=usersDAO.get(name);
        game.addUser(user);
        if (game.isFull()) {
            game.setCardList(deck.getCards());
            game.start();
        }
    }

    public boolean loadGame(int gameId,String login){
        Game game=gameDAO.load(gameId,login);
        if (game==null) return false;
        games.put(gameId,game);
        return true;
    }

    public void remove(int gameId) {
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
