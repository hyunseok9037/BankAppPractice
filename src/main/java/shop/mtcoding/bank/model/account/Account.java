package shop.mtcoding.bank.model.account;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Account {

    public int id;
    public String number;
    public String password;
    public Long balance;
    public Integer userId;
    public Timestamp createdAt;

}
