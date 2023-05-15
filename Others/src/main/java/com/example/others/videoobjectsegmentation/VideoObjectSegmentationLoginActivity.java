package com.example.others.videoobjectsegmentation;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import com.example.others.R;

public class VideoObjectSegmentationLoginActivity extends AppCompatActivity {
    EditText roomIDEdit;
    Switch isCustomRenderSwitch;
    AppCompatSpinner appOrientationModeSpinner;
    AppCompatSpinner customRotateTypeSpinner;
    Button startButton;
    Switch enableEffectsEnvSwitch;
    Switch veAlphaRenderSwitch;
    AppCompatSpinner veRenderBackendSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_object_segmentation_login);
        bindView();
        requestPermission();
        setDefaultValue();
        setLoginRoomButtonEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, VideoObjectSegmentationLoginActivity.class);
        activity.startActivity(intent);
    }

    public void bindView() {
        roomIDEdit = findViewById(R.id.loginRoomIDEdit);
        isCustomRenderSwitch = findViewById(R.id.customVideoRenderSwitch);
        appOrientationModeSpinner = findViewById(R.id.orientationModeSpinner);
        customRotateTypeSpinner = findViewById(R.id.rotateTypeSpinner);
        startButton = findViewById(R.id.loginRoomButton);
        enableEffectsEnvSwitch = findViewById(R.id.enableEffectsEnvSwitch);
        veAlphaRenderSwitch = findViewById(R.id.veAlphaRenderSwitch);
        veRenderBackendSpinner = findViewById(R.id.veRenderBackendSpinner);
    }

    // Request for permission
    public void requestPermission() {
        String[] PERMISSIONS_STORAGE = {
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO"};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(PERMISSIONS_STORAGE, 101);
            }
        }
    }

    public void setDefaultValue() {
        setTitle(R.string.subject_segmentation);

        roomIDEdit.setText("0035");
        isCustomRenderSwitch.setChecked(true);
        appOrientationModeSpinner.setSelection(0);
        customRotateTypeSpinner.setSelection(0);
    }

    public void setLoginRoomButtonEvent() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VideoObjectSegmentationActivity.class);
                intent.putExtra("roomID", roomIDEdit.getText().toString());
                intent.putExtra("isCustomRender", isCustomRenderSwitch.isChecked());
                intent.putExtra("appOrientationMode", appOrientationModeSpinner.getSelectedItemPosition());
                intent.putExtra("customRotateType", customRotateTypeSpinner.getSelectedItemPosition());
                intent.putExtra("enableEffectsEnvSwitch", enableEffectsEnvSwitch.isChecked());
                intent.putExtra("veAlphaRenderSwitch", veAlphaRenderSwitch.isChecked());
                intent.putExtra("veRenderBackend", veRenderBackendSpinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });
    }
}
