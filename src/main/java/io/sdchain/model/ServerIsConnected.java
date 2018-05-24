package io.sdchain.model;

public class ServerIsConnected {
    private boolean success;
    private boolean connected;

    public boolean isSuccess() {
        return success;
    }
    public boolean isConnected() {
        return connected;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

}
