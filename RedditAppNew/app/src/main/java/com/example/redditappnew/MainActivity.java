package com.example.redditappnew;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;

import com.example.redditappnew.ui.home.LocationBottomSheet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager orderFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        orderFragmentManager= getSupportFragmentManager();

        View postIcon = findViewById(R.id.post);
        postIcon.setPadding(10,10,10,10);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#00000000"));
        gd.setCornerRadius(20);
        gd.setStroke(5, Color.BLUE);
        gd.setShape(GradientDrawable.OVAL);
      //  postIcon.setBackground(gd);

        postIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationBottomSheet locationBottomSheet = new LocationBottomSheet();
                locationBottomSheet.show(orderFragmentManager, "exampleBottomSheet");
//                locationBottomSheet.setStyle(this, R.style.BottomSheetDialog);

            }
        });



    }

}