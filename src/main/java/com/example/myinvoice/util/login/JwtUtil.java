package com.example.myinvoice.util.login;

import com.example.myinvoice.Entity.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
@Component
public class JwtUtil {

    private static final String SECRET = "your-secret-key";  // 可以替换为更强的密钥

    // 生成 JWT Token
    public String createToken(User user) throws JOSEException {
        // Create JWT Claims
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())  // 将用户名存入subject
                .issuer("your-application")
                .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000))  // Token 1小时过期
                .claim("role", user.getRole())  // 记录角色信息
                .build();

        // Create JWT header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        // Create SignedJWT
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        // Create a signer with the specified secret
        MACSigner signer = new MACSigner(SECRET);

        // Sign the JWT
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    // 解析 JWT Token
    public String parseToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SECRET);

        if (signedJWT.verify(verifier)) {
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            return claimsSet.getSubject();  // 返回用户名
        } else {
            throw new JOSEException("Invalid JWT signature");
        }
    }
}
