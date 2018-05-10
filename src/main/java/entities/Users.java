package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
//@Table(name="Users") //by default, table name=Classname
public class Users implements Serializable {
    @Id //primary key
    @GeneratedValue
    @Column(nullable = false) //default column name=field name
    private Integer id;

    @Column(nullable =false,unique = true)
    private String login;

    @Column(length = 20)
    private byte[] password;

    @Column(length = 8)
    private byte[] salt;

    public Users(){}

    public Users(String login, byte[] password, byte[] salt){
        this.login=login;
        this.password=password;
        this.salt=salt;
    }

    public Users(String login){
        this.login=login;
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //Аннотация @Column  не является обязательной. По умолчанию все поля класса сохраняются в базе данных.
    // Если поле не должно быть сохранено, оно должно быть проаннотированно аннотацией @Transient.

}
