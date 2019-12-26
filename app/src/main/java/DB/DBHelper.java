package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.randomapps.randomapp.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import Models.UserList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RandomDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String LISTS_TABLE = "UserLists";
    private static final String LISTSCOLUMN_ID= "listId";
    private static final String LISTSCOLUMN_NAME= "name";
    private static final String LISTSCOLUMN_ITEMS = "items";
    private static final String LISTSCOLUMN_ITEMSCOUNT = "itemsCount";
    private static final String LISTSCOLUMN_CREATED = "created";
    private static final String LISTSCOLUMN_LASTUPDATED = "lastUpdated";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        CreateListTable(db);
    }

    private void CreateListTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + LISTS_TABLE + " (\n" +
                "    " + LISTSCOLUMN_ID + " INTEGER NOT NULL CONSTRAINT lists_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + LISTSCOLUMN_NAME + " varchar(200) NOT NULL,\n" +
                "    " + LISTSCOLUMN_ITEMS + " TEXT NOT NULL,\n" +
                "    " + LISTSCOLUMN_ITEMSCOUNT + " INTEGER NOT NULL,\n" +
                "    " + LISTSCOLUMN_LASTUPDATED + " datetime NOT NULL\n" +
                ");";

        /*
         * Executing the string to create the table
         * */
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + LISTS_TABLE + ";";
        db.execSQL(sql);
        onCreate(db);
    }

    public boolean addList(String name, List<String> items, Integer itemsCount) {
        String itemsListText = TextUtils.join(",", items);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String lastUpdated = sdf.format(cal.getTime());

        ContentValues contentValues = new ContentValues();
        contentValues.put(LISTSCOLUMN_NAME, name);
        contentValues.put(LISTSCOLUMN_ITEMS, itemsListText);
        contentValues.put(LISTSCOLUMN_ITEMSCOUNT, itemsCount);
        contentValues.put(LISTSCOLUMN_LASTUPDATED, lastUpdated);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(LISTS_TABLE, null, contentValues) != -1;
    }

    public Cursor getAllLists() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + LISTS_TABLE, null);
    }

    public List<String> getAllListNames(){
        List<String> listNames = new ArrayList<>();
        Cursor listNameCursor = getAllLists();
        if(listNameCursor.moveToFirst()){
            do{
                listNames.add(listNameCursor.getString(1));
            } while (listNameCursor.moveToNext());
        }
        return listNames;
    }

    public List<String> getListItems(String listName){
        List<String> items = new ArrayList<>();
        Cursor listNameCursor = getAllLists();
        if(listNameCursor.moveToFirst()){
            do{
                if(listNameCursor.getString(1).equals(listName)) {
                    items = Utils.getItemsListFromText(listNameCursor.getString(2));
                }
            } while (listNameCursor.moveToNext());
        }
        return items;
    }

    public boolean updateUserList(int id, String name, List<String> items, Integer itemsCount) {
        String itemsListText = TextUtils.join(",", items);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String lastUpdated = sdf.format(cal.getTime());

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LISTSCOLUMN_NAME, name);
        contentValues.put(LISTSCOLUMN_ITEMS, itemsListText);
        contentValues.put(LISTSCOLUMN_ITEMSCOUNT, itemsCount);
        contentValues.put(LISTSCOLUMN_LASTUPDATED, lastUpdated);
        return db.update(LISTS_TABLE, contentValues, LISTSCOLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    public boolean deleteList(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(LISTS_TABLE, LISTSCOLUMN_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    public List<UserList> GetUserListObjects() {
        List<UserList> userListsCollection = new ArrayList<>();
        Cursor cursor = getAllLists();
        if(cursor.moveToFirst()){
            do{
                String itemsText = cursor.getString(2);
                String[] itemsArray = itemsText.split(",");
                List<String> items = Arrays.asList(itemsArray);
                userListsCollection.add(new UserList(
                        cursor.getInt(0), //id
                        cursor.getString(1), //name
                        items, //items
                        cursor.getInt(3), //itemsCount
                        cursor.getString(4) //lastUpdated
                ));
            } while (cursor.moveToNext());
        }
        return userListsCollection;
    }
}
