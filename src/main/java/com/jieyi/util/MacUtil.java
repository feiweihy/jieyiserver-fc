package com.jieyi.util;

public class MacUtil {

    // Bit XOR
    public static byte[] BitXor(byte[] Data1, byte[] Data2, int Len) {
        int i;
        byte Dest[] = new byte[Len];

        for (i = 0; i < Len; i++)
            Dest[i] = (byte) (Data1[i] ^ Data2[i]);

        return Dest;
    }

    // 3DesMac
    public static byte[] MAC16(byte[] key, byte[] bInit, byte[] bCiphertext) {
        byte[] key1 = NSUtil.getBytes(key, 0, 8);
        byte[] key2 = NSUtil.getBytes(key, 8, 8);

        byte pbySrcTemp[] = new byte[8];
        byte pbyInitData[] = new byte[8];
        byte pbyDeaSrc[] = new byte[8];
        int i, j, n, iAppend;
        int nCur = 0;
        int iSrcLen = bCiphertext.length;
        n = iSrcLen / 8 + 1;
        iAppend = 8 - (n * 8 - iSrcLen);

        for (nCur = 0; nCur < 8; nCur++)
            pbyInitData[nCur] = bInit[nCur];

        for (i = 0; i < n; i++) {
            for (nCur = 0; nCur < 8; nCur++)
                pbySrcTemp[0] = 0x00;
            if (i == (n - 1)) {
                for (nCur = 0; nCur < iAppend; nCur++)
                    pbySrcTemp[nCur] = bCiphertext[i * 8 + nCur];
                pbySrcTemp[iAppend] = (byte) 0x80;
                for (j = iAppend + 1; j < 8; j++)
                    pbySrcTemp[j] = 0x00;
            } else {
                for (nCur = 0; nCur < 8; nCur++)
                    pbySrcTemp[nCur] = bCiphertext[i * 8 + nCur];
            }

            pbyDeaSrc = BitXor(pbySrcTemp, pbyInitData, 8);

            pbyInitData = DesUtil.encryptMode("DES", "ECB", "NoPadding", key1, pbyDeaSrc);
        }
        pbyDeaSrc = DesUtil.decryptMode("DES", "ECB", "NoPadding", key2, pbyInitData);
        pbyInitData = DesUtil.encryptMode("DES", "ECB", "NoPadding", key1, pbyDeaSrc);

        return pbyInitData;
    }

    // DesMac
    public static byte[] MAC(byte[] cipherKey, byte[] bInit, byte[] bCiphertext) {
        byte pbySrcTemp[] = new byte[8];
        byte pbyInitData[] = new byte[8];
        byte pbyDeaSrc[] = new byte[8];
        int i, j, n, iAppend;
        int nCur = 0;
        int iSrcLen = bCiphertext.length;
        n = iSrcLen / 8 + 1;
        iAppend = 8 - (n * 8 - iSrcLen);

        for (nCur = 0; nCur < 8; nCur++)
            pbyInitData[nCur] = bInit[nCur];

        for (i = 0; i < n; i++) {
            for (nCur = 0; nCur < 8; nCur++)
                pbySrcTemp[0] = 0x00;
            if (i == (n - 1)) {
                for (nCur = 0; nCur < iAppend; nCur++)
                    pbySrcTemp[nCur] = bCiphertext[i * 8 + nCur];
                pbySrcTemp[iAppend] = (byte) 0x80;
                for (j = iAppend + 1; j < 8; j++)
                    pbySrcTemp[j] = 0x00;
            } else {
                for (nCur = 0; nCur < 8; nCur++)
                    pbySrcTemp[nCur] = bCiphertext[i * 8 + nCur];
            }

            pbyDeaSrc = BitXor(pbySrcTemp, pbyInitData, 8);

            pbyInitData = DesUtil.encryptMode("DES", "ECB", "NoPadding", cipherKey, pbyDeaSrc);
        }
        return pbyInitData;
    }

    /*
     * 3DES strKey:密钥,Hex字符串,如:78B49F4BF5B16A17DF4AF5A36E49F4A0.长度必须为32 strInitData:初始因子.长度必须为16,一般为:0000000000000000
     * strMacData:MAC数据,长度必须为偶数
     */
    public static String mac3Des(String strKey, String strInitData, String macDataHex) {

        if ((strKey.length()) != 32) {
            throw new IllegalArgumentException("密钥长度不正确,必须为32");
        }
        if ((strInitData.length()) != 16) {
            throw new IllegalArgumentException("初始因子长度不正确,必须为16");
        }
        byte[] key = NSUtil.hexStringToBytes(strKey);
        byte[] bInit = NSUtil.hexStringToBytes(strInitData);
        byte[] macData = NSUtil.hexStringToBytes(macDataHex);
        byte[] byMac = MAC16(key, bInit, macData);
        return NSUtil.bytesToHexString(byMac);
    }

    /*
     * DES strKey:密钥,Hex字符串,如:78B49F4BF5B16A17 .长度必须为16 strInitData:初始因子.长度必须为16,一般为:0000000000000000
     * strMacData:MAC数据,长度必须为偶数
     */
    public static String macDes(String strKey, String strInitData, String macDataHex) {
        if ((strKey.length()) != 16) {
            throw new IllegalArgumentException("密钥长度不正确,必须为16");
        }
        if ((strInitData.length()) != 16) {
            throw new IllegalArgumentException("初始因子长度不正确,必须为16");
        }

        byte[] key = NSUtil.hexStringToBytes(strKey);
        byte[] bInit = NSUtil.hexStringToBytes(strInitData);
        byte[] macData = NSUtil.hexStringToBytes(macDataHex);
        byte[] byMac = MAC(key, bInit, macData);
        return NSUtil.bytesToHexString(byMac);
    }

    public static String mac3Des(String communicationKey, String macDataHex) {
        String macData = mac3Des(communicationKey, "0000000000000000", macDataHex);
        return macData;
    }

    public static String macDes(String communicationKey, String macDataHex) {
        String macData = macDes(communicationKey, "0000000000000000", macDataHex);
        return macData;
    }

    /**
     * 将原始mac数据进行异或
     *
     * @param hexData
     * @return
     */
    public static String genMacBlock(String hexData) {
        // a.补满16倍数位
        String srcData = hexData;
        int nAddCnt = 16 - hexData.length() % 16;
        if (nAddCnt < 16) {
            for (int i = 0; i < nAddCnt; i++) {
                srcData += "0";
            }
        }

        byte[] byteData = hex2byte(srcData.getBytes());
        int nByteDataLen = srcData.length() / 2;

        String macBlock = "";
        int nXorCnt = nByteDataLen / 8 - 1;

        byte[] byteTmp = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteTmp[i] = byteData[i];
        }

        // b.每8字节进行异或
        for (int i = 1; i <= nXorCnt; i++) {
            byte[] byteTmp1 = new byte[8];
            for (int j = 0; j < 8; j++) {
                byteTmp1[j] = byteData[i * 8 + j];
            }
            byteTmp = BitXor(byteTmp, byteTmp1, 8);
        }

        // c.将异或运算后的最后8个字节（RESULT BLOCK）转换成16个HEXDECIMAL
        macBlock = byte2hex(byteTmp);
        //        logger.debug("src data:" + hexData);
        //        logger.debug("last xor data:" + macBlock);
        return macBlock;
    }

    /**
     * ANSI X9.9算法(非标准)
     *
     * @param hexData
     * @param hexKey
     * @return
     */
    public static String macx99(String hexData, String hexKey) {
        if ((hexKey.length()) != 16) {
            throw new IllegalArgumentException("密钥长度不正确,必须为16");
        }

        // a.补满16倍数位
        String sTmpData = hexData;
        int nAddCnt = 16 - hexData.length() % 16;
        if (nAddCnt < 16) {
            for (int i = 0; i < nAddCnt; i++) {
                sTmpData += "0";
            }
        }
        byte[] byteData = hex2byte(sTmpData.getBytes());
        int nByteDataLen = sTmpData.length() / 2;

        String sHexTmp = "";
        int nXorCnt = nByteDataLen / 8 - 1;

        byte[] byteTmp = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteTmp[i] = byteData[i];
        }

        // b.每8字节进行异或
        for (int i = 1; i <= nXorCnt; i++) {
            byte[] byteTmp1 = new byte[8];
            for (int j = 0; j < 8; j++) {
                byteTmp1[j] = byteData[i * 8 + j];
            }
            byteTmp = BitXor(byteTmp, byteTmp1, 8);
        }

        // c.将异或运算后的最后8个字节（RESULT BLOCK）转换成16个HEXDECIMAL
        sHexTmp = byte2hex(byteTmp);
        //        logger.debug("src data:" + hexData);
        //        logger.debug("hexKey:" + hexKey);
        //        logger.debug("last xor data:" + sHexTmp);

        // d.取前8个字节用MAK加密
        byte[] byteKey = hex2byte(hexKey.getBytes());
        byte[] byteEncLeft = DesUtil
                .encryptMode("DES", "ECB", "NoPadding", byteKey, sHexTmp.substring(0, 8).getBytes());

        // e.将加密后的结果与后8个字节异或
        byte[] byteRight = sHexTmp.substring(8, 16).getBytes();
        byteTmp = BitXor(byteEncLeft, byteRight, 8);

        // f.用异或的结果TEMP BLOCK再进行一次单倍长密钥算法运算
        byte[] byteEncTmp = DesUtil.encryptMode("DES", "ECB", "NoPadding", byteKey, byteTmp);

        // g.将运算后的结果（ENC BLOCK2）转换成16个HEXDECIMAL
        String hexEncTmp = "";
        hexEncTmp = byte2hex(byteEncTmp);

        // h.取前8个字节作为MAC值
        String byteMac = "";
        byteMac = hexEncTmp.substring(0, 8);

        return byteMac;
    }

    private static String byte2hex(byte[] b) { // 一个字节的数，
        // 转成16进制字符串
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            // 整数转成十六进制表示
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase(); // 转成大写
    }

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

}
