package com.dibs.dibly.viewModel;

import greendao.ObjectDeal;

/**
 * Created by VuPhan on 4/20/16.
 */
public class DealsOutletMapsModel {

    private String title;
    private String outletId;
    private String latitude;
    private String longtitude;
    private String organizationName;
    private ObjectDeal objectDeal;


    public DealsOutletMapsModel(){
    }

    public DealsOutletMapsModel(String title, String outletId, String latitude, String longtitude, String organizationName) {
        this.title = title;
        this.outletId = outletId;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.organizationName = organizationName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public ObjectDeal getObjectDeal() {
        return objectDeal;
    }

    public void setObjectDeal(ObjectDeal objectDeal) {
        this.objectDeal = objectDeal;
    }
}
