package teammoodi.moodi;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 3/24/18.
 */

public class EmotionalResponseDB extends SQLiteOpenHelper {

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
                    "confidence REAL, " +
                    "anger REAL, " +
                    "fear REAL," +
                    "joy REAL," +
                    "sadness REAL," +
                    "analytical REAL," +
                    "confident REAL," +
                    "tentative REAL)";

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

    public ArrayList<MoodiResult> moodiResultArrayList() {
        String query = "SELECT * FROM responseHistory LIMIT 10 OFFSET (SELECT COUNT(*) FROM responseHistory) - 10;";

        ArrayList<MoodiResult> moodiResultArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MoodiResult moodiResult;

        if ((cursor.moveToFirst())) {
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
            } while (cursor.moveToNext());
        }

        return moodiResultArrayList;
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
