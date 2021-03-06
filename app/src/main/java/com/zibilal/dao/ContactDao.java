package com.zibilal.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.zibilal.dao.Contact;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONTACT".
*/
public class ContactDao extends AbstractDao<Contact, Long> {

    public static final String TABLENAME = "CONTACT";

    /**
     * Properties of entity Contact.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DisplayName = new Property(1, String.class, "displayName", false, "DISPLAY_NAME");
        public final static Property FirstName = new Property(2, String.class, "firstName", false, "FIRST_NAME");
        public final static Property LastName = new Property(3, String.class, "lastName", false, "LAST_NAME");
        public final static Property MiddleName = new Property(4, String.class, "middleName", false, "MIDDLE_NAME");
        public final static Property NickName = new Property(5, String.class, "nickName", false, "NICK_NAME");
        public final static Property Title = new Property(6, String.class, "title", false, "TITLE");
        public final static Property ContactId = new Property(7, Integer.class, "contactId", false, "CONTACT_ID");
        public final static Property DeviceContactid = new Property(8, Long.class, "deviceContactid", false, "DEVICE_CONTACTID");
        public final static Property CreateDate = new Property(9, java.util.Date.class, "createDate", false, "CREATE_DATE");
    };


    public ContactDao(DaoConfig config) {
        super(config);
    }
    
    public ContactDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONTACT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"DISPLAY_NAME\" TEXT UNIQUE ," + // 1: displayName
                "\"FIRST_NAME\" TEXT," + // 2: firstName
                "\"LAST_NAME\" TEXT," + // 3: lastName
                "\"MIDDLE_NAME\" TEXT," + // 4: middleName
                "\"NICK_NAME\" TEXT," + // 5: nickName
                "\"TITLE\" TEXT," + // 6: title
                "\"CONTACT_ID\" INTEGER," + // 7: contactId
                "\"DEVICE_CONTACTID\" INTEGER," + // 8: deviceContactid
                "\"CREATE_DATE\" INTEGER NOT NULL );"); // 9: createDate
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONTACT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Contact entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String displayName = entity.getDisplayName();
        if (displayName != null) {
            stmt.bindString(2, displayName);
        }
 
        String firstName = entity.getFirstName();
        if (firstName != null) {
            stmt.bindString(3, firstName);
        }
 
        String lastName = entity.getLastName();
        if (lastName != null) {
            stmt.bindString(4, lastName);
        }
 
        String middleName = entity.getMiddleName();
        if (middleName != null) {
            stmt.bindString(5, middleName);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(6, nickName);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(7, title);
        }
 
        Integer contactId = entity.getContactId();
        if (contactId != null) {
            stmt.bindLong(8, contactId);
        }
 
        Long deviceContactid = entity.getDeviceContactid();
        if (deviceContactid != null) {
            stmt.bindLong(9, deviceContactid);
        }
        stmt.bindLong(10, entity.getCreateDate().getTime());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Contact readEntity(Cursor cursor, int offset) {
        Contact entity = new Contact( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // displayName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // firstName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // lastName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // middleName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // nickName
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // title
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // contactId
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // deviceContactid
            new java.util.Date(cursor.getLong(offset + 9)) // createDate
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Contact entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDisplayName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFirstName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLastName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMiddleName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNickName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTitle(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setContactId(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setDeviceContactid(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setCreateDate(new java.util.Date(cursor.getLong(offset + 9)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Contact entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Contact entity) {
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
