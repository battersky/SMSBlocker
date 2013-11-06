package com.batter.smsblocker.ui;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.batter.smsblocker.R;
import com.batter.smsblocker.database.DatabaseUtils;
import com.batter.smsblocker.database.SimpleCursorLoader;
import com.batter.smsblocker.database.SmsBlockerDatabaseHelper;
import com.batter.smsblocker.ui.NewItemWidget.NewItemWidgetButtonClickListener;
import com.batter.smsblocker.util.Contant;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class BlockListFragment extends SherlockListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>, NewItemWidgetButtonClickListener {

    private final static int BLOCKLIST_LOADER_ID = 0;

    private SimpleCursorAdapter mAdapter = null;

    private NewItemWidget mNewItemWidget = null;

    private DatabaseUtils mDatabaseUtils = null;

    private ActionMode mActionMode = null;

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
            }
            return cursor;
        }

    }

    private Handler mMessageHandler = new Handler() {
            public void handleMessage(Message msg) {
            switch(msg.what) {
                case Contant.MSG_DATABASE_CONTENT_CHANGE:
                    LoaderManager loaderManager = getLoaderManager();
                    Loader loader = loaderManager.getLoader(BLOCKLIST_LOADER_ID);
                    if (loader != null) {
                        BlocklistCursorLoader blockListLoader = (BlocklistCursorLoader)loader;
                        blockListLoader.getObserver().onChange(true);
                }
                break;
            }
        }
    };

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.blocklist_action_mode_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            ListView listView = getListView();
            if (listView != null) {
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                return true;
            }
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_recycle:
                    ListView listView = getListView();
                    if (listView != null) {
                        int selectedItemCount = listView.getCheckedItemCount();
                        if (selectedItemCount > 0) {
                            long[] ids = listView.getCheckedItemIds();
                            for (int i = 0; i < ids.length; i ++) {
                                DatabaseUtils.deleteBlockPhoneNumber(ids, mMessageHandler);
                            }
                        }
                    }
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            getListView().clearChoices();
            getListView().clearFocus();
            mActionMode = null;
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(BLOCKLIST_LOADER_ID, null, this);
        setHasOptionsMenu(true);

        mNewItemWidget = (NewItemWidget)this.getView().findViewById(R.id.content_new_block_item);
        mNewItemWidget.setNewItemWidgetButtonClickListener(this);
        mDatabaseUtils = new DatabaseUtils(this.getActivity());

        ListView listView = this.getListView();
        if (listView != null) {
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                        long id) {
                    if (mActionMode != null) {
                        return false;
                    }

                    mActionMode = getSherlockActivity().startActionMode(mActionModeCallback);
                    getListView().setItemChecked(position, true);
                    return true;
                }
            });
        }
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
            mDatabaseUtils.addNewBlockPhoneNumber(text, mMessageHandler);
        }
        mNewItemWidget.setVisibility(View.GONE);
    }
}
