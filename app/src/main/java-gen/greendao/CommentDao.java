package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Comment;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table COMMENT.
*/
public class CommentDao extends AbstractDao<Comment, Long> {

    public static final String TABLENAME = "COMMENT";

    /**
     * Properties of entity Comment.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Comment_id = new Property(0, Long.class, "comment_id", true, "COMMENT_ID");
        public final static Property Text = new Property(1, String.class, "text", false, "TEXT");
        public final static Property Created_at = new Property(2, String.class, "created_at", false, "CREATED_AT");
        public final static Property Consumer_id = new Property(3, Long.class, "consumer_id", false, "CONSUMER_ID");
        public final static Property First_name = new Property(4, String.class, "first_name", false, "FIRST_NAME");
        public final static Property Last_name = new Property(5, String.class, "last_name", false, "LAST_NAME");
        public final static Property Profile_image = new Property(6, String.class, "profile_image", false, "PROFILE_IMAGE");
        public final static Property Deal_id = new Property(7, Long.class, "deal_id", false, "DEAL_ID");
        public final static Property Title = new Property(8, String.class, "title", false, "TITLE");
    };


    public CommentDao(DaoConfig config) {
        super(config);
    }
    
    public CommentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'COMMENT' (" + //
                "'COMMENT_ID' INTEGER PRIMARY KEY ," + // 0: comment_id
                "'TEXT' TEXT," + // 1: text
                "'CREATED_AT' TEXT," + // 2: created_at
                "'CONSUMER_ID' INTEGER," + // 3: consumer_id
                "'FIRST_NAME' TEXT," + // 4: first_name
                "'LAST_NAME' TEXT," + // 5: last_name
                "'PROFILE_IMAGE' TEXT," + // 6: profile_image
                "'DEAL_ID' INTEGER," + // 7: deal_id
                "'TITLE' TEXT);"); // 8: title
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'COMMENT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Comment entity) {
        stmt.clearBindings();
 
        Long comment_id = entity.getComment_id();
        if (comment_id != null) {
            stmt.bindLong(1, comment_id);
        }
 
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(2, text);
        }
 
        String created_at = entity.getCreated_at();
        if (created_at != null) {
            stmt.bindString(3, created_at);
        }
 
        Long consumer_id = entity.getConsumer_id();
        if (consumer_id != null) {
            stmt.bindLong(4, consumer_id);
        }
 
        String first_name = entity.getFirst_name();
        if (first_name != null) {
            stmt.bindString(5, first_name);
        }
 
        String last_name = entity.getLast_name();
        if (last_name != null) {
            stmt.bindString(6, last_name);
        }
 
        String profile_image = entity.getProfile_image();
        if (profile_image != null) {
            stmt.bindString(7, profile_image);
        }
 
        Long deal_id = entity.getDeal_id();
        if (deal_id != null) {
            stmt.bindLong(8, deal_id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(9, title);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Comment readEntity(Cursor cursor, int offset) {
        Comment entity = new Comment( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // comment_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // text
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // created_at
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // consumer_id
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // first_name
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // last_name
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // profile_image
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // deal_id
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // title
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Comment entity, int offset) {
        entity.setComment_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setText(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCreated_at(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setConsumer_id(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setFirst_name(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLast_name(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setProfile_image(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setDeal_id(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setTitle(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Comment entity, long rowId) {
        entity.setComment_id(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Comment entity) {
        if(entity != null) {
            return entity.getComment_id();
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
