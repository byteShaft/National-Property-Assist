package byteshaft.com.nationalpropertyassist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class AddPropertyDetailsDatabase extends SQLiteOpenHelper {

    public AddPropertyDetailsDatabase(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstants.TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DatabaseConstants.TABLE_NAME);
        onCreate(db);
    }

    public void createNewEntry(String address, int postalCode, int propertyCommercial, int propertyType,
                               int ageOfProperty, int propertyId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.ADDRESS_COLUMN, address);
        values.put(DatabaseConstants.POSTAL_CODE_COLUMN, postalCode);
        values.put(DatabaseConstants.PROPERTY_AGE_COLUMN, ageOfProperty);
        values.put(DatabaseConstants.RESIDENTIAL_COMMERCIAL_COLUMN, propertyCommercial);
        values.put(DatabaseConstants.PROPERTY_TYPE_COLUMN, propertyType);
        values.put(DatabaseConstants.PROPERTY_ID_COLUMN, propertyId);
        db.insert(DatabaseConstants.TABLE_NAME, null, values);
        db.close();
    }

    public boolean deleteEntry(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(DatabaseConstants.TABLE_NAME, DatabaseConstants.ID_COLUMN + "=" + id, null) > 0;
    }

    public void updateEntries(
            int id, String address, int postalCode, int propertyCommercial, int propertyType,
            int ageOfProperty, int propertyId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.POSTAL_CODE_COLUMN, postalCode);
        values.put(DatabaseConstants.ADDRESS_COLUMN, address);
        values.put(DatabaseConstants.PROPERTY_AGE_COLUMN, ageOfProperty);
        values.put(DatabaseConstants.RESIDENTIAL_COMMERCIAL_COLUMN, propertyCommercial);
        values.put(DatabaseConstants.PROPERTY_TYPE_COLUMN, propertyType);
        values.put(DatabaseConstants.PROPERTY_ID_COLUMN, propertyId);
        db.update(DatabaseConstants.TABLE_NAME, values, DatabaseConstants.ID_COLUMN + "=" + id, null);
        Log.i("Database", "Updated.......");
        db.close();
    }

    public ArrayList<Integer> getIdOfSaveProperty() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Integer> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.ID_COLUMN));
            list.add(id);
        }
        db.close();
        cursor.close();
        return list;
    }

    public ArrayList<HashMap> getAllRecords() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int unique_id = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.ID_COLUMN));

            String address = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.ADDRESS_COLUMN));
            String postalCode = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.POSTAL_CODE_COLUMN));

            String PropertyAge = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.PROPERTY_AGE_COLUMN));

            String commercial = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.RESIDENTIAL_COMMERCIAL_COLUMN));

            String propertyType = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.PROPERTY_TYPE_COLUMN));

            String propertyId = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.PROPERTY_ID_COLUMN));

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("unique_id", String.valueOf(unique_id));
            hashMap.put("address", address);
            hashMap.put("postal_code", postalCode);
            hashMap.put("property_age", PropertyAge);
            hashMap.put("commercial", commercial);
            hashMap.put("property_type", propertyType);
            hashMap.put("property_id", propertyId);
            list.add(hashMap);
        }
        db.close();
        cursor.close();
        return list;
    }

    public ArrayList<HashMap<String, Integer>> getidOfProperties() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap<String, Integer>> list = new ArrayList<>();
        HashMap<String, Integer> hashMap = new HashMap<>();
        while (cursor.moveToNext()) {
            String address = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.ADDRESS_COLUMN));
            int propertyId = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.ADDRESS_COLUMN));
            hashMap.put(address, propertyId);
        }
        list.add(hashMap);
        db.close();
        cursor.close();
        return list;
    }

    public ArrayList<HashMap<Integer, String[]>> getAddressOfProperties() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap<Integer, String[]>> list = new ArrayList<>();
        HashMap<Integer, String[]> hashMap = new HashMap<>();
        String[] data;
        while (cursor.moveToNext()) {
            String address = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.ADDRESS_COLUMN));
            int propertyId = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.ID_COLUMN));
            int propertyIdOnServer = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.PROPERTY_ID_COLUMN));
            data = new String[] {String.valueOf(propertyId), String.valueOf(propertyIdOnServer),
            address};
            hashMap.put(propertyId, data);
            list.add(hashMap);
        }
        db.close();
        cursor.close();
        return list;
    }
}
