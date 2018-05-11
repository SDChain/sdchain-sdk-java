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

package io.sdchain.core.encodings;

import io.sdchain.core.encodings.base58.B58;

public class B58IdentiferCodecs {
    public static final int VER_ACCOUNT_ID = 0;
    public static final int VER_FAMILY_SEED = 33;

    B58 b58;

    public B58IdentiferCodecs(B58 base58encoder) {
        this.b58 = base58encoder;
    }

    public byte[] decode(String d, int version) {
        return b58.decodeChecked(d, version);
    }

    public String encode(byte[] d, int version) {
        return b58.encodeToStringChecked(d, version);
    }

    public byte[] decodeFamilySeed(String master_seed) {
        byte[] bytes = b58.decodeChecked(master_seed, VER_FAMILY_SEED);
        return bytes;
    }

    public String encodeAddress(byte[] bytes) {
        return encode(bytes, VER_ACCOUNT_ID);
    }

    public byte[] decodeAddress(String address) {
        return decode(address, VER_ACCOUNT_ID);
    }

    public String encodeFamilySeed(byte[] bytes) {
        return encode(bytes, VER_FAMILY_SEED);
    }
}
