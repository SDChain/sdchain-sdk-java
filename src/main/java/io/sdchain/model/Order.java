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

import io.sdchain.SDChainMessage;
import io.sdchain.exception.InvalidParameterException;
import io.sdchain.util.Utility;

/**
 * Order model class
 */
public class Order extends SDChainObject {
    private Boolean success;
    private String hash;
    private double fee;
    private long sequence;
    private OrderType type;
    private Amount taker_gets; // what payer get
    private Amount taker_pays; // what payer need to pay
    private boolean passive; // passive order or not
    private MyOrder order;
    private String action;
    private Transaction.DirectionType direction;
    private String validated;
    /**
     * Order type, sell or buy	 *
     */
    public enum OrderType {
        sell, buy
    }
    /**
     * Based on the jason structure returned from Http request, need a private order structure here
     *
     */
    private class MyOrder {
        String account;
        Amount taker_gets;
        Amount taker_pays;
        Boolean passive;
        OrderType type;
        long sequence;
        public String getAccount() {
            return account;
        }

        public Amount getTaker_gets() {
            return taker_gets;
        }

        public Amount getTaker_pays() {
            return taker_pays;
        }
        public Boolean getPassive() {
            return passive;
        }
        public OrderType getType() {
            return type;
        }
        public long getSequence() {
            return sequence;
        }

        public Double getPrice() {

            if (type == OrderType.sell) {
                return taker_pays.getValue() / taker_gets.getValue();
            } else
                return taker_gets.getValue() / taker_pays.getValue();
        }

        // Return the value of the base currency
        public Double getAmount() {
            if (type == OrderType.sell) {
                return taker_gets.getValue();
            } else
                return taker_pays.getValue();
        }

        // Added new returns and disable the old ones
        public String getPair() {
            StringBuilder sb = new StringBuilder();

            if (type == OrderType.sell) {
                // Base currency is taker get
                sb.append(taker_gets.getCurrency());
                if (!taker_gets.isSDA()) {
                    sb.append(":" + taker_gets.getIssuer());
                }
                sb.append("/");
                sb.append(taker_pays.getCurrency());
                if (!taker_pays.isSDA()) {
                    sb.append(":" + taker_pays.getIssuer());
                }

            } else {
                // buy
                // Base currency is taker pays
                sb.append(taker_pays.getCurrency());
                if (!taker_pays.isSDA()) {
                    sb.append(":" + taker_pays.getIssuer());
                }
                sb.append("/");
                sb.append(taker_gets.getCurrency());
                if (!taker_gets.isSDA()) {
                    sb.append(":" + taker_gets.getIssuer());
                }

            }
            return sb.toString();
        }
    }
    /**
     * Get server state
     * @return validated
     */
    public String getValidated() {
        return validated;
    }
    /**
     * Get action type
     * @return action
     */
    public String getAction() {
        return action;
    }
    /**
     * Get direction type, incoming or outgoing
     * @return direction
     */
    public Transaction.DirectionType getDirection() {
        return direction;
    }
    /**
     * Get account
     * @return account
     */
    public String getAccount() {
        if (order != null) {
            return order.getAccount();
        }
        return null;
    }

    /*
     * Get the Tum pair
     * base currency/counter currency
     */
    public String getPair() throws InvalidParameterException {

        if (order.getType() == OrderType.sell)
            return Utility.getTumPair(this.taker_gets, this.taker_pays);
        else
            return Utility.getTumPair(this.taker_pays, this.taker_gets);
    }

    /*
     * return the base currency amount
     * sell
     * return the taker_gets
     * buy
     * return the taker_pays
     */
    public Double getAmount() {
        if (order.getType() == OrderType.sell)
            return this.taker_gets.getValue();
        else
            return this.taker_pays.getValue();
    }

    /*
     * return the base currency price
     * sell
     * return the taker_pays/taker_gets
     * buy
     * return the taker_gets/taker_pays
     */

    public Double getPrice() throws InvalidParameterException {

        if (order.getType() == OrderType.sell) {
            if (order.taker_gets.getValue() > 0)
                return order.taker_pays.getValue() / order.taker_gets.getValue();
            else
                throw new InvalidParameterException(SDChainMessage.INVALID_VALUE, String.valueOf(order.taker_gets.getValue()), null);
        } else {
            if (order.taker_pays.getValue() > 0)
                return order.taker_gets.getValue() / order.taker_pays.getValue();
            else
                throw new InvalidParameterException(SDChainMessage.INVALID_VALUE, String.valueOf(order.taker_pays.getValue()), null);
        }

    }

    /**
     * Get order type
     * @return type
     */
    public OrderType getType() {
        if (order != null) {
            return order.getType();
        }
        return type;
    }

    /**
     * Get amount pays in the order
     * @return taker_gets
     */
    public Amount getPay() {
        if (this.taker_gets == null && order != null) {
            return order.getTaker_gets();
        }
        return taker_gets;
    }
    /**
     * Get amount receive
     * depends on the type of order
     *
     * @return taker_pays
     */
    public Amount getReceive() {
        if (this.taker_gets == null && order != null) {
            return order.getTaker_pays();
        }
        return taker_pays;
    }
    /**
     * Return true if it is passive transaction
     * @return passive
     */
    public boolean getPassive() {
        if (order != null) {
            return order.getPassive();
        }
        return passive;
    }
    /**
     * Return true if the request is successful
     * @return success
     */
    public boolean getSuccess() {
        return success;
    }
    /**
     * Get transaction hash value
     * @return hash
     */
    public String getHash() {
        return hash;
    }
    /**
     * Get transaction fee, in SDA
     * @return fee
     */
    public double getFee() {
        return fee;
    }
    /**
     * Get transaction sequence number
     * @return sequence
     */
    public long getSequence() {
        if (order != null) {
            return order.getSequence();
        }
        return sequence;
    }
}
