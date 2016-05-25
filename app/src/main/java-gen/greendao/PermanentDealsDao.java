package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.PermanentDeals;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PERMANENT_DEALS.
*/
public class PermanentDealsDao extends AbstractDao<PermanentDeals, Long> {

    public static final String TABLENAME = "PERMANENT_DEALS";

    /**
     * Properties of entity PermanentDeals.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Deal_id = new Property(1, Long.class, "deal_id", false, "DEAL_ID");
        public final static Property Image = new Property(2, String.class, "image", false, "IMAGE");
        public final static Property Title = new Property(3, String.class, "title", false, "TITLE");
        public final static Property Type = new Property(4, String.class, "type", false, "TYPE");
        public final static Property Start_at = new Property(5, String.class, "start_at", false, "START_AT");
        public final static Property End_at = new Property(6, String.class, "end_at", false, "END_AT");
        public final static Property Max_claim = new Property(7, Integer.class, "max_claim", false, "MAX_CLAIM");
        public final static Property Original_price = new Property(8, Float.class, "original_price", false, "ORIGINAL_PRICE");
        public final static Property Purchase_now_price = new Property(9, Float.class, "purchase_now_price", false, "PURCHASE_NOW_PRICE");
        public final static Property Outlet_id = new Property(10, Long.class, "outlet_id", false, "OUTLET_ID");
        public final static Property Merchant_id = new Property(11, Long.class, "merchant_id", false, "MERCHANT_ID");
        public final static Property Deal_type = new Property(12, Integer.class, "deal_type", false, "DEAL_TYPE");
        public final static Property Description = new Property(13, String.class, "description", false, "DESCRIPTION");
        public final static Property Terms = new Property(14, String.class, "terms", false, "TERMS");
        public final static Property F_claimed = new Property(15, Boolean.class, "f_claimed", false, "F_CLAIMED");
        public final static Property F_call_dibs = new Property(16, Boolean.class, "f_call_dibs", false, "F_CALL_DIBS");
        public final static Property Latitude = new Property(17, Double.class, "latitude", false, "LATITUDE");
        public final static Property Longitude = new Property(18, Double.class, "longitude", false, "LONGITUDE");
        public final static Property IsNotified = new Property(19, Integer.class, "isNotified", false, "IS_NOTIFIED");
    };


    public PermanentDealsDao(DaoConfig config) {
        super(config);
    }
    
    public PermanentDealsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PERMANENT_DEALS' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'DEAL_ID' INTEGER," + // 1: deal_id
                "'IMAGE' TEXT," + // 2: image
                "'TITLE' TEXT," + // 3: title
                "'TYPE' TEXT," + // 4: type
                "'START_AT' TEXT," + // 5: start_at
                "'END_AT' TEXT," + // 6: end_at
                "'MAX_CLAIM' INTEGER," + // 7: max_claim
                "'ORIGINAL_PRICE' REAL," + // 8: original_price
                "'PURCHASE_NOW_PRICE' REAL," + // 9: purchase_now_price
                "'OUTLET_ID' INTEGER," + // 10: outlet_id
                "'MERCHANT_ID' INTEGER," + // 11: merchant_id
                "'DEAL_TYPE' INTEGER," + // 12: deal_type
                "'DESCRIPTION' TEXT," + // 13: description
                "'TERMS' TEXT," + // 14: terms
                "'F_CLAIMED' INTEGER," + // 15: f_claimed
                "'F_CALL_DIBS' INTEGER," + // 16: f_call_dibs
                "'LATITUDE' REAL," + // 17: latitude
                "'LONGITUDE' REAL," + // 18: longitude
                "'IS_NOTIFIED' INTEGER);"); // 19: isNotified
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PERMANENT_DEALS'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PermanentDeals entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long deal_id = entity.getDeal_id();
        if (deal_id != null) {
            stmt.bindLong(2, deal_id);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(3, image);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(5, type);
        }
 
        String start_at = entity.getStart_at();
        if (start_at != null) {
            stmt.bindString(6, start_at);
        }
 
        String end_at = entity.getEnd_at();
        if (end_at != null) {
            stmt.bindString(7, end_at);
        }
 
        Integer max_claim = entity.getMax_claim();
        if (max_claim != null) {
            stmt.bindLong(8, max_claim);
        }
 
        Float original_price = entity.getOriginal_price();
        if (original_price != null) {
            stmt.bindDouble(9, original_price);
        }
 
        Float purchase_now_price = entity.getPurchase_now_price();
        if (purchase_now_price != null) {
            stmt.bindDouble(10, purchase_now_price);
        }
 
        Long outlet_id = entity.getOutlet_id();
        if (outlet_id != null) {
            stmt.bindLong(11, outlet_id);
        }
 
        Long merchant_id = entity.getMerchant_id();
        if (merchant_id != null) {
            stmt.bindLong(12, merchant_id);
        }
 
        Integer deal_type = entity.getDeal_type();
        if (deal_type != null) {
            stmt.bindLong(13, deal_type);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(14, description);
        }
 
        String terms = entity.getTerms();
        if (terms != null) {
            stmt.bindString(15, terms);
        }
 
        Boolean f_claimed = entity.getF_claimed();
        if (f_claimed != null) {
            stmt.bindLong(16, f_claimed ? 1l: 0l);
        }
 
        Boolean f_call_dibs = entity.getF_call_dibs();
        if (f_call_dibs != null) {
            stmt.bindLong(17, f_call_dibs ? 1l: 0l);
        }
 
        Double latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindDouble(18, latitude);
        }
 
        Double longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindDouble(19, longitude);
        }
 
        Integer isNotified = entity.getIsNotified();
        if (isNotified != null) {
            stmt.bindLong(20, isNotified);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PermanentDeals readEntity(Cursor cursor, int offset) {
        PermanentDeals entity = new PermanentDeals( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // deal_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // image
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // title
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // type
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // start_at
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // end_at
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // max_claim
            cursor.isNull(offset + 8) ? null : cursor.getFloat(offset + 8), // original_price
            cursor.isNull(offset + 9) ? null : cursor.getFloat(offset + 9), // purchase_now_price
            cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10), // outlet_id
            cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11), // merchant_id
            cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12), // deal_type
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // description
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // terms
            cursor.isNull(offset + 15) ? null : cursor.getShort(offset + 15) != 0, // f_claimed
            cursor.isNull(offset + 16) ? null : cursor.getShort(offset + 16) != 0, // f_call_dibs
            cursor.isNull(offset + 17) ? null : cursor.getDouble(offset + 17), // latitude
            cursor.isNull(offset + 18) ? null : cursor.getDouble(offset + 18), // longitude
            cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19) // isNotified
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PermanentDeals entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDeal_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setImage(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTitle(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setStart_at(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setEnd_at(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setMax_claim(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setOriginal_price(cursor.isNull(offset + 8) ? null : cursor.getFloat(offset + 8));
        entity.setPurchase_now_price(cursor.isNull(offset + 9) ? null : cursor.getFloat(offset + 9));
        entity.setOutlet_id(cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10));
        entity.setMerchant_id(cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11));
        entity.setDeal_type(cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12));
        entity.setDescription(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setTerms(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setF_claimed(cursor.isNull(offset + 15) ? null : cursor.getShort(offset + 15) != 0);
        entity.setF_call_dibs(cursor.isNull(offset + 16) ? null : cursor.getShort(offset + 16) != 0);
        entity.setLatitude(cursor.isNull(offset + 17) ? null : cursor.getDouble(offset + 17));
        entity.setLongitude(cursor.isNull(offset + 18) ? null : cursor.getDouble(offset + 18));
        entity.setIsNotified(cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PermanentDeals entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PermanentDeals entity) {
        if(entity != null) {
            return entity.getId();
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
