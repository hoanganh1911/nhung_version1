package com.example.myapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import  android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private DatabaseReference NhietDo ;
    private DatabaseReference DoAm ;
    private DatabaseReference Speed;
    private TextView textTemp;
    private TextView textHum;
    private Slider sld;
    private float value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); // Dòng này để xóa header của android studio tạo ra

        // Ánh xạ đến các id
        textTemp = (TextView) findViewById(R.id.textView);
        textHum = (TextView) findViewById(R.id.textView2);
        sld     = (Slider) findViewById(R.id.slide);


        NhietDo = FirebaseDatabase.getInstance().getReference("NhietDo");
        DoAm    = FirebaseDatabase.getInstance().getReference("DoAm");
        Speed   = FirebaseDatabase.getInstance().getReference("Speed");
        //////////////////////////////////////////////////////////////////////////////////////
        ///////////// Lắng nghe trạng thái tốc độ từ Firebase ////////////////////////////////
        Speed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Xử lý và gán dữ liệu vào thanh trượt khi dữ liệu thay đổi
                sld.setValue(Float.parseFloat(dataSnapshot.getValue().toString()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////
        ///////////// Lắng nghe thông số độ ẩm từ Firebase //////////////////////////////////
        DoAm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Hum = dataSnapshot.getValue().toString();
                textHum.setText("Độ ẩm: "+ Hum+"%");}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //In ra lỗi khi dữ liệu lỗi
                Toast.makeText(MainActivity.this, "Kiểm tra mạng ....", Toast.LENGTH_SHORT).show();
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////
        ///////////// Lắng nghe thông số nhiệt độ từ Firebase ////////////////////////////////
        NhietDo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Temp = dataSnapshot.getValue().toString();
                textTemp.setText("Nhiệt độ: " + Temp+ "℃");}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //In ra lỗi khi dữ liệu lỗi
                Toast.makeText(MainActivity.this, "Kiểm tra mạng ....", Toast.LENGTH_SHORT).show();
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////
        /////////////////////// Post giá trị lên Firebase mỗi khi trượt///////////////////////
        sld.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
            }
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                value = slider.getValue();
                Speed.setValue(value);
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////
        //////////////// Xử lý lable hiển thị từng mức //////////////////////////////////////
        sld.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                if(value == 0.0f)
                    return "OFF";
                else if(value == 25.0f)
                    return "LOW";
                else if(value == 50.0f)
                    return "MEDIUM";
                else if(value == 75.0f)
                    return "HIGH";
                return "VERY HIGH";
            }
        });

    }
}
