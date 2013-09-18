package com.batter.smsblocker.ui;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.batter.smsblocker.R;
import com.batter.smsblocker.database.DatabaseUtils;
import com.batter.smsblocker.database.SimpleCursorLoader;
import com.batter.smsblocker.database.SmsBlockerDatabaseHelper;
import com.batter.smsblocker.ui.NewItemWidget.NewItemWidgetButtonClickListener;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BlockListFragment extends SherlockListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, NewItemWidgetButtonClickListener {

    private final static int BLOCKLIST_LOADER_ID = 0;

    private SimpleCursorAdapter mAdapter = null;

    private NewItemWidget mNewItemWidget = null;

    private DatabaseUtils mDatabaseUtils = null;

    private static class BlocklistCursorLoader extends SimpleCursorLoader {

        public BlocklistCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            SmsBlockerDatabaseHelper databaseHelper = new SmsBlockerDatabaseHelper(getContext());
            Cursor cursor = null;
            try {
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                cursor = database.query(SmsBlockerDatabaseHelper.DATABASE_TABLE_NAME_SMS_BLOCKER_LIST,
                        new String[] { "_id", "adddress" }, null, null, null, null, null);
                //TO-Do should change the hard code column name to constant projection
            } catch (SQLException ex) {
                cursor = new MatrixCursor(new String[] {"adddress"});
            }

            if (cursor != null) {
                // Ensure the cursor window is filled
                cursor.getCount();
                registerContentObserver(cursor, mObserver);
            }

            return cursor;
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(BLOCKLIST_LOADER_ID, null, this);
        setHasOptionsMenu(true);

        mNewItemWidget = (NewItemWidget)this.getView().findViewById(R.id.content_new_block_item);
        mNewItemWidget.setNewItemWidgetButtonClickListener(this);
        mDatabaseUtils = new DatabaseUtils(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_blocklist_fragment_linear, null);
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
        if (mAdapter == null) {
            mAdapter = new SimpleCursorAdapter(this.getActivity(),
                    R.layout.listitem_blocklist, data, new String[] { "adddress" }, new int[] { R.id.number_item },
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            setListAdapter(mAdapter);
        } else {
            mAdapter.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            mAdapter.changeCursor(null);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getSherlockActivity().getSupportMenuInflater().inflate(R.menu.setting_main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_block_phone_num:
                mNewItemWidget.setVisibility(View.VISIBLE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onNewItemWidgetButtonClicked(int buttonType, CharSequence text) {
        if (buttonType == NewItemWidget.BUTTON_TYPE_SAVE) {
            mDatabaseUtils.addNewBlockPhoneNumber(text);
        }
        mNewItemWidget.setVisibility(View.GONE);
    }
}
