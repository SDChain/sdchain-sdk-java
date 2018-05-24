package io.sdchain.model;

public class ServerInfo {
    private boolean success;
    private SDChaindServerStatus sdchaindServerStatus;

    public boolean isSuccess() {
        return success;
    }

    public SDChaindServerStatus getSdchaindServerStatus() {
        return sdchaindServerStatus;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setSdchaindServerStatus(SDChaindServerStatus sdchaindServerStatus) {
        this.sdchaindServerStatus = sdchaindServerStatus;
    }

    @Override
    public String toString() {
        return "ServerInfo [success=" + success + ", sdchaindServerStatus=" + sdchaindServerStatus + "]";
    }

}
