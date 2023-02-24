package com.service.ttucktak;

import com.service.ttucktak.controller.UserController;
import com.service.ttucktak.dto.auth.PostSigninReqDto;
import com.service.ttucktak.entity.ProfileEntity;
import com.service.ttucktak.entity.UserEntity;
import com.service.ttucktak.repository.ProfileRepository;
import com.service.ttucktak.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(properties = "spring.config.location= classpath:/application.yml,classpath:/secret.yml")
public class UserSignupTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @DisplayName("유저 회원가입 테스트")

    @Transactional
    @Test
    public void createUserTest() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        PostSigninReqDto postSigninReqDto = new PostSigninReqDto();
        postSigninReqDto.setUserName("Rookie");
        postSigninReqDto.setUserPW("1234");
        postSigninReqDto.setUserID("Rookie");
        postSigninReqDto.setEmail("vc54@naver.com");
        postSigninReqDto.setBirthday(format.parse("2023-02-20"));
        postSigninReqDto.setAccountType(0);

        System.out.println(format.parse("2023-02-20"));
        UserEntity entity = postSigninReqDto.toEntity(false);

        UUID userIdx = userRepository.save(entity).getUserIdx();

       UserEntity result = userRepository.findUserEntityByUserIdx(userIdx);

       System.out.println(result.getCreatedAt());

        ProfileEntity profileEntity =
                ProfileEntity.builder()
                        .userIdx(result)
                        .iconType(0)
                        .nickName(result.getUserName())
                        .build();

        UserEntity entity1 = profileRepository.save(profileEntity).getUserIdx();
        System.out.println(entity1.toString());

    }
}
