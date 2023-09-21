package bo.ae.gameten10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import bo.ae.gameten10.databinding.ActivityHistoryBinding;
import bo.ae.gameten10.databinding.ActivityMainBinding;

public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding activityHistoryBinding;
    private HistoryAdapter adapter;
    private ArrayList<Integer> pointsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHistoryBinding  = ActivityHistoryBinding.inflate(getLayoutInflater());
        View view = activityHistoryBinding.getRoot();
        setContentView(view);
        hideSystemUI();
        setUpRcv();
        activityHistoryBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setUpRcv() {
        activityHistoryBinding.historyTenRcv.setLayoutManager(new LinearLayoutManager(this));
        DBHelper dbHelper = new DBHelper(this);
        pointsList= dbHelper.getTop10Points();
        adapter = new HistoryAdapter(this, pointsList);
        activityHistoryBinding.historyTenRcv.setAdapter(adapter);
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }
}