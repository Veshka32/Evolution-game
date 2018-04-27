package model;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@ApplicationScoped
public class UserBase {
    private final HashMap<String, HttpSession> users=new HashMap<>();

    public boolean signUp(String login, HttpSession session){
        if (!users.containsKey(login)){
            users.put(login,session);
            return true;
        }
        return false;
    }

    @PostConstruct
    public void constr(){
        System.out.println("userbase is created");
    }
}
