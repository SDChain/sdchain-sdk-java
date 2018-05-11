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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.annotations.Expose;

import io.sdchain.SDChain;
import io.sdchain.SDChainMessage;
import io.sdchain.core.crypto.ecdsa.Seed;
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
 * Wallet class, main entry point
 */
public class Wallet extends AccountClass {
    @Expose
    private boolean success; // Operation success or not
    @Expose
    private long ledger;
    @Expose
    private boolean validated;
    @Expose
    private BalanceCollection balances;
    @Expose
    private PaymentCollection payments;
    @Expose
    private OrderCollection orders;
    @Expose
    private TransactionCollection transactions;
    @Expose
    private Transaction transaction;

    private boolean __isActivated = false; // If the account is activated, at least 6 SDA needed

    /**
     * Creat wallet
     *
     * @return wallet
     */
    public static Wallet createWallet() {
        String secret = Seed.generateSecret();
        String address = null;
        try {
            address = Seed.computeAddress(secret);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        try {
            return new Wallet(secret, address);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @return true if the wallet is activated
     * @throws InvalidRequestException 
     */
    public boolean isActivated() throws InvalidRequestException {
        BalanceCollection bc = null;
        if (!__isActivated) {
            try {
                bc = this.getBalance(SDChain.getCurrencySDA(), ""); // check if have enough SDA
            } catch (FailedException e) {
                e.printStackTrace();
            } catch (AuthenticationException e) {
                e.printStackTrace();
            } catch (APIConnectionException e) {
                e.printStackTrace();
            } catch (APIException e) {
                e.printStackTrace();
            } catch (ChannelException e) {
                e.printStackTrace();
            } catch (InvalidParameterException e) {
                e.printStackTrace();
            }

            if (bc != null) {
                Balance bl = bc.getData().get(0);
                // Notice that this amount may be diff from SdCharger
                // since user can set the SdCharger one.
                if (bl != null && bl.getValue() >= MIN_ACTIVATED_AMOUNT) {
                    __isActivated = true;
                }
            }
        }
        return __isActivated;
    }
    /**
     * @param __isActivated
     */
    public void setActivated(boolean __isActivated) {
        this.__isActivated = __isActivated;
    }

    /**
     * get private Transaction instance
     * @return transaction
     */
    private Transaction getTransaction() {
        return transaction;
    }
    /**
     * get private TransactionCollection instance
     * @return transactions
     */
    private TransactionCollection getMyTransactionCollection() {
        return this.transactions;
    }

    /**
     * get private Order Collection instance
     * @return orders
     */
    private OrderCollection getOrdersCollection() {
        return orders;
    }
    /**
     * get private PaymentCollection instance
     * @return payments
     */
    private PaymentCollection getPaymentsCollection() {
        return payments;
    }
    /**
     * Create Wallet instance with address and secret key
     * @param address
     * @param secret
     * @throws InvalidParameterException 
     */
    public Wallet(String secret, String address) throws InvalidParameterException {
        if (!Utility.validateKeyPair(address, secret)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_ADDRESS_OR_SECRET, address + secret, null);
        }
        this.address = address;
        this.secret = secret;

    }
    /*
     * Create Wallet instance from secret
     * Only use secret can derive address
     * @param secret
     * @throws InvalidParameterException 
     */
    public Wallet(String secret) throws InvalidParameterException {
        if (!Utility.isValidSecret(secret)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_SECRET, secret, null);
        }
        this.address = Seed.computeAddress(secret);
        this.secret = secret;
    }
    /**
     * Gson builder
     */
    public static final Gson PRETTY_PRINT_GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setLongSerializationPolicy(LongSerializationPolicy.STRING).registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == src.longValue())
                    return new JsonPrimitive(src.longValue());
                return new JsonPrimitive(src);
            }
        }).create();
    /**
     * Get private balance collection
     * @return balances
     */
    private BalanceCollection getBalances() {
        return balances;
    }
    /**
     * Get wallet address
     * @return address
     */
    public String getAddress() {
        return this.address;
    }
    /**
     * Get wallet secret key
     * @return secret
     */
    public String getSecret() {
        return this.secret;
    }

    /**
     * Static method to get the balance collection of a given address
     * without any parameters, this returns all the balances
     * @return BalanceCollection instance
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws FailedException 
     */
    public BalanceCollection getBalance()
        throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, FailedException {

        return APIServer.request(
            APIServer.RequestMethod.GET,
            APIServer.formatURL(
                Balance.class,
                this.getAddress()),
            null,
            Wallet.class).getBalances();
    }
    /**
     * Get balance filtered by currency/counterparty
     * @param currency
     * @param issuer
     * @return BalanceCollection instance
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws InvalidParameterException 
     * @throws FailedException 
     */
    public BalanceCollection getBalance(String currency, String issuer)
        throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, InvalidParameterException, FailedException {
        StringBuilder sb = new StringBuilder();
        // check if currency is valid
        if (Utility.isNotEmpty(currency)) {
            if (!Utility.isValidCurrency(currency)) {
                throw new InvalidParameterException(SDChainMessage.INVALID_CURRENCY, currency, null);
            } else {
                sb.append("?currency=");
                sb.append(currency);
            }
        }
        // check counterparty is valid
        if (Utility.isNotEmpty(issuer)) {
            if (!Utility.isValidAddress(issuer)) {
                throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_ADDRESS, issuer, null);
            } else {
                // if only one parameter is available, use ?
                // otherwise use & to seperate the parameters.
                if (sb.length() > 0)
                    sb.append("&counterparty=");
                else
                    sb.append("?counterparty=");
                sb.append(issuer);
            }
        }
        return APIServer.request(
            APIServer.RequestMethod.GET,
            APIServer.formatURL(
                Balance.class,
                this.getAddress(),
                sb.toString()),
            null,
            Wallet.class).getBalances();
    }

    /**
     * Get order by ID
     * @param payment_id
     * @return Order instance
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws InvalidParameterException
     * @throws FailedException
     */
    public Payment getPayment(String payment_id) throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, InvalidParameterException, FailedException {
        try {
            if (!isActivated()) {
                throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
            }
        } catch (InvalidRequestException e) {
            throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
        }
        if (Utility.isEmpty(payment_id)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_ID, payment_id, null);
        }

        return APIServer.request(
            APIServer.RequestMethod.GET,
            APIServer.formatURL(
                Payment.class,
                this.getAddress(), "/" + payment_id),
            null,
            Payment.class);
    }

    /**
     * @return PaymentCollection
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws FailedException 
     */
    public PaymentCollection getPaymentList() throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, FailedException {
        Options ops = new Options();
        try {
            return getPaymentList(ops);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param in_ops: contains options with sourceAccount, destinationAccount
     * @return PaymentCollection
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws InvalidParameterException
     * @throws FailedException 
     */
    // public PaymentCollection getPaymentList(String sourceAccount, String destinationAccount, boolean excludeFailed, Payment.Direction direction, int resultPerPage, int page)
    public PaymentCollection getPaymentList(Options in_ops)
        throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, InvalidParameterException, FailedException {
        try {
            if (!isActivated()) {
                throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
            }
        } catch (InvalidRequestException e) {
            throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
        }
        StringBuilder param = new StringBuilder();
        // Note, the 1st option needs to be "?", not "&"
        int op_num = 0;

        if (Utility.isNotEmpty(in_ops.getSourceAccount())) {
            if (!Utility.isValidAddress(in_ops.getSourceAccount())) {
                throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_ADDRESS, in_ops.getSourceAccount(), null);
            } else {
                if (op_num > 0)
                    param.append("&");
                else
                    param.append("?");
                op_num++;
                param.append("source_account=");
                param.append(in_ops.getSourceAccount());

            }
        }
        if (Utility.isNotEmpty(in_ops.getDestinationAccount())) {
            if (!Utility.isValidAddress(in_ops.getDestinationAccount())) {
                throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_ADDRESS, in_ops.getDestinationAccount(), null);
            } else {
                if (op_num > 0)
                    param.append("&");
                else
                    param.append("?");
                op_num++;
                param.append("destination_account=");
                param.append(in_ops.getDestinationAccount());
            }
        }
        if (in_ops.getExcludeFailed()) {
            if (op_num > 0)
                param.append("&");
            else
                param.append("?");
            op_num++;
            param.append("exclude_failed=");
            param.append(in_ops.getExcludeFailed());
        }

        if (in_ops.getResultsPerPage() < 0) {
            throw new InvalidParameterException(SDChainMessage.INVALID_PAGE_INFO, String.valueOf(in_ops.getResultsPerPage()), null);
        }
        if (in_ops.getResultsPerPage() > 0) {
            if (op_num > 0)
                param.append("&");
            else
                param.append("?");
            op_num++;
            param.append("per_page=");
            param.append(in_ops.getResultsPerPage());
        }
        if (in_ops.getPage() < 0) {
            throw new InvalidParameterException(SDChainMessage.INVALID_PAGE_INFO, String.valueOf(in_ops.getPage()), null);
        }
        if (in_ops.getPage() > 0) {
            if (op_num > 0)
                param.append("&");
            else
                param.append("?");
            op_num++;
            param.append("page=");
            param.append(in_ops.getPage());
        }

        return APIServer.request(
            APIServer.RequestMethod.GET,
            APIServer.formatURL(
                Payment.class,
                this.getAddress(), param.toString()),
            null,
            Wallet.class).getPaymentsCollection();

    }

    /**
     * Get all orders of a wallet
     * @return OrderCollection instance
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException 
     * @throws FailedException 
     */
    public OrderCollection getOrderList() throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, FailedException {
        try {
            if (!isActivated()) {
                throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
            }
        } catch (InvalidRequestException e) {
            throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
        }

        return APIServer.request(
            APIServer.RequestMethod.GET,
            APIServer.formatURL(
                Order.class,
                this.getAddress()),
            null,
            Wallet.class).getOrdersCollection();

    }
    /**
     * Get order by ID
     * @param hash_id
     * @return Order instance
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws InvalidParameterException 
     * @throws FailedException 
     */
    public Order getOrder(String hash_id) throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, InvalidParameterException, FailedException {
        try {
            if (!isActivated()) {
                throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
            }
        } catch (InvalidRequestException e) {
            throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
        }
        if (Utility.isEmpty(hash_id)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_ID, hash_id, null);
        }

        return APIServer.request(
            APIServer.RequestMethod.GET,
            APIServer.formatURL(
                Order.class,
                this.getAddress(), "/" + hash_id),
            null,
            Order.class);
    }
    public OrderBookResult getOrderBook(Amount base, Amount counter)
        throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException,
        ChannelException, InvalidParameterException, FailedException {

        if (!Utility.isValidAmount(base) || !Utility.isValidAmount(counter)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_AMOUNT, null, null);
        }
        StringBuffer sb = new StringBuffer();
        sb.append("/");
        sb.append(base.getCurrency());
        if(!base.getCurrency().equals("SDA")){
        	sb.append("%2B");
            sb.append(base.getIssuer());
        }
        sb.append("/");
        sb.append(counter.getCurrency());
        if(!counter.getCurrency().equals("SDA")){
        	 sb.append("%2B");
             sb.append(counter.getIssuer());
        }
       

        if (this.secret == null) {
            throw new InvalidParameterException(SDChainMessage.INACTIVATED_ACCOUNT, null, null);
        }

        return APIServer.request(APIServer.RequestMethod.GET,
            APIServer.formatURL(OrderBook.class, this.address, sb.toString()),
            null,
            OrderBookResult.class);
    }

    /*
    *
    */
    public OrderBookResult getOrderBook(String in_pair) throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException,
        ChannelException, InvalidParameterException, FailedException {
        Amount base_amount = new Amount();
        Amount counter_amount = new Amount();
        fillAmountfromPair(in_pair, base_amount, counter_amount);
        try {
            return getOrderBook(base_amount, counter_amount);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return TransactionCollection
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws FailedException 
     */
    public TransactionCollection getTransactionList() throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, FailedException {
        try {
            return getTransactionList(null, false, null, 0, 0);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @param destinationAccount
     * @param excludeFailed
     * @param direction
     * @param resultPerPage
     * @param page
     * @return TransactionCollection
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws InvalidParameterException 
     * @throws FailedException 
     */
    public TransactionCollection getTransactionList(String destinationAccount, boolean excludeFailed, Transaction.DirectionType direction, int resultPerPage, int page)
        // public TransactionCollection getTransactionList(Options in_ops)
        throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, InvalidParameterException, FailedException {
        try {
            if (!isActivated()) {
                throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
            }
        } catch (InvalidRequestException e) {
            throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
        }
        StringBuffer param = new StringBuffer();

        // Option number setup in the list
        int op_num = 0;

        if (Utility.isNotEmpty(destinationAccount)) {
            if (!Utility.isValidAddress(destinationAccount)) {
                throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_ADDRESS, destinationAccount, null);
            } else {
                param.append("&");
                param.append("destination_account=");
                param.append(destinationAccount);
            }
        }
        if (resultPerPage < 0 || page < 0) {
            throw new InvalidParameterException(SDChainMessage.INVALID_PAGE_INFO, destinationAccount, null);
        }
        if (resultPerPage != 0) {

            if (op_num > 0)
                param.append("&");
            else
                param.append("?");
            op_num++;
            param.append("results_per_page=");
            param.append(resultPerPage);
        }
        if (page != 0) {
            if (op_num > 0)
                param.append("&");
            else
                param.append("?");
            op_num++;
            param.append("page=");
            param.append(page);
        }
        if (excludeFailed) {
            if (op_num > 0)
                param.append("&");
            else
                param.append("?");
            op_num++;
            param.append("exclude_failed=");
            param.append(excludeFailed);
        }
        if (direction != null && direction != Transaction.DirectionType.all) {
            if (op_num > 0)
                param.append("&");
            else
                param.append("?");
            op_num++;
            param.append("direction=");
            param.append(direction);
        }

        return APIServer.request(
            APIServer.RequestMethod.GET,
            APIServer.formatURL(
                Transaction.class,
                this.getAddress(),
                param.toString()),
            null,
            Wallet.class).getMyTransactionCollection();

    }

    /**
     * @param in_ops input options to filter out the outputs
     * @return TransactionCollection trans records with the acount with filtered conditions
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws FailedException
     */
    public TransactionCollection getTransactionList(Options in_ops)
        throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, InvalidParameterException, FailedException {
        try {
            if (!isActivated()) {
                throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
            }
        } catch (InvalidRequestException e) {
            throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
        }

        StringBuilder param = new StringBuilder();
        param.append("?s=s");
        if (Utility.isNotEmpty(in_ops.getSourceAccount())) {
            if (!Utility.isValidAddress(in_ops.getSourceAccount())) {
                throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_ADDRESS, in_ops.getSourceAccount(), null);
            } else {

                param.append("&");
                param.append("source_account=");
                param.append(in_ops.getSourceAccount());
            }
        }
        if (Utility.isNotEmpty(in_ops.getDestinationAccount())) {
            if (!Utility.isValidAddress(in_ops.getDestinationAccount())) {
                throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_ADDRESS, in_ops.getDestinationAccount(), null);
            } else {
                param.append("&");
                param.append("destination_account=");
                param.append(in_ops.getDestinationAccount());
            }
        }
        if (in_ops.getExcludeFailed()) {
            param.append("&");
            param.append("exclude_failed=");
            param.append(in_ops.getExcludeFailed());
        }

        if (in_ops.getResultsPerPage() < 0) {
            throw new InvalidParameterException(SDChainMessage.INVALID_PAGE_INFO, String.valueOf(in_ops.getResultsPerPage()), null);
        }
        if (in_ops.getResultsPerPage() > 0) {
            param.append("&");
            param.append("per_page=");
            param.append(in_ops.getResultsPerPage());
        }
        if (in_ops.getPage() < 0) {
            throw new InvalidParameterException(SDChainMessage.INVALID_PAGE_INFO, String.valueOf(in_ops.getPage()), null);
        }
        if (in_ops.getPage() > 0) {
            param.append("&");
            param.append("page=");
            param.append(in_ops.getPage());
        }

        return APIServer.request(
            APIServer.RequestMethod.GET,
            APIServer.formatURL(
                Transaction.class,
                this.getAddress(),
                param.toString()),
            null,
            Wallet.class).getMyTransactionCollection();
    }

    /**
     * Get transaction by hash number
     * @param id
     * @return Transaction instance
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws InvalidParameterException 
     * @throws FailedException 
     */
    public Transaction getTransaction(String id) throws AuthenticationException, InvalidRequestException,
        APIConnectionException, APIException, ChannelException, InvalidParameterException, FailedException {
        try {
            if (!isActivated()) {
                throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
            }
        } catch (InvalidRequestException e) {
            throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
        }
        if (Utility.isEmpty(id)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_ID, id, null);
        }

        return APIServer.request(
            APIServer.RequestMethod.GET,
            APIServer.formatURL(
                Transaction.class,
                this.getAddress(),
                "/" + id),
            null,
            Wallet.class).getTransaction();

        // return APIProxy.request(
        // APIProxy.RequestMethod.GET,
        // APIProxy.formatURL(
        // Transaction.class,
        // this.getAddress(),
        // "/" + id + Utility.buildSignString(this.getAddress(), this.getSecret())),
        // null,
        // Wallet.class).getTransaction();
    }

    /**
     * Get payment available payment path
     * @param receiver
     * @param amount
     * @return PaymentCollection
     * @throws InvalidParameterException
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws ChannelException
     * @throws APIException
     * @throws FailedException 
     */
    public PaymentCollection getPathList(String receiver, Amount amount) throws InvalidParameterException, AuthenticationException, InvalidRequestException, APIConnectionException, ChannelException, APIException, FailedException {
        try {
            if (!isActivated()) {
                throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
            }
        } catch (InvalidRequestException e) {
            throw new APIException(SDChainMessage.INACTIVATED_ACCOUNT, null);
        }
        if (!Utility.isValidAddress(receiver)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_ADDRESS, receiver, null);
        }
        if (!Utility.isValidAmount(amount)) {
            throw new InvalidParameterException(SDChainMessage.INVALID_SDCHAIN_AMOUNT, null, null);
        }
        if (SDChain.getCurrencySDA().equals(amount.getCurrency())) {
            throw new InvalidParameterException(SDChainMessage.CURRENCY_OTHER_THAN_SDA + SDChain.getCurrencySDA(), null, null);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("/paths/");
        sb.append(receiver);
        sb.append("/");
        sb.append(String.valueOf(amount.getValue()));
        // sb.append(amount.getValue());
        sb.append("+" + amount.getCurrency());
        sb.append("+" + amount.getIssuer());
        String urlsb = new String();
        try {
            urlsb = URLEncoder.encode(sb.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            System.out.println("UTF-8 not supported");
        }

        return APIServer.request(
            APIServer.RequestMethod.GET,
            APIServer.formatURL(
                Payment.class,
                this.getAddress(),
                sb.toString()),
            null,
            Wallet.class).getPaymentsCollection();

    }

    /*
     * Convert the Token pair string to
     * amount value
     */
    private void fillAmountfromPair(String in_pair, Amount base_amount, Amount counter_amount) throws InvalidParameterException {
        // split the pair to get the base and counter tum
        String tum_codes[] = in_pair.split("/");

        if (tum_codes.length != 2)
            throw new InvalidParameterException(SDChainMessage.INVALID_TOKEN_PAIR, in_pair, null);

        String[] base_tum = tum_codes[0].split(":", 2);
        String[] counter_tum = tum_codes[1].split(":", 2);

        // Set the source_amount and destination amount with pair, price and amount value
        base_amount.setCurrency(base_tum[0]);
        if (base_tum.length < 2) {
            if (!base_tum[0].equals("SDA")) {
                throw new InvalidParameterException(SDChainMessage.INVALID_TOKEN_PAIR, in_pair, null);
            }

            base_amount.setIssuer("");
        } else
            base_amount.setIssuer(base_tum[1]);

        counter_amount.setCurrency(counter_tum[0]);

        if (counter_tum.length < 2) {
            if (counter_tum[0] != "SDA")
                throw new InvalidParameterException(SDChainMessage.INVALID_TOKEN_PAIR, in_pair, null);

            counter_amount.setIssuer("");
        } else
            counter_amount.setIssuer(counter_tum[1]);
    }
}
