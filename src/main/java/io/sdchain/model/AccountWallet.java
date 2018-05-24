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

import io.sdchain.core.SDChainMessage;
import io.sdchain.core.crypto.ecdsa.Seed;
import io.sdchain.exception.InvalidParameterException;
import io.sdchain.util.Utility;

/**
 * Wallet class
 */
public class AccountWallet extends AccountClass {
    @Expose
    private boolean success;
    @Expose
    private long ledger;
    @Expose
    private boolean validated;
    @Expose
    private BalanceCollection balances;
    @Expose
    private PaymentCollection payments;
    @Expose
    private OrderCollection orders;
    @Expose
    private TransactionCollection transactions;
    @Expose
    private Transaction transaction;

    /**
     * Create Wallet instance with address and secret key
     * @param address
     * @param secret
     * @throws InvalidParameterException 
     */
    public AccountWallet(String secret, String address) throws InvalidParameterException {
        if (!Utility.validateKeyPair(address, secret)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_ADDRESS_OR_SECRET, address + secret, null);
        }
        this.address = address;
        this.secret = secret;

    }
    /*
     * Create Wallet instance from secret
     * Only use secret can derive address
     * @param secret
     * @throws InvalidParameterException 
     */
    public AccountWallet(String secret) throws InvalidParameterException {
        if (!Utility.isValidSecret(secret)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_SECRET, secret, null);
        }
        this.address = Seed.computeAddress(secret);
        this.secret = secret;
    }
    /**
     * Get wallet address
     * @return address
     */
    public String getAddress() {
        return this.address;
    }
    /**
     * Get wallet secret key
     * @return secret
     */
    public String getSecret() {
        return this.secret;
    }

    /**
     * get private TransactionCollection instance
     * @return transactions
     */
    public TransactionCollection getTransactionCollection() {
        return this.transactions;
    }

    /**
     * get private Order Collection instance
     * @return orders
     */
    public OrderCollection getOrdersCollection() {
        return orders;
    }
    /**
     * get private PaymentCollection instance
     * @return payments
     */
    public PaymentCollection getPaymentsCollection() {
        return payments;
    }
    /**
     * Get private balance collection
     * @return balances
     */
    public BalanceCollection getBalances() {
        return balances;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
