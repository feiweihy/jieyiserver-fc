package com.jieyi.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//DES,DESede
//填充：NoPadding ISO10126Padding OAEPPadding, OAEPWith<digest>And<mgf>Padding PKCS1Padding PKCS5Padding SSL3Padding
//ECB 电子密本方式
//CBC 密文分组链接方式
public class DesUtil {

    /**
     * 加密
     *
     * @param desAlgor
     * @param desMode
     * @param padding
     * @param keybyte
     * @param src
     * @return
     */
    public static byte[] encryptMode(String desAlgor, String desMode, String padding, byte[] keybyte, byte[] src) {
        if (desAlgor == null || desAlgor.equals("")) {
            desAlgor = "DESede";
        }
        if (desMode == null || desMode.equals("")) {
            desMode = "ECB";
        }
        if (padding == null || padding.equals("")) {
            padding = "NoPadding";
        }

        if (desAlgor.equals("DESede")) {
            if (keybyte.length == 16) {
                // java 3des密钥保证为24个字节
                byte[] key1 = NSUtil.getBytes(keybyte, 0, 8);
                keybyte = NSUtil.byteArrayAdd(keybyte, key1);
            }
        }

        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, desAlgor);
            // 加密
            String cipherAlgor = desAlgor + "/" + desMode + "/" + padding;
            Cipher c1 = Cipher.getInstance(cipherAlgor);
            if ("ECB".equals(desMode)) {
                c1.init(Cipher.ENCRYPT_MODE, deskey);
            } else {
                byte[] charArray = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
                // 初始向量默认 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00
                IvParameterSpec iv3 = new IvParameterSpec(charArray);
                c1.init(Cipher.ENCRYPT_MODE, deskey, iv3);
            }
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param desAlgor
     * @param desMode
     * @param padding
     * @param keybyte
     * @param src
     * @return
     */
    public static byte[] decryptMode(String desAlgor, String desMode, String padding, byte[] keybyte, byte[] src) {
        if (desAlgor == null || desAlgor.equals("")) {
            desAlgor = "DESede";
        }
        if (desMode == null || desMode.equals("")) {
            desMode = "ECB";
        }
        if (padding == null || padding.equals("")) {
            padding = "NoPadding";
        }

        if (desAlgor.equals("DESede")) {
            // JAVA 3DES 加密算法为3倍长密钥 XXAAXX
            if (keybyte.length == 16) {
                byte[] b1 = NSUtil.getBytes(keybyte, 0, 8);
                keybyte = NSUtil.byteArrayAdd(keybyte, b1);
            }
        }

        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, desAlgor);
            // 解密
            String cipherAlgor = desAlgor + "/" + desMode + "/" + padding;
            Cipher c1 = Cipher.getInstance(cipherAlgor);

            if ("ECB".equals(desMode)) {
                c1.init(Cipher.DECRYPT_MODE, deskey);
            } else {
                byte[] charArray = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
                // 初始向量默认 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00
                IvParameterSpec iv3 = new IvParameterSpec(charArray);
                c1.init(Cipher.DECRYPT_MODE, deskey, iv3);
            }

            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }


}
