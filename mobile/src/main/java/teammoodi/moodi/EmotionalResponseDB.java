package teammoodi.moodi;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 3/24/18.
 */

public class EmotionalResponseDB extends SQLiteOpenHelper {

    private static final String TAG = "moodi";

    interface OnDBReadyListener {
        void onDBReady(SQLiteDatabase theDB);
    }

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "emotionalResponse.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE responseHistory (" +
                    "id PRIMARY KEY," +
                    "timestamp STRING, " +
                    "transcript STRING, " +
                    "location STRING, " +
                    "confidence REAL DEFAULT 0, " +
                    "anger REAL DEFAULT 0, " +
                    "fear REAL DEFAULT 0," +
                    "joy REAL DEFAULT 0," +
                    "sadness REAL DEFAULT 0," +
                    "analytical REAL DEFAULT 0," +
                    "confident REAL DEFAULT 0," +
                    "tentative REAL DEFAULT 0)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS responseHistory";

    public static EmotionalResponseDB theDb;
    public Context appContext;

    private EmotionalResponseDB(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        appContext = context.getApplicationContext();
    }

    public static synchronized EmotionalResponseDB getInstance(Context context) {
        if (theDb == null) {
            theDb = new EmotionalResponseDB(context.getApplicationContext());
        }
        return theDb;
    }

    public void AddResult(ContentValues content) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.insertOrThrow("responseHistory", null, content);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<MoodiResult> GetResults(int count) {
        String RESULTS_QUERY =
                String.format("SELECT * FROM responseHistory LIMIT %d OFFSET (SELECT COUNT(*) FROM responseHistory) - %d;",
                        count, count);

        ArrayList<MoodiResult> moodiResultArrayList = new ArrayList<>();
        MoodiResult moodiResult;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(RESULTS_QUERY, null);
        try {
            if (cursor.moveToLast()) {
                do {
                    moodiResult = new MoodiResult();
                    moodiResult.setTranscript(cursor.getString(cursor.getColumnIndex("transcript")));
                    moodiResult.setConfidence(cursor.getString(cursor.getColumnIndex("confidence")));

                    moodiResult.setAnger(cursor.getString(cursor.getColumnIndex("anger")));
                    moodiResult.setSadness(cursor.getString(cursor.getColumnIndex("sadness")));
                    moodiResult.setFear(cursor.getString(cursor.getColumnIndex("fear")));
                    moodiResult.setJoy(cursor.getString(cursor.getColumnIndex("joy")));
                    moodiResult.setAnalytical(cursor.getString(cursor.getColumnIndex("analytical")));
                    moodiResult.setConfident(cursor.getString(cursor.getColumnIndex("confident")));
                    moodiResult.setTentative(cursor.getString(cursor.getColumnIndex("tentative")));

                    moodiResultArrayList.add(moodiResult);
                } while(cursor.moveToPrevious());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return moodiResultArrayList;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void getReadableDatabase(OnDBReadyListener listener) {
        new OpenDbAsyncTask().execute(listener);
    }

    public void asyncWritableDatabase(OnDBReadyListener listener) {
        new OpenDbAsyncTask().execute(listener);
    }


    public static class OpenDbAsyncTask extends AsyncTask<OnDBReadyListener,Void,SQLiteDatabase> {
        OnDBReadyListener listener;

        @Override
        protected SQLiteDatabase doInBackground(OnDBReadyListener... params){
            listener = params[0];
            return EmotionalResponseDB.theDb.getWritableDatabase();
        }

        @Override
        protected void onPostExecute(SQLiteDatabase db) {
            //Make that callback
            listener.onDBReady(db);
        }
    }


}
