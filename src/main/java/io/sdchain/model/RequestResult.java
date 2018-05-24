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
 * Post request result class
 */
public class RequestResult {
    private boolean success;
    private String hash;
    private String state;
    private String result;
    private long date;
    private double fee;
    private long sequence;
    private String statusUrl;

    public boolean isSuccess() {
        return success;
    }
    public String getHash() {
        return hash;
    }
    public String getState() {
        return state;
    }
    public String getResult() {
        return result;
    }
    public long getDate() {
        return date;
    }
    public double getFee() {
        return fee;
    }
    public long getSequence() {
        return sequence;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public void setFee(double fee) {
        this.fee = fee;
    }
    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public String getStatusUrl() {
        return statusUrl;
    }
    public void setStatusUrl(String statusUrl) {
        this.statusUrl = statusUrl;
    }

    @Override
    public String toString() {
        return "RequestResult [success=" + success + ", hash=" + hash + ", state=" + state + ", result=" + result + ", date=" + date + ", fee=" + fee + ", sequence=" + sequence + ", statusUrl=" + statusUrl + "]";
    }

}
