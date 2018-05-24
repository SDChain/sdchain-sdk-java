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
public class Payment {
    private String hash;
    private String ledger;
    private Amount amount;
    private double fee;
    private String sourceAccount;
    private String destinationAccount;
    private String direction;
    private String timestamp;
    
    public String getHash() {
        return hash;
    }
    public String getLedger() {
        return ledger;
    }
    public Amount getAmount() {
        return amount;
    }
    public double getFee() {
        return fee;
    }
    public String getSourceAccount() {
        return sourceAccount;
    }
    public String getDestinationAccount() {
        return destinationAccount;
    }
    public String getDirection() {
        return direction;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public void setLedger(String ledger) {
        this.ledger = ledger;
    }
    public void setAmount(Amount amount) {
        this.amount = amount;
    }
    public void setFee(double fee) {
        this.fee = fee;
    }
    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public String toString() {
        return "Payment [hash=" + hash + ", ledger=" + ledger + ", amount=" + amount + ", fee=" + fee + ", sourceAccount=" + sourceAccount + ", destinationAccount=" + destinationAccount + ", direction=" + direction + ", timestamp="
            + timestamp + "]";
    }

}
