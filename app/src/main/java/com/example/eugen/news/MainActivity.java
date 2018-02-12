package com.example.eugen.news;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity implements ErrorConnectionDialog.OkClickListener{
    private ArrayList<Item> items;
    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private HttpSession httpSession;
    private ProgressDialogFragment progressDialogFragment;
    private boolean isRotateScreen;// Чтобы диалог загрузки не появлялся при повороте экрана
    private boolean isNetWorkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            isRotateScreen = savedInstanceState.getBoolean("LoadFragment");
        }
        else {
            isRotateScreen = false;
        }
        items = ApplicationModel.getInstance().getItems();
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewsAdapter(items);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(20));
        if(!isNetWorkConnected()){
            ErrorConnectionDialog dialog = new ErrorConnectionDialog();
            dialog.show(getSupportFragmentManager(),"error_dialog");
        }
        else {
            if(!isRotateScreen){
                isRotateScreen = true;
                progressDialogFragment = new ProgressDialogFragment();
                progressDialogFragment.show(getSupportFragmentManager(),"load_dialog");
            }
            httpSession = (HttpSession) getLastNonConfigurationInstance();
            if (httpSession == null) {
                httpSession = new HttpSession();
                httpSession.execute();
            }
        }
    }
    private class HttpSession extends AsyncTask<Void,Void,String>{
        private OkHttpClient client = new OkHttpClient();
        private int HttpCode;
        private final String URL = "http://lopoutouret.date/getnews/nhl.php?lang=ru";
        private String str;
        @Override
        protected String doInBackground(Void... parameter) {
            Request request = new Request.Builder().url(URL).build();
            try {
                Response response = client.newCall(request).execute();
                HttpCode = response.code();
                str = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonBody = new JSONObject(str);
                parseItems(items,jsonBody);
                if(HttpCode >= 400) {
                    Toast.makeText(MainActivity.this, "Code error = " + HttpCode, Toast.LENGTH_LONG).show();
                }
                if(progressDialogFragment != null){
                    progressDialogFragment.dismiss();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

        public void parseItems(List<Item> items, JSONObject jsonBody)throws IOException,JSONException{
            JSONObject channelObject = jsonBody.getJSONObject("channel");
            JSONArray itemsArray = channelObject.getJSONArray("item");
            for(int i = 0; i < itemsArray.length();i++){
                JSONObject itemObject = itemsArray.getJSONObject(i);
                Item item = new Item();
                item.setCategory(itemObject.getString("category"));
                item.setTitle(itemObject.getString("title"));
                item.setDescription(itemObject.getString("description"));
                item.setPubDate(itemObject.getString("pubDate"));
                item.setLink(itemObject.getString("link"));
                item.setGuid(itemObject.getString("guid"));
                items.add(item);
            }
            mAdapter.updateAnswers(items);
        }
    }

    private class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private Item mItem;
        public NewsHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.layout_item,parent,false));
            mTitleTextView = itemView.findViewById(R.id.title_text_view);
            itemView.setOnClickListener(this);
        }
        public void bind(Item item){
            mItem = item;
            mTitleTextView.setText(mItem.getTitle());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,ItemActivity.class);
            intent.putExtra("GUID",mItem.getGuid());
            startActivity(intent);
        }
    }

    private class NewsAdapter extends RecyclerView.Adapter<NewsHolder>{
        private List<Item> mItems;
        public NewsAdapter(List<Item> items){
            mItems = items;
        }
        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            return new NewsHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(NewsHolder holder, int position) {
            Item item = mItems.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
        public void updateAnswers(List<Item> items){
            mItems = items;
            notifyDataSetChanged();
        }
    }

    @Override
    public void Okclick() {
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("LoadFragment",isRotateScreen);
    }
}
