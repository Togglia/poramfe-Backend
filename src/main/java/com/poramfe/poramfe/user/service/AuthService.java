package com.poramfe.poramfe.user.service;

import com.poramfe.poramfe.user.UserEntity;
import com.poramfe.poramfe.user.dto.ResponseDto;
import com.poramfe.poramfe.user.dto.SignInDto;
import com.poramfe.poramfe.user.dto.SignInResponseDto;
import com.poramfe.poramfe.user.dto.SignUpDto;
import com.poramfe.poramfe.user.repository.UserRepository;
import com.poramfe.poramfe.user.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseDto<?> signUp(SignUpDto dto) {
        String userEmail = dto.getUserEmail();
        String userPassword = dto.getUserPassword();
        String userPasswordCheck = dto.getUserPasswordcheck();
        String userNickname = dto.getUserNickname();

        //nickname 중복 확인
        try{
            if(userRepository.existsByUserNickname(userNickname))
                return ResponseDto.setFailed("이미 등록된 닉네임입니다!");
        }catch (Exception e){
            return ResponseDto.setFailed("db 에러!");
        }

        //email 중복 확인
        try{
            if(userRepository.existsById(userEmail))
                return ResponseDto.setFailed("이미 등록된 이메일입니다!");
        }catch (Exception e){
            return ResponseDto.setFailed("db 에러!");
        }

        //비밀번호가 서로 다르면 failed response 발생
        if (!userPassword.equals(userPasswordCheck))
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다!");

        //UserEntity 생성
        UserEntity userEntity = new UserEntity(dto);

        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userPassword);
        userEntity.setUserPassword(encodedPassword);

        //UserRepository를 이용해서 db에 entity 저장
        try{
            userRepository.save(userEntity);
        }catch (Exception e){
            return ResponseDto.setFailed("db 에러");
        }
        //성공시 success reponse 반환
        return ResponseDto.setSuccess("회원가입 성공!",null);

    }

    public ResponseDto<SignInResponseDto> signIn(SignInDto dto){
        String userEmail = dto.getUserEmail();
        String userPassword = dto.getUserPassword();

        UserEntity userEntity;
        try {
            userEntity = userRepository.findByUserEmail(userEmail);
            //잘못된 이메일
            if (userEntity == null) return ResponseDto.setFailed(("Sign In Failed"));
            //잘못된 패스워드
            if (!passwordEncoder.matches(userPassword,userEntity.getUserPassword()))
                return ResponseDto.setFailed(("Sign In Failed"));
        }catch (Exception error){
            return ResponseDto.setFailed("db error");
        }
        userEntity.setUserPassword("");

        String token = tokenProvider.create(userEntity.getUserNickname());
        int exprTime = 3600000;

        SignInResponseDto signInResponseDto = new SignInResponseDto(token, exprTime, userEntity);
        return ResponseDto.setSuccess("로그인 성공", signInResponseDto);
    }


}