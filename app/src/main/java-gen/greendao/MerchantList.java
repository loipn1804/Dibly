package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MERCHANT_LIST.
 */
public class MerchantList {

    private Long id;
    private Long merchant_id;
    private String organization_name;
    private String website_url;
    private String logo_image;
    private String industry_name;
    private String cover_image;
    private Integer k_live_deals;
    private Boolean f_follow;
    private Integer merchant_type;

    public MerchantList() {
    }

    public MerchantList(Long id) {
        this.id = id;
    }

    public MerchantList(Long id, Long merchant_id, String organization_name, String website_url, String logo_image, String industry_name, String cover_image, Integer k_live_deals, Boolean f_follow, Integer merchant_type) {
        this.id = id;
        this.merchant_id = merchant_id;
        this.organization_name = organization_name;
        this.website_url = website_url;
        this.logo_image = logo_image;
        this.industry_name = industry_name;
        this.cover_image = cover_image;
        this.k_live_deals = k_live_deals;
        this.f_follow = f_follow;
        this.merchant_type = merchant_type;
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

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getWebsite_url() {
        return website_url;
    }

    public void setWebsite_url(String website_url) {
        this.website_url = website_url;
    }

    public String getLogo_image() {
        return logo_image;
    }

    public void setLogo_image(String logo_image) {
        this.logo_image = logo_image;
    }

    public String getIndustry_name() {
        return industry_name;
    }

    public void setIndustry_name(String industry_name) {
        this.industry_name = industry_name;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public Integer getK_live_deals() {
        return k_live_deals;
    }

    public void setK_live_deals(Integer k_live_deals) {
        this.k_live_deals = k_live_deals;
    }

    public Boolean getF_follow() {
        return f_follow;
    }

    public void setF_follow(Boolean f_follow) {
        this.f_follow = f_follow;
    }

    public Integer getMerchant_type() {
        return merchant_type;
    }

    public void setMerchant_type(Integer merchant_type) {
        this.merchant_type = merchant_type;
    }

}