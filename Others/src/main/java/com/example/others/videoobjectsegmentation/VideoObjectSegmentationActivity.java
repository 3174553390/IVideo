package com.example.others.videoobjectsegmentation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import com.example.others.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import im.zego.commontools.logtools.AppLogger;
import im.zego.commontools.logtools.LogView;
import im.zego.commontools.logtools.logLinearLayout;
import im.zego.commontools.uitools.ZegoViewUtil;
import im.zego.keycenter.KeyCenter;
import im.zego.keycenter.UserIDHelper;
import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoApiCalledEventHandler;
import im.zego.zegoexpress.callback.IZegoEventHandler;
import im.zego.zegoexpress.callback.IZegoPlayerTakeSnapshotCallback;
import im.zego.zegoexpress.callback.IZegoPublisherTakeSnapshotCallback;
import im.zego.zegoexpress.callback.IZegoPublisherUpdateCdnUrlCallback;
import im.zego.zegoexpress.constants.ZegoAlphaLayoutType;
import im.zego.zegoexpress.constants.ZegoCameraExposureMode;
import im.zego.zegoexpress.constants.ZegoCameraFocusMode;
import im.zego.zegoexpress.constants.ZegoObjectSegmentationState;
import im.zego.zegoexpress.constants.ZegoObjectSegmentationType;
import im.zego.zegoexpress.constants.ZegoOrientation;
import im.zego.zegoexpress.constants.ZegoOrientationMode;
import im.zego.zegoexpress.constants.ZegoPlayerState;
import im.zego.zegoexpress.constants.ZegoPublishChannel;
import im.zego.zegoexpress.constants.ZegoPublisherState;
import im.zego.zegoexpress.constants.ZegoRoomStateChangedReason;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoStreamResourceMode;
import im.zego.zegoexpress.constants.ZegoTrafficControlProperty;
import im.zego.zegoexpress.constants.ZegoVideoBufferType;
import im.zego.zegoexpress.constants.ZegoVideoCodecID;
import im.zego.zegoexpress.constants.ZegoVideoConfigPreset;
import im.zego.zegoexpress.constants.ZegoVideoFrameFormatSeries;
import im.zego.zegoexpress.constants.ZegoVideoMirrorMode;
import im.zego.zegoexpress.entity.ZegoCDNConfig;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoCustomVideoRenderConfig;
import im.zego.zegoexpress.entity.ZegoEffectsBeautyParam;
import im.zego.zegoexpress.entity.ZegoEngineConfig;
import im.zego.zegoexpress.entity.ZegoEngineProfile;
import im.zego.zegoexpress.entity.ZegoPlayStreamQuality;
import im.zego.zegoexpress.entity.ZegoPlayerConfig;
import im.zego.zegoexpress.entity.ZegoPublishStreamQuality;
import im.zego.zegoexpress.entity.ZegoUser;
import im.zego.zegoexpress.entity.ZegoVideoConfig;

import com.example.others.beautyandwatermarkandsnapshot.ImageFilePath;

public class VideoObjectSegmentationActivity extends AppCompatActivity {
    String userID;
    String streamID;
    String roomID;
    ZegoExpressEngine engine;
    Long appID;
    String appSign;
    ZegoUser user;
    String playStream1;
    String playStream2;
    //Store whether the user is playing the stream
    Boolean isPlay1 = false;
    Boolean isPlay2 = false;
    Boolean isPublish = false;
    ArrayList<String> cdnUrls;

    TextureView previewView;
    TextureView playView1;
    SurfaceView playView2;
    TextView roomState;
    Button startPublishButton;
    Button startPlayingButton1;
    Button startPlayingButton2;
    EditText playStream1Edit;
    EditText playStream2Edit;
    EditText publishStreamEdit;
    EditText editPublishStreamCDNUrl;

    TextView roomInfoText;
    TextView roomStateText;
    // Video Config Controls
    EditText captureResolutionWidthEdit;
    EditText captureResolutionHeightEdit;
    EditText encodeResolutionWidthEdit;
    EditText encodeResolutionHeightEdit;
    EditText videoFPSEdit;
    EditText videoBitrateEdit;
    AppCompatSpinner mirrorModeSpinner;
    Switch switchTrafficControl;
    EditText editTrafficProperty;
    AppCompatSpinner codecIDSpinner;
    Button setVideoConfigButton;
    // Hardware encode/decode
    TextView hardwareEncodeStateText;
    TextView hardwareDecodeStateText;
    Switch switchHardwareEncoder;
    //Subject segmentation
    AppCompatSpinner encoderAlphaProfileSpinner;
    Switch switchSubjectSegmentation;
    AppCompatSpinner segmentationTypeSpinner;
    //Camera
    AppCompatSpinner cameraSelectionSpinner;
    SwitchMaterial cameraFocusSwitch;
    SwitchMaterial cameraExposureSwitch;
    AppCompatSpinner exposureModeSpinner;
    AppCompatSpinner focusModeSpinner;
    SeekBar zoomFactorSeekBar;
    SeekBar exposureCompensationSeekBar;
    TextView zoomFactorValue;
    TextView exposureCompensationValue;
    TextView maxZoomFactorValue;
    TextView supportFocusState;
    float zoomMax = 2.0f;
    Switch switchCamera;
    //Beauty
    ZegoEffectsBeautyParam param;
    SwitchMaterial effectsBeautySwitch;
    SeekBar whitenSeekBar;
    SeekBar rosySeekBar;
    SeekBar smoothSeekBar;
    SeekBar sharpenSeekBar;
    TextView whitenValue;
    TextView rosyValue;
    TextView smoothValue;
    TextView sharpenValue;

    Button takePublishSnapshotButton;
    Button takePlaySnapshotButton;
    Button chooseBackgroundPictureButton;
    EditText snapshotStreamIDEdit;
    public static final int PICK_IMAGE = 1;
    LinearLayout LinearLayoutAllViews;

    // Play
    Switch switchHardwareDecoder;

    CustomVideoRender videoRenderer;

    Boolean isCustomRender = true;
    ZegoOrientationMode appOrientationMode = ZegoOrientationMode.CUSTOM;
    String customRotateType;
    Boolean veAlphaRender;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, VideoObjectSegmentationActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_object_segmentation);
        bindView();
        getAppIDAndUserIDAndAppSign();
        setDefaultValue();

        initEngineAndUser();
        loginRoom();
        requestPermission();
        setApiCalledResult();
        setLogComponent();
        setEventHandler();
        initOrinetation();
    }

    @Override
    protected void onDestroy() {
        //logout and destroy the engine.
        engine.logoutRoom(roomID);
        ZegoExpressEngine.destroyEngine(null);
        super.onDestroy();
    }

    public void bindView(){
        previewView = findViewById(R.id.preView);
        playView1 = findViewById(R.id.playView1);
        playView2 = findViewById(R.id.playView2);
        startPublishButton = findViewById(R.id.startPublishButton);
        startPlayingButton1 = findViewById(R.id.startPlayButton1);
        startPlayingButton2 = findViewById(R.id.startPlayButton2);
        playStream1Edit = findViewById(R.id.editPlayStream1);
        playStream2Edit = findViewById(R.id.editPlayStream2);
        publishStreamEdit = findViewById(R.id.editPublishStreamID);
        roomState = findViewById(R.id.roomState);
//        playStreamIDEdit = findViewById(R.id.editTrafficProperty);
//        superResolutionStreamIDEdit = findViewById(R.id.editSuperResolutionStreamID);
//        enableSuperResolutionSwitch = findViewById(R.id.switchHardwareEncoder);
//        superResolutionStateText = findViewById(R.id.textViewSuperResolutionState);
//        playerVideoSizeText = findViewById(R.id.textViewVideoSize);
        roomInfoText = findViewById(R.id.textViewRoomInfo);
        roomStateText = findViewById(R.id.roomState);


        captureResolutionWidthEdit = findViewById(R.id.captureResolutionWidth);
        captureResolutionHeightEdit = findViewById(R.id.captureResolutionHeight);
        encodeResolutionWidthEdit = findViewById(R.id.encodeResolutionWidth);
        encodeResolutionHeightEdit = findViewById(R.id.encodeResolutionHeight);
        videoFPSEdit = findViewById(R.id.videoFps);
        videoBitrateEdit = findViewById(R.id.videoBitrate);
        mirrorModeSpinner = findViewById(R.id.mirrorMode);
        hardwareEncodeStateText = findViewById(R.id.hardwareEncodeState);
        hardwareDecodeStateText = findViewById(R.id.hardwareDecodeState);
        encoderAlphaProfileSpinner = findViewById(R.id.encoderAlphaProfileSpinner);
        switchHardwareEncoder = findViewById(R.id.switchHardwareEncoder);
        editPublishStreamCDNUrl = findViewById(R.id.editPublishStreamCDNUrl);
        switchTrafficControl = findViewById(R.id.switchTrafficControl);
        editTrafficProperty = findViewById(R.id.editTrafficProperty);
        codecIDSpinner = findViewById(R.id.codecIDSpinner);
        setVideoConfigButton = findViewById(R.id.setVideoConfigButton);

        cameraSelectionSpinner = findViewById(R.id.cameraSelectionSpinner);
        cameraFocusSwitch = findViewById(R.id.cameraFocusSwitch);
        cameraExposureSwitch = findViewById(R.id.cameraExposureSwitch);
        focusModeSpinner = findViewById(R.id.focusModeSpinner);
        exposureModeSpinner = findViewById(R.id.exposureModeSpinner);
        zoomFactorSeekBar = findViewById(R.id.zoomSeekBar);
        exposureCompensationSeekBar = findViewById(R.id.exposureCompensationSeekBar);
        zoomFactorValue = findViewById(R.id.zoomFactorValue);
        exposureCompensationValue = findViewById(R.id.exposureCompensationValue);
        maxZoomFactorValue = findViewById(R.id.maxZoomFactor);
        supportFocusState = findViewById(R.id.supportFocusState);
        switchSubjectSegmentation = findViewById(R.id.switchSubjectSegmentation);
        segmentationTypeSpinner = findViewById(R.id.segmentationTypeSpinner);

        effectsBeautySwitch = findViewById(R.id.effectsBeautySwitch);
        whitenSeekBar = findViewById(R.id.whitenSeekBar);
        rosySeekBar = findViewById(R.id.rosySeekBar);
        smoothSeekBar = findViewById(R.id.smoothSeekBar);
        sharpenSeekBar = findViewById(R.id.sharpenSeekBar);
        whitenValue = findViewById(R.id.whitenValueTextView);
        rosyValue = findViewById(R.id.rosyValueTextView);
        smoothValue = findViewById(R.id.smoothValueTextView);
        sharpenValue = findViewById(R.id.sharpenValueTextView);
        chooseBackgroundPictureButton = findViewById(R.id.chooseBackgroundPictureButton);
        LinearLayoutAllViews = findViewById(R.id.LinearLayoutAllViews);

        switchHardwareDecoder = findViewById(R.id.switchHardwareDecoder);
        switchCamera = findViewById(R.id.switchCamera);
        takePublishSnapshotButton = findViewById(R.id.takePublishSnapshotButton);
        takePlaySnapshotButton = findViewById(R.id.takePlaySnapshotButton);
        snapshotStreamIDEdit = findViewById(R.id.snapshotStreamIdEdit);
    }
    public void setDefaultValue(){
        roomID = getIntent().getStringExtra("roomID");
        streamID = "0035";
        setTitle(getString(R.string.subject_segmentation));
        roomInfoText.setText(String.format("RoomID:%s", roomID));

        publishStreamEdit.setText(streamID);
        playStream1Edit.setText(streamID);
        playStream2Edit.setText(streamID);

        editTrafficProperty.setText("0");
        encoderAlphaProfileSpinner.setSelection(1);
        cameraFocusSwitch.setChecked(true);
        cameraExposureSwitch.setChecked(true);
        cameraSelectionSpinner.setSelection(0);

        //Beauty
        param = new ZegoEffectsBeautyParam();
        param.whitenIntensity = 50;
        param.rosyIntensity = 50;
        param.smoothIntensity = 50;
        param.sharpenIntensity = 50;

        switchCamera.setChecked(true);

        previewView.setOpaque(false);
//        previewView.setZOrderOnTop(true);
//        previewView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        playView1.setOpaque(false);
//        playView2.setOpaque(false);
        playView2.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//        playView2.setZOrderOnTop(true);

        isCustomRender = getIntent().getBooleanExtra("isCustomRender", true);
        appOrientationMode = ZegoOrientationMode.values()[getIntent().getIntExtra("appOrientationMode", 0)];
        String customRotateType;
    }
    //get appID and userID and appSign
    public void getAppIDAndUserIDAndAppSign(){
        appID = KeyCenter.getInstance().getAppID();
        userID = UserIDHelper.getInstance().getUserID();
        appSign = KeyCenter.getInstance().getAppSign();
    }
    public void initEngineAndUser(){
        veAlphaRender = getIntent().getBooleanExtra("veAlphaRenderSwitch", false);
        String veRenderBackend = getIntent().getStringExtra("veRenderBackend");

        if(!veRenderBackend.equals("none")){
            ZegoEngineConfig cc = new ZegoEngineConfig();
            cc.advancedConfig.put("video_render_backend",veRenderBackend);
            ZegoExpressEngine.setEngineConfig(cc);
        }

        // Initialize ZegoExpressEngine
        ZegoEngineProfile profile = new ZegoEngineProfile();
        profile.appID = appID;
        profile.appSign = appSign;

        // Here we use the high quality video call scenario as an example,
        // you should choose the appropriate scenario according to your actual situation,
        // for the differences between scenarios and how to choose a suitable scenario,
        // please refer to https://docs.zegocloud.com/article/14940
        profile.scenario = ZegoScenario.HIGH_QUALITY_VIDEO_CALL;

        profile.application = getApplication();
        engine = ZegoExpressEngine.createEngine(profile, null);

        AppLogger.getInstance().callApi("Create ZegoExpressEngine");
        //create the user
        user = new ZegoUser(userID);

        if(isCustomRender){
            videoRenderer = new CustomVideoRender();
            videoRenderer.setPreviewView(previewView);
            videoRenderer.setPlayView1(playView1);
            videoRenderer.setPlayView2(playView2);
            ZegoCustomVideoRenderConfig config = new ZegoCustomVideoRenderConfig();
            config.bufferType = ZegoVideoBufferType.RAW_DATA;
            config.frameFormatSeries = ZegoVideoFrameFormatSeries.RGB;
//        config.enableEngineRender = true;
            engine.enableCustomVideoRender(true, config);
            engine.setCustomVideoRenderHandler(videoRenderer);
        }

        boolean enableEffectsEnvSwitch = getIntent().getBooleanExtra("enableEffectsEnvSwitch", false);
        if(enableEffectsEnvSwitch){
            engine.startEffectsEnv();
            AppLogger.getInstance().callApi("startEffectsEnv");
        }

        if(appOrientationMode == ZegoOrientationMode.CUSTOM)
        {
            ZegoOrientation orientation = ZegoOrientation.ORIENTATION_0;
            if (Surface.ROTATION_0 == this.getWindowManager().getDefaultDisplay().getRotation()) {
                orientation = ZegoOrientation.ORIENTATION_0;
            } else if (Surface.ROTATION_180 == this.getWindowManager().getDefaultDisplay().getRotation()) {
                orientation = ZegoOrientation.ORIENTATION_180;
            } else if (Surface.ROTATION_270 == this.getWindowManager().getDefaultDisplay().getRotation()) {
                orientation = ZegoOrientation.ORIENTATION_270;
            } else if (Surface.ROTATION_90 == this.getWindowManager().getDefaultDisplay().getRotation()) {
                orientation = ZegoOrientation.ORIENTATION_90;
            }
            engine.setAppOrientation(orientation);
        }
        engine.setAppOrientationMode(appOrientationMode);
    }

    public void loginRoom(){
        //login room
        engine.loginRoom(roomID, user);
        AppLogger.getInstance().callApi("LoginRoom: %s",roomID);
        //enable the camera
        engine.enableCamera(true);
        //enable the microphone
        engine.muteMicrophone(false);
        //enable the speaker
        engine.muteSpeaker(false);
    }

    public void requestPermission() {
        String[] PERMISSIONS_STORAGE = {
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(PERMISSIONS_STORAGE, 101);
            }
        }
    }

    public void setStartPublishButtonEvent(){
        startPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPublishConfig();

                if (!isPublish) {
                    if(isCustomRender){
                        videoRenderer.EnablePreview(true);
                        engine.startPreview();
                    }else{
                        ZegoCanvas canvas = new ZegoCanvas(previewView);
                        canvas.alphaBlend = veAlphaRender;
                        engine.startPreview(canvas);
                    }

                    // Start publishing stream
                    engine.startPublishingStream(publishStreamEdit.getText().toString());
                    AppLogger.getInstance().callApi("Start Publishing Stream:%s",publishStreamEdit.getText().toString());
                    startPublishButton.setText(getResources().getString(R.string.stop_publishing));
                    startPublishButton.setText(getResources().getString(R.string.stop_publishing));
                    isPublish = true;
                } else {
                    engine.stopPreview();
                    engine.stopPublishingStream();
                    if(isCustomRender){
                        videoRenderer.EnablePreview(false);
                        clearPreview();
                    }
                    isPublish = false;
                    AppLogger.getInstance().callApi("Stop Publishing Stream");
                    startPublishButton.setText(getResources().getString(R.string.start_publishing));
                }
            }
        });
    }
    public void setStartPlayButtonEvent(){
        startPlayingButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the user is playing the stream, this button is used to stop playing. Otherwise, this button is used to start publishing.
                if (isPlay1){
                    if(playStream1.equals(playStream2) && isPlay2)
                    {
//                        playStream2 = playStream2Edit.getText().toString();
//                        ZegoPlayerConfig playerConfig = new ZegoPlayerConfig();
//                        playerConfig.resourceMode = ZegoStreamResourceMode.ONLY_RTC;
//
//                        if(isCustomRender){
//                            engine.startPlayingStream(playStream2, null, playerConfig);
//                            videoRenderer.setPlayStream2(playStream2);
//                        }else{
//                            ZegoCanvas canvas = new ZegoCanvas(playView2);
//                            canvas.alphaBlend = veAlphaRender;
//                            engine.startPlayingStream(playStream2, canvas, playerConfig);
//                        }
//                        AppLogger.getInstance().callApi("Start Playing Stream:%s",playStream2);
                    }else{
                        engine.stopPlayingStream(playStream1);
                    }
                    if(isCustomRender){
                        videoRenderer.setPlayStream1("");
                        clearPlayView1();
                    }
                    AppLogger.getInstance().callApi("Stop Playing Stream:%s",playStream1);
                    startPlayingButton1.setText(getString(R.string.start_playing));
                    isPlay1 = false;
                } else {
                    playStream1 = playStream1Edit.getText().toString();
                    ZegoPlayerConfig playerConfig = new ZegoPlayerConfig();
                    playerConfig.resourceMode = ZegoStreamResourceMode.ONLY_RTC;
                    if(isCustomRender){
                        engine.startPlayingStream(playStream1, null, playerConfig);
                        videoRenderer.setPlayStream1(playStream1);
                    }else{
                        ZegoCanvas canvas = new ZegoCanvas(playView1);
                        canvas.alphaBlend = veAlphaRender;
                        engine.startPlayingStream(playStream1, canvas, playerConfig);
                    }

                    startPlayingButton1.setText(getString(R.string.stop_playing));
                    AppLogger.getInstance().callApi("Start Playing Stream:%s",playStream1);
                    isPlay1 = true;
                }
            }
        });

        startPlayingButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the user is playing the stream, this button is used to stop playing. Otherwise, this button is used to start publishing.
                if (isPlay2){
                    if(playStream1.equals(playStream2) && isPlay1)
                    {
//                        playStream1 = playStream1Edit.getText().toString();
//                        ZegoPlayerConfig playerConfig = new ZegoPlayerConfig();
//                        playerConfig.resourceMode = ZegoStreamResourceMode.ONLY_RTC;
//
//                        if(isCustomRender){
//                            engine.startPlayingStream(playStream1, null, playerConfig);
//                            videoRenderer.setPlayStream1(playStream1);
//                        }
//                        else
//                        {
//                            ZegoCanvas canvas = new ZegoCanvas(playView1);
//                            canvas.alphaBlend = veAlphaRender;
//                            engine.startPlayingStream(playStream1, canvas, playerConfig);
//                        }
//
//                        AppLogger.getInstance().callApi("Start Playing Stream:%s",playStream1);
                    }else{
                        engine.stopPlayingStream(playStream2);
                    }
                    if(isCustomRender) {
                        videoRenderer.setPlayStream2("");
                        clearPlayView2();
                    }
                    AppLogger.getInstance().callApi("Stop Playing Stream:%s",playStream2);
                    startPlayingButton2.setText(getString(R.string.start_playing));
                    isPlay2 = false;
                } else {
                    playStream2 = playStream2Edit.getText().toString();
                    ZegoPlayerConfig playerConfig = new ZegoPlayerConfig();
                    playerConfig.resourceMode = ZegoStreamResourceMode.ONLY_RTC;

                    if(isCustomRender) {
                        engine.startPlayingStream(playStream2, null, playerConfig);
                        videoRenderer.setPlayStream2(playStream2);
                    }
                    else{
                        ZegoCanvas canvas = new ZegoCanvas(playView2);
                        canvas.alphaBlend = veAlphaRender;
                        engine.startPlayingStream(playStream2, canvas, playerConfig);
                    }
                    isPlay2 = true;
                    startPlayingButton2.setText(getString(R.string.stop_playing));
                    AppLogger.getInstance().callApi("Start Playing Stream:%s",playStream2);
                }
            }
        });
    }

    public void clearPreview(){
        previewView.post(new Runnable() {
            @Override
            public void run() {
                Canvas canvas = previewView.lockCanvas();
                if(canvas != null){
                    Paint paintPlayView = new Paint();
                    paintPlayView.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    canvas.drawPaint(paintPlayView);
                }
                previewView.unlockCanvasAndPost(canvas);
            }
        });
    }

    public void clearPlayView1(){
        playView1.post(new Runnable() {
            @Override
            public void run() {
                Canvas canvas = playView1.lockCanvas();
                if(canvas != null){
                    Paint paintPlayView = new Paint();
                    paintPlayView.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    canvas.drawPaint(paintPlayView);
                }
                playView1.unlockCanvasAndPost(canvas);
            }
        });
    }

    public void clearPlayView2(){
        playView1.post(new Runnable() {
            @Override
            public void run() {
                Canvas canvas = playView2.getHolder().lockCanvas();
                if(canvas != null){
                    Paint paintPlayView = new Paint();
                    paintPlayView.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    canvas.drawPaint(paintPlayView);
                }
                playView2.getHolder().unlockCanvasAndPost(canvas);
            }
        });
    }

    public void setVideoConfigs(){
        // Set video config
        ZegoVideoConfig current_video_config = engine.getVideoConfig();
        AppLogger.getInstance().callApi("getVideoConfig");
        try {
            current_video_config.bitrate = Integer.parseInt(videoBitrateEdit.getText().toString());
        }catch (NumberFormatException ex){
            Toast.makeText(getApplicationContext(), "Bitrate is too large", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            current_video_config.fps = Integer.parseInt(videoFPSEdit.getText().toString());
        }catch(NumberFormatException ex){
            Toast.makeText(getApplicationContext(), "FPS is too large", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            current_video_config.encodeHeight = Integer.parseInt(encodeResolutionHeightEdit.getText().toString());
        }catch(NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Encode resolution height is too large", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            current_video_config.encodeWidth = Integer.parseInt(encodeResolutionWidthEdit.getText().toString());
        }catch(NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Encode resolution width is too large", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            current_video_config.captureHeight = Integer.parseInt(captureResolutionHeightEdit.getText().toString());
        }catch(NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Capture resolution height is too large", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            current_video_config.captureWidth = Integer.parseInt(captureResolutionWidthEdit.getText().toString());
        }catch(NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Capture resolution width is too large", Toast.LENGTH_SHORT).show();
            return;
        }
        engine.setVideoConfig(current_video_config);
        AppLogger.getInstance().callApi("setVideoConfig");

//        ZegoVideoMirrorMode mirror_mode = (ZegoVideoMirrorMode.values()[mirrorModeSpinner.getSelectedItemPosition()]);
//        engine.setVideoMirrorMode(mirror_mode);
//        AppLogger.getInstance().callApi("setVideoMirrorMode, mode:%d", mirror_mode.value());
    }

    public void setVideoConfigButtonEvent(){
        setVideoConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVideoConfigs();
            }
        });
    }

    public void setCameraSelectionSpinnerEvent(){
        cameraSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] options = getResources().getStringArray(R.array.cameraSelection);
                switch (options[position]) {
                    case "Front":
                        engine.useFrontCamera(true);
                        AppLogger.getInstance().callApi("Switch Camera: Front");
                        break;
                    case "Back":
                        engine.useFrontCamera(false);
                        AppLogger.getInstance().callApi("Switch Camera: Back");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setCameraFocusSwitchEvent() {
        cameraFocusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AppLogger.getInstance().callApi("Camera Focus On");
                } else {
                    AppLogger.getInstance().callApi("Camera Focus Off");
                }
            }
        });
    }

    public void setCameraExposureSwitchEvent() {
        cameraExposureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AppLogger.getInstance().callApi("Camera Exposure On");
                } else {
                    AppLogger.getInstance().callApi("Camera Exposure Off");
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setPreviewTouchEvent() {
        previewView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float x = event.getX() / v.getWidth();
                    float y = event.getY() / v.getHeight();
                    if (cameraFocusSwitch.isChecked()) {
                        engine.setCameraFocusPointInPreview(x, y, ZegoPublishChannel.MAIN);
                    }
                    if (cameraExposureSwitch.isChecked()) {
                        engine.setCameraExposurePointInPreview(x, y, ZegoPublishChannel.MAIN);
                    }
                }
                return true;
            }
        });
    }

    public void setCameraFocusModeSpinnerEvent(){
        focusModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] options = getResources().getStringArray(R.array.focusMode);
                switch (options[position]) {
                    case "ContinuousAuto":
                        engine.setCameraFocusMode(ZegoCameraFocusMode.CONTINUOUS_AUTO_FOCUS, ZegoPublishChannel.MAIN);
                        AppLogger.getInstance().callApi("Switch Focus Mode: ContinuousAuto");
                        break;
                    case "Auto":
                        engine.setCameraFocusMode(ZegoCameraFocusMode.AUTO_FOCUS, ZegoPublishChannel.MAIN);
                        AppLogger.getInstance().callApi("Switch Focus Mode: Auto");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setCameraExposureModeSpinnerEvent(){
        exposureModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] options = getResources().getStringArray(R.array.exposureMode);
                switch (options[position]) {
                    case "ContinuousAuto":
                        engine.setCameraExposureMode(ZegoCameraExposureMode.CONTINUOUS_AUTO_EXPOSURE, ZegoPublishChannel.MAIN);
                        AppLogger.getInstance().callApi("Switch Exposure Mode: ContinuousAuto");
                        break;
                    case "Auto":
                        engine.setCameraExposureMode(ZegoCameraExposureMode.AUTO_EXPOSURE, ZegoPublishChannel.MAIN);
                        AppLogger.getInstance().callApi("Switch Exposure Mode: Auto");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setCameraZoomFactorSeekBarEvent() {
        zoomFactorSeekBar.setProgress(0);
        // Because the range of zoom factor is [1.0, zoomMax]
        // Set the range of seek bar is [0, zoomMax*10]
        zoomFactorSeekBar.setMax((int)(zoomMax*10)-10);
        zoomFactorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float zoomFactor = (float)progress/10+1.0f;
                engine.setCameraZoomFactor(zoomFactor);
                zoomFactorValue.setText(String.valueOf(zoomFactor));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setCameraExposureCompensationSeekBarEvent() {
        exposureCompensationSeekBar.setProgress(0);
        // Because the range of exposure compensation is [-1, 1]
        // Set the range of seek bar is [-10, 10]
        exposureCompensationSeekBar.setMax(10);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            exposureCompensationSeekBar.setMin(-10);
        }

        exposureCompensationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = (float)progress/10;
                engine.setCameraExposureCompensation(value);
                exposureCompensationValue.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setPublishConfig() {
        // Set video config
        setVideoConfigs();

        // Set subject segmentation video encoder alpha profile
        ZegoAlphaLayoutType layoutType = ZegoAlphaLayoutType.values()[encoderAlphaProfileSpinner.getSelectedItemPosition()];
        engine.enableAlphaChannelVideoEncoder(true, layoutType, ZegoPublishChannel.MAIN);
        AppLogger.getInstance().callApi("enableAlphaChannelVideoEncoder, layoutType:%s", layoutType.toString());

        // Set hardware encoder
        engine.enableHardwareEncoder(switchHardwareEncoder.isChecked());
        AppLogger.getInstance().callApi("enableHardwareEncoder, json:%b", switchHardwareEncoder.isChecked());

        // Set traffic control
        int traffic_property = 0;
        try {
            traffic_property = Integer.parseInt(editTrafficProperty.getText().toString());
        }catch(NumberFormatException e){
            Toast.makeText(getApplicationContext(), "TrafficProperty is too large", Toast.LENGTH_SHORT).show();
            return;
        }
        engine.enableTrafficControl(switchTrafficControl.isChecked(), traffic_property);
        AppLogger.getInstance().callApi("enableTrafficControl");
        if(switchTrafficControl.isChecked() && (traffic_property & ZegoTrafficControlProperty.ADAPTIVE_RESOLUTION.value()) > 0){
            engine.setMinVideoResolutionForTrafficControl(360, 640, ZegoPublishChannel.MAIN);
            AppLogger.getInstance().callApi("setMinVideoResolutionForTrafficControl");
        }

        // codecID
        ZegoVideoConfig config = engine.getVideoConfig();
        config.codecID = ZegoVideoCodecID.values()[codecIDSpinner.getSelectedItemPosition()];
        engine.setVideoConfig(config);

        // set cdn url publish
        String cdnUrl = editPublishStreamCDNUrl.getText().toString();
        ZegoCDNConfig cdnConfig = new ZegoCDNConfig();
        cdnConfig.url = cdnUrl;
        String publishStreamID = publishStreamEdit.getText().toString();
        if(cdnUrl.isEmpty())
        {
            engine.enablePublishDirectToCDN(false, cdnConfig);
//            engine.removePublishCdnUrl(publishStreamID, cdnUrl, new IZegoPublisherUpdateCdnUrlCallback() {
//                @Override
//                public void onPublisherUpdateCdnUrlResult(int errorCode) {
//                    AppLogger.getInstance().i(String.format("onPublisherUpdateCdnUrlResult, errorCode:%d", errorCode));
//                }
//            });
        }
        else
        {
            engine.enablePublishDirectToCDN(true, cdnConfig);
            engine.addPublishCdnUrl(publishStreamID, cdnUrl, new IZegoPublisherUpdateCdnUrlCallback() {
                @Override
                public void onPublisherUpdateCdnUrlResult(int errorCode) {
                    AppLogger.getInstance().i(String.format("onPublisherUpdateCdnUrlResult, errorCode:%d", errorCode));
                }
            });
        }
    }

    public void setApiCalledResult(){
        // Update log with api called results
        ZegoExpressEngine.setApiCalledCallback(new IZegoApiCalledEventHandler() {
            @Override
            public void onApiCalledResult(int errorCode, String funcName, String info) {
                super.onApiCalledResult(errorCode, funcName, info);
                if (errorCode == 0){
                    AppLogger.getInstance().success("[%s]:%s", funcName, info);
                } else {
                    AppLogger.getInstance().fail("[%d]%s:%s", errorCode, funcName, info);
                }
            }
        });
    }

    public void setSubjectSegmentationEvent(){
        switchSubjectSegmentation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                engine.enableVideoObjectSegmentation(switchSubjectSegmentation.isChecked(), ZegoObjectSegmentationType.values()[segmentationTypeSpinner.getSelectedItemPosition()], ZegoPublishChannel.MAIN);
                AppLogger.getInstance().callApi("enableVideoObjectSegmentation, enable:%b, type:%s", switchSubjectSegmentation.isChecked(), ZegoObjectSegmentationType.values()[segmentationTypeSpinner.getSelectedItemPosition()].toString());
            }
        });
    }

    public void setEffectsBeautyEvent() {
        effectsBeautySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                engine.enableEffectsBeauty(isChecked);
            }
        });

        whitenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                whitenValue.setText(String.valueOf(progress));
                param.whitenIntensity = progress;
                engine.setEffectsBeautyParam(param);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rosySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rosyValue.setText(String.valueOf(progress));
                param.rosyIntensity = progress;
                engine.setEffectsBeautyParam(param);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        smoothSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                smoothValue.setText(String.valueOf(progress));
                param.smoothIntensity = progress;
                engine.setEffectsBeautyParam(param);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sharpenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sharpenValue.setText(String.valueOf(progress));
                param.sharpenIntensity = progress;
                engine.setEffectsBeautyParam(param);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setChosenFileButtonEvent(){
        chooseBackgroundPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choose the watermark from the file.
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data!=null) {
            // Get chosen file path
            String realPath = ImageFilePath.getPath(getApplicationContext(), data.getData());

            setBackgroundPicture(realPath);
        }
    }

    public void setBackgroundPicture(String path){
        File file = new File(path);
        if(file.exists())
        {
            try {
                int width = LinearLayoutAllViews.getWidth();
                int height = LinearLayoutAllViews.getHeight();
                Bitmap bmp1 = BitmapFactory.decodeFile(path);

                if(bmp1 != null){
                    Bitmap bmp = Bitmap.createScaledBitmap(bmp1, width, height, true);
                    BitmapDrawable drawable = new BitmapDrawable(LinearLayoutAllViews.getResources(), bmp);
                    LinearLayoutAllViews.setBackground(drawable);
                }
                else
                {
                    AppLogger.getInstance().fail(String.format("BitmapFactory.decodeFile fail!"));
                }
            }catch(Exception e){
                AppLogger.getInstance().fail(String.format("Set background exception:%s", e.toString()));
            }
        }
    }

    public void setSwitchHardwareDecoderEvent(){
        switchHardwareDecoder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                engine.enableHardwareDecoder(b);
                AppLogger.getInstance().callApi("enableHardwareDecoder");
            }
        });
    }

    public void setCameraSwitchEvent(){
        switchCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                engine.enableCamera(b);
                AppLogger.getInstance().callApi("enableCamera, enable:%b", b);
            }
        });
    }
    // Set log component. It includes a pop-up dialog.
    public void setLogComponent(){
        logLinearLayout logHiddenView = findViewById(R.id.logView);
        logHiddenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogView logview = new LogView(getApplicationContext());
                logview.show(getSupportFragmentManager(),null);
            }
        });
    }

    public void setMirrorModeEvent(){
        mirrorModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] options = getResources().getStringArray(R.array.mirrorMode);
                switch (options[position]) {
                    case "OnlyPreview":
                        engine.setVideoMirrorMode(ZegoVideoMirrorMode.ONLY_PREVIEW_MIRROR, ZegoPublishChannel.MAIN);
                        AppLogger.getInstance().callApi("Switch Exposure Mode: ContinuousAuto");
                        break;
                    case "OnlyPublish":
                        engine.setVideoMirrorMode(ZegoVideoMirrorMode.ONLY_PUBLISH_MIRROR, ZegoPublishChannel.MAIN);
                        AppLogger.getInstance().callApi("Switch Exposure Mode: Auto");
                        break;
                    case "Both":
                        engine.setVideoMirrorMode(ZegoVideoMirrorMode.BOTH_MIRROR, ZegoPublishChannel.MAIN);
                        AppLogger.getInstance().callApi("Switch Exposure Mode: Auto");
                        break;
                    case "None":
                        engine.setVideoMirrorMode(ZegoVideoMirrorMode.NO_MIRROR, ZegoPublishChannel.MAIN);
                        AppLogger.getInstance().callApi("Switch Exposure Mode: Auto");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setTakeSnapshotEvent(){
        takePublishSnapshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engine.takePublishStreamSnapshot(new IZegoPublisherTakeSnapshotCallback() {
                    @Override
                    public void onPublisherTakeSnapshotResult(int errorCode, Bitmap image) {
                        AppLogger.getInstance().e(String.format("onPlayerTakeSnapshotResult, error:%d", errorCode));
                        if(errorCode !=0)
                        {
                            return;
                        }

                        String targetPath = getApplicationContext().getExternalFilesDir(null) + "/images/";
                        AppLogger.getInstance().e(String.format("Save Bitmap, path:%s", targetPath));

                        File ff = new File(targetPath);
                        if(!ff.exists())
                        {
                            ff.mkdirs();
                        }

                        String filename; // declaration file name
                        Date date = new Date(System.currentTimeMillis());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        filename = sdf.format(date) + ".png";

                        File saveFile = new File(targetPath, filename);

                        try {
                            FileOutputStream saveImgOut = new FileOutputStream(saveFile);
                            image.compress(Bitmap.CompressFormat.PNG, 100, saveImgOut);
                            saveImgOut.flush();
                            saveImgOut.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });

        takePlaySnapshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engine.takePlayStreamSnapshot(snapshotStreamIDEdit.getText().toString(), new IZegoPlayerTakeSnapshotCallback() {
                    @Override
                    public void onPlayerTakeSnapshotResult(int errorCode, Bitmap image) {
                        AppLogger.getInstance().e(String.format("onPlayerTakeSnapshotResult, error:%d", errorCode));
                        if(errorCode != 0)
                        {
                            return;
                        }

                        String TargetPath = getApplicationContext().getExternalFilesDir(null) + "/images/";
                        Log.d("Save Bitmap", "Save Path=" + TargetPath);

                        File ff = new File(TargetPath);
                        if(!ff.exists())
                        {
                            ff.mkdirs();
                        }

                        String filename; // declaration file name
                        Date date = new Date(System.currentTimeMillis());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        filename = sdf.format(date) + ".png";

                        File saveFile = new File(TargetPath, filename);

                        try {
                            FileOutputStream saveImgOut = new FileOutputStream(saveFile);
                            image.compress(Bitmap.CompressFormat.PNG, 100, saveImgOut);
                            saveImgOut.flush();
                            saveImgOut.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void setEventHandler(){
        setStartPublishButtonEvent();
        setVideoConfigButtonEvent();
        setCameraSelectionSpinnerEvent();
        setCameraFocusSwitchEvent();
        setCameraExposureSwitchEvent();
        setCameraFocusSwitchEvent();
        setCameraFocusModeSpinnerEvent();
        setCameraExposureModeSpinnerEvent();
        setCameraExposureCompensationSeekBarEvent();
        setCameraZoomFactorSeekBarEvent();
        setSubjectSegmentationEvent();
        setEffectsBeautyEvent();
        setChosenFileButtonEvent();
        setSwitchHardwareDecoderEvent();
        setStartPlayButtonEvent();
        setCameraSwitchEvent();
        setPreviewTouchEvent();
        setMirrorModeEvent();
        setTakeSnapshotEvent();

        engine.setEventHandler(new IZegoEventHandler() {
            // The callback triggered when the room connection state changes.
            @Override
            public void onRoomStateChanged(String roomID, ZegoRoomStateChangedReason reason, int errorCode, JSONObject extendedData) {
                ZegoViewUtil.UpdateRoomState(roomState, reason);
            }

            // The callback triggered when the state of stream playing changes.
            @Override
            public void onPlayerStateUpdate(String streamID, ZegoPlayerState state, int errorCode, JSONObject extendedData) {
                super.onPlayerStateUpdate(streamID, state, errorCode, extendedData);
                // If the state is PLAYER_STATE_NO_PLAY and the errcode is not 0, it means that stream playing has failed and
                // no more retry will be attempted by the engine. At this point, the failure of stream playing can be indicated
                // on the UI of the App.
            }

            @Override
            public void onPlayerQualityUpdate(String streamID, ZegoPlayStreamQuality quality) {
                super.onPlayerQualityUpdate(streamID, quality);
                if (quality.isHardwareDecode) {
                    hardwareDecodeStateText.setText(String.format("On(%s)", streamID));
                } else {
                    hardwareDecodeStateText.setText(String.format("Off(%s)", streamID));
                }
            }

            @Override
            public void onPublisherStateUpdate(String streamID, ZegoPublisherState state, int errorCode, JSONObject extendedData) {
                super.onPublisherStateUpdate(streamID, state, errorCode, extendedData);
            }

            @Override
            public void onPublisherQualityUpdate(String streamID, ZegoPublishStreamQuality quality) {
                super.onPublisherQualityUpdate(streamID, quality);
                if (quality.isHardwareEncode) {
                    hardwareEncodeStateText.setText(String.format("On"));
                } else {
                    hardwareEncodeStateText.setText(String.format("Off"));
                }
            }

            @Override
            public void onPublisherVideoSizeChanged(int width, int height, ZegoPublishChannel channel) {
                super.onPublisherVideoSizeChanged(width, height, channel);
                AppLogger.getInstance().i(String.format("onPublisherVideoSizeChanged, %dx%d, channel:%d", width, height, channel.value()));
            }

            @Override
            public void onPlayerVideoSizeChanged(String streamID, int width, int height) {
                super.onPlayerVideoSizeChanged(streamID, width, height);

                AppLogger.getInstance().i(String.format("onPlayerVideoSizeChanged, %dx%d, streamID:%s", width, height, streamID));
            }

            @Override
            public void onVideoObjectSegmentationStateChanged(ZegoObjectSegmentationState state, ZegoPublishChannel channel, int errorCode) {
                super.onVideoObjectSegmentationStateChanged(state, channel, errorCode);

                AppLogger.getInstance().i(String.format("onVideoObjectSegmentationStateChanged, state:%s, channel:%s, errorCode:%d", state.toString(), channel.toString(), errorCode));
            }
        });
    }

    public void initOrinetation()
    {
//        if(appOrientationMode == ZegoOrientationMode.CUSTOM)
        {
            int rotateType = getIntent().getIntExtra("customRotateType", 0);
            ZegoVideoConfig config = engine.getVideoConfig();
            switch (rotateType) {
                case 0:
                    // Lock layout orientation to Portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    config.setEncodeResolution(360, 640);
                    engine.setAppOrientation(ZegoOrientation.ORIENTATION_0);
                    break;
                case 1:
                    // Lock layout orientation to landscape.
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    config.setEncodeResolution(640, 360);
                    engine.setAppOrientation(ZegoOrientation.ORIENTATION_90);
                    break;
                case 2:
                    // Allow automatic rotation
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(appOrientationMode != ZegoOrientationMode.CUSTOM){
            return;
        }

        ZegoVideoConfig config = engine.getVideoConfig();

        int widthPortrait,heightPortrait,widthLandscape,heightLandscape;
        if(config.encodeWidth >= config.encodeHeight){
            widthPortrait = config.encodeHeight;
            heightPortrait = config.encodeWidth;

            heightLandscape = config.encodeHeight;
            widthLandscape = config.encodeWidth;
        }else{
            widthPortrait = config.encodeWidth;
            heightPortrait = config.encodeHeight;

            heightLandscape = config.encodeWidth;
            widthLandscape = config.encodeHeight;
        }

        ZegoOrientation orientation = ZegoOrientation.ORIENTATION_0;
        if (Surface.ROTATION_0 == this.getWindowManager().getDefaultDisplay().getRotation()) {
            orientation = ZegoOrientation.ORIENTATION_0;
                    config.setEncodeResolution(widthPortrait, heightPortrait);
        } else if (Surface.ROTATION_180 == this.getWindowManager().getDefaultDisplay().getRotation()) {
            orientation = ZegoOrientation.ORIENTATION_180;
                    config.setEncodeResolution(widthPortrait, heightPortrait);
        } else if (Surface.ROTATION_270 == this.getWindowManager().getDefaultDisplay().getRotation()) {
            orientation = ZegoOrientation.ORIENTATION_270;
                    config.setEncodeResolution(widthLandscape, heightLandscape);
        } else if (Surface.ROTATION_90 == this.getWindowManager().getDefaultDisplay().getRotation()) {
            orientation = ZegoOrientation.ORIENTATION_90;
                    config.setEncodeResolution(widthLandscape, heightLandscape);
        }
        AppLogger.getInstance().i(String.format("onConfigurationChanged, orientation:%s", orientation.toString()));

        engine.setAppOrientation(orientation);
        engine.setVideoConfig(config);
    }
}
