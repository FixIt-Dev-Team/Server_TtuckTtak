package com.service.ttucktak.oAuth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.auth.KakaoUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class OAuthService {
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenReqUrl;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String infoReqUrl;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoTokenKey;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    /**
     * 카카오 토큰받기
     * */
    public String getKakaoAccessToken(String authCode){
        String accessToken = "";

        try{
            URL url = new URL(tokenReqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청
            conn.setRequestMethod("POST");
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

            int resCode = conn.getResponseCode();
            log.info("Kakao Token Code : " + resCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while((line = br.readLine()) != null) {
                result.append(line);
            }
            log.info("response at Kakao OAuth Token :" + result.toString());

            JsonObject object = (JsonObject) JsonParser.parseString(result.toString());

            accessToken = object.get("access_token").getAsString();

            br.close();
            bw.close();
        }catch (Exception exception){
            log.error(exception.getMessage());
        }

        return accessToken;
    }

    /**
     * 카카오 정보 받기
     * */
    public KakaoUserDto getKakaoUserInfo(String authToken) throws BaseException{
        try{
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

            while((line = br.readLine()) != null){
                result.append(line);
            }

            log.info("Res Packet at kakao user info : " + result.toString());

            JsonObject object = (JsonObject) JsonParser.parseString(result.toString());

            boolean hasEmail = object.get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            String email;
            if(hasEmail)
                email = object.get("kakao_account").getAsJsonObject().get("email").getAsString();
            else throw new BaseException(BaseErrorCode.KAKAO_EMAIL_NOT_EXIST);

            String birthday = object.get("kakao_account").getAsJsonObject().get("birthday").getAsString();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String[] temp = birthday.split("");
            Date date = format.parse("1998" + "-" + temp[0] + temp[1] + "-" + temp[2] + temp[3]);

            String nickName = object.get("kakao_account")
                    .getAsJsonObject().get("profile")
                    .getAsJsonObject().get("nickname").getAsString();

            KakaoUserDto res = KakaoUserDto.builder().userEmail(email).userName(nickName).birthday(date).build();

            log.info(res.toString());

            return res;

        }catch (BaseException exception){
            throw exception;
        } catch (Exception exception){
            log.error("Exception in getKakaoUserInfo : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.KAKAO_OAUTH_ERROR);
        }
    }
}