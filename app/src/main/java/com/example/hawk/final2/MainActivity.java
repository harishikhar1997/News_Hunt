package com.example.hawk.final2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private int no = 0;
    private TextView titleTv,contentTv;
    private ImageView imageView;
    private ArrayList<String> imageUrls,title,content,url;
    private GestureDetector gestureDetector;
    private LinearLayout layout;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Log.d("Hari", "onTouch: ");
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTv = (TextView) findViewById(R.id.title);
        contentTv = (TextView) findViewById(R.id.content);
        imageView = (ImageView) findViewById(R.id.imageSplash);
        layout = (LinearLayout) findViewById(R.id.layout);

        gestureDetector=new GestureDetector(this,new OnSwipeListener(){

            @Override
            public boolean onSwipe(Direction direction) {
                if (direction==Direction.up){
                    //do your stuff
                    next();

                }

                if (direction==Direction.down){
                    //do your stuff
                    previous();
                }

                if(direction==Direction.left){
                    Intent i = new Intent(getApplicationContext(),Web.class);
                    i.putExtra("url",url.get(no));

                    //Toast.makeText(getApplicationContext(),imageUrls.get(no),Toast.LENGTH_LONG);
                    startActivity(i);


                }
                return true;
            }


        });
        layout.setOnTouchListener(this);

        Http http = new Http();
        String data="";
        try {
            data=http.execute("https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=705f0c6f5ae84ab5879e04c725722541").get();
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("articles");

             imageUrls = new ArrayList<String>();
             title = new ArrayList<String>();
             content = new ArrayList<String>();
             url = new ArrayList<String>();

            for(int i=0;i<jsonArray.length();i++){

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                imageUrls.add(jsonObject1.getString("urlToImage"));
                title.add(jsonObject1.getString("title"));
                content.add(jsonObject1.getString("description"));
                url.add(jsonObject1.getString("url"));
            }
            titleTv.setText(title.get(0));
            contentTv.setText(content.get(0));
            Picasso.with(getApplicationContext()).load(imageUrls.get(0)).into(imageView);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void next(){
        no+=1;
        if(no==title.size())
            no=0;
        titleTv.setText(title.get(no));
        contentTv.setText(content.get(no));

        Picasso.with(getApplicationContext()).load(imageUrls.get(no)).into(imageView);
    }

    public void previous(){

        no-=1;
        if(no==-1)
            no+=title.size();
        titleTv.setText(title.get(no));
        contentTv.setText(content.get(no));

        Picasso.with(getApplicationContext()).load(imageUrls.get(no)).into(imageView);
    }



    public class Http extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String result="";
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1)
                {
                    result += (char)data;
                    data = reader.read();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

}


