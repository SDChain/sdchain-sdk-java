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
/**
 * Payment model class
 */
public class Payment extends SDChainObject {
    private boolean success;
    private String client_resource_id;
    private String hash;
    private StateType state;
    private String result;
    private long date;
    private double fee;
    private String type;
    private String counterparty;
    private Amount amount;
    private EffectCollection effects;
    private String paths;
    private String source_account;
    private String destination_account;
    private Amount source_amount;
    private double source_slippage;
    private Amount destination_amount;
    private MemoCollection memos;

    /**
     * Server state:
     * validated or failed
     */
    public enum StateType {
        validated, failed
    }
    /**
     * payment direction
     *
     */
    public enum Direction {
        incoming, outgoing, all
    }
    /**
     * Get payment paths
     * @return paths
     */
    public String getPaths() {
        return paths;
    }

    public MemoCollection getMemos() {
        return memos;
    }
    /**
     * Get source account in finding payment path
     * @return source account
     */
    public String getSourceAccount() {
        return source_account;
    }
    /**
     * Get destination account in finding payment path
     * @return destination account
     */
    public String getDestinationAccount() {
        return destination_account;
    }
    /**
     * Get destination amount info in finding payment path
     * @return destination_amount
     */
    public Amount getDestinationAmount() {
        return destination_amount;
    }
    /**
     * Get source slippage
     * @return source_sliepage
     */
    public double getSourceSlippage() {
        return source_slippage;
    }
    /**
     * Get source payment needed in finding payment path
     * @return source_amount
     */
    public Amount getSourceAmount() {
        return source_amount;
    }
    /**
     * Get payment effect effect collection
     * @return effects
     */
    public EffectCollection getEffects() {
        return effects;
    }
    /**
     * Get payment type
     * @return type
     */
    public String getType() {
        return type;
    }
    /**
     * Get currency counter party
     * @return counterparty
     */
    public String getCounterparty() {
        return counterparty;
    }
    /**
     * Get payment amount
     * @return amount
     */
    public Amount getAmount() {
        return amount;
    }
    /**
     * Return true if request is successful
     * @return success
     */
    public boolean getSuccess() {
        return success;
    }
    /**
     * Get payment resource id
     * @return client_resource_id
     */
    public String getClient_resource_id() {
        return client_resource_id;
    }
    /**
     * Get payment hash value
     * @return hash
     */
    public String getHash() {
        return hash;
    }
    /**
     * Get state type 
     * @return state
     */
    public StateType getState() {
        return state;
    }
    /**
     * Get server result
     * @return result
     */
    public String getResult() {
        return result;
    }
    /**
     * Get payment time, in UNIXTIME
     * @return date
     */
    public long getDate() {
        return date;
    }
    /**
     * Get payment transaction fee, in SDA
     * @return fee
     */
    public double getFee() {
        return fee;
    }
}
