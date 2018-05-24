package io.sdchain.model;

public class OrderSubmit {
    private String type;
    private Amount takerPays;
    private Amount takerGets;

    public String getType() {
        return type;
    }
    public Amount getTakerPays() {
        return takerPays;
    }
    public Amount getTakerGets() {
        return takerGets;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setTakerPays(Amount takerPays) {
        this.takerPays = takerPays;
    }
    public void setTakerGets(Amount takerGets) {
        this.takerGets = takerGets;
    }

}
