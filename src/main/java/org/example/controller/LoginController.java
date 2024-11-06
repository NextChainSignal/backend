package org.example.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    // 假设我们从区块链上获取 DID 和公钥
    private static final String STORED_PUBLIC_KEY = "0xYourEthereumPublicKeyHere";  // 区块链中存储的用户公钥

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 验证签名
            boolean isVerified = verifySignature(loginRequest.getAccount(), loginRequest.getChallenge(), loginRequest.getSignature());

            if (isVerified) {
                response.put("success", true);
            } else {
                response.put("success", false);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    private boolean verifySignature(String account, String challenge, String signature) {
        try {
            // 从存储的公钥中构建 BigInteger 对象
            BigInteger publicKey = Numeric.toBigInt(STORED_PUBLIC_KEY.replace("0x", ""));

            // 将签名从十六进制字符串转换为字节数组
            byte[] signatureBytes = Numeric.hexStringToByteArray(signature);

            // Ethereum 签名是由 v, r, s 三部分组成的
            // Ethereum 签名格式：0x {r} {s} {v}，其中 {r} 和 {s} 是签名的数字部分，{v} 是恢复标识符
            BigInteger r = new BigInteger(1, new byte[32]);
            BigInteger s = new BigInteger(1, new byte[32]);
            byte v = 0;

            // 提取 r, s, v 三个部分
            System.arraycopy(signatureBytes, 0, r.toByteArray(), 0, 32);
            System.arraycopy(signatureBytes, 32, s.toByteArray(), 0, 32);
            v = signatureBytes[64];

            // 使用恢复标识符和 r, s，v 来恢复公钥并验证
            Sign.SignatureData signatureData = new Sign.SignatureData(v, r.toByteArray(), s.toByteArray());

            // 使用 challenge 进行签名验证
            String message = challenge;
            byte[] messageHash = message.getBytes();

            // 使用公钥和 challenge 验证签名
            Sign.SignatureData recoveredSignature = Sign.signMessage(messageHash, ECKeyPair.create(publicKey));

            return recoveredSignature.equals(signatureData);
        } catch (Exception e) {
            return false;
        }
    }

    // 登录请求体
    public static class LoginRequest {
        private String account;
        private String challenge;
        private String signature;

        // getters and setters
        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getChallenge() {
            return challenge;
        }

        public void setChallenge(String challenge) {
            this.challenge = challenge;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
}
