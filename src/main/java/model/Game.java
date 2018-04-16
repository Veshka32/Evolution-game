package model;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private static Game ourInstance = new Game();
    HashMap<String, String> players;

    public static Game getInstance() {
        return ourInstance;
    }

    private Game() {
        players=new HashMap<>();
    }

    public void addPlayer(HttpSession session, String userName){
        players.put(session.getId(),userName);
    }

    public String playersList(){
        List<String> playersList=new ArrayList(players.values());
        return playersList.stream().collect(Collectors.joining(", "));
    }
}
