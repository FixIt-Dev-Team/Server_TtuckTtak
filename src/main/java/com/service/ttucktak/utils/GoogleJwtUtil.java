package com.service.ttucktak.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
@Slf4j
public class GoogleJwtUtil {

    private final GoogleIdTokenVerifier verifier;

    public GoogleJwtUtil(@Value("${jwt.client-id}") String CLIENT_ID){

        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
    }

    public GoogleIdToken CheckGoogleIdTokenVerifier (String idTokenString) throws BaseException{

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException e) {
            log.error("Google ID token verification GeneralSecurityException error (GoogleJwtUtil)",e);
            throw new BaseException(BaseErrorCode.GOOGLE_GENERALSECURITY_EXCEPTION);
        } catch (IOException e) {
            log.error("Google ID token verification IOException error (GoogleJwtUtil)",e);
            throw new BaseException(BaseErrorCode.GOOGLE_IOEXCEPTION);
        }

        return idToken;

    }

}
