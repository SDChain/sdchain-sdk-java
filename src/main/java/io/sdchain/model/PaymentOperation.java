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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
 * Payment operation class
 */
public class PaymentOperation extends OperationClass {

    // Amount used for the payment
    private String paths;
    private String dest_address;
    private Amount source_amount;
    private Memo memo;
    private double source_slippage;
    private Amount destination_amount;

    private Wallet src_wallet;

    private String client_resource_id;
    private String prefix;

    public PaymentOperation(Wallet in_src_wallet) {
        // check if the wallet if an active one, this may delay the process of Operation
        //
        this.src_wallet = in_src_wallet;

        this.setSrcAddress(src_wallet.getAddress());
        this.setSrcSecret(src_wallet.getSecret());

        client_resource_id = "";

    }

    /* Get payment paths
     * @return paths
     */
    public String getPaths() {
        return paths;
    }

    /**
     * Get payment amount
     * @return amount
     */
    public Amount getAmount() {
        return source_amount;
    }
    /**
     * Get payment resource id
     * @return client_resource_id
     */
    public String getClientID() {
        return client_resource_id;
    }

    /**
     * Get payment resource id
     * @param in_id input client_resource_id
     */
    public void setClientID(String in_id) {
        this.client_resource_id = in_id;
    };
    public void setClientId(String in_id) {
        this.client_resource_id = in_id;
    };

    /**
     * Set the payment resource id
     * @param in_id the prefix will be used to generate the client id internally
     */
    public void setPrefix(String in_id) {
        this.prefix = in_id;
    };

    public void setAmount(Amount in_amt) throws InvalidParameterException {
        if (!Utility.isValidAmount(in_amt)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_AMOUNT, null, null);
        }
        this.source_amount = in_amt;
    };

    /*
     * Set the payment Memo info.
     */
    public void setMemo(Memo in_memo) throws InvalidParameterException {
        if (!Utility.isValidMemo(in_memo)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_AMOUNT, null, null);
        }
        this.memo = in_memo;
    };

    public void setMemo(String in_str) throws InvalidParameterException {
        if (this.memo == null) {
            this.memo = new Memo();
        }
        this.memo.setMemoType("String");
        this.memo.setMemoData(in_str);
    };

    public void setDestAddress(String in_address) throws InvalidParameterException {
        if (Utility.isValidAddress(in_address))
            dest_address = in_address;
        else
            throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_ADDRESS, null, null);
    };

    /*
     * Set the payment path
     */
    public void setPath(String in_str) {
        this.paths = in_str;

    };

    /*
     * Set the payment path
     */
    public void setChoice(String in_str) {
        // this.paths = this.src_wallet.getPathList();
    }

    /**
     * Submit a payment by organizing the parameters
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

        if (!Utility.isValidAddress(this.dest_address)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_ADDRESS, this.dest_address, null);
        }
        if (!Utility.isValidAmount(this.source_amount)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_AMOUNT, null, null);
        }
        if (this.source_amount.getValue() <= 0) {
            throw new InvalidParameterException(SDChainMessage.INVALID_VALUE, String.valueOf(source_amount.getValue()), null);
        }

        if (Utility.isEmpty(this.client_resource_id)) { // if uid is null

            // Generate an uid if the user didn't set it.
            this.client_resource_id = "paymentid" + Long.toString(System.currentTimeMillis()); // payment_id// generate a resouce ID

        } else {
        }

        HashMap<String, String> destination_amount = new HashMap<String, String>();
        destination_amount.put("currency", source_amount.getCurrency());
        destination_amount.put("value", Utility.doubleToString(source_amount.getValue()));
        destination_amount.put("issuer", source_amount.getIssuer());

        HashMap<String, Object> payment = new HashMap<String, Object>();
        payment.put("source_account", this.getSrcAddress());
        payment.put("destination_account", this.dest_address);
        payment.put("amount", destination_amount);

        if (this.memo != null) {
            HashMap[] payment_memos = new HashMap[1];
            HashMap<String, String> payment_memo = new HashMap<String, String>();
            payment_memo.put("MemoType", this.memo.getMemoType());
            payment_memo.put("MemoData", this.memo.getMemoData());
            payment_memos[0] = payment_memo;
            payment.put("memos", payment_memos);
        }

        // payment.put("paths", this.src_wallet.getPathList());

        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("secret", this.getSrcSecret());
        content.put("client_resource_id", this.client_resource_id);
        content.put("payment", payment);

        String params = APIServer.GSON.toJson(content);

        String url = APIServer.formatURL(Payment.class, this.dest_address, VALIDATED + Boolean.toString(this.validate));

        return APIServer.request(APIServer.RequestMethod.POST, url, params, RequestResult.class);
    }

    public void submit(PaymentListener listener) throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, InvalidParameterException, FailedException {
        // Later all operators in one ExecutorService?
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new PaymentRunnable(this, listener));
        exec.shutdown();
    }

    private class PaymentRunnable implements Runnable {
        private PaymentOperation operator;
        private PaymentListener listener;

        private PaymentRunnable(PaymentOperation operator, PaymentListener listener) {
            this.operator = operator;
            this.listener = listener;
        }
        public void run() {
            try {
                RequestResult result = this.operator.submit();

                this.listener.onComplete(result);
            } catch (AuthenticationException e) {
                e.printStackTrace();
            } catch (InvalidRequestException e) {
                e.printStackTrace();
            } catch (APIConnectionException e) {
                e.printStackTrace();
            } catch (APIException e) {
                e.printStackTrace();
            } catch (ChannelException e) {
                e.printStackTrace();
            } catch (InvalidParameterException e) {
                e.printStackTrace();
            } catch (FailedException e) {
                e.printStackTrace();
            }
        }
    }

    public interface PaymentListener {
        public void onComplete(RequestResult result);
    }
}
