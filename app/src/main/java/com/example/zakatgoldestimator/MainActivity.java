package com.example.zakatgoldestimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // 1. DECLARE GLOBAL VARIABLES AT THE TOP
    private TextInputLayout weightInputLayout, valueInputLayout;
    private TextInputEditText etWeight, etGoldValue;
    private RadioGroup rgGoldType;
    private RadioButton rbKeep;
    private MaterialCardView resultCard;
    private TextView tvTotalValue, tvPayableValue, tvTotalZakat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // System edge-to-edge window padding logic
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // INITIALIZE WIDGETS LINKED TO activity_main.xml IDs
        weightInputLayout = findViewById(R.id.weightInputLayout);
        valueInputLayout = findViewById(R.id.valueInputLayout);
        etWeight = findViewById(R.id.etWeight);
        etGoldValue = findViewById(R.id.etGoldValue);
        rgGoldType = findViewById(R.id.rgGoldType);
        rbKeep = findViewById(R.id.rbKeep);
        resultCard = findViewById(R.id.resultCard);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvPayableValue = findViewById(R.id.tvPayableValue);
        tvTotalZakat = findViewById(R.id.tvTotalZakat);

        Button btnCalculate = findViewById(R.id.btnCalculate);

        // Hide results card layout structure initially
        resultCard.setVisibility(View.GONE);

        // ATTACH THE CALCULATION CLICK LISTENER TO THE BUTTON
        btnCalculate.setOnClickListener(v -> performZakatCalculation());
    }

    // CALCULATION METHOD
    private void performZakatCalculation() {
        weightInputLayout.setError(null);
        valueInputLayout.setError(null);

        String weightStr = etWeight.getText().toString().trim();
        String valueStr = etGoldValue.getText().toString().trim();

        boolean hasError = false;

        if (weightStr.isEmpty()) {
            weightInputLayout.setError("Gold weight is required");
            hasError = true;
        }
        if (valueStr.isEmpty()) {
            valueInputLayout.setError("Gold value per gram is required");
            hasError = true;
        }

        if (hasError) {
            resultCard.setVisibility(View.GONE);
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            double goldPrice = Double.parseDouble(valueStr);

            double urufThreshold = rbKeep.isChecked() ? 85.0 : 200.0;

            double totalGoldValue = weight * goldPrice;

            double payableWeight = weight - urufThreshold;
            if (payableWeight < 0) {
                payableWeight = 0;
            }
            double zakatPayableValue = payableWeight * goldPrice;
            double totalZakatDue = zakatPayableValue * 0.025;

            tvTotalValue.setText(String.format(Locale.getDefault(), "RM %.2f", totalGoldValue));
            tvPayableValue.setText(String.format(Locale.getDefault(), "RM %.2f", zakatPayableValue));
            tvTotalZakat.setText(String.format(Locale.getDefault(), "RM %.2f", totalZakatDue));

            resultCard.setVisibility(View.VISIBLE);

        } catch (NumberFormatException e) {
            weightInputLayout.setError("Please enter valid numerical values");
            resultCard.setVisibility(View.GONE);
        }
    }

    // INFLATE OPTIONS MENU FOR NAVIGATION & SYSTEM SHARING
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = "Check out my ZakatGold Estimator repository: https://github.com/aemmbug/ZakatGold-Estimator";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }

        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}