package shop.mtcoding.bank.Controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.bank.dto.user.joinReqDto;
import shop.mtcoding.bank.dto.user.loginReqDto;
import shop.mtcoding.bank.handler.ex.CustomException;
import shop.mtcoding.bank.model.user.User;
import shop.mtcoding.bank.model.user.UserRepository;
import shop.mtcoding.bank.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @PostMapping("/login")
    public String login(loginReqDto loginReqDto) {
        if (loginReqDto.getUsername() == null ||
                loginReqDto.getUsername().isEmpty()) {
            throw new CustomException("username을 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (loginReqDto.getPassword() == null ||
                loginReqDto.getPassword().isEmpty()) {
            throw new CustomException("password을 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        User principal = userRepository.findByUsernameAndPassword(loginReqDto);

        if (principal == null) {
            throw new CustomException("아이디 혹은 비번이 틀렸습니다", HttpStatus.BAD_REQUEST);
        }

        session.setAttribute("principal", "principal");

        return "redirect:/";
    }

    @PostMapping("/join")
    public String join(joinReqDto joinReqDto) {
        if (joinReqDto.getUsername() == null ||
                joinReqDto.getUsername().isEmpty()) {
            throw new CustomException("username을 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (joinReqDto.getPassword() == null ||
                joinReqDto.getPassword().isEmpty()) {
            throw new CustomException("password을 입력해주세요", HttpStatus.BAD_REQUEST);
        }
        if (joinReqDto.getFullname() == null ||
                joinReqDto.getFullname().isEmpty()) {
            throw new CustomException("fullname을 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        userService.회원가입(joinReqDto);

        return "redirect:/loginForm";
    }

    @GetMapping("/loginForm")
    public String login() {
        return "user/loginForm";
    }

    @GetMapping("/joinForm")
    public String join() {
        return "user/joinForm";
    }
}
