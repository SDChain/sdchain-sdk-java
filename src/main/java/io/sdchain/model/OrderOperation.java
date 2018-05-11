/*
 * Copyright www.sdchain.io Inc.
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

import java.util.HashMap;

import io.sdchain.SDChainMessage;
import io.sdchain.exception.APIConnectionException;
import io.sdchain.exception.APIException;
import io.sdchain.exception.AuthenticationException;
import io.sdchain.exception.ChannelException;
import io.sdchain.exception.FailedException;
import io.sdchain.exception.InvalidParameterException;
import io.sdchain.exception.InvalidRequestException;
import io.sdchain.net.APIServer;
import io.sdchain.util.Utility;

/**
 * Order operation class
 */
public class OrderOperation extends OperationClass {
    // Amount used for the submit order
    private String paths;

    private Amount taker_pays;
    private Amount taker_gets;

    private double src_amount_value;
    private double price;

    // pair of Tum, tumCode:issuer/tumCode:issuer
    private String pair;
    private String type;// sell or buy

    // String used to identify the asy mode or sy mode
    public static final String SELL = "sell";
    public static final String BUY = "buy";

    public OrderOperation(Wallet src_wallet) {
        // check if the wallet if an active one, this may delay the process of Operation
        //
        this.setSrcAddress(src_wallet.getAddress());
        this.setSrcSecret(src_wallet.getSecret());
        // set default mode to syn
        this.validate = true;
    }

    /**
     * Set the Tum pair in the order
     * split the
     * @param in_str Input string to set the Order type
     *               must be either sell or buy
     *
     */
    public void setType(String in_str) throws InvalidParameterException {
        if (in_str == "sell" || in_str == "buy") {

            this.type = in_str;
        } else
            throw new InvalidParameterException(SDChainMessage.SPECIFY_ORDER_TYPE, in_str, null);

    }

    /**
     * Set the Tum pair in the order
     * @param in_pair Input string contains the pair info
     *
     *
     */
    public void setPair(String in_pair) {
        // TODO check the pair format
        this.pair = in_pair;

    }

    // only two digits allowed in the value
    // TODO check the value format
    public void setAmount(double in_value) {
        this.src_amount_value = in_value;
    };
    public void setAmount(String in_value) {
        this.src_amount_value = Double.parseDouble(in_value);
    };

    //
    public void setPrice(float in_price) {
        this.price = in_price;
    }

    /**
     * Submit the order with the info
     *
     * @return PostResult instance
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws InvalidParameterException
     * @throws FailedException
     */
    public RequestResult submit()
        throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, InvalidParameterException, FailedException {

        // split the pair to get the base and counter tum
        String tum_codes[] = this.pair.split("/");

        if (tum_codes.length != 2)
            throw new InvalidParameterException(SDChainMessage.INVALID_TOKEN_PAIR, this.pair, null);

        String[] base_tum = tum_codes[0].split(":");;
        String[] counter_tum = tum_codes[1].split(":");

        // Set the source_amount and destination amount with pair, price and amount value
        HashMap<String, String> base_amount = new HashMap<String, String>();
        base_amount.put("currency", base_tum[0]);
        base_amount.put("value", Utility.doubleToString(this.src_amount_value));
        if (base_tum.length < 2)
            base_amount.put("counterparty", "");
        else
            base_amount.put("counterparty", base_tum[1]);

        HashMap<String, String> counter_amount = new HashMap<String, String>();
        counter_amount.put("currency", counter_tum[0]);
        counter_amount.put("value", Utility.doubleToString(this.src_amount_value * this.price));
        if (counter_tum.length < 2)
            counter_amount.put("counterparty", "");
        else
            counter_amount.put("counterparty", counter_tum[1]);

        HashMap<String, Object> order_data = new HashMap<String, Object>();

        // Set the parameters
        if (this.type == this.BUY) {

            order_data.put("type", this.type);
            order_data.put("taker_pays", base_amount);
            order_data.put("taker_gets", counter_amount);
        } else if (this.type == this.SELL) {
            order_data.put("type", this.type);
            order_data.put("taker_pays", counter_amount);
            order_data.put("taker_gets", base_amount);
        } else
            throw new InvalidParameterException(SDChainMessage.SPECIFY_ORDER_TYPE, this.type, null);

        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("secret", this.getSrcSecret());
        content.put("order", order_data);

        String params = APIServer.GSON.toJson(content);
        String url = APIServer.formatURL(Order.class, this.getSrcAddress(), VALIDATED + Boolean.toString(this.validate));

        return APIServer.request(APIServer.RequestMethod.POST, url, params, RequestResult.class);
    }
}
