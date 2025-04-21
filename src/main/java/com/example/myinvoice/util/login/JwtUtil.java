//package com.example.myinvoice.util.login;
//
//import com.example.myinvoice.Entity.User;
//import com.nimbusds.jose.*;
//import com.nimbusds.jose.crypto.MACSigner;
//import com.nimbusds.jose.crypto.MACVerifier;
//import com.nimbusds.jwt.JWTClaimsSet;
//import com.nimbusds.jwt.SignedJWT;
//import org.springframework.beans.factory.annotation.Value;
//import java.nio.charset.StandardCharsets;
//import org.springframework.stereotype.Component;
//
//import java.text.ParseException;
//import java.util.Date;
//
///**
// * JWT工具类，用于生成和解析JWT Token
// */
//@Component
//public class JwtUtil {
//
//    // 通过@Value注入配置值
//    @Value("${jwt.secret}")
//    private String secret;  // 移除static final
//
//    @Value("${jwt.expiration}")
//    private long expiration;
//
//    public String createToken(User user) throws JOSEException {
//        // 添加长度校验（防御性编程）
//        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
//            throw new IllegalArgumentException("密钥必须≥32字节");
//        }
//
//        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
//                .subject(user.getUsername())
//                .expirationTime(new Date(System.currentTimeMillis() + expiration * 1000))
//                .claim("id", user.getId())       // 添加用户唯一标识
//                .claim("role", user.getRole())
//                .build();
//
//        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
//        signedJWT.sign(new MACSigner(secret));  // 使用注入的密钥
//        return signedJWT.serialize();
//    }
//
//    public String parseToken(String token) throws ParseException, JOSEException {
//        SignedJWT signedJWT = SignedJWT.parse(token);
//        if (!signedJWT.verify(new MACVerifier(secret))) {  // 使用注入的密钥
//            throw new JOSEException("Invalid JWT signature");
//        }
//        return signedJWT.getJWTClaimsSet().getSubject();
//    }
//    public Long parseUserId(String token) throws ParseException, JOSEException {
//        SignedJWT signedJWT = SignedJWT.parse(token);
//        if (!signedJWT.verify(new MACVerifier(secret))) {
//            throw new JOSEException("Invalid JWT signature");
//        }
//
//        // 从 Token 中提取用户 ID
//        return signedJWT.getJWTClaimsSet().getLongClaim("id");
//    }
//}
//
package com.example.myinvoice.util.login;

import com.example.myinvoice.Entity.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;

/**
 * JWT工具类，用于生成和解析JWT Token
 */
@Component
public class JwtUtil {

    // 通过@Value注入配置值
    @Value("${jwt.secret}")
    private String secret;  // 移除static final

    @Value("${jwt.expiration}")
    private long expiration;

    public String createToken(User user) throws JOSEException {
        // 添加长度校验（防御性编程）
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("密钥必须≥32字节");
        }

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .expirationTime(new Date(System.currentTimeMillis() + expiration * 1000))
                .claim("role", user.getRole())
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(new MACSigner(secret));  // 使用注入的密钥
        return signedJWT.serialize();
    }

    public String parseToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        if (!signedJWT.verify(new MACVerifier(secret))) {  // 使用注入的密钥
            throw new JOSEException("Invalid JWT signature");
        }
        return signedJWT.getJWTClaimsSet().getSubject();
    }
}

