package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table TEMP_DEAL_MERCHANT.
 */
public class TempDealMerchantDao extends AbstractDao<TempDealMerchant, Long> {

    public static final String TABLENAME = "TEMP_DEAL_MERCHANT";

    /**
     * Properties of entity TempDealMerchant.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Deal_id = new Property(0, Long.class, "deal_id", true, "DEAL_ID");
    }

    ;


    public TempDealMerchantDao(DaoConfig config) {
        super(config);
    }

    public TempDealMerchantDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "'TEMP_DEAL_MERCHANT' (" + //
                "'DEAL_ID' INTEGER PRIMARY KEY );"); // 0: deal_id
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TEMP_DEAL_MERCHANT'";
        db.execSQL(sql);
    }

    /**
     * @inheritdoc
     */
    @Override
    protected void bindValues(SQLiteStatement stmt, TempDealMerchant entity) {
        stmt.clearBindings();

        Long deal_id = entity.getDeal_id();
        if (deal_id != null) {
            stmt.bindLong(1, deal_id);
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /**
     * @inheritdoc
     */
    @Override
    public TempDealMerchant readEntity(Cursor cursor, int offset) {
        TempDealMerchant entity = new TempDealMerchant( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0) // deal_id
        );
        return entity;
    }

    /**
     * @inheritdoc
     */
    @Override
    public void readEntity(Cursor cursor, TempDealMerchant entity, int offset) {
        entity.setDeal_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
    }

    /**
     * @inheritdoc
     */
    @Override
    protected Long updateKeyAfterInsert(TempDealMerchant entity, long rowId) {
        entity.setDeal_id(rowId);
        return rowId;
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long getKey(TempDealMerchant entity) {
        if (entity != null) {
            return entity.getDeal_id();
        } else {
            return null;
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

}
