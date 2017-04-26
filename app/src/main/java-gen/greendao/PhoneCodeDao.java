package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.PhoneCode;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PHONE_CODE.
*/
public class PhoneCodeDao extends AbstractDao<PhoneCode, Void> {

    public static final String TABLENAME = "PHONE_CODE";

    /**
     * Properties of entity PhoneCode.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Integer.class, "id", false, "ID");
        public final static Property Code = new Property(1, String.class, "code", false, "CODE");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Dial_code = new Property(3, String.class, "dial_code", false, "DIAL_CODE");
        public final static Property F_disabled = new Property(4, String.class, "f_disabled", false, "F_DISABLED");
    };


    public PhoneCodeDao(DaoConfig config) {
        super(config);
    }
    
    public PhoneCodeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PHONE_CODE' (" + //
                "'ID' INTEGER," + // 0: id
                "'CODE' TEXT," + // 1: code
                "'NAME' TEXT," + // 2: name
                "'DIAL_CODE' TEXT," + // 3: dial_code
                "'F_DISABLED' TEXT);"); // 4: f_disabled
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PHONE_CODE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PhoneCode entity) {
        stmt.clearBindings();
 
        Integer id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(2, code);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String dial_code = entity.getDial_code();
        if (dial_code != null) {
            stmt.bindString(4, dial_code);
        }
 
        String f_disabled = entity.getF_disabled();
        if (f_disabled != null) {
            stmt.bindString(5, f_disabled);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public PhoneCode readEntity(Cursor cursor, int offset) {
        PhoneCode entity = new PhoneCode( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // code
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // dial_code
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // f_disabled
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PhoneCode entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDial_code(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setF_disabled(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(PhoneCode entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(PhoneCode entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
