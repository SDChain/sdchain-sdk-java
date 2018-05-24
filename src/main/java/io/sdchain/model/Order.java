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
 * Order model class
 */
public class Order {
    private Long sequence;
    private Amount takerGets; // what payer get
    private Amount takerPays; // what payer need to pay
    private String type;
    private boolean passive; // passive order or not
    public Long getSequence() {
        return sequence;
    }
    public Amount getTakerGets() {
        return takerGets;
    }
    public Amount getTakerPays() {
        return takerPays;
    }
    public String getType() {
        return type;
    }
    public boolean isPassive() {
        return passive;
    }
    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
    public void setTakerGets(Amount takerGets) {
        this.takerGets = takerGets;
    }
    public void setTakerPays(Amount takerPays) {
        this.takerPays = takerPays;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setPassive(boolean passive) {
        this.passive = passive;
    }

    @Override
    public String toString() {
        return "Order [sequence=" + sequence + ", takerGets=" + takerGets + ", takerPays=" + takerPays + ", type=" + type + ", passive=" + passive + "]";
    }

}
