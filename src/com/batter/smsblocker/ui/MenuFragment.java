package com.batter.smsblocker.ui;

import com.batter.smsblocker.R;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class MenuFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] menus = getResources().getStringArray(R.array.menus);
        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, menus);
        setListAdapter(menuAdapter);
    }

}
