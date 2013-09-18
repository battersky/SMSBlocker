package com.batter.smsblocker.ui;

import com.batter.smsblocker.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class NewItemWidget extends RelativeLayout {

    public final static int BUTTON_TYPE_SAVE = 0;
    public final static int BUTTON_TYPE_CANCEL = 1;

    public interface NewItemWidgetButtonClickListener {
        public void onNewItemWidgetButtonClicked(int buttonType, CharSequence text);
    }

    private NewItemWidgetButtonClickListener mNewItemWidgetButtonClickListener = null;
    private EditText mEditText;

    public NewItemWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.new_item_widget_relative, null);
        this.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mEditText = (EditText)this.findViewById(R.id.blocklist_new_item_text_editor);

        ImageView cancelView = (ImageView)findViewById(R.id.new_item_cancel);
        cancelView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mNewItemWidgetButtonClickListener != null) {
                    mNewItemWidgetButtonClickListener.onNewItemWidgetButtonClicked(
                            BUTTON_TYPE_CANCEL, mEditText.getText().toString());
                }
            }

        });


        ImageView saveView = (ImageView)findViewById(R.id.new_item_save);
        saveView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mNewItemWidgetButtonClickListener != null) {
                    mNewItemWidgetButtonClickListener.onNewItemWidgetButtonClicked(
                            BUTTON_TYPE_SAVE, mEditText.getText().toString());
                }
            }

        });

    }

    public void setNewItemWidgetButtonClickListener(NewItemWidgetButtonClickListener listener) {
        mNewItemWidgetButtonClickListener = listener;
    }
}
