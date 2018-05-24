package io.sdchain.model;

/**
 * 
 * options used to filter the payment and transactions
 * 
 */
public class Options {

    private String sourceAccount;
    private String destinationAccount;

    private String direction;

    private int perPage;
    private int page;

    public String getSourceAccount() {
        return sourceAccount;
    }
    public String getDestinationAccount() {
        return destinationAccount;
    }
    public String getDirection() {
        return direction;
    }
    public int getPerPage() {
        return perPage;
    }
    public int getPage() {
        return page;
    }
    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }
    public void setPage(int page) {
        this.page = page;
    }

}
