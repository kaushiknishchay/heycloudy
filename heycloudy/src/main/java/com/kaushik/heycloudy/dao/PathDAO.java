package com.kaushik.heycloudy.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kaushik.heycloudy.db.PathsHelper;
import com.kaushik.heycloudy.model.PathModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHolmes on 09-Jul-17.
 */

public class PathDAO {
    private PathsHelper dbHelper;
    private SQLiteDatabase dbInstance;
    private String[] tableColumns = {PathsHelper.COLUMN_ID, PathsHelper.COLUMN_PATH};

    public PathDAO(Context context) {
        dbHelper = new PathsHelper(context);
    }

    public void close() throws SQLException {
        dbHelper.close();
    }

    public void open() throws SQLException {
        dbInstance = dbHelper.getWritableDatabase();
    }

    public PathModel addNewPath(String path) throws Exception{

        //Create New Key-Value Pair for the path
        ContentValues values = new ContentValues();
        values.put(PathsHelper.COLUMN_PATH, path);

        //Insert the above in db and get the row id
        long insertId = dbInstance.insert(PathsHelper.TABLE_NAME, null, values);

        //Get db Cursor and move to newly added row and return it.
        Cursor cursor = dbInstance.query(PathsHelper.TABLE_NAME, tableColumns, PathsHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        Log.d("PathDAO", insertId+"");
        cursor.moveToFirst();
        PathModel newPath = cursorToPath(cursor);
        cursor.close();
        return newPath;
    }

    public boolean deletePath(long id) {

        int rowCount = dbInstance.delete(PathsHelper.TABLE_NAME, PathsHelper.COLUMN_ID + " = ?", new String[]{
                String.valueOf(id)
        });

        if (rowCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<PathModel> getAllPaths() {

        List<PathModel> pathList = new ArrayList<PathModel>();

        //Query DB
        Cursor cursor = dbInstance.query(PathsHelper.TABLE_NAME, tableColumns, null, null, null, null, null);
        cursor.moveToFirst();

        //Iterate DB and add to List
        while (!cursor.isAfterLast()) {
            PathModel path = cursorToPath(cursor);
            pathList.add(path);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return pathList;
    }

    private PathModel cursorToPath(Cursor cursor) {
        PathModel path = new PathModel();
        path.setId(cursor.getLong(0));
        path.setDirPath(cursor.getString(1));
        return path;
    }
}
