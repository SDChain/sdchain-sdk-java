package io.sdchain.model;

import java.util.List;

public class PaymentSubmit {
    private String sourceAccount;
    private String destinationAccount;
    private Amount amount;
    private List<Memo> memos;

    public String getSourceAccount() {
        return sourceAccount;
    }
    public String getDestinationAccount() {
        return destinationAccount;
    }
    public Amount getAmount() {
        return amount;
    }
    public List<Memo> getMemos() {
        return memos;
    }
    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
    public void setAmount(Amount amount) {
        this.amount = amount;
    }
    public void setMemos(List<Memo> memos) {
        this.memos = memos;
    }

}
