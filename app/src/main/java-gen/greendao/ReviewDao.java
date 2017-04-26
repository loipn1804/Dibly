package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Review;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table REVIEW.
*/
public class ReviewDao extends AbstractDao<Review, Long> {

    public static final String TABLENAME = "REVIEW";

    /**
     * Properties of entity Review.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Review_id = new Property(1, Long.class, "review_id", false, "REVIEW_ID");
        public final static Property Consumer_id = new Property(2, Long.class, "consumer_id", false, "CONSUMER_ID");
        public final static Property Deal_id = new Property(3, Long.class, "deal_id", false, "DEAL_ID");
        public final static Property Merchant_id = new Property(4, Long.class, "merchant_id", false, "MERCHANT_ID");
        public final static Property Review = new Property(5, String.class, "review", false, "REVIEW");
        public final static Property Is_yay = new Property(6, Boolean.class, "is_yay", false, "IS_YAY");
        public final static Property Created_at = new Property(7, String.class, "created_at", false, "CREATED_AT");
        public final static Property Updated_at = new Property(8, String.class, "updated_at", false, "UPDATED_AT");
        public final static Property Fullname = new Property(9, String.class, "fullname", false, "FULLNAME");
        public final static Property Profile_image = new Property(10, String.class, "profile_image", false, "PROFILE_IMAGE");
    };


    public ReviewDao(DaoConfig config) {
        super(config);
    }
    
    public ReviewDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'REVIEW' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'REVIEW_ID' INTEGER," + // 1: review_id
                "'CONSUMER_ID' INTEGER," + // 2: consumer_id
                "'DEAL_ID' INTEGER," + // 3: deal_id
                "'MERCHANT_ID' INTEGER," + // 4: merchant_id
                "'REVIEW' TEXT," + // 5: review
                "'IS_YAY' INTEGER," + // 6: is_yay
                "'CREATED_AT' TEXT," + // 7: created_at
                "'UPDATED_AT' TEXT," + // 8: updated_at
                "'FULLNAME' TEXT," + // 9: fullname
                "'PROFILE_IMAGE' TEXT);"); // 10: profile_image
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'REVIEW'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Review entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long review_id = entity.getReview_id();
        if (review_id != null) {
            stmt.bindLong(2, review_id);
        }
 
        Long consumer_id = entity.getConsumer_id();
        if (consumer_id != null) {
            stmt.bindLong(3, consumer_id);
        }
 
        Long deal_id = entity.getDeal_id();
        if (deal_id != null) {
            stmt.bindLong(4, deal_id);
        }
 
        Long merchant_id = entity.getMerchant_id();
        if (merchant_id != null) {
            stmt.bindLong(5, merchant_id);
        }
 
        String review = entity.getReview();
        if (review != null) {
            stmt.bindString(6, review);
        }
 
        Boolean is_yay = entity.getIs_yay();
        if (is_yay != null) {
            stmt.bindLong(7, is_yay ? 1l: 0l);
        }
 
        String created_at = entity.getCreated_at();
        if (created_at != null) {
            stmt.bindString(8, created_at);
        }
 
        String updated_at = entity.getUpdated_at();
        if (updated_at != null) {
            stmt.bindString(9, updated_at);
        }
 
        String fullname = entity.getFullname();
        if (fullname != null) {
            stmt.bindString(10, fullname);
        }
 
        String profile_image = entity.getProfile_image();
        if (profile_image != null) {
            stmt.bindString(11, profile_image);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Review readEntity(Cursor cursor, int offset) {
        Review entity = new Review( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // review_id
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // consumer_id
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // deal_id
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // merchant_id
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // review
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0, // is_yay
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // created_at
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // updated_at
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // fullname
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // profile_image
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Review entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setReview_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setConsumer_id(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setDeal_id(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setMerchant_id(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setReview(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIs_yay(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0);
        entity.setCreated_at(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUpdated_at(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFullname(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setProfile_image(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Review entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Review entity) {
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
