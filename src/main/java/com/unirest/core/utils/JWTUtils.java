package com.unirest.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureAlgorithm;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;

public class JWTUtils {
    private static final SignatureAlgorithm alg = Jwts.SIG.ES256;
    private static KeyPair pair;

    static {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed("UniRestSaveSecretKey".getBytes());
            keyPairGenerator.initialize(256, secureRandom);
            pair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException ignored) {
        }
    }

    public static JwtBuilder create(IToken... token) {
        HashMap<String, Object> map = new HashMap<>();
        for (IToken iToken : token) {
            map.putAll(iToken.getValues());
        }
        return Jwts.builder()
                .subject(token[0].getSubject())
                .issuer(token[0].getIssuer())
                .claims(map)
                .signWith(pair.getPrivate(), alg);
    }

    public static Claims parse(String jwtToken) {
        try {
            return Jwts.parser()
                    .verifyWith(pair.getPublic())
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();
        } catch (JwtException ignored) {
        }
        return null;
    }

}