package shop.mtcoding.bank.dto.user;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.model.user.User;

@Setter
@Getter
public class joinReqDto {
    private String username;
    private String password;
    private String fullname;

    public User toModel() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullname(fullname);
        return user;
    }
}
