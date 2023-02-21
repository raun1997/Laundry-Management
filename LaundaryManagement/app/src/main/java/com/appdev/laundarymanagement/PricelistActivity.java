package com.appdev.laundarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class PricelistActivity extends AppCompatActivity {
    ListView ls;
    TextView tv1;
    BottomNavigationView bv;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageButton add;
    ProgressBar pb;
    ArrayList<pricelistclass> a= new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pricelist);
        ls=findViewById(R.id.list);
        tv1=findViewById(R.id.textView);
        bv=findViewById(R.id.bottom_nav);
        add=findViewById(R.id.add);
        pb=findViewById(R.id.progressbar);
        bv.getMenu().findItem(R.id.navigation_pricelist).setChecked(true);
        swipeRefreshLayout= (SwipeRefreshLayout)findViewById(R.id.refresh2);
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
        CustomAdapter2 customAdapter=new CustomAdapter2(PricelistActivity.this,R.layout.pricelist_listview,a);
        readDataFromGoogleSheet();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PricelistActivity.this,add_in_price_list_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        bv.setOnNavigationItemSelectedListener(item -> {
            myClickItem(item);
            return true;
        });

    }
    public void myClickItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_dashboard:
                Intent intent = new Intent(PricelistActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.navigation_pricelist:
                Intent intent2 = new Intent(PricelistActivity.this, PricelistActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
        }
    }

    private void readDataFromGoogleSheet() {
        String spreadsheetId = "1myN4i5Nu7oTZqm9CrOyT4O7aQjJ7f8AcucQ1-MnmU4w";
        String range = "Sheet2!A:C";
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
                a.add(new pricelistclass(rows.get(0).get(0).toString(),rows.get(0).get(1).toString(), (String) rows.get(0).get(2)));
                for(int i=1;i<rows.size();i++){
                        a.add(new pricelistclass(rows.get(i).get(0).toString(),rows.get(i).get(1).toString(), (String) rows.get(i).get(2)));
                }
                CustomAdapter2 customAdapter=new CustomAdapter2(PricelistActivity.this,R.layout.pricelist_listview,a);
                pb.setVisibility(View.GONE);
                ls.setAdapter(customAdapter);
                System.out.println(a.size()==1);
                if(a.size()==1){
                    ArrayList<pricelistclass> d;
                    d=new ArrayList<pricelistclass>();
                    d.add(new pricelistclass("No Particulars added","", ""));
                    CustomAdapter2 customAdapter2=new CustomAdapter2(PricelistActivity.this,R.layout.pricelist_listview,d);
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
                Toast.makeText(PricelistActivity.this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                ArrayList<pricelistclass> e=new ArrayList<pricelistclass>();
                e.add(new pricelistclass("No Internet Connection","", ""));
                CustomAdapter2 customAdapter2=new CustomAdapter2(PricelistActivity.this,R.layout.pricelist_listview,e);
                pb.setVisibility(View.GONE);
                ls.setAdapter(customAdapter2);
                // Handle error
            }
        });

    }
}