/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ar.pro.AR.module;

import java.util.HashMap;

import com.baidu.ar.ARController;
import com.baidu.ar.util.UiThreadUtil;
import com.baidu.ar.arplay.util.MsgParamsUtil;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Module 控制器
 */
public class Module {

    private Context mContext;

    private ARController mARController;

    private RelativeLayout mPluginContainer;

    // 业务层自定义参数 接收lua数据
    private int mSource;

    public Module(Context context, ARController arController) {
        mContext = context;
        mARController = arController;
    }

    public void parseLuaMessage(HashMap<String, Object> luaMsg) {
        if (null != luaMsg) {
            int id = MsgParamsUtil.obj2Int(luaMsg.get("id"), -1);
            switch (id) {
                case MsgType.MSG_TYPE_TTS_SPEAK:
                case MsgType.MSG_TYPE_TTS_STOP:
                case MsgType.MSG_TYPE_TTS_PAUSE:
                case MsgType.MSG_TYPE_TTS_RESUME:
                case MsgType.MSG_TYPE_VOICE_START:
                case MsgType.MSG_TYPE_VOICE_CLOSE:
                case MsgType.MSG_TYPE_VOICE_SHOW_MIC_ICON:
                case MsgType.MSG_TYPE_VOICE_HIDE_MIC_ICON:
                case MsgType.MSG_TYPE_THIRD:
                    final int source = MsgParamsUtil.obj2Int(luaMsg.get("source"), 0);
                    mSource += source;
                    UiThreadUtil.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(mContext, mSource + "", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case MsgType.MSG_TYPE_LOGO_START:
                case MsgType.MSG_TYPE_LOGO_STOP:
                default:
                    break;
            }
        }
    }

    public void onRelease() {
        mContext = null;
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void setPluginContainer(RelativeLayout pluginContainer) {
        mPluginContainer = pluginContainer;
    }

}
