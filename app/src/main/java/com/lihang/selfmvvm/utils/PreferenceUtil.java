package com.lihang.selfmvvm.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lihang.selfmvvm.vo.res.UserInfoVo;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class PreferenceUtil {

    public static final String FILE_NAME = "leo_pro";

    private PreferenceUtil() {

    }

    /**
     * 这里是对基本数据类型进行的操作
     */
    /*
     * 这里是保存基本数据类型 -- 表名是上面设置的FILE_NAME
     * */
    public static void put(String key, Object object) {

        SharedPreferences sp = Utils.getApp().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /*
     * 这里是根据key，获取数据。表名是 -- FILE_NAME
     * 第二个参数是  默认值
     * */
    public static Object get(String key, Object defaultObject) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 保存 list
     *
     * @param tag
     * @param datalist
     */
    public static void setDataList(String tag, List<UserInfoVo> datalist) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();
    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public static List<UserInfoVo> getDataList(String tag) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        List<UserInfoVo> datalist = new ArrayList<>();
        String strJson = sp.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<UserInfoVo>>() {
        }.getType());
        return datalist;
    }


    /*
     * 根据某个key值获取数据  表名 -- FILE_NAME
     * */
    public static void remove(String key) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /*
     * 清楚 表名 -- FILE_NAME 里所有的数据
     * */
    public static void clear() {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /*
     * 判断当前key值 是否存在于  表名--FILE_NAME 表里
     * */
    public static boolean contains(String key) {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }


    /*
     * 返回表名 -- FILE_NAME里所有的数据，以Map键值对的方式
     * */
    public static Map<String, ?> getAll() {
        SharedPreferences sp = Utils.getApp().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    public static void putStrListValue(Context context, String key, List<String> strList) {
        if (null == strList) {
            return;
        }
        int size = strList.size();
        putIntValue(context, key + "size", size);
        for (int i = 0; i < size; i++) {
            putStringValue(context, key + i, strList.get(i));
        }
    }

    public static List<String> getStrListValue(Context context, String key) {
        List<String> strList = new ArrayList<String>();
        int size = getIntValue(context, key + "size", 0);
        //Log.d("sp", "" + size);
        for (int i = 0; i < size; i++) {
            strList.add(getStringValue(context, key + i, null));
        }
        return strList;
    }

    public static int getIntValue(Context context, String s, int i) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        int value = sp.getInt(s, i);
        return value;
    }

    public static String getStringValue(Context context, String s, String o) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String value = sp.getString(s, o);
        return value;
    }

    public static void putIntValue(Context context, String s, int size) {
        SharedPreferences.Editor sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
        sp.putInt(s, size);
        sp.commit();
    }

    public static void putStringValue(Context context, String s, String s1) {
        SharedPreferences.Editor sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
        sp.putString(s, s1);
        sp.commit();
    }

    public static void removeStrList(Context context, String key) {
        int size = getIntValue(context, key + "size", 0);
        if (0 == size) {
            return;
        }
        removeKey(context, key + "size");
        for (int i = 0; i < size; i++) {
            removeKey(context, key + i);
        }
    }

    public static void removeKey(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

    /**
     * 以下是保存类的方式，跟上面的FILE_NAME表不在一个表里
     */
    /*
     * 保存类，当前SharedPreferences以 class类名被表名
     * */
    public static <T extends Serializable> boolean putByClass(String key, T entity) {
        if (entity == null) {
            return false;
        }
        String prefFileName = entity.getClass().getName();
        SharedPreferences sp = Utils.getApp().getSharedPreferences(prefFileName, 0);
        SharedPreferences.Editor et = sp.edit();
        String json = GsonUtil.ser(entity);
        et.putString(key, json);
        return et.commit();
    }


    /*
     * 获取某个以class 为表名的。所有class 对象
     * */
    public static <T extends Serializable> List<T> getAllByClass(Class<T> clazz) {
        String prefFileName = clazz.getName();
        SharedPreferences sp = Utils.getApp().getSharedPreferences(prefFileName, 0);
        Map<String, String> values = (Map<String, String>) sp.getAll();
        List<T> results = new ArrayList<T>();
        if (values == null || values.isEmpty())
            return results;
        Collection<String> colles = values.values();
        for (String json : colles) {
            results.add(GsonUtil.deser(json, clazz));
        }
        return results;
    }

    /*
     * 获取以类名为表名的，某个key值上的value
     * 第二个参数是，类名class,也就是当前的表名
     * */
    public static <T extends Serializable> T getByClass(String key, Class<T> clazz) {
        String prefFileName = clazz.getName();
        SharedPreferences sp = Utils.getApp().getSharedPreferences(prefFileName, 0);

        String json = sp.getString(key, null);
        if (json == null) {
            return null;
        }
        return GsonUtil.deser(json, clazz);
    }

    /*
     * 在以类名为表名的表上，移除key值
     * 第二个参数是，类名class,也就是当前的表名
     * */
    public static <T extends Serializable> void removeByClass(String key, Class<T> clazz) {
        String prefFileName = clazz.getName();
        SharedPreferences sp = Utils.getApp().getSharedPreferences(prefFileName, 0);
        if (sp.contains(key)) {
            sp.edit().remove(key).commit();
        }
    }

    /*
     * 移除 某个以类名为表名上的所有的值
     * */
    public static <T extends Serializable> void clearByClass(Class<T> clazz) {
        String prefFileName = clazz.getName();
        SharedPreferences sp = Utils.getApp().getSharedPreferences(prefFileName, 0);
        sp.edit().clear().commit();
    }

}
