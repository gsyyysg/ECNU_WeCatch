/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.ar.pro.AR.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetsCopyToSdcard {
    private Context mContext;

    public AssetsCopyToSdcard(Context context) {
        super();
        this.mContext = context;
    }

    /**
     * @param assetpath  asset下的路径
     * @param sdcardPath SDpath下保存路径
     */
    public void assetToSD(String assetpath, String sdcardPath) {
        AssetManager asset = mContext.getAssets();
        // 循环的读取asset下的文件，并且写入到SD卡
        String[] filenames = null;
        FileOutputStream out = null;
        InputStream in = null;
        try {
            filenames = asset.list(assetpath);
            if (filenames.length > 0) {
                // 说明是目录
                // 创建目录
                getDirectory(assetpath);

                for (String fileName : filenames) {
                    assetToSD(assetpath + "/" + fileName, sdcardPath + "/" + fileName);
                }
            } else {
                // 说明是文件，直接复制到SD卡
                File sdcardFile = new File(sdcardPath);
                String path = assetpath.substring(0, assetpath.lastIndexOf("/"));
                getDirectory(path);

                if (sdcardFile.exists()) {
                    sdcardFile.delete();
                }
                sdcardFile.createNewFile();
                // 将内容写入到文件中
                in = asset.open(assetpath);
                out = new FileOutputStream(sdcardFile);
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = in.read(buffer)) != -1) {
                    out.write(buffer, 0, byteCount);
                }
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (in != null) {
                    in.close();
                    in = null;
                }
                //                if (asset != null) {
                //                    asset.close();
                //                    asset = null;
                //                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // 分级建立文件夹
    private void getDirectory(String path) {
        // 对SDpath进行处理，分层级建立文件夹
        String[] s = path.split("/");
        String str = Environment.getExternalStorageDirectory().toString();
        for (int i = 0; i < s.length; i++) {
            str = str + "/" + s[i];
            File file = new File(str);
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }
}