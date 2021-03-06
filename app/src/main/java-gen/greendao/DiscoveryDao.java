package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Discovery;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DISCOVERY.
*/
public class DiscoveryDao extends AbstractDao<Discovery, Long> {

    public static final String TABLENAME = "DISCOVERY";

    /**
     * Properties of entity Discovery.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property ImageLink = new Property(2, String.class, "imageLink", false, "IMAGE_LINK");
        public final static Property Position = new Property(3, Integer.class, "position", false, "POSITION");
        public final static Property Num_deal = new Property(4, Integer.class, "num_deal", false, "NUM_DEAL");
    };


    public DiscoveryDao(DaoConfig config) {
        super(config);
    }
    
    public DiscoveryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DISCOVERY' (" + //
                "'ID' INTEGER PRIMARY KEY ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'IMAGE_LINK' TEXT," + // 2: imageLink
                "'POSITION' INTEGER," + // 3: position
                "'NUM_DEAL' INTEGER);"); // 4: num_deal
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DISCOVERY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Discovery entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String imageLink = entity.getImageLink();
        if (imageLink != null) {
            stmt.bindString(3, imageLink);
        }
 
        Integer position = entity.getPosition();
        if (position != null) {
            stmt.bindLong(4, position);
        }
 
        Integer num_deal = entity.getNum_deal();
        if (num_deal != null) {
            stmt.bindLong(5, num_deal);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Discovery readEntity(Cursor cursor, int offset) {
        Discovery entity = new Discovery( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // imageLink
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // position
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4) // num_deal
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Discovery entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setImageLink(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPosition(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setNum_deal(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Discovery entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Discovery entity) {
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
