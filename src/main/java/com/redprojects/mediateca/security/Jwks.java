package com.redprojects.mediateca.security;



import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

import javax.json.*;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Jwks {

    public static final String ISSUER = "client.security.mediateca.red-projects.com";
    public static final String AUDIENCE = "client.server.mediateca.red-projects.com";
    private int numOfKeys;
    private Random random;
    private ArrayList<String> keyIds;
    private JWKSet jwkSet;

    public Jwks(int numOfKeys) {
        this.random = new Random();
        this.numOfKeys = numOfKeys;
        this.keyIds = new ArrayList<>();
        generateKeyPairs();
    }


    private void generateKeyPairs() {
        List<JWK> keys = new ArrayList<>();
        for (int i = 0; i < numOfKeys; i++) {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                KeyPair keyPair = keyPairGenerator.genKeyPair();

                // build jwk
                String keyId = UUID.randomUUID().toString();
                RSAKey jwk = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                        .privateKey((RSAPrivateKey) keyPair.getPrivate())
                        .algorithm(JWSAlgorithm.RS256)
                        .keyUse(KeyUse.SIGNATURE)
                        .keyID(keyId)
                        .build();
                keys.add(jwk);
                keyIds.add(keyId);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        jwkSet = new JWKSet(keys);
    }

    public RSAKey getJwk() {
        int randomNumber = random.nextInt(numOfKeys);
        return (RSAKey) jwkSet.getKeyByKeyId(keyIds.get(randomNumber));
    }

    public RSAKey getJwk(String keyId) {
        return (RSAKey) jwkSet.getKeyByKeyId(keyId);
    }

    public JsonObject getPublicKeys() {
        String publicKeys = jwkSet.toPublicJWKSet().toString();
        JsonReader jsonReader = Json.createReader(new StringReader(publicKeys));
        JsonObject jsonKeys = jsonReader.readObject();
        jsonReader.close();
        return jsonKeys;
    }
}
