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
public class RequestResult extends SDChainObject {
    private boolean success;
    private String client_resource_id;
    private String hash;
    private String state;
    private String result;
    private long date;
    private double fee;
    private long sequence;
    /**
     * Get true if request is successful
     * @return success
     */
    public boolean getSuccess() {
        return success;
    }
    /**
     * Get resource id
     * @return client_resource_id
     */
    public String getClient_resource_id() {
        return client_resource_id;
    }
    /**
     * Get hash number
     * @return hash
     */
    public String getHash() {
        return hash;
    }
    /**
     * Get server state
     * @return state
     */
    public String getState() {
        return state;
    }
    /**
     * Get server result, tesSuccess means successful
     * @return result
     */
    public String getResult() {
        return result;
    }
    /**
     * Get time, in UNIXTIME
     * @return date
     */
    public long getDate() {
        return date;
    }
    /**
     * Get transaction fee, in SDA
     * @return fee
     */
    public double getFee() {
        return fee;
    }
    /**
     * Get sequence number
     * @return sequence
     */
    public long getSequence() {
        return sequence;
    }
}
