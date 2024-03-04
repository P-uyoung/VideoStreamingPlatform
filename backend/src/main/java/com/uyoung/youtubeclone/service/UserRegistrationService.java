package com.uyoung.youtubeclone.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uyoung.youtubeclone.dto.UserInfoDto;
import com.uyoung.youtubeclone.model.User;
import com.uyoung.youtubeclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    @Value("${auth0.userinfoEndpoint}")
    private String userInfoEndpoint;

    private final UserRepository userRepository;

    public void loginUser(String tokenValue) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userInfoEndpoint))
                .setHeader("Authorization", String.format("Bearer %s", tokenValue))
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        try {                                                                                                           // https://uyoung-videostreaming.jp.auth0.com/userinfo 호출시
            HttpResponse<String> responseString = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());   // postman 에서 확인가능
            String body = responseString.body();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  // Json에서 Java로 역직렬화할 때, Java 객채에 없는 속성이 있더라도 오류 발생 No
            UserInfoDto userInfoDTO = objectMapper.readValue(body, UserInfoDto.class);

            // 데이터베이스에서 사용자의 'sub' 값을 기준으로 사용자 존재 여부 확인
            Optional<User> existingUser = userRepository.findBySub(userInfoDTO.getSub());

            if (existingUser.isEmpty()) {

                User user = new User();
                user.setFirstName(userInfoDTO.getGivenName());
                user.setLastName(userInfoDTO.getFamilyName());
                user.setFullName(userInfoDTO.getName());
                user.setEmailAddress(userInfoDTO.getEmail());
                user.setSub(userInfoDTO.getSub());

                userRepository.save(user);
            }

        } catch (Exception exception) {
            throw new RuntimeException("Exception occurred while registering user", exception);
        }

    }
}