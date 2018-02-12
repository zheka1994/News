package com.example.eugen.news;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;



public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    public SpacesItemDecoration(int space)
    {
        mSpace = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        outRect.bottom = mSpace;
    }
}
