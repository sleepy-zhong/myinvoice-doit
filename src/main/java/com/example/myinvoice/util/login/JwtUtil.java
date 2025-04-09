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

/**
 * JWT工具类，用于生成和解析JWT Token
 */
@Component
public class JwtUtil {

    // 定义JWT签名所使用的密钥，应保持安全性
    private static final String SECRET = "your-secret-key";  // 可以替换为更强的密钥

    /**
     * 生成JWT Token
     *
     * @param user 用户实体，包含用户名和角色信息
     * @return 生成的JWT Token字符串
     * @throws JOSEException 如果在签名过程中发生错误
     */
    public String createToken(User user) throws JOSEException {
        // 创建JWT Claim，包含用户信息和Token过期时间
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())  // 将用户名存入subject
                .issuer("your-application")
                .expirationTime(new Date(System.currentTimeMillis() + 3600 * 1000))  // Token 1小时过期
                .claim("role", user.getRole())  // 记录角色信息
                .build();

        // 创建JWT头，指定签名算法
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        // 创建带签名的JWT实例
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        // 创建签名器，使用指定的密钥
        MACSigner signer = new MACSigner(SECRET);

        // 对JWT进行签名
        signedJWT.sign(signer);

        // 返回序列化后的JWT Token字符串
        return signedJWT.serialize();
    }

    /**
     * 解析JWT Token
     *
     * @param token JWT Token字符串
     * @return 解析出的用户名
     * @throws ParseException 如果解析JWT时发生错误
     * @throws JOSEException 如果验证JWT签名失败
     */
    public String parseToken(String token) throws ParseException, JOSEException {
        // 解析JWT Token
        SignedJWT signedJWT = SignedJWT.parse(token);
        // 创建验证器，使用指定的密钥
        JWSVerifier verifier = new MACVerifier(SECRET);

        // 验证JWT签名，如果验证成功则返回用户名，否则抛出异常
        if (signedJWT.verify(verifier)) {
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            return claimsSet.getSubject();  // 返回用户名
        } else {
            throw new JOSEException("Invalid JWT signature");
        }
    }
}
