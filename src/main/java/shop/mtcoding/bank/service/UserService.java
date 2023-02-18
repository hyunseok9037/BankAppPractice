package shop.mtcoding.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.bank.dto.user.joinReqDto;
import shop.mtcoding.bank.handler.ex.CustomException;
import shop.mtcoding.bank.model.user.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void 회원가입(joinReqDto joinReqDto) {
        int result = userRepository.insert(joinReqDto);
        if (result != 1) {
            throw new CustomException("회원가입실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
