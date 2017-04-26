package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.ObjectDealMerchant;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table OBJECT_DEAL_MERCHANT.
*/
public class ObjectDealMerchantDao extends AbstractDao<ObjectDealMerchant, Long> {

    public static final String TABLENAME = "OBJECT_DEAL_MERCHANT";

    /**
     * Properties of entity ObjectDealMerchant.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Merchant_id = new Property(1, Long.class, "merchant_id", false, "MERCHANT_ID");
        public final static Property Deal_id = new Property(2, Long.class, "deal_id", false, "DEAL_ID");
        public final static Property Organization_name = new Property(3, String.class, "organization_name", false, "ORGANIZATION_NAME");
        public final static Property Logo_image_url = new Property(4, String.class, "logo_image_url", false, "LOGO_IMAGE_URL");
    };


    public ObjectDealMerchantDao(DaoConfig config) {
        super(config);
    }
    
    public ObjectDealMerchantDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'OBJECT_DEAL_MERCHANT' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'MERCHANT_ID' INTEGER," + // 1: merchant_id
                "'DEAL_ID' INTEGER," + // 2: deal_id
                "'ORGANIZATION_NAME' TEXT," + // 3: organization_name
                "'LOGO_IMAGE_URL' TEXT);"); // 4: logo_image_url
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'OBJECT_DEAL_MERCHANT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ObjectDealMerchant entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long merchant_id = entity.getMerchant_id();
        if (merchant_id != null) {
            stmt.bindLong(2, merchant_id);
        }
 
        Long deal_id = entity.getDeal_id();
        if (deal_id != null) {
            stmt.bindLong(3, deal_id);
        }
 
        String organization_name = entity.getOrganization_name();
        if (organization_name != null) {
            stmt.bindString(4, organization_name);
        }
 
        String logo_image_url = entity.getLogo_image_url();
        if (logo_image_url != null) {
            stmt.bindString(5, logo_image_url);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ObjectDealMerchant readEntity(Cursor cursor, int offset) {
        ObjectDealMerchant entity = new ObjectDealMerchant( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // merchant_id
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // deal_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // organization_name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // logo_image_url
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ObjectDealMerchant entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMerchant_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setDeal_id(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setOrganization_name(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLogo_image_url(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ObjectDealMerchant entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ObjectDealMerchant entity) {
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
