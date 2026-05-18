package com.example.zakatgoldestimator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);

        // Handles standard edge-to-edge system window paddings
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.about_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Clickable URL handler mechanism
        Button btnGitHubLink = findViewById(R.id.btnGitHubLink);
        btnGitHubLink.setOnClickListener(v -> {
            // Replace with your actual repository URL link when pushing code
            String githubUrl = "https://github.com/aemmbug/ZakatGold-Estimator";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl));
            startActivity(browserIntent);
        });
    }
}
