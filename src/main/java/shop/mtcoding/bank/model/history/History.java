package shop.mtcoding.bank.model.history;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class History {

    public int id;
    public Long amount;
    public Long wBalance;
    public Long dBalance;
    public Integer wAccountId;
    public Integer dAccountId;
    public Timestamp createdAt;

}
