package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Outlet;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table OUTLET.
*/
public class OutletDao extends AbstractDao<Outlet, Long> {

    public static final String TABLENAME = "OUTLET";

    /**
     * Properties of entity Outlet.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Outlet_id = new Property(0, Long.class, "outlet_id", true, "OUTLET_ID");
        public final static Property Merchant_id = new Property(1, Long.class, "merchant_id", false, "MERCHANT_ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Phone = new Property(3, String.class, "phone", false, "PHONE");
        public final static Property Address1 = new Property(4, String.class, "address1", false, "ADDRESS1");
        public final static Property Address2 = new Property(5, String.class, "address2", false, "ADDRESS2");
        public final static Property Zip_code = new Property(6, String.class, "zip_code", false, "ZIP_CODE");
        public final static Property Latitude = new Property(7, String.class, "latitude", false, "LATITUDE");
        public final static Property Longitude = new Property(8, String.class, "longitude", false, "LONGITUDE");
        public final static Property Secret_code = new Property(9, String.class, "secret_code", false, "SECRET_CODE");
    };


    public OutletDao(DaoConfig config) {
        super(config);
    }
    
    public OutletDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'OUTLET' (" + //
                "'OUTLET_ID' INTEGER PRIMARY KEY ," + // 0: outlet_id
                "'MERCHANT_ID' INTEGER," + // 1: merchant_id
                "'NAME' TEXT," + // 2: name
                "'PHONE' TEXT," + // 3: phone
                "'ADDRESS1' TEXT," + // 4: address1
                "'ADDRESS2' TEXT," + // 5: address2
                "'ZIP_CODE' TEXT," + // 6: zip_code
                "'LATITUDE' TEXT," + // 7: latitude
                "'LONGITUDE' TEXT," + // 8: longitude
                "'SECRET_CODE' TEXT);"); // 9: secret_code
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'OUTLET'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Outlet entity) {
        stmt.clearBindings();
 
        Long outlet_id = entity.getOutlet_id();
        if (outlet_id != null) {
            stmt.bindLong(1, outlet_id);
        }
 
        Long merchant_id = entity.getMerchant_id();
        if (merchant_id != null) {
            stmt.bindLong(2, merchant_id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(4, phone);
        }
 
        String address1 = entity.getAddress1();
        if (address1 != null) {
            stmt.bindString(5, address1);
        }
 
        String address2 = entity.getAddress2();
        if (address2 != null) {
            stmt.bindString(6, address2);
        }
 
        String zip_code = entity.getZip_code();
        if (zip_code != null) {
            stmt.bindString(7, zip_code);
        }
 
        String latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindString(8, latitude);
        }
 
        String longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindString(9, longitude);
        }
 
        String secret_code = entity.getSecret_code();
        if (secret_code != null) {
            stmt.bindString(10, secret_code);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Outlet readEntity(Cursor cursor, int offset) {
        Outlet entity = new Outlet( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // outlet_id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // merchant_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // phone
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // address1
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // address2
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // zip_code
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // latitude
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // longitude
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // secret_code
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Outlet entity, int offset) {
        entity.setOutlet_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMerchant_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPhone(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAddress1(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setAddress2(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setZip_code(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setLatitude(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setLongitude(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSecret_code(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Outlet entity, long rowId) {
        entity.setOutlet_id(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Outlet entity) {
        if(entity != null) {
            return entity.getOutlet_id();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
