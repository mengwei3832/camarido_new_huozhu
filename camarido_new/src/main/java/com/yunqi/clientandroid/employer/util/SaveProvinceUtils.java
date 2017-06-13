package com.yunqi.clientandroid.employer.util;

import android.os.Environment;
import android.util.Log;

import com.yunqi.clientandroid.employer.entity.GetProvince;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
/**
 * 保存和获取省市区
 * Created by mengwei on 2017/5/23.
 */

public class SaveProvinceUtils {
    // 应用缓存文件
    private static final File CACHE_FILE = new File(Environment
            .getExternalStorageDirectory()+"/yqtms");


    /**
     * 保存对象
     *
     * @param ser
     * @param fileName
     * @throws IOException
     */
    public static boolean saveObjectList(ArrayList<GetProvince> ser, String fileName) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            if (!CACHE_FILE.exists()) {
                CACHE_FILE.mkdir();
            }
            File file = new File(CACHE_FILE + "/" + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            oos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 读取对象
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static ArrayList<GetProvince> readObjectList(String fileName) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        if (!CACHE_FILE.exists()) {
            CACHE_FILE.mkdir();
        }
        File file = new File(CACHE_FILE + "/" + fileName);
        Log.e("TAG",file.getAbsolutePath());
        try {
            if (!file.exists()) {
                // 没有聊天记录返回空集合
                return new ArrayList<>();
            }
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            return (ArrayList<GetProvince>) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            //反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                file.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }
}
