package shop.mtcoding.bank.dto.account;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountWithdrawReqDto {
    private Long amount;
    private String wAccountNumber;
    private String wAccountPassword;
}
