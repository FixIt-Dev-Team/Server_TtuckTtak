package com.service.ttucktak.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
@Slf4j
public class GoogleJwtUtil {

    private static final String CLIENT_ID = "500809518937-2oeb6srpcf4u4p5mk3kk5e6rashj9qv0.apps.googleusercontent.com";

    private GoogleIdTokenVerifier verifier;

    public GoogleJwtUtil(){
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
    }

    public GoogleIdToken CheckGoogleIdTokenVerifier (String idTokenString){

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException e) {
            log.error("Google ID token verification GeneralSecurityException error",e);
        } catch (IOException e) {
            log.error("Google ID token verification IOException error",e);
        }

        return idToken;

    }

}
