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

import com.google.gson.annotations.Expose;

/**
 * Currency Amount class
 * Updated with getIssuer() for new interface
 */
public class Amount {
    @Expose
    private String value;
    @Expose
    private String currency;
    @Expose
    private String issuer = ""; // for balance
    @Expose
    private String counterparty = "";// for order,

    public Amount() {
        super();
    }

    public Amount(double value, String currency, String issuerorcounterparty) {
        super();
        this.value = String.valueOf(value);
        this.currency = currency;
        this.issuer = issuerorcounterparty;
        this.counterparty = issuerorcounterparty;
    }

    public String getCurrency() {
        return currency;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(double value) {
        this.value = "" + value;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    @Override
    public String toString() {
        return "Amount [value=" + value + ", currency=" + currency + ", issuer=" + issuer + ", counterparty=" + counterparty + "]";
    }

}
