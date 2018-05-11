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
/** * 
 * Transaction model class
 */
public class Transaction extends SDChainObject {
    @Expose
    private long date;
    @Expose
    private String hash;
    @Expose
    private TranType type;
    @Expose
    private String offertype;
    @Expose
    private int seq;
    @Expose
    private String fee;
    @Expose
    private String result;
    @Expose
    private String client_resource_id;
    @Expose
    private String counterparty;
    @Expose
    private Amount amount;
    @Expose
    private Amount gets;
    @Expose
    private Amount pays;
    @Expose
    private EffectCollection effects;
    @Expose
    private MemoCollection memos;

    /**
     * Transaction type:
     *  sent, received, trusted, trusting,
     *	convert, offernew, offercancel, offereffect,
     *	accountset, sdchaining, failed
     *
     */
    public enum TranType {
        sent, received, trusted, trusting, convert, offernew, offercancel, offereffect, accountset, sdchaining, failed
    }
    /**
     * Direction type:
     * incoming, outgoing
     *
     */
    public enum DirectionType {
        incoming, outgoing, all
    }
    /**
     * Get date, in UNIXTIME
     * @return date
     */
    public long getDate() {
        return date;
    }
    /**
     * Get hash number
     * @return hash
     */
    public String getHash() {
        return hash;
    }
    /**
     * Get transaction type
     * @return type
     */
    public TranType getType() {
        return type;
    }
    /**
     * Get transaction fee, in SDA
     * @return fee
     */
    public String getFee() {
        return fee;
    }
    /**
     * Get server result
     * @return result
     */
    public String getResult() {
        return result;
    }
    /**
     * Get resource id
     * @return client_resource_id
     */
    public String getClient_resource_id() {
        return client_resource_id;
    }
    /**
     * Get transaction counter party
     * @return counterparty
     */
    public String getCounterparty() {
        return counterparty;
    }
    /**
     * Get transaction amount
     * @return amount
     */
    public Amount getAmount() {
        return amount;
    }
    /**
     * Get transaction effect collection
     * @return effects
     */
    public EffectCollection getEffects() {
        return effects;
    }
    /**
     * Order gets
     * @return gets
     */
    public Amount getGets() {
        return gets;
    }
    /**
     * Order pays
     * @return pays
     */
    public Amount getPays() {
        return pays;
    }
    /**
     * Get transaction memo collection
     * @return memos
     */
    public MemoCollection getMemos() {
        return memos;
    }

    /**
     * Gets offertype.
     *
     * @return the offertype
     */
    public String getOffertype() {
        return offertype;
    }
    /**
     * Gets seq.
     *
     * @return the seq
     */
    public int getSeq() {
        return seq;
    }
}
