package com.example.medease;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

public class HealthArticleDetailsActivity extends AppCompatActivity {

    private TextView tv1;
    private ViewPager2 viewPager;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_health_article_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv1 = findViewById(R.id.textViewHADTitle);
        viewPager = findViewById(R.id.viewPager);
        btnBack = findViewById(R.id.buttonHADBack);

        Intent intent = getIntent();
        String title = intent.getStringExtra("text1");
        int imageResId = intent.getIntExtra("text2", -1);
        String videoId = intent.getStringExtra("videoId");

        tv1.setText(title);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageResId, videoId);
        viewPager.setAdapter(adapter);

        btnBack.setOnClickListener(v -> startActivity(new Intent(HealthArticleDetailsActivity.this, HealthArticleActivity.class)));
    }
}
