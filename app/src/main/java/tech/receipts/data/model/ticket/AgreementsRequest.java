package tech.receipts.data.model.ticket;

public class AgreementsRequest {

    private Boolean termsOfService;
    private Boolean personalDataProcessing;
    private Boolean useMyEffigy;

    public AgreementsRequest() {
    }

    public AgreementsRequest(Boolean termsOfService, Boolean personalDataProcessing, Boolean useMyEffigy) {
        this.termsOfService = termsOfService;
        this.personalDataProcessing = personalDataProcessing;
        this.useMyEffigy = useMyEffigy;
    }

    public Boolean getTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(Boolean termsOfService) {
        this.termsOfService = termsOfService;
    }

    public Boolean getPersonalDataProcessing() {
        return personalDataProcessing;
    }

    public void setPersonalDataProcessing(Boolean personalDataProcessing) {
        this.personalDataProcessing = personalDataProcessing;
    }

    public Boolean getUseMyEffigy() {
        return useMyEffigy;
    }

    public void setUseMyEffigy(Boolean useMyEffigy) {
        this.useMyEffigy = useMyEffigy;
    }
}
