package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.MyLocation;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table MY_LOCATION.
*/
public class MyLocationDao extends AbstractDao<MyLocation, Long> {

    public static final String TABLENAME = "MY_LOCATION";

    /**
     * Properties of entity MyLocation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Latitude = new Property(1, Double.class, "latitude", false, "LATITUDE");
        public final static Property Longitude = new Property(2, Double.class, "longitude", false, "LONGITUDE");
        public final static Property Distance = new Property(3, Double.class, "distance", false, "DISTANCE");
        public final static Property LastUpdateTime = new Property(4, String.class, "lastUpdateTime", false, "LAST_UPDATE_TIME");
    };


    public MyLocationDao(DaoConfig config) {
        super(config);
    }
    
    public MyLocationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'MY_LOCATION' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'LATITUDE' REAL," + // 1: latitude
                "'LONGITUDE' REAL," + // 2: longitude
                "'DISTANCE' REAL," + // 3: distance
                "'LAST_UPDATE_TIME' TEXT);"); // 4: lastUpdateTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'MY_LOCATION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, MyLocation entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Double latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindDouble(2, latitude);
        }
 
        Double longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindDouble(3, longitude);
        }
 
        Double distance = entity.getDistance();
        if (distance != null) {
            stmt.bindDouble(4, distance);
        }
 
        String lastUpdateTime = entity.getLastUpdateTime();
        if (lastUpdateTime != null) {
            stmt.bindString(5, lastUpdateTime);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public MyLocation readEntity(Cursor cursor, int offset) {
        MyLocation entity = new MyLocation( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getDouble(offset + 1), // latitude
            cursor.isNull(offset + 2) ? null : cursor.getDouble(offset + 2), // longitude
            cursor.isNull(offset + 3) ? null : cursor.getDouble(offset + 3), // distance
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // lastUpdateTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, MyLocation entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLatitude(cursor.isNull(offset + 1) ? null : cursor.getDouble(offset + 1));
        entity.setLongitude(cursor.isNull(offset + 2) ? null : cursor.getDouble(offset + 2));
        entity.setDistance(cursor.isNull(offset + 3) ? null : cursor.getDouble(offset + 3));
        entity.setLastUpdateTime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(MyLocation entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(MyLocation entity) {
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
