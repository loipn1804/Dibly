package com.dibs.dibly.location;

import greendao.ObjectDeal;
import greendao.ObjectDealOutlet;

/**
 * Created by USER on 7/9/2015.
 */
public class DLocation {
    public ObjectDeal dealInfo;
    public ObjectDealOutlet outletInfo;

    public DLocation() {

    }

    public double getLatitude() {


        if (outletInfo != null) {
            return Double.parseDouble(outletInfo.getLatitude());
        } else {
            return 0.0;
        }
    }

    public double getLongitude() {
        if (outletInfo != null) {
            return Double.parseDouble(outletInfo.getLongitude());
        } else {
            return 0.0;
        }
    }
}
