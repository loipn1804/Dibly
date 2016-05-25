package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.DealFilter;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DEAL_FILTER.
*/
public class DealFilterDao extends AbstractDao<DealFilter, Long> {

    public static final String TABLENAME = "DEAL_FILTER";

    /**
     * Properties of entity DealFilter.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Purchase_type = new Property(1, String.class, "purchase_type", false, "PURCHASE_TYPE");
        public final static Property Distance = new Property(2, Long.class, "distance", false, "DISTANCE");
        public final static Property Industry_type = new Property(3, String.class, "industry_type", false, "INDUSTRY_TYPE");
        public final static Property Keyword = new Property(4, String.class, "keyword", false, "KEYWORD");
    };


    public DealFilterDao(DaoConfig config) {
        super(config);
    }
    
    public DealFilterDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DEAL_FILTER' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'PURCHASE_TYPE' TEXT," + // 1: purchase_type
                "'DISTANCE' INTEGER," + // 2: distance
                "'INDUSTRY_TYPE' TEXT," + // 3: industry_type
                "'KEYWORD' TEXT);"); // 4: keyword
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DEAL_FILTER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DealFilter entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String purchase_type = entity.getPurchase_type();
        if (purchase_type != null) {
            stmt.bindString(2, purchase_type);
        }
 
        Long distance = entity.getDistance();
        if (distance != null) {
            stmt.bindLong(3, distance);
        }
 
        String industry_type = entity.getIndustry_type();
        if (industry_type != null) {
            stmt.bindString(4, industry_type);
        }
 
        String keyword = entity.getKeyword();
        if (keyword != null) {
            stmt.bindString(5, keyword);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DealFilter readEntity(Cursor cursor, int offset) {
        DealFilter entity = new DealFilter( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // purchase_type
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // distance
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // industry_type
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // keyword
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DealFilter entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPurchase_type(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDistance(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setIndustry_type(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setKeyword(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DealFilter entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DealFilter entity) {
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