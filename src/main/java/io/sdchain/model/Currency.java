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

package io.sdchain.model;

import io.sdchain.core.SDChainMessage;
import io.sdchain.exception.InvalidParameterException;
import io.sdchain.util.Utility;

/**
 * 
 * the type of Currency 
 * 
 */
public class Currency {
    private String currency; // e.g. CNY, USD
    private String counterparty;

    public Currency(String currency, String counterparty) throws InvalidParameterException {
        if (!Utility.isValidCurrency(currency)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_CURRENCY, currency, null);
        }
        this.currency = currency;

        if (!"".equals(counterparty) && !Utility.isValidAddress(counterparty)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_ADDRESS, counterparty, null);
        }
        this.counterparty = counterparty;
    }

    /**
     * @return currency
     */
    public String getCurrency() {
        return currency;
    }
    /**
     * @return counterParty
     */
    public String getIssuer() {
        return counterparty;
    }
}
