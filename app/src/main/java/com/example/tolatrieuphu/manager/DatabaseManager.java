package com.example.tolatrieuphu.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.tolatrieuphu.model.Question;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseManager extends SQLiteOpenHelper {
    private Context mContext;
    private SQLiteDatabase questionDatabase;
    private static final String DATABASE_NAME = "Question";
    public final static String DATABASE_PATH = "/data/data/com.example.tolatrieuphu/databases/";
    public static final int DATABASE_VERSION = 1;
    private static final String SQL_QUESTION_QUERY = "";

    public DatabaseManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        try {
            createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();
        if(!dbExist) {
            this.getReadableDatabase();
            openDatabase();
            copyDataBase();
            closeDataBase();
            this.close();
        }

    }

    private void copyDataBase() {
        try {
            String outFileName = DATABASE_PATH + DATABASE_NAME;
//            InputStream mInput = mContext.getAssets().open(DATABASE_NAME);
//            OutputStream mOutput = new FileOutputStream(outFileName);
            DataInputStream mInput = new DataInputStream(mContext.getAssets().open(DATABASE_NAME));
            DataOutputStream mOutput = new DataOutputStream(new FileOutputStream(outFileName));
            byte[] mBuffer = new byte[2024];
            int mLength;
            while ((mLength = mInput.read(mBuffer)) > 0) {
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean checkDataBase()
    {
        boolean checkDB = false;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        }
        catch(SQLiteException e) {
        }
        return checkDB;
    }

    //Open database
    public void openDatabase() throws SQLException {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        questionDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase()throws SQLException
    {
        if(questionDatabase != null)
            questionDatabase.close();
        super.close();
    }

    public Question getQuestion(int level) {
        openDatabase();
        String sql_query = "SELECT * FROM Question WHERE level = '" + level + "' ORDER BY RANDOM() LIMIT 1";
        Cursor cursor = questionDatabase.rawQuery(sql_query, null);
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        String ques = cursor.getString(cursor.getColumnIndexOrThrow("question"));
        String caseA = cursor.getString(cursor.getColumnIndexOrThrow("casea"));
        String caseB = cursor.getString(cursor.getColumnIndexOrThrow("caseb"));
        String caseC = cursor.getString(cursor.getColumnIndexOrThrow("casec"));
        String caseD = cursor.getString(cursor.getColumnIndexOrThrow("cased"));
        int trueCase = cursor.getInt(cursor.getColumnIndexOrThrow("truecase"));
        Question question = new Question(ques, caseA, caseB, caseC, caseD, level, trueCase);
        closeDataBase();
        return question;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
