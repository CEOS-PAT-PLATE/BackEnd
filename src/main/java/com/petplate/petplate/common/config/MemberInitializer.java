package com.petplate.petplate.common.config;

import com.petplate.petplate.user.domain.Role;
import com.petplate.petplate.user.domain.SocialType;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserRepository;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;


//5E5h7 UN48H 1R37x MS6Wj NTVF0 y05lh 23hpt QXqA5 er0
    //hH9sC zesdV e93z9 uFZVe CX0Af jt8-9 jxQEP DEVnk aKc

    @Override
    public void run(ApplicationArguments args) throws Exception {




        List<User> userList  = new ArrayList<>();

//100만건의 Member 데이터를 저장한다.
        for(int i=0;i<100000;i++) {

            User user  = User.builder()
                    .name(generateRandomString(7))
                    .activated(false)
                    .password("password")
                    .phoneNumber(null)
                    .isReceiveAd(false)
                    .socialType(SocialType.NAVER)
                    .email(generateRandomString(5)+"@naver.com")
                    .role(Role.GENERAL)
                    .socialLoginId(generateRandomString(43))
                    .build();

            System.out.println("현재 i:"+i);
            userList.add(user);


        }



        String sql = "INSERT INTO users (activated,deleted,is_receive_ad,level,password,name,role,social_type,username,email,social_login_id,created_at,updated_at) " +"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
// jdbc 의 batch 를 이용해서 직접 대용량의 데이터를 저장한다. (GenerationType.Identity이므로)
        jdbcTemplate.batchUpdate(sql,
                userList,userList.size(),
                (PreparedStatement ps, User user )->{
                    ps.setBoolean(1,false);
                    ps.setBoolean(2,false);
                    ps.setBoolean(3,false);
                    ps.setInt(4,1);
                    ps.setString(5,user.getPassword());
                    ps.setString(6,user.getName());
                    ps.setString(7,user.getRole().toString());
                    ps.setString(8,user.getSocialType().toString());
                    ps.setString(9,user.getUsername());
                    ps.setString(10,user.getEmail());
                    ps.setString(11,user.getSocialLoginId());
                    ps.setTimestamp(12,null);
                    ps.setTimestamp(13,null);
                });








    }

    //이름을 만들때 랜덤하게 만들기 위한 메서드

    public static String generateRandomString(int length) {
        // 생성할 문자열에 포함될 문자들
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // SecureRandom 객체 생성
        SecureRandom random = new SecureRandom();

        // 랜덤 문자열 생성
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }
}
