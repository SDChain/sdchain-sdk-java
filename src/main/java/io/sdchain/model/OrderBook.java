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
 * Order book class
 * changed to new format by simplifing the inputs
 */
public class OrderBook extends SDChainObject {
    // private Amount price;
    private Amount taker_gets_funded;
    private Amount taker_gets_total;
    private Amount taker_pays_funded;
    private Amount taker_pays_total;
    private String order_maker; // Orderbook format:
    private long sequence;
    private boolean passive;
    private boolean sell;
    /**
     * Get  base currency price as Amount object
     * @return amount 
     *
     */
    public Amount getAmountPrice() {
        if (sell)
            return taker_gets_funded;
        else
            return taker_pays_funded;
    }

    /**
     * Get  base currency price
     * @return price
     *
     */
    public String getPrice() {

        if (sell)
            return String.valueOf(taker_gets_funded.getValue());
        else
            return String.valueOf(taker_pays_funded.getValue());
        // return String.valueOf(price.getValue());
    }

    /**
    * Get funded value of the base currency
    * asks(sell = true)
     * Funded
    * bids(sell = false)
     * @return string value of the currency
     *
     */
    public String getFunded() {
        if (sell)
            return String.valueOf(taker_gets_funded.getValue());
        else
            return String.valueOf(taker_pays_funded.getValue());
    }

    public String getTotal() {
        if (sell)
            return String.valueOf(taker_gets_total.getValue());
        else
            return String.valueOf(taker_pays_total.getValue());
    }

    /**
     * Get actual amount get
     * @return taker_gets_funded
     * obsoleted
     */
    public Amount getTaker_gets_funded() {
        return taker_gets_funded;
    }
    /**
     * Get total amount get
     * @return taker_gets_total
     */
    public Amount getTaker_gets_total() {
        return taker_gets_total;
    }
    /**
     * Get actual amount pay
     * @return taker_pays_funded
     */
    public Amount getTaker_pays_funded() {
        return taker_pays_funded;
    }
    /**
     * Get total amount pay
     * @return taker_pays_total
     */
    public Amount getTaker_pays_total() {
        return taker_pays_total;
    }
    /**
     * Get order maker
     * @return order_maker
     */
    public String getOrderMaker() {
        return order_maker;
    }
    /**
     * Get transaction sequence
     * @return sequence
     */
    public long getSequence() {
        return sequence;
    }
    /**
     * Return true if transaction is passive
     * @return passive
     */
    public boolean getPassive() {
        return passive;
    }
    /**
     * Return true if the order is sell
     * @return sell
     */
    public boolean getSell() {
        return sell;
    }
    /**
     * Get base and countr Tum pair from the order
     * @return tum_pair
     */
    /*	public String getPair() throws InvalidParameterException {
    
    		if (pair.length() < 1) {
    			//If the pair is not set yet, create it from the order_maker String
    			//
    			if (order_maker.length() < 1)
    				throw new InvalidParameterException(SDChainMessage.INVALID_TUM_PAIR, "Empty tum pair in order book", null);
    			String[] temp_str = order_maker.split("+");
    StringBuffer new_pair = new StringBuffer();
    for ( int i = 0; i < temp_str.length; i ++ ) {
    	new_pair.append(temp_str[i]);
    	if (i < temp_str.length -1)
    		new_pair.append(":");
    }
    pair = new_pair.toString();
    
    		}
    			return pair;
    	}*/
}
