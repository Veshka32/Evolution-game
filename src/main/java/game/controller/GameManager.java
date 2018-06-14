package game.controller;

import game.entities.Game;
import services.dataBaseService.GameDAO;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//https://docs.oracle.com/cd/E19798-01/821-1841/gipsz/index.html - singleton concurrency
@Singleton
@ApplicationScoped
public class GameManager {

    @Inject
    private IdGenerator generator;

    @Inject
    private GameDAO gameDAO;

    @Inject
    private Deck deck;

    private Map<Integer, Game> games = new HashMap<>();

    @Schedule(hour = "*") //every minute
    public void removeGames() {
        games.values().removeIf(Game::isLeft); //safety removing from map
    }

    public String loadSavedGames(String login) {
        List<Game> savedGames;
        try {
            savedGames = gameDAO.getSavedGames(login);
        } catch (Exception e) {
            savedGames = new ArrayList<>();
        }
        if (savedGames.isEmpty()) return " no games";
        return savedGames.stream().map(Game::toString).collect(Collectors.joining("\n"));
    }

    public String getNewGames(String login) {
        //get all the games those are not full or are full but contain this player
        return games.values().stream().filter(game -> !game.onProgress() || game.hasPlayer(login)).map(Game::toString).collect(Collectors.joining("\n"));
    }

    public Integer createGame(String name, int number) {
        Game game = new Game();
        game.setId(generator.getGame_next_id());
        game.setNumberOfPlayers(number);
        game.addPlayer(name);
        games.put(game.getId(), game); //not in user
        return game.getId();
    }

    public void joinPlayer(Integer gameId,String login) throws IllegalArgumentException {
        if (!games.containsKey(gameId)) throw new IllegalArgumentException();
        Game game = games.get(gameId);
        if (game.hasPlayer(login))
            game.playerBack(login);
        else game.addPlayer(login);

        if (game.isFull()) {
            game.setCardList(deck.getCards());
            game.start();
        }
    }


    public void loadGame(Integer gameId, String login) throws IllegalArgumentException {
        Game game = gameDAO.load(gameId, login);
        if (game == null) throw new IllegalArgumentException();
        games.put(gameId, game);
    }

    public void remove(Integer gameId) {
        games.remove(gameId);
        try {
            gameDAO.remove(gameId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Game getGame(Integer id) {
        return games.get(id);
    }

    public void save(Integer gameId) {
        Game game = gameDAO.save(games.get(gameId));
        games.put(gameId, game);
    }

}
