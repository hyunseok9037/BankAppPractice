package shop.mtcoding.bank.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class joinReqDto {
    private String username;
    private String password;
    private String fullname;
}
