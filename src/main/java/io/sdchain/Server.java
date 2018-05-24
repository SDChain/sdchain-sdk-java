package io.sdchain;

import io.sdchain.exception.RequestException;
import io.sdchain.model.ServerInfo;
import io.sdchain.model.ServerIsConnected;
import io.sdchain.model.TransactionFee;
import io.sdchain.net.APIServer;
import io.sdchain.util.Utility;

public class Server {

    /**
     * 获取交易的费用
     * @return
     * @throws RequestException
     */
    public static Double getTransactionFee() throws RequestException {
        TransactionFee transactionFee = APIServer.request(APIServer.API_GETTRANSACTIONFEE, null, null, null, TransactionFee.class);
        if (transactionFee == null || Utility.isEmpty(transactionFee.getFee())) {
            return null;
        }
        return Double.valueOf(transactionFee.getFee());
    }

    /**
     * 获取底层服务状态信息
     * @return
     * @throws RequestException
     */
    public static ServerInfo getInfo() throws RequestException {
        return APIServer.request(APIServer.API_GETINFO, null, null, null, ServerInfo.class);
    }

    /**
     * 判断rest是否成功连接到了底层
     * @return
     * @throws RequestException
     */
    public static boolean isConnected() throws RequestException {
        ServerIsConnected serverIsConnected = APIServer.request(APIServer.API_ISCONNECTED, null, null, null, ServerIsConnected.class);
        return serverIsConnected.isConnected();
    }
}
