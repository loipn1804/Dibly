package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table OBJECT_DEAL_OUTLET.
 */
public class ObjectDealOutlet {

    private Long id;
    private Long outlet_id;
    private Long deal_id;
    private String latitude;
    private String longitude;
    private Float distance;
    private String name;
    private String phone;
    private String address1;
    private String address2;

    public ObjectDealOutlet() {
    }

    public ObjectDealOutlet(Long id) {
        this.id = id;
    }

    public ObjectDealOutlet(Long id, Long outlet_id, Long deal_id, String latitude, String longitude, Float distance, String name, String phone, String address1, String address2) {
        this.id = id;
        this.outlet_id = outlet_id;
        this.deal_id = deal_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.name = name;
        this.phone = phone;
        this.address1 = address1;
        this.address2 = address2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(Long outlet_id) {
        this.outlet_id = outlet_id;
    }

    public Long getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(Long deal_id) {
        this.deal_id = deal_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

}
