package io.sdchain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.sdchain.exception.InvalidParameterException;
import io.sdchain.exception.RequestException;
import io.sdchain.model.AccountWallet;
import io.sdchain.model.Amount;
import io.sdchain.model.Balance;
import io.sdchain.model.BalanceCollection;
import io.sdchain.model.Memo;
import io.sdchain.model.Options;
import io.sdchain.model.Order;
import io.sdchain.model.OrderCollection;
import io.sdchain.model.Payment;
import io.sdchain.model.PaymentCollection;
import io.sdchain.model.RequestResult;
import io.sdchain.model.Transaction;
import io.sdchain.model.TransactionCollection;
import io.sdchain.net.APIServer;

public class WalletTest {
    @Before
    public void init() {
        APIServer.init(BaseTestConfig.API_SERVER, BaseTestConfig.API_VERSION);
    }

    @Test
    public void testNewWallet() throws InvalidParameterException {
        AccountWallet newWallet = Wallet.newWallet();
        String address = newWallet.getAddress();
        String secret = newWallet.getSecret();
        System.out.println("newWallet: " + newWallet.getAddress());
        System.out.println("newWallet: " + newWallet.getSecret());
        assertEquals("6", address.substring(0, 1));
        assertEquals("s", secret.substring(0, 1));
    }

    @Test
    public void testGetBalance() throws RequestException {
        BalanceCollection balance = Wallet.getBalance(BaseTestConfig.TEST_ADDRESS_A);
        Iterator<Balance> it = balance.getData().iterator();
        while (it.hasNext()) {
            Balance bl = (Balance) it.next();
            System.out.println(bl.toString());
            assertNotNull(bl.getValue());
        }
    }

    @Test
    public void testSubmitPayment() throws RequestException {
        Amount amount = new Amount();
        amount.setValue(10);
        amount.setCurrency("SDA");
        amount.setIssuer("");
        Memo memo = new Memo();
        memo.setMemoType("javaSdk");
        memo.setMemoData("" + new Date());
        RequestResult result = Wallet.submitPayment(BaseTestConfig.TEST_SECRET_A, BaseTestConfig.TEST_ADDRESS_A, BaseTestConfig.TEST_ADDRESS_B, amount, memo);
        System.out.println(result);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testGetPaymentList() throws RequestException {
        Options opts = new Options();
        opts.setPerPage(200);
        PaymentCollection paymentList = Wallet.getPaymentList(BaseTestConfig.TEST_ADDRESS_A, opts);
        assertNotNull(paymentList);
        List<Payment> data = paymentList.getData();
        for (Payment payment : data) {
            System.out.println(payment);
            assertNotNull(payment);
        }
    }

    @Test
    public void testGetPaymentInfo() throws RequestException {
        Payment paymentInfo = Wallet.getPaymentInfo(BaseTestConfig.TEST_ADDRESS_A, "FA3C029C5068CABF430F1BA390F124D07BC4CFEA3F4FD04009D1FE52F1199907");
        System.out.println(paymentInfo);
        assertNotNull(paymentInfo);
    }

    @Test
    public void testGetTransactionList() throws RequestException {
        Options opts = new Options();
        opts.setPerPage(200);
        TransactionCollection transactionList = Wallet.getTransactionList(BaseTestConfig.TEST_ADDRESS_A, opts);
        assertNotNull(transactionList);
        List<Transaction> data = transactionList.getData();
        for (Transaction transaction : data) {
            System.out.println(transaction);
            assertNotNull(transaction);
        }
    }
    
    @Test
    public void testTGetTransactionInfo() throws RequestException{
        Transaction transaction = Wallet.getTransactionInfo(BaseTestConfig.TEST_ADDRESS_A, "D908CF3CF7687F1ADC33A4BD7621858E02F043D770AFA32A707AFC1713619B55");
        System.out.println(transaction);
        assertNotNull(transaction);
    }

    @Test
    public void testGetOrderList() throws RequestException {
        OrderCollection orderList = Wallet.getOrderList(BaseTestConfig.TEST_ADDRESS_A);
        assertNotNull(orderList);
        List<Order> data = orderList.getData();
        for (Order order : data) {
            System.out.println(order);
            assertNotNull(order);
        }
    }

    @Test
    public void testSubmitOrder() throws RequestException {
        Amount baseAmount = new Amount(5, "CNY", "6ULez2WuK5Hhq8AKK8X4NDmN8cVF5hYy4N");
        Amount counterAmount = new Amount(1, "SDA", "");
        RequestResult result = Wallet.submitOrder(BaseTestConfig.TEST_SECRET_A, BaseTestConfig.TEST_ADDRESS_A, baseAmount, counterAmount, true);
        assertTrue(result.isSuccess());
        System.out.println(result);
        String hash = result.getHash();
        Order orderInfo = Wallet.getOrderInfo(BaseTestConfig.TEST_ADDRESS_A, hash);
        System.out.println(orderInfo);
        assertNotNull(orderInfo);
    }

    @Test
    public void testDeleteOrder() throws RequestException {
        RequestResult result = Wallet.deleteOrder(BaseTestConfig.TEST_SECRET_A, BaseTestConfig.TEST_ADDRESS_A, "15");
        System.out.println(result);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testGetOrderInfo() throws RequestException {
        Order orderInfo = Wallet.getOrderInfo(BaseTestConfig.TEST_ADDRESS_A, "48B6C19C174E353AC2FAEF277C05705DAAA24C3C5C1A56F0D9DDF20837C16EA3");
        assertNotNull(orderInfo);
    }
}
