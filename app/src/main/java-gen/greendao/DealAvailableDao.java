package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.DealAvailable;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DEAL_AVAILABLE.
*/
public class DealAvailableDao extends AbstractDao<DealAvailable, Long> {

    public static final String TABLENAME = "DEAL_AVAILABLE";

    /**
     * Properties of entity DealAvailable.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Deal_id = new Property(0, Long.class, "deal_id", true, "DEAL_ID");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Start_at = new Property(2, String.class, "start_at", false, "START_AT");
        public final static Property End_at = new Property(3, String.class, "end_at", false, "END_AT");
        public final static Property Purchase_now_price = new Property(4, Float.class, "purchase_now_price", false, "PURCHASE_NOW_PRICE");
        public final static Property Merchant_id = new Property(5, Long.class, "merchant_id", false, "MERCHANT_ID");
        public final static Property Organization_name = new Property(6, String.class, "organization_name", false, "ORGANIZATION_NAME");
        public final static Property Uuid = new Property(7, String.class, "uuid", false, "UUID");
        public final static Property Type = new Property(8, String.class, "type", false, "TYPE");
        public final static Property Secret_code = new Property(9, String.class, "secret_code", false, "SECRET_CODE");
        public final static Property Validity = new Property(10, String.class, "validity", false, "VALIDITY");
        public final static Property Outlet_name = new Property(11, String.class, "outlet_name", false, "OUTLET_NAME");
    };


    public DealAvailableDao(DaoConfig config) {
        super(config);
    }
    
    public DealAvailableDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DEAL_AVAILABLE' (" + //
                "'DEAL_ID' INTEGER PRIMARY KEY ," + // 0: deal_id
                "'TITLE' TEXT," + // 1: title
                "'START_AT' TEXT," + // 2: start_at
                "'END_AT' TEXT," + // 3: end_at
                "'PURCHASE_NOW_PRICE' REAL," + // 4: purchase_now_price
                "'MERCHANT_ID' INTEGER," + // 5: merchant_id
                "'ORGANIZATION_NAME' TEXT," + // 6: organization_name
                "'UUID' TEXT," + // 7: uuid
                "'TYPE' TEXT," + // 8: type
                "'SECRET_CODE' TEXT," + // 9: secret_code
                "'VALIDITY' TEXT," + // 10: validity
                "'OUTLET_NAME' TEXT);"); // 11: outlet_name
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DEAL_AVAILABLE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DealAvailable entity) {
        stmt.clearBindings();
 
        Long deal_id = entity.getDeal_id();
        if (deal_id != null) {
            stmt.bindLong(1, deal_id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String start_at = entity.getStart_at();
        if (start_at != null) {
            stmt.bindString(3, start_at);
        }
 
        String end_at = entity.getEnd_at();
        if (end_at != null) {
            stmt.bindString(4, end_at);
        }
 
        Float purchase_now_price = entity.getPurchase_now_price();
        if (purchase_now_price != null) {
            stmt.bindDouble(5, purchase_now_price);
        }
 
        Long merchant_id = entity.getMerchant_id();
        if (merchant_id != null) {
            stmt.bindLong(6, merchant_id);
        }
 
        String organization_name = entity.getOrganization_name();
        if (organization_name != null) {
            stmt.bindString(7, organization_name);
        }
 
        String uuid = entity.getUuid();
        if (uuid != null) {
            stmt.bindString(8, uuid);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(9, type);
        }
 
        String secret_code = entity.getSecret_code();
        if (secret_code != null) {
            stmt.bindString(10, secret_code);
        }
 
        String validity = entity.getValidity();
        if (validity != null) {
            stmt.bindString(11, validity);
        }
 
        String outlet_name = entity.getOutlet_name();
        if (outlet_name != null) {
            stmt.bindString(12, outlet_name);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DealAvailable readEntity(Cursor cursor, int offset) {
        DealAvailable entity = new DealAvailable( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // deal_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // start_at
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // end_at
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4), // purchase_now_price
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // merchant_id
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // organization_name
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // uuid
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // type
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // secret_code
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // validity
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // outlet_name
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DealAvailable entity, int offset) {
        entity.setDeal_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setStart_at(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setEnd_at(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPurchase_now_price(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
        entity.setMerchant_id(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setOrganization_name(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setUuid(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setType(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSecret_code(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setValidity(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setOutlet_name(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DealAvailable entity, long rowId) {
        entity.setDeal_id(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DealAvailable entity) {
        if(entity != null) {
            return entity.getDeal_id();
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
