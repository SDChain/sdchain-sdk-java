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

package io.sdchain.core.config;

import io.sdchain.core.encodings.B58IdentiferCodecs;
import io.sdchain.core.encodings.base58.B58;

/**
 * B58 Identifer Codecs
 * @author sean
 */
public class IdentiferCodecs {

    public static final String DEFAULT_ALPHABET = "6pshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCgr5jkm8oFqi1tuvAxyz";

    private static B58IdentiferCodecs b58IdentiferCodecs;

    public static void setAlphabet(String alphabet) {
        B58 b58 = new B58(alphabet);
        b58IdentiferCodecs = new B58IdentiferCodecs(b58);
    }

    /**
     * @return the configured B58IdentiferCodecs object
     */
    public static B58IdentiferCodecs getB58IdentiferCodecs() {
        return b58IdentiferCodecs;
    }
    /***
     * We set up all the defaults here
     */
    static {
        setAlphabet(DEFAULT_ALPHABET);
    }
}
