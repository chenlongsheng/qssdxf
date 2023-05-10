package cn.cdsoft.common.utils;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * RSA公钥/私钥/签名工具包
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 *
 */
public class RSAEncrypUtil {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    
    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 公钥的key
     */
    public static final String PUBLIC_KEY_V1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUFa5Ci2CPrTvs+Iuz/7NPwQLZSD5buOKpunI9wRw242SlrPNg9RItjnI3AISKU5wW1M50r1fegQB+dKxXKmg7RU/11sLCM+zuQtWXALp/VfhGDfBlecljsbGL3Id16C6lwtDFbaHpx1S18hdjfai+6otrPdzU0PCn0Mdp1eqmkwIDAQAB";
    
    /**
     * 私钥的key
     */
    public static final String PRIVATE_KEY_V1 = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJQVrkKLYI+tO+z4i7P/s0/BAtlIPlu44qm6cj3BHDbjZKWs82D1Ei2OcjcAhIpTnBbUznSvV96BAH50rFcqaDtFT/XWwsIz7O5C1ZcAun9V+EYN8GV5yWOxsYvch3XoLqXC0MVtoenHVLXyF2N9qL7qi2s93NTQ8KfQx2nV6qaTAgMBAAECgYAQ6ITULkvhO9t6zEpPTCPyOEyV+Ky+FlczIAnJSVJqZf5o+F11taXUs84Qv6tii/dnl/bW1eqHil0/Ex1Weo4aAdg9ds8ex3WuDhU49X9j/xQwi0obbgcosUJFCNL7avRCZzvusU4TxqYzmVxj6afOHtqG/IcyrhmkPUv7URvqIQJBANBe7wKt5/vMLk591dUQd21PSE3udoUJbmyCCRRsmWZy9qAvex/bLKbZ4QCjRYUbw1iiSUMXdsn7fYEU/XE+rjECQQC17wgpNlvkkbsSB0FEAqeFTiyaQCwwW/ghMnFmOh8vmpcIr8xUzCQt7Oql2RBjOtmcOF7ff6NuTDhohsiWpVwDAkAuuzvLVAlVFWPFQr90O28tWrySjcqS55yWRUuYAwscSOg64KsyZ8tJ2E93/yYWRjW3aR4ZJ+gCQJ7TFkwI28/hAkA8ij38+Mm7VYMajK97Iib8hPbynUgXD4OSCGcTzWNzQ83eZiFoVkXzwlKSvZhmbL7c7SUk2iRYH6Tlo3Q8LOphAkAiXMet2OTJC0SAEmHezp3JY3Op7HQ9VtzLAR8Jcq+T8l2FM3FjtOBkOrUQZ1rZoUIJO02ddUzhysLEmtY3RC4+";
        /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 生成密钥对(公钥和私钥)
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);//初始大小
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY_V1, publicKey);
        keyMap.put(PRIVATE_KEY_V1, privateKey);
        return keyMap;
    }
    
    /**
     * 
     * 用私钥对信息生成数字签名
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * 
     * 校验数字签名
     * 
     * 
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     * 
     * @return
     * @throws Exception
     * 
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
    	 byte[] keyBytes = Base64.decodeBase64(publicKey);
         X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
         KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
         PublicKey publicK = keyFactory.generatePublic(keySpec);
         Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
         signature.initVerify(publicK);
         signature.update(data);
         return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * 
     * 私钥解密
     * 
     * 
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 
     * 公钥解密
     * 
     * 
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 
     * 公钥加密
     * 
     * 
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 
     * 私钥加密
     * 
     * 
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

//    /**
//     * 
//     * 获取私钥
//     * 
//     * 
//     * @param keyMap 密钥对
//     * @return
//     * @throws Exception
//     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY_V1);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 
     * 获取公钥
     * 
     * 
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY_V1);
        return Base64.encodeBase64String(key.getEncoded());
    }
    
    
    /**
     * 对数据加密 
     * 返回经过Base64编码的加密字符串
     * @param data 需要加密的数据 
     * @param publicKey 公钥
     * @return
     */
    public synchronized static String RSAEncode(String data,String publicKey) throws Exception{
		//加密后的byte数组
		byte[] encryptDataBytes = encryptByPublicKey(data.getBytes("UTF-8"), publicKey);
		//经过base64编码
		return Base64.encodeBase64String(encryptDataBytes);
    }
    
    /**
     * 数据解密
     * 数据是经过Base64编码的加密字符串
     * 返回解密后的字符串
     * @param data
     * @return
     */
    public synchronized static String RSADecode(String privateKey,String data){
        try {
            //加密数据进过Base64解码
            byte[] decodeBytes = Base64.decodeBase64(data);
            //通过私钥对数据解密
            byte[]  decryptData = decryptByPrivateKey(decodeBytes, privateKey);
            return new String(decryptData,"UTF-8");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) throws Exception {
        //生成公钥私钥对
        Map<String, Object> data = genKeyPair();
        String publicKey = getPublicKey(data);
        String privateKey = getPrivateKey(data);
        System.out.println(publicKey);
        String 公钥 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUFa5Ci2CPrTvs+Iuz/7NPwQLZSD5buOKpunI9wRw242SlrPNg9RItjnI3AISKU5wW1M50r1fegQB+dKxXKmg7RU/11sLCM+zuQtWXALp/VfhGDfBlecljsbGL3Id16C6lwtDFbaHpx1S18hdjfai+6otrPdzU0PCn0Mdp1eqmkwIDAQAB";
        String 私钥 = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJQVrkKLYI+tO+z4i7P/s0/BAtlIPlu44qm6cj3BHDbjZKWs82D1Ei2OcjcAhIpTnBbUznSvV96BAH50rFcqaDtFT/XWwsIz7O5C1ZcAun9V+EYN8GV5yWOxsYvch3XoLqXC0MVtoenHVLXyF2N9qL7qi2s93NTQ8KfQx2nV6qaTAgMBAAECgYAQ6ITULkvhO9t6zEpPTCPyOEyV+Ky+FlczIAnJSVJqZf5o+F11taXUs84Qv6tii/dnl/bW1eqHil0/Ex1Weo4aAdg9ds8ex3WuDhU49X9j/xQwi0obbgcosUJFCNL7avRCZzvusU4TxqYzmVxj6afOHtqG/IcyrhmkPUv7URvqIQJBANBe7wKt5/vMLk591dUQd21PSE3udoUJbmyCCRRsmWZy9qAvex/bLKbZ4QCjRYUbw1iiSUMXdsn7fYEU/XE+rjECQQC17wgpNlvkkbsSB0FEAqeFTiyaQCwwW/ghMnFmOh8vmpcIr8xUzCQt7Oql2RBjOtmcOF7ff6NuTDhohsiWpVwDAkAuuzvLVAlVFWPFQr90O28tWrySjcqS55yWRUuYAwscSOg64KsyZ8tJ2E93/yYWRjW3aR4ZJ+gCQJ7TFkwI28/hAkA8ij38+Mm7VYMajK97Iib8hPbynUgXD4OSCGcTzWNzQ83eZiFoVkXzwlKSvZhmbL7c7SUk2iRYH6Tlo3Q8LOphAkAiXMet2OTJC0SAEmHezp3JY3Op7HQ9VtzLAR8Jcq+T8l2FM3FjtOBkOrUQZ1rZoUIJO02ddUzhysLEmtY3RC4+";

        String 密文 = RSAEncrypUtil.RSAEncode("123456",公钥);
        String 原文 = RSAEncrypUtil.RSADecode(私钥,密文);

        System.out.println(密文);
        System.out.println(原文);
    }

}