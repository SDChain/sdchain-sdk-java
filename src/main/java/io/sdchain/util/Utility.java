/*
 * Copyright SDChain Alliance
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.sdchain.util;

import static io.sdchain.core.config.IdentiferCodecs.getB58IdentiferCodecs;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.sdchain.core.SDChain;
import io.sdchain.core.SDChainMessage;
import io.sdchain.core.crypto.ecdsa.IKeyPair;
import io.sdchain.core.crypto.ecdsa.KeyPair;
import io.sdchain.core.crypto.ecdsa.Seed;
import io.sdchain.exception.InvalidParameterException;
import io.sdchain.model.Amount;
import io.sdchain.model.Memo;
/**
 * Utility class
 */
public class Utility {
    @SuppressWarnings("unused")
    private boolean success;
    final protected static char[] hexArray = "0123456789abcdef".toCharArray();

    /**
     * Convert byte to hex
     * @param bytes Input byte array to convert to HEX codes
     * @return String
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    /**
     * Build signature string
     * @param address
     * @param secret
     * @return String
     */
    public static String buildSignString(String address, String secret) {

        long timestamp = System.currentTimeMillis();
        String messageString = SDChain.getSignString() + address + timestamp;
        IKeyPair keyPair = null;
        try {
            keyPair = Seed.getKeyPair(secret);
        } catch (InvalidParameterException e1) {
            e1.printStackTrace();
        }
        // get hash
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] digest = md.digest(messageString.getBytes());
        byte[] signData = Arrays.copyOfRange(digest, 0, 32);

        // get signature
        byte[] signature = KeyPair.sign(signData, keyPair.priv());

        // transmit hex parameters, they are what we actually need
        String messageHex = Utility.bytesToHex(signData);
        String signatureHex = Utility.bytesToHex(signature);
        String publicKeyHex = keyPair.pubHex();

        StringBuffer sb = new StringBuffer();
        sb.append("?k=");
        sb.append(publicKeyHex);
        sb.append("&s=");
        sb.append(signatureHex);
        sb.append("&h=");
        sb.append(messageHex);
        sb.append("&t=");
        sb.append(timestamp);
        return sb.toString();
    }

    /**
     * @param aValue
     * @param aKey
     * @return hmac string
     */
    public static String buildHmac(String aValue, String aKey) {
        byte k_ipad[] = new byte[64];
        byte k_opad[] = new byte[64];
        byte keyb[];
        byte value[];
        try {
            keyb = aKey.getBytes("UTF-8");
            value = aValue.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            keyb = aKey.getBytes();
            value = aValue.getBytes();
        }

        Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
        Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
        for (int i = 0; i < keyb.length; i++) {
            k_ipad[i] = (byte) (keyb[i] ^ 0x36);
            k_opad[i] = (byte) (keyb[i] ^ 0x5c);
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md.update(k_ipad);
        md.update(value);
        byte dg[] = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 16);
        dg = md.digest();
        return Utility.bytesToHex(dg);

    }
    /**
     * Validate the address and secret pair
     * @param address
     * @param secret
     * @return true if the address and secret is valid
     */
    public static boolean validateKeyPair(String address, String secret) {
        String myAddress = null;
        try {
            myAddress = Seed.computeAddress(secret); // compute address from secret
        } catch (Exception e) {
            return false;
        }
        if (Utility.isNotEmpty(address) && address.equals(myAddress)) {
            return true;
        }
        return false;
    }
    /**
     * Verify if an address is valid
     * @param address
     * @return true if address is valid
     */
    public static boolean isValidAddress(String address) {
        try {
            getB58IdentiferCodecs().decodeAddress(address);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
     * Verify if the secret is valid
     * @param secret
     * @return true if secret is valid
     */
    @SuppressWarnings("unused")
    public static boolean isValidSecret(String secret) {
        try {
            String myAddress = Seed.computeAddress(secret);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
     * Verify if the currency is valid
     * @param currency
     * @return true if the currency is valid
     */
    public static boolean isValidCurrency(String currency) {
        // Currently only check the length of the currency
        if (currency != null && (currency.length() == 3 || currency.length() == 40)) {
            return true;
        }
        return false;
    }
    /**
     * Return true if string is not null or empty
     * @param str
     * @return true if string is not null or empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    /**
     * Return true if string is null or empty
     * @param str
     * @return true if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str);
    }
    /**
     * Get the String format of double value
     * @param value
     * @return String 
     */
    public static String doubleToString(double value) {
        DecimalFormat df = new DecimalFormat("#0.0######");
        return df.format(value);
    }
    /**
     * Check if the SDChainAmount is valid
     * @param amount
     * @return true if the SDChainAmount is valid
     */
    public static boolean isValidAmount(Amount amount) {
        return (amount != null) && isValidCurrency(amount.getCurrency()) && isValidAddress(amount.getIssuer())
            || ((amount != null) && SDChain.getCurrencySDA().equals(amount.getCurrency()) && "".equals(amount.getIssuer()));
    }
    /**
     * Check if the Memo is valid
     * @param in_memo
     * @return true if the SDChainAmount is valid
     */
    public static boolean isValidMemo(Memo in_memo) {
        return (in_memo != null);
        // TO DO:make better check
    }

    /**
     * sync call
     */
    public static void callback(Runnable runnable) {
        // Later all runnables in one ExecutorService?
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(runnable);
        exec.shutdown();
    }
    /**
     * Verify if an input string is a valid Tum code
     * with the format
     * Tum code : Tum issuer
     * @param in_str
     * @return true if address is valid
     */
    public static boolean isValidTumString(String in_str) {

        String[] str_array = in_str.split(":");

        if (str_array.length == 2) {
            // check if the 2nd param is a valid SDChain address
            if (Utility.isValidAddress(str_array[1]))
                return true;

        }
        return false;
    }
    /**
     * Return the Tum string from the input Amount object
     * @param in_amt
     * @return String of Tum as currency:issuer
     */

    public static String getTumString(Amount in_amt) throws InvalidParameterException {

        if (isValidAmount(in_amt)) {
            if (in_amt.getCurrency() == "SDA") {
                return in_amt.getCurrency();
            } else {
                return in_amt.getCurrency() + ":" + in_amt.getIssuer();
            }
        } else
            throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_AMOUNT, in_amt.getCurrency() + ":" + in_amt.getIssuer(), null);
    }

    /**
     * Return the Tum pair string from the input base and counter Amount objects
     * @param base_amt
     * @param counter_amt
     * @return String of Tum as currency:issuer
     */
    public static String getTumPair(Amount base_amt, Amount counter_amt) throws InvalidParameterException {

        return getTumString(base_amt) + "/" + getTumString(counter_amt);

    }

    /**
     * @Title: hexStringToString   
     * @Description: TODO  
     * @param: @param s
     * @param: @return      
     * @return: String      
     * @throws
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                    s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

}