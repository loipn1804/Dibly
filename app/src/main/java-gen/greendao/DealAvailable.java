package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DEAL_AVAILABLE.
 */
public class DealAvailable {

    private Long deal_id;
    private String title;
    private String start_at;
    private String end_at;
    private Float purchase_now_price;
    private Long merchant_id;
    private String organization_name;
    private String uuid;
    private String type;
    private String secret_code;
    private String validity;
    private String outlet_name;

    public DealAvailable() {
    }

    public DealAvailable(Long deal_id) {
        this.deal_id = deal_id;
    }

    public DealAvailable(Long deal_id, String title, String start_at, String end_at, Float purchase_now_price, Long merchant_id, String organization_name, String uuid, String type, String secret_code, String validity, String outlet_name) {
        this.deal_id = deal_id;
        this.title = title;
        this.start_at = start_at;
        this.end_at = end_at;
        this.purchase_now_price = purchase_now_price;
        this.merchant_id = merchant_id;
        this.organization_name = organization_name;
        this.uuid = uuid;
        this.type = type;
        this.secret_code = secret_code;
        this.validity = validity;
        this.outlet_name = outlet_name;
    }

    public Long getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(Long deal_id) {
        this.deal_id = deal_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public Float getPurchase_now_price() {
        return purchase_now_price;
    }

    public void setPurchase_now_price(Float purchase_now_price) {
        this.purchase_now_price = purchase_now_price;
    }

    public Long getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(Long merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSecret_code() {
        return secret_code;
    }

    public void setSecret_code(String secret_code) {
        this.secret_code = secret_code;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }

}
