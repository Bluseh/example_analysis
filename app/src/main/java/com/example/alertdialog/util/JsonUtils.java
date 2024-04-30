//���Ի�������һ��GSON��װ����
package com.example.alertdialog.util;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
  
/**
 * �������� {@code JSON} ���ݵĳ��÷����Ĺ����ࡣ
 * <p />
 * �ù�����ʹ�õ� {@code JSON} ת��������
 * {@code Google Gson}</a>�� �����ǹ������ʹ�ð�����
 *
 */
public class JsonUtils {
    /** �յ� {@code JSON} ���� - <pre>"{}"</pre>�� */
    public static final String EMPTY_JSON = "{}";
  
    /** �յ� {@code JSON} ����(����)���� - {@code "[]"}�� */
    public static final String EMPTY_JSON_ARRAY = "[]";
  
    /** Ĭ�ϵ� {@code JSON} ����/ʱ���ֶεĸ�ʽ��ģʽ�� */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss+hh:mm";
  
    /** {@code Google Gson} �� <pre>@Since</pre> ע�ⳣ�õİ汾�ų��� - {@code 1.0}�� */
    public static final double SINCE_VERSION_10 = 1.0d;
  
    /** {@code Google Gson} �� <pre>@Since</pre> ע�ⳣ�õİ汾�ų��� - {@code 1.1}�� */
    public static final double SINCE_VERSION_11 = 1.1d;
  
    /** {@code Google Gson} �� <pre>@Since</pre> ע�ⳣ�õİ汾�ų��� - {@code 1.2}�� */
    public static final double SINCE_VERSION_12 = 1.2d;
  
    /** {@code Google Gson} �� <pre>@Until</pre> ע�ⳣ�õİ汾�ų��� - {@code 1.0}�� */
    public static final double UNTIL_VERSION_10 = SINCE_VERSION_10;
  
    /** {@code Google Gson} �� <pre>@Until</pre> ע�ⳣ�õİ汾�ų��� - {@code 1.1}�� */
    public static final double UNTIL_VERSION_11 = SINCE_VERSION_11;
  
    /** {@code Google Gson} �� <pre>@Until</pre> ע�ⳣ�õİ汾�ų��� - {@code 1.2}�� */
    public static final double UNTIL_VERSION_12 = SINCE_VERSION_12;
  
    /**
     * <p>
     * <pre>JSONUtils</pre> instances should NOT be constructed in standard programming. Instead,
     * the class should be used as <pre>JSONUtils.fromJson("foo");</pre>.
     * </p>
     * <p>
     * This constructor is public to permit tools that require a JavaBean instance to operate.
     * </p>
     */
    public JsonUtils() {
        super();
    }
  
    /**
     * ��������Ŀ��������ָ������������ת���� {@code JSON} ��ʽ���ַ�����
     * <p />
     * <strong>�÷���ת����������ʱ�������׳��κ��쳣������������ʱ����ͨ���󷵻� <pre>"{}"</pre>�� ���ϻ�������󷵻� <pre>"[]"</pre>
     * </strong>
     *
     * @param target Ŀ�����
     * @param targetType Ŀ���������͡�
     * @param isSerializeNulls �Ƿ����л� {@code null} ֵ�ֶΡ�
     * @param version �ֶεİ汾��ע�⡣
     * @param datePattern �����ֶεĸ�ʽ��ģʽ��
     * @param excludesFieldsWithoutExpose �Ƿ��ų�δ��ע {@literal @Expose} ע����ֶΡ�
     * @return Ŀ������ {@code JSON} ��ʽ���ַ�����
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, boolean isSerializeNulls, Double version,
            String datePattern, boolean excludesFieldsWithoutExpose) {
        if (target == null) return EMPTY_JSON;
        GsonBuilder builder = new GsonBuilder();
        if (isSerializeNulls) builder.serializeNulls();
        if (version != null) builder.setVersion(version.doubleValue());
        if (isBlank(datePattern)) datePattern = DEFAULT_DATE_PATTERN;
        builder.setDateFormat(datePattern);
        if (excludesFieldsWithoutExpose) builder.excludeFieldsWithoutExposeAnnotation();
        return toJson(target, targetType, builder);
    }
  
    /**
     * ��������Ŀ�����ת���� {@code JSON} ��ʽ���ַ�����<strong>�˷���ֻ����ת����ͨ�� {@code JavaBean} ����</strong>
     * <ul>
     * <li>�÷���ֻ��ת������ {@literal @Expose} ע����ֶΣ�</li>
     * <li>�÷�������ת�� {@code null} ֵ�ֶΣ�</li>
     * <li>�÷�����ת������δ��ע���ѱ�ע {@literal @Since} ���ֶΣ�</li>
     * <li>�÷���ת��ʱʹ��Ĭ�ϵ� ����/ʱ�� ��ʽ��ģʽ - {@code yyyy-MM-dd HH:mm:ss SSS}��</li>
     * </ul>
     *
     * @param target Ҫת���� {@code JSON} ��Ŀ�����
     * @return Ŀ������ {@code JSON} ��ʽ���ַ�����
     * @since 1.0
     */
    public static String toJson(Object target) {
        return toJson(target, null, false, null, null, true);
    }
  
    /**
     * ��������Ŀ�����ת���� {@code JSON} ��ʽ���ַ�����<strong>�˷���ֻ����ת����ͨ�� {@code JavaBean} ����</strong>
     * <ul>
     * <li>�÷���ֻ��ת������ {@literal @Expose} ע����ֶΣ�</li>
     * <li>�÷�������ת�� {@code null} ֵ�ֶΣ�</li>
     * <li>�÷�����ת������δ��ע���ѱ�ע {@literal @Since} ���ֶΣ�</li>
     * </ul>
     *
     * @param target Ҫת���� {@code JSON} ��Ŀ�����
     * @param datePattern �����ֶεĸ�ʽ��ģʽ��
     * @return Ŀ������ {@code JSON} ��ʽ���ַ�����
     * @since 1.0
     */
    public static String toJson(Object target, String datePattern) {
        return toJson(target, null, false, null, datePattern, true);
    }
  
    /**
     * ��������Ŀ�����ת���� {@code JSON} ��ʽ���ַ�����<strong>�˷���ֻ����ת����ͨ�� {@code JavaBean} ����</strong>
     * <ul>
     * <li>�÷���ֻ��ת������ {@literal @Expose} ע����ֶΣ�</li>
     * <li>�÷�������ת�� {@code null} ֵ�ֶΣ�</li>
     * <li>�÷���ת��ʱʹ��Ĭ�ϵ� ����/ʱ�� ��ʽ��ģʽ - {@code yyyy-MM-dd HH:mm:ss SSS}��</li>
     * </ul>
     *
     * @param target Ҫת���� {@code JSON} ��Ŀ�����
     * @param version �ֶεİ汾��ע��({@literal @Since})��
     * @return Ŀ������ {@code JSON} ��ʽ���ַ�����
     * @since 1.0
     */
    public static String toJson(Object target, Double version) {
        return toJson(target, null, false, version, null, true);
    }
  
    /**
     * ��������Ŀ�����ת���� {@code JSON} ��ʽ���ַ�����<strong>�˷���ֻ����ת����ͨ�� {@code JavaBean} ����</strong>
     * <ul>
     * <li>�÷�������ת�� {@code null} ֵ�ֶΣ�</li>
     * <li>�÷�����ת������δ��ע���ѱ�ע {@literal @Since} ���ֶΣ�</li>
     * <li>�÷���ת��ʱʹ��Ĭ�ϵ� ����/ʱ�� ��ʽ��ģʽ - {@code yyyy-MM-dd HH:mm:ss SSS}��</li>
     * </ul>
     *
     * @param target Ҫת���� {@code JSON} ��Ŀ�����
     * @param excludesFieldsWithoutExpose �Ƿ��ų�δ��ע {@literal @Expose} ע����ֶΡ�
     * @return Ŀ������ {@code JSON} ��ʽ���ַ�����
     * @since 1.0
     */
    public static String toJson(Object target, boolean excludesFieldsWithoutExpose) {
        return toJson(target, null, false, null, null, excludesFieldsWithoutExpose);
    }
  
    /**
     * ��������Ŀ�����ת���� {@code JSON} ��ʽ���ַ�����<strong>�˷���ֻ����ת����ͨ�� {@code JavaBean} ����</strong>
     * <ul>
     * <li>�÷�������ת�� {@code null} ֵ�ֶΣ�</li>
     * <li>�÷���ת��ʱʹ��Ĭ�ϵ� ����/ʱ�� ��ʽ��ģʽ - {@code yyyy-MM-dd HH:mm:ss SSS}��</li>
     * </ul>
     *
     * @param target Ҫת���� {@code JSON} ��Ŀ�����
     * @param version �ֶεİ汾��ע��({@literal @Since})��
     * @param excludesFieldsWithoutExpose �Ƿ��ų�δ��ע {@literal @Expose} ע����ֶΡ�
     * @return Ŀ������ {@code JSON} ��ʽ���ַ�����
     * @since 1.0
     */
    public static String toJson(Object target, Double version, boolean excludesFieldsWithoutExpose) {
        return toJson(target, null, false, version, null, excludesFieldsWithoutExpose);
    }
  
    /**
     * ��������Ŀ�����ת���� {@code JSON} ��ʽ���ַ�����<strong>�˷���ͨ������ת��ʹ�÷��͵Ķ���</strong>
     * <ul>
     * <li>�÷���ֻ��ת������ {@literal @Expose} ע����ֶΣ�</li>
     * <li>�÷�������ת�� {@code null} ֵ�ֶΣ�</li>
     * <li>�÷�����ת������δ��ע���ѱ�ע {@literal @Since} ���ֶΣ�</li>
     * <li>�÷���ת��ʱʹ��Ĭ�ϵ� ����/ʱ�� ��ʽ��ģʽ - {@code yyyy-MM-dd HH:mm:ss SSSS}��</li>
     * </ul>
     *
     * @param target Ҫת���� {@code JSON} ��Ŀ�����
     * @param targetType Ŀ���������͡�
     * @return Ŀ������ {@code JSON} ��ʽ���ַ�����
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType) {
        return toJson(target, targetType, false, null, null, true);
    }
  
    /**
     * ��������Ŀ�����ת���� {@code JSON} ��ʽ���ַ�����<strong>�˷���ͨ������ת��ʹ�÷��͵Ķ���</strong>
     * <ul>
     * <li>�÷���ֻ��ת������ {@literal @Expose} ע����ֶΣ�</li>
     * <li>�÷�������ת�� {@code null} ֵ�ֶΣ�</li>
     * <li>�÷���ת��ʱʹ��Ĭ�ϵ� ����/ʱ�� ��ʽ��ģʽ - {@code yyyy-MM-dd HH:mm:ss SSSS}��</li>
     * </ul>
     *
     * @param target Ҫת���� {@code JSON} ��Ŀ�����
     * @param targetType Ŀ���������͡�
     * @param version �ֶεİ汾��ע��({@literal @Since})��
     * @return Ŀ������ {@code JSON} ��ʽ���ַ�����
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, Double version) {
        return toJson(target, targetType, false, version, null, true);
    }
  
    /**
     * ��������Ŀ�����ת���� {@code JSON} ��ʽ���ַ�����<strong>�˷���ͨ������ת��ʹ�÷��͵Ķ���</strong>
     * <ul>
     * <li>�÷�������ת�� {@code null} ֵ�ֶΣ�</li>
     * <li>�÷�����ת������δ��ע���ѱ�ע {@literal @Since} ���ֶΣ�</li>
     * <li>�÷���ת��ʱʹ��Ĭ�ϵ� ����/ʱ�� ��ʽ��ģʽ - {@code yyyy-MM-dd HH:mm:ss SSS}��</li>
     * </ul>
     *
     * @param target Ҫת���� {@code JSON} ��Ŀ�����
     * @param targetType Ŀ���������͡�
     * @param excludesFieldsWithoutExpose �Ƿ��ų�δ��ע {@literal @Expose} ע����ֶΡ�
     * @return Ŀ������ {@code JSON} ��ʽ���ַ�����
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, null, null, excludesFieldsWithoutExpose);
    }
  
    /**
     * ��������Ŀ�����ת���� {@code JSON} ��ʽ���ַ�����<strong>�˷���ͨ������ת��ʹ�÷��͵Ķ���</strong>
     * <ul>
     * <li>�÷�������ת�� {@code null} ֵ�ֶΣ�</li>
     * <li>�÷���ת��ʱʹ��Ĭ�ϵ� ����/ʱ�� ��ʽ��ģʽ - {@code yyyy-MM-dd HH:mm:ss SSS}��</li>
     * </ul>
     *
     * @param target Ҫת���� {@code JSON} ��Ŀ�����
     * @param targetType Ŀ���������͡�
     * @param version �ֶεİ汾��ע��({@literal @Since})��
     * @param excludesFieldsWithoutExpose �Ƿ��ų�δ��ע {@literal @Expose} ע����ֶΡ�
     * @return Ŀ������ {@code JSON} ��ʽ���ַ�����
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, Double version, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, version, null, excludesFieldsWithoutExpose);
    }
  
    /**
     * �������� {@code JSON} �ַ���ת����ָ�������Ͷ���
     *
     * @param <T> Ҫת����Ŀ�����͡�
     * @param json ������ {@code JSON} �ַ�����
     * @param token {@code com.google.gson.reflect.TypeToken} ������ָʾ�����
     * @param datePattern ���ڸ�ʽģʽ��
     * @return ������ {@code JSON} �ַ�����ʾ��ָ�������Ͷ���
     * @since 1.0
     */
    public static <T> T fromJson(String json, TypeToken<T> token, String datePattern) {
        if (isBlank(json)) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        if (isBlank(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);
        Gson gson = builder.create();
        try {
            return gson.fromJson(json, token.getType());
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(json + " �޷�ת��Ϊ " + token.getRawType().getName() + " ����!", ex.getMessage());
            return null;
        }
    }
  
    /**
     * �������� {@code JSON} �ַ���ת����ָ�������Ͷ���
     *
     * @param <T> Ҫת����Ŀ�����͡�
     * @param json ������ {@code JSON} �ַ�����
     * @param token {@code com.google.gson.reflect.TypeToken} ������ָʾ�����
     * @return ������ {@code JSON} �ַ�����ʾ��ָ�������Ͷ���
     * @since 1.0
     */
    public static <T> T fromJson(String json, TypeToken<T> token) {
        return fromJson(json, token, "yyyy-MM-dd HH:mm:ss");
    }
  
    /**
     * �������� {@code JSON} �ַ���ת����ָ�������Ͷ���<strong>�˷���ͨ������ת����ͨ�� {@code JavaBean} ����</strong>
     *
     * @param <T> Ҫת����Ŀ�����͡�
     * @param json ������ {@code JSON} �ַ�����
     * @param clazz Ҫת����Ŀ���ࡣ
     * @param datePattern ���ڸ�ʽģʽ��
     * @return ������ {@code JSON} �ַ�����ʾ��ָ�������Ͷ���
     * @since 1.0
     */
    public static <T> T fromJson(String json, Class<T> clazz, String datePattern) {
        if (isBlank(json)) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        if (isBlank(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);
        Gson gson = builder.create();
        T t;
        try {
        	t = gson.fromJson(json, clazz);
        	return t;
            //return gson.fromJson(json, clazz);
        } catch (Exception ex) {
            Log.e(json + " �޷�ת��Ϊ " + clazz.getName() + " ����!", ex.getMessage());
    		System.out.println(""+ex.getMessage());
            return null;
        }
    }
  
    /**
     * �������� {@code JSON} �ַ���ת����ָ�������Ͷ���<strong>�˷���ͨ������ת����ͨ�� {@code JavaBean} ����</strong>
     *
     * @param <T> Ҫת����Ŀ�����͡�
     * @param json ������ {@code JSON} �ַ�����
     * @param clazz Ҫת����Ŀ���ࡣ
     * @return ������ {@code JSON} �ַ�����ʾ��ָ�������Ͷ���
     * @since 1.0
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(json, clazz, null);
    }
  
    /**
     * ��������Ŀ��������{@code GsonBuilder} ��ָ������������ת���� {@code JSON} ��ʽ���ַ�����
     * <p />
     * �÷���ת����������ʱ�������׳��κ��쳣������������ʱ��{@code JavaBean} ���󷵻� <pre>"{}"</pre>�� ���ϻ�������󷵻�
     * <pre>"[]"</pre>�� �䱾�������ͣ�������Ӧ�Ļ���ֵ��
     *
     * @param target Ŀ�����
     * @param targetType Ŀ���������͡�
     * @param builder �ɶ��Ƶ�{@code Gson} ��������
     * @return Ŀ������ {@code JSON} ��ʽ���ַ�����
     * @since 1.1
     */
    public static String toJson(Object target, Type targetType, GsonBuilder builder) {
        if (target == null) return EMPTY_JSON;
        Gson gson = null;
        if (builder == null) {
            gson = new Gson();
        } else {
            gson = builder.create();
        }
        String result = EMPTY_JSON;
        try {
            if (targetType == null) {
                result = gson.toJson(target);
            } else {
                result = gson.toJson(target, targetType);
            }
        } catch (Exception ex) {
            Log.w("Ŀ����� " + target.getClass().getName() + " ת�� JSON �ַ���ʱ�������쳣��", ex.getMessage());
            if (target instanceof Collection<?> || target instanceof Iterator<?> || target instanceof Enumeration<?>
                    || target.getClass().isArray()) {
                result = EMPTY_JSON_ARRAY;
            }
        }
        return result;
    }
     
    private static boolean isBlank(String text) {
        return null == text || "".equals(text.trim());
    }
}