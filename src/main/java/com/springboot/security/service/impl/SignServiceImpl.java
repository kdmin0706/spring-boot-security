package com.springboot.security.service.impl;

import com.springboot.security.common.CommonResponse;
import com.springboot.security.config.security.JwtTokenProvider;
import com.springboot.security.data.dto.SignInResultDto;
import com.springboot.security.data.dto.SignUpResultDto;
import com.springboot.security.data.entity.User;
import com.springboot.security.data.repository.UserRepository;
import com.springboot.security.service.SignService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {
    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignUpResultDto signUp(String id, String password, String name, String role) {
        LOGGER.info("[getSignUpResult] 회원 가입 정보 전달");
        User user;
        if (role.equalsIgnoreCase("admin")) {
            user = User.builder()
                    .uid(id)
                    .name(name)
                    .password(passwordEncoder.encode(password))
                    .roles(Collections.singletonList("ROLE_ADMIN"))
                    .build();
        } else {
            user = User.builder()
                    .uid(id)
                    .name(name)
                    .password(passwordEncoder.encode(password))
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        }

        User savedUser = userRepository.save(user);
        SignInResultDto signInResultDto = new SignInResultDto();

        LOGGER.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");
        if (!savedUser.getName().isEmpty()) {
            LOGGER.info("[getSignUpResult] 정상 처리 완료");
            setSuccessResult(signInResultDto);
        } else {
            LOGGER.info("[getSignUpResult] 정상 처리 완료");
            setFailResult(signInResultDto);
        }

        return signInResultDto;
    }

    private void setFailResult(SignInResultDto signInResultDto) {
        signInResultDto.setSuccess(false);
        signInResultDto.setCode(CommonResponse.FAIL.getCode());
        signInResultDto.setMsg(CommonResponse.FAIL.getMsg());
    }

    private void setSuccessResult(SignInResultDto signInResultDto) {
        signInResultDto.setSuccess(true);
        signInResultDto.setCode(CommonResponse.SUCCESS.getCode());
        signInResultDto.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    @Override
    public SignInResultDto signIn(String id, String password) throws RuntimeException {
        LOGGER.info("[getSignInResult] signDataHandler 로 회원 정보 요청");
        User user = userRepository.getByUid(id);
        LOGGER.info("[getSignInResult] Id : {}", id);

        LOGGER.info("[getSignInResult] 패스워드 비교 수행");
        if (!passwordEncoder.matches(password, user.getPassword())) {
            LOGGER.info("[getSignInResult] 패스워드 불일치");
            throw new RuntimeException();
        }

        LOGGER.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtTokenProvider.createToken(
                        String.valueOf(user.getUid()), user.getRoles()))
                .build();

        LOGGER.info("[getSignInResult] SignInResultDto 객체 주입");
        setSuccessResult(signInResultDto);

        return signInResultDto;
    }
}
