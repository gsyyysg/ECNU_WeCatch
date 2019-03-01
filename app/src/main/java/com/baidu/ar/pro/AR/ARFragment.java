/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ar.pro.AR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baidu.ar.ARController;
import com.baidu.ar.DuMixSource;
import com.baidu.ar.DuMixTarget;
import com.baidu.ar.bean.ARConfig;
import com.baidu.ar.pro.AR.callback.PreviewCallback;
import com.baidu.ar.pro.AR.callback.PromptCallback;
import com.baidu.ar.pro.AR.camera.ARCameraCallback;
import com.baidu.ar.pro.AR.camera.ARCameraManager;
import com.baidu.ar.pro.AR.camera.ARStartCameraCallback;
import com.baidu.ar.pro.AR.camera.ARSwitchCameraCallback;
import com.baidu.ar.pro.AR.draw.ARRenderer;
import com.baidu.ar.pro.AR.draw.GLConfigChooser;
import com.baidu.ar.pro.AR.ui.Prompt;
import com.baidu.ar.pro.AR.view.ARControllerManager;
import com.baidu.ar.pro.R;
import com.baidu.ar.pro.draw.ARRenderCallback;
import com.baidu.ar.recg.CornerPoint;
import com.baidu.ar.recg.CornerPointController;
import com.baidu.ar.recg.ImgRecognitionClient;
import com.baidu.ar.util.SystemInfoUtil;
import com.baidu.ar.util.UiThreadUtil;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * AR Fragment
 * Created by xiegaoxi on 2017/11/21.
 */
public class ARFragment extends Fragment {

    private static final String TAG = "ARFragment";

    /**
     * Fragment 真正的容器
     */
    private FrameLayout mFragmentContainer;

    /**
     * Ar RootView
     */
    private View mRootView;

    /**
     * AR View
     */
    private GLSurfaceView mArGLSurfaceView;

    /**
     * Prompt View 提示层View
     */
    private Prompt mPromptUi;

    /**
     * AR Renderer
     */
    private ARRenderer mARRenderer;

    /**
     * AR相机管理
     */
    private ARCameraManager mARCameraManager;

    /**
     * 最长录制最长时间设置
     */
    private int mRecordMaxTime = 10 * 1000;
    /**
     * 需要手动申请的权限
     */
    private static final String[] ALL_PERMISSIONS = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};

    /**
     * ar sdk 接口ARController
     */
    private ARController mARController;
    /**
     * AR输入参数类
     */
    private DuMixSource mDuMixSource;
    /**
     * 返回参数类
     */
    private DuMixTarget mDuMixTarget;

    /**
     * 本地识图 云端识图扫描
     */
    private ImgRecognitionClient mImgRecognitionClient;

    /**
     * 扫描云点控制器
     */
    private CornerPointController mCornerPointController;

    private int mCameraPriWidth = 1280;
    private int mCameraPriHeight = 720;

    // 接收 参数
    private String mArKey;
    private int mArTpye;
    private String mArFile;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        // 进入时需要先配置环境
        super.onCreate(savedInstanceState);
        // 创建 Fragment root view
        mFragmentContainer = new FrameLayout(getActivity());
        // 初始化ARConfig
        Bundle bundle = getArguments();
        if (bundle != null) {
            mArKey = bundle.getString(Config.AR_KEY);
            mArTpye = bundle.getInt(Config.AR_TYPE);
            mArFile = bundle.getString(Config.AR_FILE);
            ARConfig.setARPath(mArFile);
        }
        mARCameraManager = new ARCameraManager();

        // 6.0以下版本直接同意使用权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            setupViews();
        } else {
            if (hasNecessaryPermission()) {
                setupViews();
            } else {
                requestPermissions(ALL_PERMISSIONS, 1123);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mFragmentContainer;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mARController != null) {
            mARController.resume();
            mARController.onAppear();
        }
        if (hasNecessaryPermission()) {
            // 打开相机
            startCamera();
        }
        if (mPromptUi != null) {
            mPromptUi.resume();
        }
    }

    private boolean hasNecessaryPermission() {
        List<String> permissionsList = new ArrayList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : ALL_PERMISSIONS) {
                if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(permission);
                }
            }
        }
        return permissionsList.size() == 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mARController != null) {
            mARController.pause();
        }
        if (mPromptUi != null) {
            mPromptUi.pause();
        }
        mARCameraManager.stopCamera(null, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPromptUi != null) {
            mPromptUi.release();
            mPromptUi = null;
        }
        if (mARRenderer != null) {
            mARRenderer.release();
            mARRenderer = null;
        }

        if (mCornerPointController != null) {
            mCornerPointController.release();
        }

        ARControllerManager.getInstance(getActivity()).release();

        if (mARController != null) {
            mARController.release();
            mARController = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1123) {
            setupViews();
        }
    }

    public boolean onFragmentBackPressed() {
        quit();
        return true;
    }

    /**
     * 退出逻辑
     * 1. 先关闭相机
     * 2. 通知外部回调
     * 3. 释放资源
     */
    private void quit() {
        if (mARCameraManager != null) {
            mARCameraManager.stopCamera(new ARCameraCallback() {
                @Override
                public void onResult(final boolean result) {
                    try {
                        UiThreadUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result) {
                                    if (getActivity() != null) {
                                        getActivity().finish();
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, false);
        }
    }

    private void setupViews() {

        mRootView = getActivity().getLayoutInflater().inflate(R.layout.bdar_layout_arui, null);

        mArGLSurfaceView = mRootView.findViewById(R.id.bdar_view);
        mArGLSurfaceView.setEGLContextClientVersion(2);
        mARRenderer = new ARRenderer(isScreenOrientationLandscape(mRootView.getContext()));
        mARRenderer.setARFrameListener(
                new SurfaceTexture.OnFrameAvailableListener() {
                    @Override
                    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                        mArGLSurfaceView.requestRender();
                    }
                }
        );
        mArGLSurfaceView.setEGLConfigChooser(new GLConfigChooser());
        mArGLSurfaceView.setRenderer(mARRenderer);
        mArGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        mPromptUi = mRootView.findViewById(R.id.bdar_prompt_view);
        mPromptUi.setPromptCallback(promptCallback);
        mFragmentContainer.addView(mRootView);
    }

    /**
     * 开始打开相机
     */
    private void startCamera() {
        SurfaceTexture surfaceTexture = mARRenderer.getCameraTexture();
        mARCameraManager.startCamera(surfaceTexture, new ARStartCameraCallback() {
            @Override
            public void onCameraStart(boolean result, SurfaceTexture surfaceTexture, int width, int height) {
                if (result) {
                    if (mARController == null) {
                        showArView();
                    }

                }
            }
        });
    }

    /**
     * 开始渲染AR View
     */
    public void showArView() {
        mARController = ARControllerManager.getInstance(getActivity()).getArController();
        mArGLSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mARController != null) {
                    return mARController.onTouchEvent(motionEvent);
                }
                return false;
            }
        });
        mARCameraManager.setPreviewCallback(new PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, int width, int height) {
                if (mARController != null) {
                    mARController.onCameraPreviewFrame(data, width, height);
                }
                if (mImgRecognitionClient != null) {
                    byte[] rotatedData = rotateImage(data, mCameraPriWidth, mCameraPriHeight);
                    CornerPoint[] cornerPoints = mImgRecognitionClient.extractCornerPoints
                            (rotatedData, mCameraPriHeight, mCameraPriWidth);
                    if (mPromptUi != null) {
                        mPromptUi.setCornerPoint(cornerPoints);
                    }
                }
            }
        });

        mARRenderer.setARRenderCallback(new ARRenderCallback() {
            @Override
            public void onCameraDrawerCreated(SurfaceTexture surfaceTexture, int width, int height) {
                mDuMixSource = new DuMixSource(surfaceTexture, width, height);
                if (TextUtils.isEmpty(mArFile)) {
                    mDuMixSource.setArKey(mArKey);
                    mDuMixSource.setArType(mArTpye);
                } else {
                    // TODO: 2018/6/13 本地case路径加载
                    mDuMixSource.setArType(mArTpye);
                    mDuMixSource.setResFilePath(mArFile);
                }
                mPromptUi.setDuMixSource(mDuMixSource);

            }

            @Override
            public void onARDrawerCreated(SurfaceTexture surfaceTexture, SurfaceTexture.OnFrameAvailableListener
                    arFrameListener, int width, int height) {
                if (isScreenOrientationLandscape(getContext())) {
                    mDuMixTarget = new DuMixTarget(surfaceTexture, arFrameListener, height, width, true);
                } else {
                    mDuMixTarget = new DuMixTarget(surfaceTexture, arFrameListener, width, height, true);
                }

                if (mDuMixSource != null) {
                    mDuMixSource.setCameraSource(null);
                }

                if (mARController != null) {
                    mARController.setup(mDuMixSource, mDuMixTarget, mPromptUi.getDuMixCallback());
                    // todo update 需要封装此函数
                    mARController.resume();
                }
            }

            @Override
            public void onARDrawerChanged(SurfaceTexture surfaceTexture, int width, int height) {
                if (mARController != null) {
                    if (SystemInfoUtil.isScreenOrientationLandscape(getContext())) {
                        mARController.reSetup(surfaceTexture, height, width);
                    } else {
                        mARController.reSetup(surfaceTexture, width, height);
                    }
                }
            }
        });
        if (mArTpye == 6 || mArTpye == 7) {
            mCornerPointController = new CornerPointController();
            if (mCornerPointController != null) {
                mImgRecognitionClient = mCornerPointController.getImgRecognitionClient();
                mPromptUi.initPreviewScreenScale(mCameraPriWidth, mCameraPriHeight);
                mPromptUi.setPointViewVisible(true);
            }
        }
    }

    /**
     * 判断屏幕是否是横屏状态
     *
     * @param context
     */
    public boolean isScreenOrientationLandscape(Context context) {
        // 获取设置的配置信息
        Configuration cf = context.getResources().getConfiguration();
        // 获取屏幕方向
        int ori = cf.orientation;
        if (ori == cf.ORIENTATION_LANDSCAPE) {
            return true;
        }
        return false;
    }

    /**
     * prompt ui callback
     */
    PromptCallback promptCallback = new PromptCallback() {
        @Override
        public void onCameraFlashStatus(boolean open) {
            if (open) {
                mARCameraManager.openFlash(null);
            } else {
                mARCameraManager.closeFlash(null);
            }
        }

        @Override
        public void onSwitchCamera() {
            mARCameraManager.switchCamera(new ARSwitchCameraCallback() {
                @Override
                public void onCameraSwitch(boolean result, boolean rear) {
                    if (mARController != null) {
                        mARController.switchCamera(!rear);
                    }
                }
            });
        }

        @Override
        public void onBackPressed() {
            getActivity().onBackPressed();
        }

        @Override
        public void onChangeCase(String key, int type) {
            if (mARController != null) {
                mARController.switchCase(key, type);
            }
        }

        @Override
        public void onTackPicture() {
        }

        @Override
        public void onStartRecord() {
        }

        @Override
        public void onStopRecord() {
        }

        @Override
        public void onCaseChange() {
        }
    };
    /**
     * 图片旋转90 度
     *
     * @param data
     * @param width
     * @param height
     *
     * @return
     */
    private byte[] rotateImage(byte[] data, int width, int height) {
        byte[] rotatedData = null;
        try {
            rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    rotatedData[x * height + height - y - 1] = data[x + y * width];
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return rotatedData;
    }

}
