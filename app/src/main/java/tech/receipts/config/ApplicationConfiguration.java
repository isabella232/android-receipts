package tech.receipts.config;

public class ApplicationConfiguration {
    public static final String TRUSTSTORE_NAME = "receipts.tech.truststore.bks";
    public static final String HOSTNAME = "receipts.tech";
    public static final String HOST = "https://" + HOSTNAME;
    public static final Integer RETRY_COUNT = 2;
    public static final Integer READ_TIMEOUT_MS = 25000;
    public static final Integer CONNECTION_TIMEOUT_MS = 25000;
    public static final String JSON = "json";
    public static final String API_VERSION_1 = "application/vnd.receipts.v1+" + JSON;

    public static final Integer TRADES_CONNECTION_TIMEOUT_MS = 5000;
    public static final Integer CREATE_TICKET_CONNECTION_TIMEOUT_MS = 5000;
    public static final Integer RESULTS_CONNECTION_TIMEOUT_MS = 25000;
    public static final Integer GET_TICKETS_CONNECTION_TIMEOUT_MS = 25000;
}
