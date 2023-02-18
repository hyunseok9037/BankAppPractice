package shop.mtcoding.bank.model.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import shop.mtcoding.bank.dto.user.joinReqDto;
import shop.mtcoding.bank.dto.user.loginReqDto;

@Mapper
public interface UserRepository {

    public User findByUsernameAndPassword(loginReqDto loginReqDto);

    public int insert(joinReqDto joinReqDto);

    public int updateById(User user);

    public int deleteById(int id);

    public List<User> findAll();

    public User findById(int id);
}
