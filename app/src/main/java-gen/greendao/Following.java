package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table FOLLOWING.
 */
public class Following {

    private Long id;
    private Long merchant_id;
    private String merchant_name;
    private Integer num_of_new_deals;
    private String logo_image_url;
    private String cover_image_url;

    public Following() {
    }

    public Following(Long id) {
        this.id = id;
    }

    public Following(Long id, Long merchant_id, String merchant_name, Integer num_of_new_deals, String logo_image_url, String cover_image_url) {
        this.id = id;
        this.merchant_id = merchant_id;
        this.merchant_name = merchant_name;
        this.num_of_new_deals = num_of_new_deals;
        this.logo_image_url = logo_image_url;
        this.cover_image_url = cover_image_url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(Long merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public Integer getNum_of_new_deals() {
        return num_of_new_deals;
    }

    public void setNum_of_new_deals(Integer num_of_new_deals) {
        this.num_of_new_deals = num_of_new_deals;
    }

    public String getLogo_image_url() {
        return logo_image_url;
    }

    public void setLogo_image_url(String logo_image_url) {
        this.logo_image_url = logo_image_url;
    }

    public String getCover_image_url() {
        return cover_image_url;
    }

    public void setCover_image_url(String cover_image_url) {
        this.cover_image_url = cover_image_url;
    }

}
