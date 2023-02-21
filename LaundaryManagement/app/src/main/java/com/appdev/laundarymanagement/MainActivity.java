package com.appdev.laundarymanagement;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ListView ls;
    TextView tv1;
    BottomNavigationView bv;
    SearchView sv;
    LinearLayout ll;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar pb;
    ArrayList<newClass> a= new ArrayList<>();
    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ls=findViewById(R.id.list);
        tv1=findViewById(R.id.textView);
        bv=findViewById(R.id.bottom_nav);
        sv=findViewById(R.id.search);
        ll=findViewById(R.id.homepage);
        pb=findViewById(R.id.progressbar);
        bv.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
        swipeRefreshLayout= (SwipeRefreshLayout)findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        a=new ArrayList<>();
                        readDataFromGoogleSheet();
                        // Your code goes here
                        // In this code, we are just
                        // changing the text in the textbox

                        // This line is important as it explicitly
                        // refreshes only once
                        // If "true" it implicitly refreshes forever
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
//        a.add(new newClass("vasu","320"));
//        a.add(new newClass("keyush","320"));
//        a.add(new newClass("darshan","320"));
//        a.add(new newClass("pratyush","320"));
//        a.add(new newClass("vishal","319"));
//        a.add(new newClass("akshat","319"));
//        a.add(new newClass("samarth","319"));
//        a.add(new newClass("prabhav","319"));
//        a.add(new newClass("few","98"));
//        a.add(new newClass("few","34"));
//        a.add(new newClass("few","34"));
//        a.add(new newClass("few","543"));
//        a.add(new newClass("few","12"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("efw","efw"));
//        a.add(new newClass("fewty","efw"));
//        a.add(new newClass("fat","efw"));
//        a.add(new newClass("fun","efw"));
//        a.add(new newClass("fuck","efw"));
//        a.add(new newClass("family","efw"));
//        a.add(new newClass("vasu","efw"));
//        a.add(new newClass("fuss","efw"));
//        a.add(new newClass("fuzz","efw"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("few","efw"));
//        a.add(new newClass("few","efw"));
        readDataFromGoogleSheet();
        bv.setOnNavigationItemSelectedListener(item -> {
            myClickItem(item);
            return true;
        });
        CustomAdapter customAdapter=new CustomAdapter(MainActivity.this,R.layout.listview_request,a);

        ls.setBackgroundResource(R.drawable.rounded_corners_table);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<newClass> b = new ArrayList<>();
                if (query.length() == 0) {
                    b.addAll(a);
                    CustomAdapter customAdapter2 = new CustomAdapter(MainActivity.this, R.layout.listview_request, b);
                    ls.setAdapter(customAdapter2);
                } else {
                    for (newClass wp : a) {
                        if (wp.gettitle().toLowerCase(Locale.getDefault()).contains(query) || wp.getValue().toLowerCase(Locale.getDefault()).contains(query)) {
                            b.add(wp);
                        }
                    }

                }
                if(!b.isEmpty()) {
                    CustomAdapter customAdapter2 = new CustomAdapter(MainActivity.this, R.layout.listview_request, b);
                    ls.setAdapter(customAdapter2);
                }
                else{
                    Toast.makeText(MainActivity.this, "No match found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<newClass> b = new ArrayList<>();
                if(newText.length() == 0){
                    ls.setAdapter(customAdapter);
                }
                else {
                    for (newClass wp : a) {
                        if (wp.gettitle().toLowerCase(Locale.getDefault()).contains(newText) || wp.getValue().toLowerCase(Locale.getDefault()).contains(newText)) {
                            b.add(wp);
                        }
                    }
                    if (!b.isEmpty()) {
                        CustomAdapter customAdapter2 = new CustomAdapter(MainActivity.this, R.layout.listview_request, b);
                        ls.setAdapter(customAdapter2);
                    } else {
                        CustomAdapter customAdapter2 = new CustomAdapter(MainActivity.this, R.layout.listview_request, b);
                        ls.setAdapter(customAdapter2);
                    }
                }
                return false;
            }
        });
        KeyboardUtils.addKeyboardToggleListener(this, isVisible -> {
            if(isVisible) {
                bv.setVisibility(View.GONE);
                tv1.setVisibility(View.GONE);
            }
            else{
                bv.setVisibility(View.VISIBLE);
                tv1.setVisibility(View.VISIBLE);
            }
            Log.d("keyboard", "keyboard visible: "+isVisible);
        });




    }
    public void myClickItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_dashboard:
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.navigation_pricelist:
                Intent intent2 = new Intent(MainActivity.this, PricelistActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
        }
    }
    private void readDataFromGoogleSheet() {
        String spreadsheetId = "1myN4i5Nu7oTZqm9CrOyT4O7aQjJ7f8AcucQ1-MnmU4w";
        String range = "Sheet1!A:I";
        String apiKey = "AIzaSyAtB0JJF5JEcr3gCW6W_wz2AHgtBYhGBmk";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sheets.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SheetsService sheetsService = retrofit.create(SheetsService.class);

        Call<ValueRange> call = sheetsService.getValues(spreadsheetId, range, apiKey);
        call.enqueue(new Callback<ValueRange>() {
            @Override
            public void onResponse(@NonNull Call<ValueRange> call, @NonNull Response<ValueRange> response) {
                //try {
                    System.out.println(response.toString());
                    ValueRange values = response.body();
                    List<List<Object>> rows = values.getValues();
                a.add(new newClass(rows.get(0).get(1).toString(),rows.get(0).get(0).toString()));
                    for(int i=1;i<rows.size();i++){
                        if(rows.get(i).get(7).toString().equals("TRUE")) {
                            a.add(new newClass(rows.get(i).get(1).toString(), rows.get(i).get(0).toString()));
                        }
                    }
                CustomAdapter customAdapter=new CustomAdapter(MainActivity.this,R.layout.listview_request,a);
                pb.setVisibility(View.GONE);
                ls.setAdapter(customAdapter);
                System.out.println(a.size()==1);
                if(a.size()==1){
                    ArrayList<newClass> d;
                    d=new ArrayList<newClass>();
                    d.add(new newClass("No Requests",""));
                    CustomAdapter customAdapter2=new CustomAdapter(MainActivity.this,R.layout.listview_request,d);
                    ls.setAdapter(customAdapter2);
                }


                    // Process the rows here
                //System.out.println(rows.toString());
                   // Toast.makeText(MainActivity.this, rows.get(1).get(0).toString(), Toast.LENGTH_SHORT).show();
                //}
//                catch (AssertionError a){
//                    System.out.println(a.getMessage());
//                    Toast.makeText(MainActivity.this, a.getMessage(), Toast.LENGTH_SHORT).show();
//                }
                }

            @Override
            public void onFailure(@NonNull Call<ValueRange> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                ArrayList<pricelistclass> e=new ArrayList<pricelistclass>();
                e.add(new pricelistclass("No Internet Connection","", ""));
                CustomAdapter2 customAdapter2=new CustomAdapter2(MainActivity.this,R.layout.pricelist_listview,e);
                pb.setVisibility(View.GONE);
                ls.setAdapter(customAdapter2);
                // Handle error
   }
});

    }
}

