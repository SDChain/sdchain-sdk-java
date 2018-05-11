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

package io.sdchain.core.crypto.ecdsa;

import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import io.sdchain.core.utils.Utils;

import java.math.BigInteger;

public class KeyPair implements IKeyPair {
    BigInteger priv, pub;

    public BigInteger pub() {
        return pub;
    }

    public KeyPair(BigInteger priv, BigInteger pub) {
        this.priv = priv;
        this.pub = pub;
    }

    public BigInteger priv() {
        return priv;
    }

    public byte[] sign(byte[] bytes) {
        return sign(bytes, priv);
    }

    public String pubHex() {
        return Utils.bigHex(pub);
    }

    public static byte[] sign(byte[] bytes, BigInteger secret) {
        ECDSASignature sig = createECDSASignature(bytes, secret);
        byte[] der = sig.encodeToDER();
        if (!isStrictlyCanonical(der)) {
            throw new IllegalStateException("Signature is not strictly canonical");
        }
        return der;
    }
    public static boolean isStrictlyCanonical(byte[] sig) {
        return checkIsCanonical(sig, true);
    }

    public static boolean checkIsCanonical(byte[] sig, boolean strict) {
        // Make sure signature is canonical
        // To protect against signature morphing attacks

        // Signature should be:
        // <30> <len> [ <02> <lenR> <R> ] [ <02> <lenS> <S> ]
        // where
        // 6 <= len <= 70
        // 1 <= lenR <= 33
        // 1 <= lenS <= 33

        int sigLen = sig.length;

        if ((sigLen < 8) || (sigLen > 72))
            return false;

        if ((sig[0] != 0x30) || (sig[1] != (sigLen - 2)))
            return false;

        // Find R and check its length
        int rPos = 4, rLen = sig[rPos - 1];

        if ((rLen < 1) || (rLen > 33) || ((rLen + 7) > sigLen))
            return false;

        // Find S and check its length
        int sPos = rLen + 6, sLen = sig[sPos - 1];
        if ((sLen < 1) || (sLen > 33) || ((rLen + sLen + 6) != sigLen))
            return false;

        if ((sig[rPos - 2] != 0x02) || (sig[sPos - 2] != 0x02))
            return false; // R or S have wrong type

        if ((sig[rPos] & 0x80) != 0)
            return false; // R is negative

        if ((sig[rPos] == 0) && rLen == 1)
            return false; // R is zero

        if ((sig[rPos] == 0) && ((sig[rPos + 1] & 0x80) == 0))
            return false; // R is padded

        if ((sig[sPos] & 0x80) != 0)
            return false; // S is negative

        if ((sig[sPos] == 0) && sLen == 1)
            return false; // S is zero

        if ((sig[sPos] == 0) && ((sig[sPos + 1] & 0x80) == 0))
            return false; // S is padded

        byte[] rBytes = new byte[rLen];
        byte[] sBytes = new byte[sLen];

        System.arraycopy(sig, rPos, rBytes, 0, rLen);
        System.arraycopy(sig, sPos, sBytes, 0, sLen);

        BigInteger r = new BigInteger(1, rBytes), s = new BigInteger(1, sBytes);

        BigInteger order = SECP256K1.order();

        if (r.compareTo(order) != -1 || s.compareTo(order) != -1) {
            return false; // R or S greater than modulus
        }
        if (strict) {
            return order.subtract(s).compareTo(s) != -1;
        } else {
            return true;
        }

    }

    private static ECDSASignature createECDSASignature(byte[] bytes, BigInteger secret) {
        ECDSASigner signer = new ECDSASigner();
        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(secret, SECP256K1.params());
        signer.init(true, privKey);
        BigInteger[] sigs = signer.generateSignature(bytes);
        BigInteger r = sigs[0], s = sigs[1];

        BigInteger otherS = SECP256K1.order().subtract(s);
        if (s.compareTo(otherS) == 1) {
            s = otherS;
        }

        return new ECDSASignature(r, s);
    }
}
