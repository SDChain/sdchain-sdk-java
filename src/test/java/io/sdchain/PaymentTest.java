package io.sdchain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.sdchain.exception.APIConnectionException;
import io.sdchain.exception.APIException;
import io.sdchain.exception.AuthenticationException;
import io.sdchain.exception.ChannelException;
import io.sdchain.exception.FailedException;
import io.sdchain.exception.InvalidParameterException;
import io.sdchain.exception.InvalidRequestException;
import io.sdchain.model.Amount;
import io.sdchain.model.Options;
import io.sdchain.model.Payment;
import io.sdchain.model.PaymentCollection;
import io.sdchain.model.PaymentOperation;
import io.sdchain.model.RequestResult;
import io.sdchain.model.Wallet;
import io.sdchain.net.APIServer;
import io.sdchain.util.Utility;

public class PaymentTest {

    /**
    *
    * Pay validate=true(default) test
     * @throws FailedException 
    *  
    */
    @Test
    public void testPay() throws InvalidParameterException, AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, FailedException {

        APIServer.init(BaseConfigInfo.API_SERVER, BaseConfigInfo.API_VERSION);

        // 已有钱包1余额充足 作为支付方
        Wallet wallet1 = new Wallet(BaseConfigInfo.TEST_SECRET_A); // 如进行支付，密钥为必须参数
        Options pop = new Options();//
        Amount amount = new Amount(); // 构建支付的货币
        amount.setIssuer(BaseConfigInfo.TEST_COUNTERPARTY); // 货币发行方
        amount.setCurrency(BaseConfigInfo.TEST_CURRENCY); // 货币单位
        amount.setValue(0.12); // 金额
        //
        // //Init the payment operation
        // // 1. Get the payment list by using options
        int payment_num = 2;
        pop.setResultsPerPage(payment_num);
        pop.setPage(1);
        PaymentCollection pc = wallet1.getPaymentList(pop);
        //
        // assertEquals(payment_num,wallet1.getPaymentList(pop).getData().size());
        // //change the number
        // payment_num = 3;
        // pop.setResultsPerPage(payment_num);
        assertEquals(payment_num, pc.getData().size());
        //
        // //op.setDestAddress();
        // // 2. construct payment operation
        PaymentOperation op = new PaymentOperation(wallet1);
        op.setDestAddress(BaseConfigInfo.TEST_ADDRESS_B);
        op.setAmount(amount);
        op.setValidate(true);
        // String payment_id = "paymenttest"+Long.toString(System.currentTimeMillis());
        //// op.setClientID(payment_id);//optional
        op.setMemo("Java test memo");
        // 3. submit payment
        RequestResult payment01 = op.submit();
        //
        // //正常情况1 是否等待结果为true时
        assertEquals(true, payment01.getSuccess()); // 交易是否成功
        assertEquals("validated", payment01.getState()); // 交易状态

        // Check the payment after ledger close
        Payment payment1 = wallet1.getPayment(payment01.getHash());
        assertEquals(true, payment1.getSuccess());
        assertEquals("Java test memo", Utility.hexStringToString(payment1.getMemos().getData().get(0).getMemoData().toString()));
    }

}
