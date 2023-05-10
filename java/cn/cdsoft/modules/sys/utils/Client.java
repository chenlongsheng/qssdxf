/**
 * 
 */
package cn.cdsoft.modules.sys.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.data.redis.core.index.RedisIndexDefinition.LowercaseIndexValueTransformer;
import org.springframework.util.DigestUtils;

import cn.cdsoft.modules.sys.service.SystemService;

import java.security.MessageDigest;

/**
 * @author admin
 *
 */
public class Client {

    // 测试主函数
    public static void main(String args[]) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        System.out.println(DigestUtils.md5DigestAsHex("cd123%".getBytes()));

    }

}
