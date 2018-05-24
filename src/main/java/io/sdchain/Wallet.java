package io.sdchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.sdchain.core.crypto.ecdsa.Seed;
import io.sdchain.exception.InvalidParameterException;
import io.sdchain.exception.RequestException;
import io.sdchain.model.AccountWallet;
import io.sdchain.model.Amount;
import io.sdchain.model.BalanceCollection;
import io.sdchain.model.Memo;
import io.sdchain.model.Options;
import io.sdchain.model.Order;
import io.sdchain.model.OrderCollection;
import io.sdchain.model.OrderSubmit;
import io.sdchain.model.Payment;
import io.sdchain.model.PaymentCollection;
import io.sdchain.model.PaymentSubmit;
import io.sdchain.model.RequestResult;
import io.sdchain.model.Transaction;
import io.sdchain.model.TransactionCollection;
import io.sdchain.net.APIServer;

public class Wallet {
    public static final String ORDER_TYPE_BUY = "buy";
    public static final String ORDER_TYPE_SELL = "sell";

    /**
     * 获取新钱包
     * @return
     */
    public static AccountWallet newWallet() {
        String secret = Seed.generateSecret();
        String address = null;
        try {
            address = Seed.computeAddress(secret);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        try {
            return new AccountWallet(secret, address);
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得账户余额
     * @param address 
     * @return
     * @throws RequestException 
     */
    public static BalanceCollection getBalance(String address) throws RequestException {
        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put("address", address);

        AccountWallet wallet = APIServer.request(APIServer.API_GETBALANCE, pathVars, null, null, AccountWallet.class);

        return wallet.getBalances();
    }

    /**
     * 支付提交请求
     * @param secret
     * @param sourceAddress
     * @param destAddress
     * @param amount
     * @param memo
     * @return
     * @throws RequestException 
     */
    public static RequestResult submitPayment(String secret, String sourceAddress, String destAddress, Amount amount, Memo memo) throws RequestException {
        PaymentSubmit paymentOp = new PaymentSubmit();
        paymentOp.setSourceAccount(sourceAddress);
        paymentOp.setDestinationAccount(destAddress);
        paymentOp.setAmount(amount);
        List<Memo> memos = new ArrayList<Memo>();
        if (memo != null) {
            memos.add(memo);
        }
        paymentOp.setMemos(memos);

        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put("address", sourceAddress);
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("submit", "true");

        Map<String, Object> content = new HashMap<String, Object>();
        content.put("secret", secret);
        content.put("payment", paymentOp);

        String body = APIServer.GSON.toJson(content);
        RequestResult result = APIServer.request(APIServer.API_SUBMITPAYMENT, pathVars, urlParams, body, RequestResult.class);
        return result;
    }

    /**
     * 获取支付信息列表
     * @param address
     * @param opts
     * @return
     * @throws RequestException 
     */
    public static PaymentCollection getPaymentList(String address, Options opts) throws RequestException {

        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put("address", address);
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("source_account", opts.getSourceAccount());
        urlParams.put("destination_account", opts.getDestinationAccount());
        urlParams.put("direction", opts.getDirection());
        urlParams.put("per_page", "" + opts.getPerPage());
        urlParams.put("page", "" + opts.getPage());

        return APIServer.request(APIServer.API_GETPAYMENTLIST, pathVars, urlParams, null, AccountWallet.class).getPaymentsCollection();
    }

    /**
     * 获取指定支付历史详情
     * @param address
     * @param hash
     * @return
     * @throws RequestException 
     */
    public static Payment getPaymentInfo(String address, String hash) throws RequestException {
        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put("address", address);
        pathVars.put("hash", hash);
        return APIServer.request(APIServer.API_GETPAYMENTINFO, pathVars, null, null, Payment.class);
    }

    /**
     * 获取交易历史
     * @param address
     * @param opts
     * @return
     * @throws RequestException 
     */
    public static TransactionCollection getTransactionList(String address, Options opts) throws RequestException {
        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put("address", address);
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("source_account", opts.getSourceAccount());
        urlParams.put("destination_account", opts.getDestinationAccount());
        urlParams.put("direction", opts.getDirection());
        urlParams.put("per_page", "" + opts.getPerPage());
        urlParams.put("page", "" + opts.getPage());

        return APIServer.request(APIServer.API_GETTRANSACTIONLIST, pathVars, urlParams, null, AccountWallet.class).getTransactionCollection();
    }

    /**
     * 获取交易详情
     * @param address
     * @param hash
     * @return
     * @throws RequestException
     */
    public static Transaction getTransactionInfo(String address, String hash) throws RequestException {
        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put("address", address);
        pathVars.put("hash", hash);
        return APIServer.request(APIServer.API_GETTRANSACTIONINFO, pathVars, null, null, AccountWallet.class).getTransaction();
    }

    /**
     * 获取挂单历史
     * @param address
     * @return
     * @throws RequestException 
     */
    public static OrderCollection getOrderList(String address) throws RequestException {
        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put("address", address);
        return APIServer.request(APIServer.API_GETORDERLIST, pathVars, null, null, AccountWallet.class).getOrdersCollection();
    }

    /**
     * 提交挂单
     * @param secret
     * @param address
     * @param baseAmount
     * @param counterAmount
     * @param isBuy
     * @return
     * @throws RequestException 
     */
    public static RequestResult submitOrder(String secret, String address, Amount baseAmount, Amount counterAmount, boolean isBuy) throws RequestException {
        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put("address", address);

        OrderSubmit order = new OrderSubmit();
        if (isBuy) {
            order.setType(ORDER_TYPE_BUY);
            order.setTakerPays(baseAmount);
            order.setTakerGets(counterAmount);
        } else {
            order.setType(ORDER_TYPE_SELL);
            order.setTakerPays(counterAmount);
            order.setTakerGets(baseAmount);
        }

        Map<String, Object> content = new HashMap<String, Object>();
        content.put("secret", secret);
        content.put("order", order);
        String body = APIServer.GSON.toJson(content);

        return APIServer.request(APIServer.API_SUBMITORDER, pathVars, null, body, RequestResult.class);
    }

    /**
     * 取消挂单
     * @param secret
     * @param address
     * @param sequence
     * @return
     * @throws RequestException 
     */
    public static RequestResult deleteOrder(String secret, String address, String sequence) throws RequestException {
        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put("address", address);
        pathVars.put("sequence", sequence);

        Map<String, Object> content = new HashMap<String, Object>();
        content.put("secret", secret);
        String body = APIServer.GSON.toJson(content);

        return APIServer.request(APIServer.API_DELETEORDER, pathVars, null, body, RequestResult.class);
    }

    /**
     * 获取指定挂单的详情
     * @param address
     * @param hash
     * @return
     * @throws RequestException 
     */
    public static Order getOrderInfo(String address, String hash) throws RequestException {
        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put("address", address);
        pathVars.put("hash", hash);

        return APIServer.request(APIServer.API_GETORDERINFO, pathVars, null, null, Order.class);

    }

}
