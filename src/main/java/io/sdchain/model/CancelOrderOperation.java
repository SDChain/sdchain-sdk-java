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

/**
 * Cancel Order class
 */
public class CancelOrderOperation extends OperationClass {

    // Order used to cancel the order
    private String order_id;

    public CancelOrderOperation(Wallet src_wallet) {
        // check if the wallet if an active one, this may delay the process of Operation
        //
        this.setSrcAddress(src_wallet.getAddress());
        this.setSrcSecret(src_wallet.getSecret());
        // set default mode to syn
        this.validate = true;
    }

    /**
     * Set the sequence number of the submitted number
     * @param in_seq Set the sequence number
     *
     */
    public void setSequence(String in_seq) throws InvalidParameterException {
        if (in_seq.length() < 1) {

            this.order_id = in_seq;
        } else
            throw new InvalidParameterException(SDChainMessage.INVALID_ORDER_NUMBER, in_seq, null);

    }

    public void setSequence(long in_seq) throws InvalidParameterException {
        if (in_seq > 0) {

            this.order_id = Long.toString(in_seq);
        } else
            throw new InvalidParameterException(SDChainMessage.INVALID_ORDER_NUMBER, Long.toString(in_seq), null);

    }
    /**
     * Submit the cancel order operation with the info
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

        // Check the order id, should be a valid number
        if (this.order_id.length() < 1)
            throw new InvalidParameterException(SDChainMessage.INVALID_ORDER_NUMBER, this.order_id, null);

        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("secret", this.getSrcSecret());

        String params = APIServer.GSON.toJson(content);
        String url = APIServer.formatURL(Order.class, this.getSrcAddress(), "/" + this.order_id + VALIDATED + Boolean.toString(this.validate));

        return APIServer.request(APIServer.RequestMethod.DELETE, url, params, RequestResult.class);
    }

}
