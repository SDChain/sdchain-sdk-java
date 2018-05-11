package io.sdchain;

import static org.junit.Assert.*;

import java.util.Iterator;

import io.sdchain.model.*;
import io.sdchain.net.APIServer;
import org.junit.Test;

import io.sdchain.exception.APIConnectionException;
import io.sdchain.exception.APIException;
import io.sdchain.exception.AuthenticationException;
import io.sdchain.exception.ChannelException;
import io.sdchain.exception.FailedException;
import io.sdchain.exception.InvalidParameterException;
import io.sdchain.exception.InvalidRequestException;

public class TransactionTest {

    /**
    * 
    * 获取transaction信息
    */
    @Test
    public void testGetTransactions() throws InvalidParameterException, AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, FailedException {

        // 设置测试环境
        APIServer.init(BaseConfigInfo.API_SERVER, BaseConfigInfo.API_VERSION);
        Wallet wallet = new Wallet(BaseConfigInfo.TEST_SECRET_A, BaseConfigInfo.TEST_ADDRESS_A);
        Transaction tran;
        Options ops = new Options();

        // 正常情况1 接收方地址为null,excludeFailed为true
        TransactionCollection tc = wallet.getTransactionList(ops);
        ops.setDestinationAccount("");
        ops.setPage(1);
        ops.setResultsPerPage(5);
        // null,true,Transaction.DirectionType.outgoing,5,2); //参数为：支付交易的接收方地址，是否移除失败的交易历史，支付交易的方向->incoming或outgoing,每页数据量,第几页
        // 测试对象tc是否为null
        assertNotNull(tc);

        Iterator<Transaction> it1 = tc.getData().iterator();
        Integer i = 0;
        while (it1.hasNext()) {
            i++;
            tran = (Transaction) it1.next();
            assertEquals("tesSUCCESS", tran.getResult()); // 交易结果
            assertEquals("0.0001", tran.getFee()); // 交易费用

            if (tran.getAmount() != null) {
                if (tran.getAmount().getCurrency() == "SDA") {
                    assertEquals("", tran.getAmount().getIssuer()); // 货币发行方
                }
            }

            if (tran.getGets() != null) {
                if (tran.getGets().getCurrency() == "SDA") {
                    assertEquals("", tran.getGets().getIssuer()); // 货币发行方
                }
            }

            if (tran.getPays() != null) {
                if (tran.getGets().getCurrency() != "SDA") {
                    assertEquals(BaseConfigInfo.TEST_COUNTERPARTY, tran.getPays().getIssuer()); // 货币发行方
                } else {
                    assertEquals("", tran.getPays().getIssuer()); // 货币发行方
                }
            }
        }

        // 正常情况2 excludeFailed为true时
        Wallet wallet2 = new Wallet(BaseConfigInfo.TEST_SECRET_A, BaseConfigInfo.TEST_ADDRESS_A);
        // 正常情况1 接收方地址为null,excludeFailed为false
        TransactionCollection tc2 = wallet2.getTransactionList(null, true, Transaction.DirectionType.outgoing, 10, 1); // 参数为：支付交易的接收方地址，是否移除失败的交易历史，支付交易的方向->incoming或outgoing,每页数据量,第几页
        // 测试对象tc是否为null
        assertNotNull(tc2);

        // 正常情况3 excludeFailed为false时
        Wallet wallet3 = new Wallet(BaseConfigInfo.TEST_SECRET_A, BaseConfigInfo.TEST_ADDRESS_A);
        // 正常情况1 接收方地址为null,excludeFailed为false
        TransactionCollection tc3 = wallet3.getTransactionList(null, false, null, 0, 0); // 参数为：支付交易的接收方地址，是否移除失败的交易历史，支付交易的方向->incoming或outgoing,每页数据量,第几页
        // 测试对象tc是否为null
        assertNotNull(tc3);

        // 正常情况4 接收方地址为null,excludeFailed为false
        TransactionCollection tc4 = wallet.getTransactionList(null, false, Transaction.DirectionType.outgoing, 5, 2); // 参数为：支付交易的接收方地址，是否移除失败的交易历史，支付交易的方向->incoming或outgoing,每页数据量,第几页
        // 测试对象tc4是否为null
        assertNotNull(tc4);

        Iterator<Transaction> it4 = tc.getData().iterator();
        Integer i4 = 0;
        while (it4.hasNext()) {
            i4++;
            tran = (Transaction) it4.next();
            // assertEquals("",tran.getClient_resource_id()); //交易资源号
            assertEquals("0.0001", tran.getFee()); // 交易费用

            if (tran.getAmount() != null) {
                if (tran.getAmount().getCurrency() == "SDA") {
                    assertEquals("", tran.getAmount().getIssuer()); // 货币发行方
                }
            }

            if (tran.getGets() != null) {
                if (tran.getGets().getCurrency() == "SDA") {
                    assertEquals("", tran.getGets().getIssuer()); // 货币发行方
                }
            }

            if (tran.getPays() != null) {
                if (tran.getGets().getCurrency() != "SDA") {
                    assertEquals("janxMdrWE2SUzTqRUtfycH4UGewMMeHa9f", tran.getPays().getIssuer()); // 货币发行方
                } else {
                    assertEquals("", tran.getPays().getIssuer()); // 货币发行方
                }
            }
        }

        // 异常情况1 钱包未激活时
        Wallet wallet01 = new Wallet("snsrzpVA7jQZfaeDKN5r9KvX2CD5A", "69vuxT37wpBe8du3Hi4GfSQDGdMaV1cPLR");
        // 捕获异常
        try {
            @SuppressWarnings("unused")
            TransactionCollection tc01 = wallet01.getTransactionList(BaseConfigInfo.TEST_ADDRESS_A, false, Transaction.DirectionType.outgoing, 10, 1); // 参数为：支付交易的接收方地址，是否移除失败的交易历史，支付交易的方向->incoming或outgoing,每页数据量,第几页
        } catch (APIException ex) {
            assertEquals("Inactivated Account;", ex.getMessage());
        }

        // 异常情况2 resultPerPage为负数时
        Wallet wallet02 = new Wallet(BaseConfigInfo.TEST_SECRET_A, BaseConfigInfo.TEST_ADDRESS_A);
        // 捕获异常
        try {
            @SuppressWarnings("unused")
            TransactionCollection tc02 = wallet02.getTransactionList(BaseConfigInfo.TEST_ADDRESS_B, false, Transaction.DirectionType.outgoing, -1, 1); // 参数为：支付交易的接收方地址，是否移除失败的交易历史，支付交易的方向->incoming或outgoing,每页数据量,第几页
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid paging option!", ex.getMessage());
        }

        // 异常情况3 page为负数时
        Wallet wallet03 = new Wallet(BaseConfigInfo.TEST_SECRET_A, BaseConfigInfo.TEST_ADDRESS_A);;
        // 捕获异常
        try {
            @SuppressWarnings("unused")
            TransactionCollection tc03 = wallet03.getTransactionList(BaseConfigInfo.TEST_ADDRESS_B, false, Transaction.DirectionType.outgoing, 10, -1); // 参数为：支付交易的接收方地址，是否移除失败的交易历史，支付交易的方向->incoming或outgoing,每页数据量,第几页
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid paging option!", ex.getMessage());
        }

        // 异常情况4 destinationAccount为无效时
        Wallet wallet04 = new Wallet(BaseConfigInfo.TEST_SECRET_A, BaseConfigInfo.TEST_ADDRESS_A);;
        // 捕获异常
        try {
            @SuppressWarnings("unused")
            TransactionCollection tc04 = wallet04.getTransactionList("6GiohV7XVhabbtV1DfKjWEyikeyimiDSNxsssss", false, Transaction.DirectionType.outgoing, 10, -1); // 参数为：支付交易的接收方地址，是否移除失败的交易历史，支付交易的方向->incoming或outgoing,每页数据量,第几页
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid SDChain address!", ex.getMessage());
        }
    }

    /**
    * 
    * 根据hash获取transaction
    * 
    */
    @Test
    public void testGetTransactionByHash() throws InvalidParameterException, AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, FailedException {
        // 设置测试环境
        APIServer.init(BaseConfigInfo.API_SERVER, BaseConfigInfo.API_VERSION);

        Wallet wallet = new Wallet(BaseConfigInfo.TEST_SECRET_A, BaseConfigInfo.TEST_ADDRESS_A);
        Transaction tran;

        // 首先提交一个payment
        Amount jtc = new Amount(); // 构建支付的货币
        jtc.setIssuer(BaseConfigInfo.TEST_COUNTERPARTY); // 货币发行方
        jtc.setCurrency(BaseConfigInfo.TEST_CURRENCY); // 货币单位
        jtc.setValue(0.8); // 金额

        PaymentOperation op = new PaymentOperation(wallet);
        op.setDestAddress(BaseConfigInfo.TEST_ADDRESS_B);
        op.setAmount(jtc);
        op.setValidate(true);
        //// String payment_id = "paymenttest"+Long.toString(System.currentTimeMillis());
        //// op.setClientID(payment_id);//optional
        //// 3. submit payment
        RequestResult payment01 = op.submit();
        // String next_uuid = SDChainAPIAndWSServer.getTestInstance().getNextUUID();
        // RequestResult payment01 = wallet.submitPayment("jfCiWtSt4juFbS3NaXvYV9xNYxakm5yP9S", jtc, true, next_uuid); //支付，参数为：获取方地址，货币，是否等待支付结果，和资源号（选填）
        assertEquals(true, payment01.getSuccess()); // 交易是否成功
        assertEquals("validated", payment01.getState()); // 交易状态
        // assertEquals("tesSUCCESS",payment01.getResult()); //支付服务器结果
        String payment_hash = payment01.getHash();

        // 正常情况 hash值有效时
        tran = wallet.getTransaction(payment_hash); // 参数：hash值
        // 判断获取数据是否成功
        // assertEquals("tesSUCCESS",tran.getResult()); //交易结果
        assertEquals("0.0001", tran.getFee()); // 交易费用
        assertEquals(payment_hash, tran.getHash());// 交易hash值

        if (tran.getGets() != null) {
            if (tran.getGets().getCurrency() == "SDA") {
                assertEquals("", tran.getGets().getIssuer()); // 货币发行方
            }
        }

        if (tran.getPays() != null) {
            if (tran.getGets().getCurrency() != "SDA") {
                assertEquals(BaseConfigInfo.TEST_COUNTERPARTY, tran.getPays().getIssuer()); // 货币发行方
            } else {
                assertEquals("", tran.getPays().getIssuer()); // 货币发行方
            }
        }

        // 异常情况1 hash值为空时
        try {
            tran = wallet.getTransaction(""); // 参数：hash值
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid ID!", ex.getMessage());
        }

        // 异常情况2 hash值为null时
        try {
            tran = wallet.getTransaction(null); // 参数：hash值
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid ID!", ex.getMessage());
        }

        // 异常情况3 hash值为无效时
        try {
            tran = wallet.getTransaction("1111111111111111ssssssssssssssssssssssssbbbbbbbbbbbbb"); // 参数：hash值
        } catch (InvalidRequestException ex) {
            assertEquals("Error type: invalid_request\n\t Error message: Transaction not found.\n", ex.getMessage());
        }

        // 异常情况1 钱包未激活时
        Wallet wallet01 = new Wallet("shysZHBXbAVBcvV8cKoziGPqujZHr", "6BW7GY5t7dpVHM3hETE8d7DFaJgdRxvVDj");
        // 捕获异常
        try {
            @SuppressWarnings("unused")
            Transaction tran01 = wallet01.getTransaction("B1871C3D45A2D4D9C2F5943C50704C6792C56A6D838F09FC077A686FF4048A02"); // 参数：hash值
        } catch (APIException ex) {
            assertEquals("Inactivated Account;", ex.getMessage());
        }
    }
}
