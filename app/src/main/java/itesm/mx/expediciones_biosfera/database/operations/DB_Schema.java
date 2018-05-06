package itesm.mx.expediciones_biosfera.database.operations;

import android.provider.BaseColumns;

/**
 * Created by javier on 29/04/18.
 */

public final class DB_Schema {

    private DB_Schema(){}

    public static class UserTable implements BaseColumns{
        public static final String TABLE_NAME = "User";
        public static String COLUMN_NAME_ID = "id";
        public static String COLUMN_NAME_FIREBASEID = "fbid";
        public static String COLUMN_NAME_OCCUPATIONS = "occupations";
        public static String COLUMN_NAME_INTERESTS = "interests";
        public static String COLUMN_NAME_PHONE = "phone";
        public static String COLUMN_NAME_PICTURE = "imagen";
    }
}
