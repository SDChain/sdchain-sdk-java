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
public class Transaction {
    @Expose
    private String hash;
    @Expose
    private String ledger;
    @Expose
    private String date;
    @Expose
    private String type;
    @Expose
    private String result;
    @Expose
    private Long offerseq;
    @Expose
    private String offertype;
    @Expose
    private String fee;
    @Expose
    private String state;
    @Expose
    private EffectCollection effects;
    @Expose
    private Amount amount;
    @Expose
    private String pair;
    @Expose
    private String price;
    @Expose
    private Long seq;
    @Expose
    private MemoCollection memos;

    public enum DirectionType {
        incoming, outgoing, all
    }

    public String getHash() {
        return hash;
    }

    public String getLedger() {
        return ledger;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getResult() {
        return result;
    }

    public Long getOfferseq() {
        return offerseq;
    }

    public String getOffertype() {
        return offertype;
    }

    public String getFee() {
        return fee;
    }

    public String getState() {
        return state;
    }

    public EffectCollection getEffects() {
        return effects;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getPair() {
        return pair;
    }

    public String getPrice() {
        return price;
    }

    public Long getSeq() {
        return seq;
    }

    public MemoCollection getMemos() {
        return memos;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setLedger(String ledger) {
        this.ledger = ledger;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setOfferseq(Long offerseq) {
        this.offerseq = offerseq;
    }

    public void setOffertype(String offertype) {
        this.offertype = offertype;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setEffects(EffectCollection effects) {
        this.effects = effects;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public void setMemos(MemoCollection memos) {
        this.memos = memos;
    }

    @Override
    public String toString() {
        return "Transaction [hash=" + hash + ", ledger=" + ledger + ", date=" + date + ", type=" + type + ", result=" + result + ", offerseq=" + offerseq + ", offertype=" + offertype + ", fee=" + fee + ", state=" + state + ", effects="
            + effects + ", amount=" + amount + ", pair=" + pair + ", price=" + price + ", seq=" + seq + ", memos=" + memos + "]";
    }

}
