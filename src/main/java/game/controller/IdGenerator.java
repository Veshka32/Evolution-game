package game.controller;

import services.dataBaseService.IdDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class IdGenerator {
    private AtomicInteger nextGameId;

    @Inject
    private IdDAO idDAO;

    public IdGenerator(){
    }

    @PostConstruct
    private void setIds(){
        nextGameId =new AtomicInteger(idDAO.getGameLastId());
    }

    int getGame_next_id(){
        return nextGameId.incrementAndGet();
    }
}
