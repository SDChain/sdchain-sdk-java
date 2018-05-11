package io.sdchain;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import io.sdchain.net.APIServer;
import org.junit.Test;

import io.sdchain.exception.APIConnectionException;
import io.sdchain.exception.APIException;
import io.sdchain.exception.AuthenticationException;
import io.sdchain.exception.ChannelException;
import io.sdchain.exception.FailedException;
import io.sdchain.exception.InvalidParameterException;
import io.sdchain.exception.InvalidRequestException;
import io.sdchain.model.Balance;
import io.sdchain.model.BalanceCollection;
import io.sdchain.model.Wallet;

public class BalancesTest {

    /**
    * 
    * 查询所有余额
    * @throws FailedException 
    * 
    */
    @Test
    public void testGetBalance() throws InvalidParameterException, AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, NoSuchAlgorithmException, FailedException {

        // 设置测试环境
        APIServer.init(BaseConfigInfo.API_SERVER, BaseConfigInfo.API_VERSION);
        // 已激活的钱包
        Wallet wallet = new Wallet(BaseConfigInfo.TEST_SECRET_A);
        BalanceCollection bc = wallet.getBalance();
        // 测试对象bc是否为null
        assertNotNull(bc);

        Iterator<Balance> it = bc.getData().iterator();
        while (it.hasNext()) {
            Balance bl = (Balance) it.next();
            assertNotNull(bl.getValue());
        }

        // 正常情况2 钱包未激活时
        Wallet wallet01 = new Wallet("saadV1p5vQeh4N1YdPGo3N3NS7dZo");
        try {
            @SuppressWarnings("unused")
            BalanceCollection bc1 = wallet01.getBalance();
        } catch (InvalidRequestException ex) {
            assertEquals("Error type: transaction\n\t Error message: Inactivated Account;Account not found.\n", ex.getMessage());
        }
    }

    /**
    * 
    * 用currency或counterparty作为条件查询余额
    * SDA 无counterparty
     * @throws FailedException 
    */
    @Test
    public void testGetParaBalance() throws InvalidParameterException, AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, FailedException {
        // 设置测试环境
        APIServer.init(BaseConfigInfo.API_SERVER, BaseConfigInfo.API_VERSION);

        // 已激活的钱包
        Wallet wallet = new Wallet(BaseConfigInfo.TEST_SECRET_A);
        // 正常情况1 货币为SDA，无银关时
        BalanceCollection bc = wallet.getBalance("SDA", "");
        // 测试对象bc是否为null
        assertNotNull(bc);
        Balance bl;
        Iterator<Balance> it = bc.getData().iterator();
        while (it.hasNext()) {
            bl = (Balance) it.next();
            // 判断获取账户余额信息是否正确
            assertEquals("SDA", bl.getCurrency());
            assertEquals("", bl.getIssuer());
        }

        // 正常情况2 货币为SDA，银关为6ss6oK8v3uKo33z1uL7Jqtt1abAQYu9cMq时
        BalanceCollection bc2 = wallet.getBalance(BaseConfigInfo.TEST_CURRENCY, BaseConfigInfo.TEST_COUNTERPARTY);
        Balance bl2;
        Iterator<Balance> it2 = bc2.getData().iterator();
        while (it2.hasNext()) {
            bl2 = (Balance) it2.next();
            // 判断获取账户余额信息是否正确
            assertEquals("504.0", String.valueOf(bl2.getValue()));
            assertEquals(BaseConfigInfo.TEST_CURRENCY, bl2.getCurrency());
            assertEquals(BaseConfigInfo.TEST_COUNTERPARTY, bl2.getIssuer());
        }

        // 正常情况3 货币为CNY，银关为6ss6oK8v3uKo33z1uL7Jqtt1abAQYu9cMq时
        BalanceCollection bc3 = wallet.getBalance(BaseConfigInfo.TEST_CURRENCY, BaseConfigInfo.TEST_COUNTERPARTY);
        Balance bl3;
        Iterator<Balance> it3 = bc3.getData().iterator();
        while (it3.hasNext()) {
            bl3 = (Balance) it3.next();
            // 判断获取账户余额信息是否正确
            assertEquals(BaseConfigInfo.TEST_CURRENCY, bl3.getCurrency());
            assertEquals(BaseConfigInfo.TEST_COUNTERPARTY, bl3.getIssuer());
        }

        // 正常情况4 货币为CNY，银关为空时
        BalanceCollection bc4 = wallet.getBalance(BaseConfigInfo.TEST_CURRENCY, "");
        Balance bl4;
        Iterator<Balance> it4 = bc4.getData().iterator();
        while (it4.hasNext()) {
            bl4 = (Balance) it4.next();
            // 判断获取账户余额信息是否正确
            assertEquals(BaseConfigInfo.TEST_CURRENCY, bl4.getCurrency());
        }

        // 正常情况5 货币为CNY，银关为null时
        BalanceCollection bc5 = wallet.getBalance(BaseConfigInfo.TEST_CURRENCY, null);
        Balance bl5;
        Iterator<Balance> it5 = bc5.getData().iterator();
        while (it5.hasNext()) {
            bl5 = (Balance) it5.next();
            // 判断获取账户余额信息是否正确
            assertEquals(BaseConfigInfo.TEST_CURRENCY, bl5.getCurrency());
        }

        // 异常情况1 钱包未激活时
        Wallet wallet01 = new Wallet("sh1SedpEztdwbauuCm72SyouZA14r", "6btJ4or3LVsK4Ab2k93ZEJryzyNKme2j7");
        try {
            @SuppressWarnings("unused")
            BalanceCollection bc01 = wallet01.getBalance("SDA", "");
        } catch (InvalidRequestException ex) {
            assertEquals("Error type: transaction\n\t Error message: Inactivated Account;Account not found.\n", ex.getMessage());
        }

        // 异常情况2 钱包未激活时
        Wallet wallet02 = new Wallet("sh1SedpEztdwbauuCm72SyouZA14r");
        try {
            @SuppressWarnings("unused")
            BalanceCollection bc02 = wallet02.getBalance(BaseConfigInfo.TEST_CURRENCY, BaseConfigInfo.TEST_COUNTERPARTY);
        } catch (InvalidRequestException ex) {
            assertEquals("Error type: transaction\n\t Error message: Inactivated Account;Account not found.\n", ex.getMessage());
        }

        // 异常情况3 货币为无效时，捕获异常
        try {
            Wallet wallet03 = new Wallet(BaseConfigInfo.TEST_SECRET_A);
            @SuppressWarnings("unused")
            BalanceCollection bc03 = wallet03.getBalance("CNY01", BaseConfigInfo.TEST_COUNTERPARTY);
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid currency!", ex.getMessage());
        }

        // 异常情况4 银关为无效时,捕获异常
        try {
            Wallet wallet03 = new Wallet(BaseConfigInfo.TEST_SECRET_A);
            @SuppressWarnings("unused")
            BalanceCollection bc03 = wallet03.getBalance(BaseConfigInfo.TEST_CURRENCY, "6UPd52jHtu1d88nc3S3WeroACFQpKfy000");
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid SDChain address!", ex.getMessage());
        }
    }
}
