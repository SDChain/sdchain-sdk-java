package io.sdchain.model;

public class SDChaindServerStatus {
    private Double reserveBaseSda;
    private String completeLedgers;
    private String serverState;
    public Double getReserveBaseSda() {
        return reserveBaseSda;
    }
    public String getCompleteLedgers() {
        return completeLedgers;
    }
    public String getServerState() {
        return serverState;
    }
    public void setReserveBaseSda(Double reserveBaseSda) {
        this.reserveBaseSda = reserveBaseSda;
    }
    public void setCompleteLedgers(String completeLedgers) {
        this.completeLedgers = completeLedgers;
    }
    public void setServerState(String serverState) {
        this.serverState = serverState;
    }
    @Override
    public String toString() {
        return "SDChaindServerStatus [reserveBaseSda=" + reserveBaseSda + ", completeLedgers=" + completeLedgers + ", serverState=" + serverState + "]";
    }

}
