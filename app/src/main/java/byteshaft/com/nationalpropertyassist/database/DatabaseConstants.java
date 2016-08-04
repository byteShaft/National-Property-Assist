package byteshaft.com.nationalpropertyassist.database;

public class DatabaseConstants {


    public static final String DATABASE_NAME = "AddPropertyDetails.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "AddPropertyDatabase";
    public static final String POSTAL_CODE_COLUMN = "POSTAL_CODE";
    public static final String ADDRESS_COLUMN = "ADDRESS";
    public static final String RESIDENTIAL_COMMERCIAL_COLUMN = "RESIDENTIAL_COMMERCIAL";
    public static final String PROPERTY_TYPE_COLUMN = "PROPERTY_TYPE";
    public static final String PROPERTY_AGE_COLUMN = "PROPERTY_AGE";
    public static final String PROPERTY_ID_COLUMN = "PROPERTY_ID";
    public static final String ID_COLUMN = "ID";

    private static final String OPENING_BRACE = "(";
    private static final String CLOSING_BRACE = ")";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + OPENING_BRACE
            + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ADDRESS_COLUMN + " TEXT,"
            + POSTAL_CODE_COLUMN + " TEXT,"
            + RESIDENTIAL_COMMERCIAL_COLUMN + " TEXT,"
            + PROPERTY_TYPE_COLUMN + " TEXT,"
            + PROPERTY_AGE_COLUMN + " TEXT,"
            + PROPERTY_ID_COLUMN + " TEXT"
            + CLOSING_BRACE;
}
