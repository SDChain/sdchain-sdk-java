package io.sdchain;

import static org.junit.Assert.*;

import io.sdchain.net.APIServer;
import org.junit.Test;

import io.sdchain.exception.APIConnectionException;
import io.sdchain.exception.APIException;
import io.sdchain.exception.AuthenticationException;
import io.sdchain.exception.ChannelException;
import io.sdchain.exception.InvalidParameterException;
import io.sdchain.exception.InvalidRequestException;
import io.sdchain.model.Wallet;

public class WalletTest {

    /**
    *
    * No parameters
    */
    @Test
    public void testWallet() throws AuthenticationException, InvalidRequestException,
        InvalidParameterException, APIConnectionException, APIException, ChannelException {

        // 正常创建钱包
        APIServer.init(BaseConfigInfo.API_SERVER, BaseConfigInfo.API_VERSION);

        // 正常情况 钱包尚未激活且地址正确时
        Wallet wallet = Wallet.createWallet();
        assertEquals("6", (wallet.getAddress()).substring(0, 1));
        assertEquals("s", (wallet.getSecret()).substring(0, 1));
    }

    /**
    *
    * Having parameters
    * @throws InvalidParameterException 
    */
    // @Test
    public void testParametersWallet() throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, InvalidParameterException {

        // 正常创建钱包
        Wallet wallet2 = new Wallet(BaseConfigInfo.TEST_SECRET_A, BaseConfigInfo.TEST_ADDRESS_A);
        assertEquals(BaseConfigInfo.TEST_ADDRESS_A, wallet2.getAddress());
        assertEquals(BaseConfigInfo.TEST_SECRET_A, wallet2.getSecret());

        // 异常情况1 地址为空时
        try {
            @SuppressWarnings("unused")
            Wallet wallet01 = new Wallet("", BaseConfigInfo.TEST_SECRET_A);
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid address or secret!", ex.getMessage());
        }

        // 异常情况2 密钥为空时
        try {
            @SuppressWarnings("unused")
            Wallet wallet02 = new Wallet(BaseConfigInfo.TEST_ADDRESS_A, "");
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid address or secret!", ex.getMessage());
        }

        // 异常情况3 地址和密钥都为空时
        try {
            @SuppressWarnings("unused")
            Wallet wallet03 = new Wallet("", "");
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid address or secret!", ex.getMessage());
        }

        // 异常情况4 地址为null时
        try {
            @SuppressWarnings("unused")
            Wallet wallet04 = new Wallet(null, BaseConfigInfo.TEST_SECRET_A);
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid address or secret!", ex.getMessage());
        }

        // 异常情况5 密钥为null时
        try {
            @SuppressWarnings("unused")
            Wallet wallet05 = new Wallet(BaseConfigInfo.TEST_ADDRESS_A, null);
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid address or secret!", ex.getMessage());
        }

        // 异常情况6 地址和密钥都为null时
        try {
            @SuppressWarnings("unused")
            Wallet wallet06 = new Wallet(null, null);
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid address or secret!", ex.getMessage());
        }

        // 异常情况07 地址和密钥不配对时
        try {
            @SuppressWarnings("unused")
            Wallet wallet07 = new Wallet("spoWzgEy9oTnkK6ZgiAY1stWp2vsC", BaseConfigInfo.TEST_ADDRESS_A);
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid address or secret!", ex.getMessage());
        }

        // 异常情况8 地址为无效时
        try {
            @SuppressWarnings("unused")
            Wallet wallet08 = new Wallet("111ssssssss", BaseConfigInfo.TEST_SECRET_A);
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid address or secret!", ex.getMessage());
        }

        // 异常情况9 密钥为无效时
        try {
            @SuppressWarnings("unused")
            Wallet wallet09 = new Wallet(BaseConfigInfo.TEST_ADDRESS_A, "aaaaaa1111");
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid address or secret!", ex.getMessage());
        }

        // 异常情况10 地址和密钥都无效时
        try {
            @SuppressWarnings("unused")
            Wallet wallet10 = new Wallet("@@@@bbbb2222ssssssssssssssssssssssssss001", "***aaa1111");
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid address or secret!", ex.getMessage());
        }
    }

    /**
    *
    *  secret parameter
    */
    // @Test
    public void testSecretWallet() throws AuthenticationException, InvalidRequestException, APIConnectionException, APIException, ChannelException, InvalidParameterException {
        // 正常创建钱包 密钥有效时
        Wallet wallet = new Wallet(BaseConfigInfo.TEST_SECRET_A);
        assertEquals(BaseConfigInfo.TEST_SECRET_A, wallet.getSecret());
        assertEquals(BaseConfigInfo.TEST_ADDRESS_A, wallet.getAddress());

        // 异常情况1 密钥为空时
        try {
            @SuppressWarnings("unused")
            Wallet wallet01 = new Wallet("");
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid SDChain account secret!", ex.getMessage());
        }

        // 异常情况2 密钥为null时
        try {
            @SuppressWarnings("unused")
            Wallet wallet02 = new Wallet(null);
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid SDChain account secret!", ex.getMessage());
        }

        // 异常情况3 密钥为无效时
        try {
            @SuppressWarnings("unused")
            Wallet wallet03 = new Wallet("1111111111111ssssssssaaaaa22222");
        } catch (InvalidParameterException ex) {
            assertEquals("Invalid SDChain account secret!", ex.getMessage());
        }
    }
}