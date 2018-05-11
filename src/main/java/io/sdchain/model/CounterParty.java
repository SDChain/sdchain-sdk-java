package io.sdchain.model;

/**
 * Counter Party class
 */
public class CounterParty extends SDChainObject {
    private String account;
    private int seq;
    private String hash;
    public int getSeq() {
        return seq;
    }
    public String getAccount() {
        return account;
    }
    public String getHash() {
        return hash;
    }
}
