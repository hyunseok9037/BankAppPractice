package shop.mtcoding.bank.model.user;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    public int id;
    public String username;
    public String password;
    public String fullname;
    public Timestamp createdAt;
}
