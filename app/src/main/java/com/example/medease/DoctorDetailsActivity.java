package com.example.medease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorDetailsActivity extends AppCompatActivity {
    private String[][] doctor_details1 =
            {
                    {"Doctor Name : Jacob Fink", "Hospital Address : West Jakarta", "Exp : 5yrs", "Mobile No. : 9876543210", "600"},
                    {"Doctor Name : Chiara Altieri", "Hospital Address : North Jakarta", "Exp : 15yrs", "Mobile No. : 1234567890", "900"},
            };
    private String[][] doctor_details2 =
            {
                    {"Doctor Name : Sheldon Cooper", "Hospital Address : East Jakarta", "Exp : 10yrs", "Mobile No. : 8679054231", "600"},
                    {"Doctor Name : Uhtred Bamburgh", "Hospital Address : Central Jakarta", "Exp : 15yrs", "Mobile No. : 2356478190", "900"},
            };
    private String[][] doctor_details3 =
            {
                    {"Doctor Name : Dexter Morgan", "Hospital Address : South Jakarta", "Exp : 9yrs", "Mobile No. : 8679054231", "600"},
                    {"Doctor Name : Ludovica Storti", "Hospital Address : East Jakarta", "Exp : 7yrs", "Mobile No. : 3256478190", "700"},
            };

    TextView tv;
    Button btn;
    String[][] doctor_details = {};
    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv = findViewById(R.id.textViewDDTitle);
        btn = findViewById(R.id.buttonDDBack);

        Intent it = getIntent();
        String title = it.getStringExtra("title");
        tv.setText(title);

        // Set doctor details based on the title
        if ("Family Physicians".equals(title)) {
            doctor_details = doctor_details1;
        } else if ("Dieticians".equals(title)) {
            doctor_details = doctor_details2;
        } else if ("Dentist".equals(title)) {
            doctor_details = doctor_details3;
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorDetailsActivity.this, FindDoctorActivity.class));
            }
        });

        // Prepare list with only the first 3 items
        list = new ArrayList<>();
        for (int i = 0; i < Math.min(3, doctor_details.length); i++) {
            item = new HashMap<>();
            item.put("line1", doctor_details[i][0]);
            item.put("line2", doctor_details[i][1]);
            item.put("line3", doctor_details[i][2]);
            item.put("line4", doctor_details[i][3]);
            item.put("line5", "Cons Fees : " + doctor_details[i][4] + "/-");
            list.add(item);
        }

        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e}
        );
        ListView lst = findViewById(R.id.listViewDD);
        lst.setAdapter(sa);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(DoctorDetailsActivity.this, BookAppointmentActivity.class);
                it.putExtra("text1", title);
                it.putExtra("text2", doctor_details[i][0]);
                it.putExtra("text3", doctor_details[i][1]);
                it.putExtra("text4", doctor_details[i][3]);
                it.putExtra("text5", doctor_details[i][4]);
                startActivity(it);
            }
        });
    }
}
