package com.batter.smsblocker.ui;

import com.batter.smsblocker.database.SimpleCursorLoader;
import com.batter.smsblocker.database.SmsBlockerDatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class BlockListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static int BLOCKLIST_LOADER_ID = 0;

    private static class BlocklistCursorLoader extends SimpleCursorLoader {

        public BlocklistCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            SmsBlockerDatabaseHelper databaseHelper = new SmsBlockerDatabaseHelper(getContext());
            try {
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                return database.query(SmsBlockerDatabaseHelper.DATABASE_TABLE_NAME_SMS_BLOCKER_LIST,
                        new String[] {"adddress"}, null, null, null, null, null);
            } catch (SQLException ex) {
                MatrixCursor emptyCursor = new MatrixCursor(new String[] {"adddress"});
                return emptyCursor;
            }
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(BLOCKLIST_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == BLOCKLIST_LOADER_ID) {
            return new BlocklistCursorLoader(getActivity());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
