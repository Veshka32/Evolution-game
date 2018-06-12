package game.controller;

import services.dataBaseService.IdDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class IdGenerator {
    private int game_next_id;
    private int player_next_id;
    @Inject
    private IdDAO idDAO;

    public IdGenerator(){
    }

    @PostConstruct
    private void setIds(){
        game_next_id=idDAO.getGameLastId();
        player_next_id=idDAO.getPlayerLastId();
    }

    public synchronized int getGame_next_id(){
        return game_next_id++;
    }

    public synchronized int getPlayer_next_id(){
        return player_next_id++;
    }
}
