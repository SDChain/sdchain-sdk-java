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
 * Effect class, additional information about payments or transactions
 * 
 */
public class Effect {
    private String effect;
    private String type;
    private Amount gets;
    private Amount pays;
    private String price;
    private boolean cancelled;
    private boolean remaining;
    private boolean deleted;
    private int seq;
    private Amount got;
    private Amount paid;
    private CounterParty counterparty;
    private Amount limit;
    private String currency;
    private String hash;
    private Amount from;
    private Amount to;
    private Amount amount;
    private String pair;
    public String getEffect() {
        return effect;
    }
    public String getType() {
        return type;
    }
    public Amount getGets() {
        return gets;
    }
    public Amount getPays() {
        return pays;
    }
    public String getPrice() {
        return price;
    }
    public boolean isCancelled() {
        return cancelled;
    }
    public boolean isRemaining() {
        return remaining;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public int getSeq() {
        return seq;
    }
    public Amount getGot() {
        return got;
    }
    public Amount getPaid() {
        return paid;
    }
    public CounterParty getCounterparty() {
        return counterparty;
    }
    public Amount getLimit() {
        return limit;
    }
    public String getCurrency() {
        return currency;
    }
    public String getHash() {
        return hash;
    }
    public Amount getFrom() {
        return from;
    }
    public Amount getTo() {
        return to;
    }
    public Amount getAmount() {
        return amount;
    }
    public String getPair() {
        return pair;
    }
    public void setEffect(String effect) {
        this.effect = effect;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setGets(Amount gets) {
        this.gets = gets;
    }
    public void setPays(Amount pays) {
        this.pays = pays;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    public void setRemaining(boolean remaining) {
        this.remaining = remaining;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public void setSeq(int seq) {
        this.seq = seq;
    }
    public void setGot(Amount got) {
        this.got = got;
    }
    public void setPaid(Amount paid) {
        this.paid = paid;
    }
    public void setCounterparty(CounterParty counterparty) {
        this.counterparty = counterparty;
    }
    public void setLimit(Amount limit) {
        this.limit = limit;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public void setFrom(Amount from) {
        this.from = from;
    }
    public void setTo(Amount to) {
        this.to = to;
    }
    public void setAmount(Amount amount) {
        this.amount = amount;
    }
    public void setPair(String pair) {
        this.pair = pair;
    }

    @Override
    public String toString() {
        return "Effect [effect=" + effect + ", type=" + type + ", gets=" + gets + ", pays=" + pays + ", price=" + price + ", cancelled=" + cancelled + ", remaining=" + remaining + ", deleted=" + deleted + ", seq=" + seq + ", got=" + got
            + ", paid=" + paid + ", counterparty=" + counterparty + ", limit=" + limit + ", currency=" + currency + ", hash=" + hash + ", from=" + from + ", to=" + to + ", amount=" + amount + ", pair=" + pair + "]";
    }

}
