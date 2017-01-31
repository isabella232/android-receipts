package tech.receipts.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;

import tech.receipts.injection.ApplicationContext;
import timber.log.Timber;

import static tech.receipts.data.database.DbConstants.COLUMN_POS_NAME;
import static tech.receipts.data.database.DbConstants.COLUMN_POS_SCORE;
import static tech.receipts.data.database.DbConstants.COLUMN_POS_TAX_NUMBER;
import static tech.receipts.data.database.DbConstants.COLUMN_TAX_NUMBER;
import static tech.receipts.data.database.DbConstants.COLUMN_TAX_SCORE;
import static tech.receipts.data.database.DbConstants.DATABASE_NAME;
import static tech.receipts.data.database.DbConstants.POS_TABLE_NAME;
import static tech.receipts.data.database.DbConstants.TAX_TABLE_NAME;

public class AutocompleteDbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_TAX_TABLE_CREATE =
            "CREATE TABLE " + TAX_TABLE_NAME + " (" +
                    COLUMN_TAX_NUMBER + " TEXT PRIMARY KEY, " +
                    COLUMN_TAX_SCORE + " LONG);";

    private static final String SQL_POS_TABLE_CREATE =
            "CREATE TABLE " + POS_TABLE_NAME + " (" +
                    COLUMN_POS_TAX_NUMBER + " TEXT, " +
                    COLUMN_POS_SCORE + " LONG, " +
                    COLUMN_POS_NAME + " TEXT," +
                    "PRIMARY KEY(" + COLUMN_POS_TAX_NUMBER + ", " + COLUMN_POS_NAME + "));";

    private static final String SQL_TAX_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TAX_TABLE_NAME;
    private static final String SQL_POS_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + POS_TABLE_NAME;

    public static final String SQL_GET_TAX_COLLECTION = "SELECT " + COLUMN_TAX_NUMBER + " FROM " + TAX_TABLE_NAME + " ORDER BY " + COLUMN_TAX_NUMBER + " ASC";
    public static final String SQL_GET_POS_COLLECTION = "SELECT " + COLUMN_POS_NAME + "," + COLUMN_POS_TAX_NUMBER + " FROM " + POS_TABLE_NAME + " ORDER BY " + COLUMN_POS_NAME + " ASC";

    public static final String SQL_UPDATE_TAX = "UPDATE " + TAX_TABLE_NAME + " SET " + COLUMN_TAX_SCORE + " = " + COLUMN_TAX_SCORE + " + 1 WHERE " + COLUMN_TAX_NUMBER + " = ?";
    public static final String SQL_UPDATE_POS = "UPDATE " + POS_TABLE_NAME + " SET " + COLUMN_POS_SCORE + " = " + COLUMN_POS_SCORE + " + 1 WHERE " + COLUMN_POS_NAME + " = ? AND " + COLUMN_POS_TAX_NUMBER + " = ?";

    @Inject
    public AutocompleteDbOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Timber.e("Create databases");
        db.beginTransaction();
        try {
            //Uncomment line below if you want to enable foreign keys
            //db.execSQL("PRAGMA foreign_keys=ON;");
            db.execSQL(SQL_TAX_TABLE_CREATE);
            db.execSQL(SQL_POS_TABLE_CREATE);
            //Add other tables here
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_TAX_DELETE_ENTRIES);
        db.execSQL(SQL_POS_DELETE_ENTRIES);
        onCreate(db);
    }
}
