package itesm.mx.expediciones_biosfera.database.operations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class UserOperations {

    private SQLiteDatabase db;
    private UserDBHelper dbHelper;
    private User user;

    public UserOperations(Context context){
        dbHelper = new UserDBHelper(context);
    }

    public void open() throws SQLException{
        try{
            db = dbHelper.getWritableDatabase();
        }catch(SQLException e){
            Log.e("SQLOPEN", e.toString());
        }
    }

    public void close(){db.close();}

    public long addUser(User user){
        long newRowId = 0;

        try{
            ContentValues values = new ContentValues();
            values.put(DB_Schema.UserTable.COLUMN_NAME_FIREBASEID, user.getFbid());
            values.put(DB_Schema.UserTable.COLUMN_NAME_OCCUPATIONS, user.getOccupations());
            values.put(DB_Schema.UserTable.COLUMN_NAME_INTERESTS, user.getInterests());
            values.put(DB_Schema.UserTable.COLUMN_NAME_PHONE, user.getPhone());
            values.put(DB_Schema.UserTable.COLUMN_NAME_PICTURE, user.getPicture());
            newRowId = db.insert(DB_Schema.UserTable.TABLE_NAME, null, values);
        }catch(SQLException e){
            Log.e("SQLADD", e.toString());
        }

        return newRowId;
    }

    public void deleteUser(String fbid){

        String query = "SELECT * FROM " + DB_Schema.UserTable.TABLE_NAME +
                " WHERE " + DB_Schema.UserTable.COLUMN_NAME_FIREBASEID +
                " = \"" + fbid + "\"";
        try{
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst()){
                int id = Integer.parseInt(cursor.getString(0));
                db.delete(DB_Schema.UserTable.TABLE_NAME,
                        DB_Schema.UserTable._ID+" = ?",
                        new String[]{String.valueOf(id)});
            }
            cursor.close();
        }catch(SQLException e){
            Log.e("SQLDELETE", e.toString());
        }

    }

    public ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + DB_Schema.UserTable.TABLE_NAME;

        try{
            Cursor cursor = db.rawQuery(selectQuery,null);
            if(cursor.moveToFirst()){
                do{
                    user = new User(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getBlob(5)
                    );
                    users.add(user);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }catch(SQLException e){
            Log.e("SQLList", e.toString());
        }
        return users;
    }

    public void deleteAll(){
        db.execSQL("DELETE FROM  " + DB_Schema.UserTable.TABLE_NAME);
    }


}
