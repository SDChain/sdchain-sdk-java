package io.sdchain;

import io.sdchain.exception.*;
import io.sdchain.model.*;
import io.sdchain.model.Transaction.DirectionType;
import io.sdchain.net.APIServer;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderOperationTest {

    @Test
    public void testGetOrderBook() throws InvalidParameterException, AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, FailedException {
        // 设置测试环境
        APIServer.init(BaseConfigInfo.API_SERVER, BaseConfigInfo.API_VERSION);

        // 正常情况1,同步调用
        String cur_pair = "SDA/" + BaseConfigInfo.TEST_CURRENCY + ":" + BaseConfigInfo.TEST_COUNTERPARTY;
        Wallet wallet1 = new Wallet(BaseConfigInfo.TEST_SECRET_A); // 如进行支付，密钥为必须参数
        OrderBookResult order01 = wallet1.getOrderBook(cur_pair);

        // 正常情况1 是否等待结果为true时
        assertEquals(true, order01.getSuccess()); // 返回是否成功
        assertEquals(true, order01.getValidated()); // 返回方式
        assertEquals(cur_pair, order01.getPair()); // 返回方式

    }

    /**
    * 
    * submitOrder 挂单
     * CancleOrder 取消挂单测试
    * 
    */
    @Test
    public void testSubmitAndCancelOrder() throws InvalidParameterException, AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, FailedException {
        // 设置测试环境
        APIServer.init(BaseConfigInfo.API_SERVER, BaseConfigInfo.API_VERSION);
        Wallet wallet = new Wallet(BaseConfigInfo.TEST_SECRET_A); // 根据钱包地址和密钥生成钱包实例

        // 正常情况1,同步调用
        OrderOperation op = new OrderOperation(wallet);

        op.setPair("SDA/" + BaseConfigInfo.TEST_CURRENCY + ":" + BaseConfigInfo.TEST_COUNTERPARTY);
        op.setType(OrderOperation.BUY);
        op.setAmount(1.00);
        op.setPrice(5);
        op.setValidate(true);
        RequestResult order01 = op.submit();

        // 正常情况1 是否等待结果为true时
        assertEquals(true, order01.getSuccess()); // 挂单是否成功
        assertEquals("validated", order01.getState()); // 挂单方式

        //

        if (order01.getSequence() > 0) {
            Order order02 = wallet.getOrder(order01.getHash());

            assertEquals(true, order02.getSuccess()); // 挂单是否查询成功
            assertEquals(order01.getHash(), order02.getHash());// 是否查找到正确的挂单

            CancelOrderOperation cop = new CancelOrderOperation(wallet);
            cop.setSequence(order01.getSequence());
            cop.setValidate(true);
            RequestResult order03 = cop.submit();

            assertEquals(true, order03.getSuccess()); // 挂单是否取消成功

        }

        // 正常情况2,异步调用

    }

    /**
    * 
    * GetOrders 获取全部挂单
    * 
    */
    @Test
    public void testGetOrders() throws InvalidParameterException, AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, FailedException {

        APIServer.init(BaseConfigInfo.API_SERVER, BaseConfigInfo.API_VERSION);
        Wallet wallet = new Wallet(BaseConfigInfo.TEST_SECRET_A);
        OrderCollection oc = wallet.getOrderList(); // 获取所有挂单
        // 测试对象oc是否为null
        assertNotNull(oc);

        Order od_3;
        Iterator<Order> it_3 = oc.getData().iterator();
        while (it_3.hasNext()) {
            od_3 = (Order) it_3.next();
            assertEquals(Order.OrderType.sell, od_3.getType()); // 挂单类型:sell
            assertEquals(false, od_3.getPassive()); // 是否被动交易
            assertEquals("SLC", od_3.getReceive().getCurrency()); // 获得的货币单位
            assertEquals(BaseConfigInfo.TEST_COUNTERPARTY, od_3.getReceive().getIssuer()); // 获得货币的发行方
            assertEquals("SDA", od_3.getPay().getCurrency()); // 支付的货币单位
            assertEquals("", od_3.getPay().getIssuer()); // 支付货币的发行方
        }

        // 异常情况1 钱包未激活时
        Wallet wallet01 = new Wallet("sswhZu6P3PUq5J1ViaEn9MWqsY9Hq");
        // 捕获异常
        try {
            @SuppressWarnings("unused")
            OrderCollection oc01 = wallet01.getOrderList(); // 获取所有挂单
        } catch (APIException ex) {
            assertEquals("Inactivated Account;", ex.getMessage());
        }
    }

    /**
    * 
    * GetOrderByHash 根据hash值获取挂单信息
    * 
    */
    @Test
    public void testGetOrderByHash() throws InvalidParameterException, AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, FailedException {

        APIServer.init(BaseConfigInfo.API_SERVER, BaseConfigInfo.API_VERSION);
        Wallet wallet = new Wallet(BaseConfigInfo.TEST_SECRET_A, BaseConfigInfo.TEST_ADDRESS_A);

        OrderOperation op = new OrderOperation(wallet);

        op.setPair("SDA/" + BaseConfigInfo.TEST_CURRENCY + ":" + BaseConfigInfo.TEST_COUNTERPARTY);
        op.setType(OrderOperation.SELL);
        op.setAmount(1.1);
        op.setPrice(5);
        op.setValidate(true);
        RequestResult od_4 = op.submit();
        // 正常情况 hash值正确时
        Order od_5 = wallet.getOrder(od_4.getHash()); // 根据hash值获得挂单

        // 判断获取挂单信息是否成功
        assertEquals(true, od_5.getSuccess()); // 请求结果
        assertEquals(od_4.getHash(), od_5.getHash()); // 交易hash
        assertEquals("true", od_5.getValidated()); // 交易服务器状态
        assertEquals(DirectionType.outgoing, od_5.getDirection()); // 交易方向，incoming或outgoing
        assertEquals(BaseConfigInfo.TEST_ADDRESS_A, od_5.getAccount()); // 交易帐号
        assertEquals("", od_5.getPay().getIssuer());
        assertEquals("SDA", od_5.getPay().getCurrency());
        assertEquals("1.1", String.valueOf(od_5.getPay().getValue()));
        assertEquals(BaseConfigInfo.TEST_COUNTERPARTY, od_5.getReceive().getIssuer());
        assertEquals("SLC", od_5.getReceive().getCurrency());
        assertEquals("5.5", String.valueOf(od_5.getReceive().getValue()));
        assertEquals(false, od_5.getPassive()); // 交易是否是被动交易
        assertEquals(OrderOperation.SELL, od_5.getType().toString()); // 交易类型，sell或buy

        // 异常情况1 hash值为空时
        // 捕获异常
        try {
            @SuppressWarnings("unused")
            Order od_01 = wallet.getOrder(""); // 根据hash值获得挂单
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid ID!", ex.getMessage());
        }

        // 异常情况2 hash值为null时
        // 捕获异常
        try {
            @SuppressWarnings("unused")
            Order od_02 = wallet.getOrder(null); // 根据hash值获得挂单
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid ID!", ex.getMessage());
        }

        // 异常情况3 hash值为无效时
        try {
            @SuppressWarnings("unused")
            Order od_03 = wallet.getOrder("fhfhhfhfhhfhfhhffhfffhh11111sssshhhhhhahhhhh"); // 根据hash值获得挂单
        } catch (InvalidRequestException ex) {
            assertEquals("Error type: invalid_request\n\t Error message: Parameter is not a valid transaction hash: identifier\n", ex.getMessage());
        }

        // 异常情况4 钱包未激活且hash值有效时
        // 捕获异常
        try {
            Wallet wallet4 = new Wallet("sswhZu6P3PUq5J1ViaEn9MWqsY9Hq", "6MfpkbVr1xJjg1pNrtw8iXMJNx1huJw32P");
            @SuppressWarnings("unused")
            Order od_04 = wallet4.getOrder("B92FA3ED367AEE1B98A784DD7E871CEF3585757ECD940FB75734A56EF4C30875"); // 根据hash值获得挂单
        } catch (APIException ex) {
            assertEquals("Inactivated Account;", ex.getMessage());
        }
    }
}
