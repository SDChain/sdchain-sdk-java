package io.sdchain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import io.sdchain.exception.RequestException;
import io.sdchain.model.ServerInfo;
import io.sdchain.net.APIServer;

public class ServerTest {
    @Before
    public void init() {
        APIServer.init(BaseTestConfig.API_SERVER, BaseTestConfig.API_VERSION);
    }

    @Test
    public void testGetTransactionFee() throws RequestException {
        Double fee = Server.getTransactionFee();
        System.out.println(fee);
        assertNotNull(fee);
    }

    @Test
    public void testGetInfo() throws RequestException {
        ServerInfo serverInfo = Server.getInfo();
        System.out.println(serverInfo);
        assertTrue(serverInfo.isSuccess());
    }

    @Test
    public void testIsConnected() throws RequestException {
        boolean connected = Server.isConnected();
        assertTrue(connected);;
    }

}
