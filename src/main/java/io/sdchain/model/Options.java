package io.sdchain.model;

/**
 * 
 * options used to filter the payment and transactions
 * 
 */
public class Options {

    private String source_account;
    private String destination_account;

    private Boolean exclude_failed;

    private int results_per_page;
    private int page;

    public Options() {
        source_account = "";
        destination_account = "";
        exclude_failed = false;
        results_per_page = 0;
        page = 0;
    }
    /**
     * Set the source account parameter
     * @param in_str  Input source account address
     */
    public void setSourceAccount(String in_str) {
        this.source_account = in_str;
    }

    /**
     * Set the destination account parameter
     * @param in_str
     */
    public void setDestinationAccount(String in_str) {
        this.destination_account = in_str;
    }

    /**
     * Set the source account parameter
     * @param in_flag A boolean flag to indicate if the records should include failed transactions.
     */
    public void setExcludeFailed(Boolean in_flag) {
        this.exclude_failed = in_flag;
    }

    /**
     * Set the results displayed per page
     * @param in_int number of records showed in one page
     */
    public void setResultsPerPage(int in_int) {
        this.results_per_page = in_int;
    }

    /**
     * Set the source account parameter
     * @param in_int The page number to be displayed
     */
    public void setPage(int in_int) {
        this.page = in_int;
    }

    /**
     * Get source account
     * @return source_account
     */
    public String getSourceAccount() {
        return source_account;
    }
    /**
     * Get destination account
     * @return source_account
     */
    public String getDestinationAccount() {
        return destination_account;
    }

    public Boolean getExcludeFailed() {
        return exclude_failed;
    }

    public int getPage() {
        return page;
    }

    /**
     * Get the result per page
     * @return the result per page
     */
    public int getResultsPerPage() {
        return results_per_page;
    }

}
