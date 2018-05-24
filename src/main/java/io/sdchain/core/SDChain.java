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
package io.sdchain.core;
/**
 * 
 * the Configuration of sdchain's currency
 *  
 */
public abstract class SDChain
{	
	private static final String SIGN_HASH_STRING = "Sixdomainchain2018";  //used in building signature string
	
	private static final String CURRENCY_SDA = "SDA";
	private static final String CURRENCY_CNY = "CNY";
	private static final String CURRENCY_USD = "USD";
	private static final String CURRENCY_EUR = "EUR";
	private static final String CURRENCY_JPY = "JPY";
	private static final String CURRENCY_SLC = "SLC";

	public static String getCurrencyUSD() {
		return CURRENCY_USD;
	}
	/**
	 * Get currency EUR
	 * @return currency EUR
	 */
	public static String getCurrencyEUR() {
		return CURRENCY_EUR;
	}
	/**
	 * Get currency JPY
	 * @return currency JPY
	 */
	public static String getCurrencyJPY() {
		return CURRENCY_JPY;
	}
	/**
     * Get currency CNY
     * @return currency CNY
     */
    public static String getCurrencyCNY() {
		return CURRENCY_CNY;
	}
	/**
     * Get SDChain defined currency SDA
     * @return currency SDA
     */
    public static String getCurrencySDA() {
		return CURRENCY_SDA;
	}
	/**
     * Get SDChain defined currency SDA
     * @return currency SDA
     */
    public static String getCurrencySLC() {
		return CURRENCY_SLC;
	}

    /**
     * Get sign string used in authentic check
     * @return string used in sign functionality
     */
    public static String getSignString(){
    	return SIGN_HASH_STRING;
    }    
}