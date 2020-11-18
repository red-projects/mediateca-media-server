package com.red_projects.mediateca.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.red_projects.mediateca.entities.utils.AccessLevel;

import java.text.ParseException;
import java.util.Date;

public class JwtUtil {

    private static final long HOUR = 3600*1000;
    private static final Jwks jwks = new Jwks(5);

    public static String generateJwt(String subject, AccessLevel accessLevel) {
        try {
            RSAKey jwk = jwks.getJwk();
            JWSSigner signer = new RSASSASigner(jwk);

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .keyID(jwk.getKeyID())
                    .build();

            Date currentDateTime = new Date();
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(subject)
                    .issuer(Jwks.ISSUER)
                    .audience(Jwks.AUDIENCE)
                    .claim("accessLevel", accessLevel.name())
                    .issueTime(currentDateTime)
                    .expirationTime(new Date(currentDateTime.getTime() + HOUR))
                    .build();

            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isJWtValid(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String keyId = signedJWT.getHeader().getKeyID();

            // verify signature
            JWSVerifier verifier = new RSASSAVerifier(jwks.getJwk(keyId));
            if (signedJWT.verify(verifier)) {
                // verify claims
                JWTClaimsSet jwt = signedJWT.getJWTClaimsSet();
                boolean validIssuer = jwt.getIssuer().contentEquals(Jwks.ISSUER);
                boolean validAudience = jwt.getAudience().get(0).contentEquals(Jwks.AUDIENCE);
                boolean validExpiration = jwt.getExpirationTime().after(new Date());
                return validIssuer && validAudience && validExpiration;
            }
            return false;
        } catch (ParseException | NullPointerException | JOSEException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JWTClaimsSet getJwtClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUsername(JWTClaimsSet claimsSet) {
        return claimsSet.getSubject();
    }

    public static boolean isAdmin(JWTClaimsSet claimsSet) {
        String accessLevel = claimsSet.getClaim("accessLevel").toString();
        return accessLevel.contentEquals(AccessLevel.ADMIN.name()) || accessLevel.contentEquals(AccessLevel.OWNER.name());
    }

    public static boolean isOwner(JWTClaimsSet claimsSet) {
        String accessLevel = claimsSet.getClaim("accessLevel").toString();
        return accessLevel.contentEquals(AccessLevel.OWNER.name());
    }

}
