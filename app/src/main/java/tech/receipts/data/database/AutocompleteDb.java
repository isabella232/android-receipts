package tech.receipts.data.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import tech.receipts.BuildConfig;
import tech.receipts.data.model.autocomplete.Autocomplete;
import timber.log.Timber;

import static tech.receipts.data.database.DbConstants.COLUMN_POS_NAME;
import static tech.receipts.data.database.DbConstants.COLUMN_POS_SCORE;
import static tech.receipts.data.database.DbConstants.COLUMN_POS_TAX_NUMBER;
import static tech.receipts.data.database.DbConstants.COLUMN_TAX_NUMBER;
import static tech.receipts.data.database.DbConstants.COLUMN_TAX_SCORE;
import static tech.receipts.data.database.DbConstants.POS_TABLE_NAME;
import static tech.receipts.data.database.DbConstants.TAX_TABLE_NAME;

@Singleton
public class AutocompleteDb {

    private final BriteDatabase db;
    private final AutocompleteDbOpenHelper dbOpenHelper;

    @Inject
    public AutocompleteDb(AutocompleteDbOpenHelper dbOpenHelper) {
        this.dbOpenHelper = dbOpenHelper;
        db = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
        db.setLoggingEnabled(BuildConfig.DEBUG);
    }

    public void saveOrUpdateTax(Autocomplete autocomplete) {
        db.execute(AutocompleteDbOpenHelper.SQL_UPDATE_TAX, autocomplete.getTax());
        saveTax(autocomplete);
    }

    public void saveOrUpdatePos(Autocomplete autocomplete) {
        db.execute(AutocompleteDbOpenHelper.SQL_UPDATE_POS, autocomplete.getPos(), autocomplete.getTax());
        savePos(autocomplete);
    }

    private void saveTax(Autocomplete autocomplete) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TAX_NUMBER, autocomplete.getTax());
        values.put(COLUMN_TAX_SCORE, 0);

        db.insert(TAX_TABLE_NAME, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void savePos(Autocomplete autocomplete) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_POS_NAME, autocomplete.getPos());
        values.put(COLUMN_POS_SCORE, 0);
        values.put(COLUMN_POS_TAX_NUMBER, autocomplete.getTax());

        db.insert(POS_TABLE_NAME, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public Observable<Autocomplete> saveTaxAndPos(final Autocomplete autocomplete) {
        Timber.d("Autocomplete: '%s'", autocomplete);
        return Observable.create(new Observable.OnSubscribe<Autocomplete>() {
            @Override
            public void call(Subscriber<? super Autocomplete> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    Timber.e("Failed to save autocomplete because of missing subscriber");
                    return;
                }

                BriteDatabase.Transaction transaction = db.newTransaction();
                try {
                    saveOrUpdateTax(autocomplete);
                    saveOrUpdatePos(autocomplete);
                    subscriber.onNext(autocomplete);

                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Autocomplete>> getPosCollection() {
        return db.createQuery(POS_TABLE_NAME, AutocompleteDbOpenHelper.SQL_GET_POS_COLLECTION)
                .mapToList(new Func1<Cursor, Autocomplete>() {
                    @Override
                    public Autocomplete call(Cursor cursor) {
                        return new Autocomplete(cursor.getString(1), cursor.getString(0));
                    }
                });
    }

    public Observable<List<String>> getTaxCollection() {
        return db.createQuery(TAX_TABLE_NAME, AutocompleteDbOpenHelper.SQL_GET_TAX_COLLECTION)
                .mapToList(new Func1<Cursor, String>() {
                    @Override
                    public String call(Cursor cursor) {
                        return cursor.getString(0);
                    }
                });
    }

    public int getPosCount() {
        Timber.d("getPosCount");
        return (int) DatabaseUtils.queryNumEntries(dbOpenHelper.getReadableDatabase(), POS_TABLE_NAME, null, null);
    }

    public void clearAutocomplete() {
        db.delete(TAX_TABLE_NAME, null);
        db.delete(POS_TABLE_NAME, null);
    }

    public Observable<Void> clearTables() {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = db.newTransaction();
                try {
                    Cursor cursor = db.query("SELECT name FROM sqlite_master WHERE type='table'");
                    while (cursor.moveToNext()) {
                        db.delete(cursor.getString(cursor.getColumnIndex("name")), null);
                    }
                    cursor.close();
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

}
