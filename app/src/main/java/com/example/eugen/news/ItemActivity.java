package com.example.eugen.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ItemActivity extends AppCompatActivity {
    private TextView mTitleView;
    private TextView mDescriptionView;
    private TextView mPubDateView;
    private TextView mLinkView;
    private TextView mCategoryView;
    private String guid;
    private Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        mTitleView = (TextView)findViewById(R.id.topic_title_view);
        mDescriptionView = (TextView)findViewById(R.id.description_view);
        mPubDateView = (TextView)findViewById(R.id.pub_date_view);
        mLinkView = (TextView)findViewById(R.id.link_view);
        mCategoryView = (TextView)findViewById(R.id.category_view);
        guid = getIntent().getStringExtra("GUID");
        item = ApplicationModel.getInstance().getItemById(guid);
        mTitleView.setText(item.getTitle());
        mDescriptionView.setText(item.getDescription());
        String stringDateFormat = "EEE, dd MMM yyyy";
        SimpleDateFormat format = new SimpleDateFormat(stringDateFormat, Locale.US);
        try {
            Date date = format.parse(item.getPubDate());
            String s = format.format(date);
            mPubDateView.setText(s);

        } catch (ParseException e) {
            mPubDateView.setText(item.getPubDate());
        }
        mLinkView.setText(item.getLink());
        mCategoryView.setText(item.getCategory());
    }

}
