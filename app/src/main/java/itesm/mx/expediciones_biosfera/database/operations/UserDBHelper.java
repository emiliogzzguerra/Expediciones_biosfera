package itesm.mx.expediciones_biosfera.database.operations;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by javier on 30/04/18.
 */

public class UserDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "UserDB.db";
    private static final int DATABASE_VERSION = 1;

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_USERS_TABLE = "CREATE TABLE " +
                DB_Schema.UserTable.TABLE_NAME +
                "(" +
                DB_Schema.UserTable._ID + " INTEGER PRIMARY KEY," +
                DB_Schema.UserTable.COLUMN_NAME_FIREBASEID + " TEXT," +
                DB_Schema.UserTable.COLUMN_NAME_OCCUPATIONS + " TEXT," +
                DB_Schema.UserTable.COLUMN_NAME_INTERESTS + " TEXT," +
                DB_Schema.UserTable.COLUMN_NAME_PHONE + " TEXT," +
                DB_Schema.UserTable.COLUMN_NAME_PICTURE + " BLOB " +
                ")";
        Log.i("Producthelper onCreate", CREATE_USERS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String DELETE_USERS_TABLE = "DROP TABLE IF EXISTS " +
                DB_Schema.UserTable.TABLE_NAME;
        db.execSQL(DELETE_USERS_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }


}
