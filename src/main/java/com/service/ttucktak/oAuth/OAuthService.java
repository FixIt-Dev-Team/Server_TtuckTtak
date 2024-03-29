package com.service.ttucktak.oAuth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.auth.SocialAccountUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class OAuthService {
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenReqUrl;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String infoReqUrl;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoTokenKey;
    private String redirectUri = "https://ttukttak.store/api/auths/oauth2/kakao";

    private final String defaultImageUrl = ""; // Todo: 뚝딱 서비스 default image url 설정

    /**
     * 카카오 토큰받기
     */
    public String getKakaoAccessToken(String authCode) throws BaseException {
        String accessToken = "";

        try {
            URL url = new URL(tokenReqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true);

            //요청 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + kakaoTokenKey);
            sb.append("&redirect_uri=" + redirectUri);
            sb.append("&code=" + authCode);

            bw.write(sb.toString());
            bw.flush();

            log.info(sb.toString());
            int resCode = conn.getResponseCode();
            log.info("Kakao Token Code : " + resCode);
            log.info("msg : " + conn.getResponseMessage());

            //요청 받기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            log.info("response at Kakao OAuth Token :" + result.toString());

            JsonObject object = (JsonObject) JsonParser.parseString(result.toString());

            //카카오 access token
            accessToken = object.get("access_token").getAsString();

            br.close();
            bw.close();

        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.KAKAO_OAUTH_ERROR);
        }

        return accessToken;
    }

    /**
     * 카카오 정보 받기
     */
    public SocialAccountUserInfo getKakaoUserInfo(String authToken) throws BaseException {
        try {
            //Target URL
            URL url = new URL(infoReqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //Connection 정의
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + authToken);

            //Response 받음
            int responseCode = conn.getResponseCode();
            log.info("Res Code at Kakao user info : " + responseCode);

            //Response Parsing
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            log.info("Res Packet at kakao user info : " + result.toString());

            //카카오 유저 정보 파싱 - Start
            JsonObject object = (JsonObject) JsonParser.parseString(result.toString());

            //이메일 있는지 여부
            boolean hasEmail = object.get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            String email;
            if (hasEmail)
                email = object.get("kakao_account").getAsJsonObject().get("email").getAsString();
            else throw new BaseException(BaseErrorCode.KAKAO_EMAIL_NOT_EXIST);

            String nickName = object.get("kakao_account")
                    .getAsJsonObject().get("profile")
                    .getAsJsonObject().get("nickname").getAsString();

            String imgUrl;

            // 프로필 이미지 권한이 있는 경우 해당 이미지를 사용하고
            // 프로필 이미지 권한이 없는 경우 뚝딱 서비스 default image url로 설정한다.
            boolean needProfileImageAgreement = object.get("kakao_account")
                    .getAsJsonObject().get("profile_image_needs_agreement")
                    .getAsBoolean();

            if (needProfileImageAgreement) {
                imgUrl = defaultImageUrl;
            } else {
                imgUrl = object.get("kakao_account")
                        .getAsJsonObject().get("profile")
                        .getAsJsonObject().get("profile_image_url").getAsString();
            }

            SocialAccountUserInfo res = SocialAccountUserInfo.builder()
                    .userName(nickName)
                    .userEmail(email)
                    .imgURL(imgUrl)
                    .build();

            log.info(res.toString());

            return res;
            //카카오 유저 정보 파싱 - end

        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            log.error("Exception in getKakaoUserInfo : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.KAKAO_OAUTH_ERROR);
        }
    }

    public SocialAccountUserInfo getGoogleUserInfo(GoogleIdToken idToken) throws BaseException {
        try {

            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");

            log.info("user info | name :" + name + " | email : " + email + " | imgURL : " + pictureUrl);

            SocialAccountUserInfo res = SocialAccountUserInfo.builder()
                    .userName(name)
                    .userEmail(email)
                    .imgURL(pictureUrl)
                    .build();

            log.info(res.toString());

            return res;
            //구글 유저 정보 파싱 - end

        } catch (Exception exception) {
            log.error("Exception in getGoogleUserInfo : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.GOOGLE_OAUTH_ERROR);
        }
    }
}
