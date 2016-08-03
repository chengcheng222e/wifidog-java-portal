/*
 * Copyright (c) 2016, fangstar.com
 *
 * All rights reserved.
 */
package net.fangstar.wifi.portal.util;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编码工具.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.1.0, Jun 13, 2016
 * @since 1.0.0
 */
public final class Codes {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Codes.class);

    /**
     * AES 加密.
     *
     * @param content 指定的加密内容（明文）
     * @param key 指定的密钥
     * @return 加密结果（密文）
     * @see #decryptByAES(java.lang.String, java.lang.String)
     */
    public static String encryptByAES(final String content, final String key) {
        try {
            final KeyGenerator kgen = KeyGenerator.getInstance("AES");
            final SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes());
            kgen.init(128, secureRandom);
            final SecretKey secretKey = kgen.generateKey();
            final byte[] enCodeFormat = secretKey.getEncoded();
            final SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
            final Cipher cipher = Cipher.getInstance("AES");
            final byte[] byteContent = content.getBytes("UTF-8");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            final byte[] result = cipher.doFinal(byteContent);

            return Hex.encodeHexString(result);
        } catch (final Exception e) {
            LOGGER.error("Encrypt failed", e);

            return null;
        }
    }

    /**
     * AES 解密.
     *
     * @param content 指定的解密内容（密文）
     * @param key 指定的密钥
     * @return 解密结果（明文）
     * @see #encryptByAES(java.lang.String, java.lang.String)
     */
    public static String decryptByAES(final String content, final String key) {
        try {
            final byte[] data = Hex.decodeHex(content.toCharArray());
            final KeyGenerator kgen = KeyGenerator.getInstance("AES");
            final SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes());
            kgen.init(128, secureRandom);
            final SecretKey secretKey = kgen.generateKey();
            final byte[] enCodeFormat = secretKey.getEncoded();
            final SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            final byte[] result = cipher.doFinal(data);

            return new String(result, "UTF-8");
        } catch (final Exception e) {
            LOGGER.error("Decrypt failed", e);

            return null;
        }
    }

    private Codes() {
    }
}
