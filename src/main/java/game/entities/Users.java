package game.entities;

import game.controller.Game;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
//@Table(name="Users") //by default, table name=Classname
public class Users implements Serializable {

    @Id //primary key
    @Column //default column name=field name
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true,nullable = false,updatable = false)
    private String login;

    @Column(length = 20,nullable = false)
    private byte[] password;

    @Column(length = 8)
    private byte[] salt;

//    @ManyToMany(cascade = CascadeType.MERGE)
//    @JoinTable(name="users_games",    joinColumns =
//    @JoinColumn(name = "users_id", referencedColumnName = "id"),
//    inverseJoinColumns =
//    @JoinColumn(name = "game_id", referencedColumnName = "id"))
//    private Set<Game> games=new HashSet<>();

    public Users(){}

    public Users(String login, byte[] password, byte[] salt){
        this.login=login;
        this.password=password;
        this.salt=salt;
    }

    public Users(String login){
        this.login=login;
    }

//    public void addGame(Game game){
//        games.add(game);
//    }

    public String getLogin(){
        return login;
    }

    public void setLogin(String login){
        this.login=login;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //Аннотация @Column  не является обязательной. По умолчанию все поля класса сохраняются в базе данных.
    // Если поле не должно быть сохранено, оно должно быть проаннотированно аннотацией @Transient.

}
