package cn.thinking;

import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;

import cn.thinkingdata.analytics.TDAnalytics;
import cn.thinkingdata.analytics.TDAnalyticsAPI;
import cn.thinkingdata.analytics.TDConfig;
import cn.thinkingdata.analytics.TDPresetProperties;
import cn.thinkingdata.analytics.model.TDFirstEventModel;
import cn.thinkingdata.analytics.model.TDOverWritableEventModel;
import cn.thinkingdata.analytics.model.TDUpdatableEventModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class RNThinkingAnalyticsModule extends ReactContextBaseJavaModule {

    public RNThinkingAnalyticsModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    private static final String MODULE_NAME = "RNThinkingAnalyticsModule";

    private boolean isEnableAutoTrack = false;
    private boolean isEnablePageView = false;
    private boolean isEnableViewClick = false;
    private final List<JSONObject> previewPageViewList = new ArrayList<>();
    private String lastScreenName;
    private String mCurrentScreenName;
    private String mCurrentTitle;
    private Map<Integer, TDViewProperties> viewPropertiesMap = new HashMap<>();

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    private JSONObject convertToJSONObject(ReadableMap properties) {
        if (properties == null) {
            return null;
        }

        JSONObject json = new JSONObject();
        try {
            json = new JSONObject(properties.toString()).getJSONObject("NativeMap");
        } catch (Exception e) {
            try {
                json = new JSONObject(properties.toString());
            } catch (Exception e1) {

            }
        }
        return json;
    }

    private TimeZone getTimeZone(double timeZoneOffset) {
        if (timeZoneOffset < -12 || timeZoneOffset > 14) {
            return TimeZone.getDefault();
        }
        int hour = ( int ) timeZoneOffset;
        int minute = ( int ) Math.round((timeZoneOffset - hour) * 60);
        if (minute >= 60) {
            hour += 1;
            minute -= 60;
        } else if (minute <= -60) {
            hour -= 1;
            minute += 60;
        }
        String timeZoneId = String.format(Locale.ROOT,
                hour >= 0 ? "GMT+%02d:%02d" : "GMT%02d:%02d",
                hour,
                Math.abs(minute)
        );
        return TimeZone.getTimeZone(timeZoneId);
    }

    @ReactMethod
    public void init(ReadableMap readableMap, String libVersion) {
        try {
            String appId = readableMap.getString("appId");
            String serverUrl = readableMap.getString("serverUrl");
            if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(serverUrl)) return;
            TDConfig config = TDConfig.getInstance(getReactApplicationContext(), appId, serverUrl);
            if (readableMap.hasKey("timeZone")) {
                double timeZone = readableMap.getDouble("timeZone");
                config.setDefaultTimeZone(getTimeZone(timeZone));
            }
            if (readableMap.hasKey("mode")) {
                String mode = readableMap.getString("mode");
                if (TextUtils.equals(mode, "debug")) {
                    config.setMode(TDConfig.TDMode.DEBUG);
                } else if (TextUtils.equals(mode, "debugOnly")) {
                    config.setMode(TDConfig.TDMode.DEBUG_ONLY);
                }
            }
            if (readableMap.hasKey("enableEncrypt") && readableMap.hasKey("secretKey")) {
                boolean enableEncrypt = readableMap.getBoolean("enableEncrypt");
                if (enableEncrypt) {
                    ReadableMap secretKey = readableMap.getMap("secretKey");
                    if (secretKey != null) {
                        config.enableEncrypt(secretKey.getInt("version"), secretKey.getString("publicKey"));
                    }
                }
            }
            if (readableMap.hasKey("enableLog")) {
                boolean enableLog = readableMap.getBoolean("enableLog");
                TDAnalytics.enableLog(enableLog);
            }
            TDAnalytics.setCustomerLibInfo("ReactNative", libVersion);
            TDAnalytics.init(config);
        } catch (Exception ignore) {
        }
    }


    @ReactMethod
    public void track(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            String eventName = readableMap.getString("eventName");
            JSONObject properties = convertToJSONObject(readableMap.getMap("properties"));
            Date date = null;
            TimeZone timeZone = null;
            if (readableMap.hasKey("time")) {
                long time = ( long ) readableMap.getDouble("time");
                if (time > 0) {
                    date = new Date(time);
                }
            }
            if (readableMap.hasKey("timeZone")) {
                double zoneOffset = readableMap.getDouble("timeZone");
                timeZone = getTimeZone(zoneOffset);
            }
            if (date != null && timeZone != null) {
                TDAnalyticsAPI.track(eventName, properties, date, timeZone, appId);
            } else {
                TDAnalyticsAPI.track(eventName, properties, appId);
            }
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void trackUpdate(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            String eventName = readableMap.getString("eventName");
            JSONObject properties = convertToJSONObject(readableMap.getMap("properties"));
            String eventId = readableMap.getString("eventId");
            Date date = null;
            TimeZone timeZone = null;
            if (readableMap.hasKey("time")) {
                long time = ( long ) readableMap.getDouble("time");
                if (time > 0) {
                    date = new Date(time);
                }
            }
            if (readableMap.hasKey("timeZone")) {
                double zoneOffset = readableMap.getDouble("timeZone");
                timeZone = getTimeZone(zoneOffset);
            }
            TDUpdatableEventModel model = new TDUpdatableEventModel(eventName, properties, eventId);
            if (date != null && timeZone != null) {
                model.setEventTime(date, timeZone);
            }
            TDAnalyticsAPI.track(model, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void trackOverwrite(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            String eventName = readableMap.getString("eventName");
            JSONObject properties = convertToJSONObject(readableMap.getMap("properties"));
            String eventId = readableMap.getString("eventId");
            Date date = null;
            TimeZone timeZone = null;
            if (readableMap.hasKey("time")) {
                long time = ( long ) readableMap.getDouble("time");
                if (time > 0) {
                    date = new Date(time);
                }
            }
            if (readableMap.hasKey("timeZone")) {
                double zoneOffset = readableMap.getDouble("timeZone");
                timeZone = getTimeZone(zoneOffset);
            }
            TDOverWritableEventModel model = new TDOverWritableEventModel(eventName, properties, eventId);
            if (date != null && timeZone != null) {
                model.setEventTime(date, timeZone);
            }
            TDAnalyticsAPI.track(model, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void trackFirstEvent(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            String eventName = readableMap.getString("eventName");
            JSONObject properties = convertToJSONObject(readableMap.getMap("properties"));
            String eventId = readableMap.getString("eventId");
            Date date = null;
            TimeZone timeZone = null;
            if (readableMap.hasKey("time")) {
                long time = ( long ) readableMap.getDouble("time");
                if (time > 0) {
                    date = new Date(time);
                }
            }
            if (readableMap.hasKey("timeZone")) {
                double zoneOffset = readableMap.getDouble("timeZone");
                timeZone = getTimeZone(zoneOffset);
            }
            TDFirstEventModel model = new TDFirstEventModel(eventName, properties);
            model.setFirstCheckId(eventId);
            if (date != null && timeZone != null) {
                model.setEventTime(date, timeZone);
            }
            TDAnalyticsAPI.track(model, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void timeEvent(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            if (readableMap.hasKey("eventName")) {
                String eventName = readableMap.getString("eventName");
                TDAnalyticsAPI.timeEvent(eventName, appId);
            }
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void login(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            String loginId = readableMap.getString("loginId");
            TDAnalyticsAPI.login(loginId, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void logout(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            TDAnalyticsAPI.logout(appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void userSet(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            JSONObject properties = convertToJSONObject(readableMap.getMap("properties"));
            TDAnalyticsAPI.userSet(properties, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void userUnset(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            String property = readableMap.getString("property");
            TDAnalyticsAPI.userUnset(property, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void userSetOnce(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            JSONObject properties = convertToJSONObject(readableMap.getMap("properties"));
            TDAnalyticsAPI.userSetOnce(properties, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void userAdd(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            JSONObject properties = convertToJSONObject(readableMap.getMap("properties"));
            TDAnalyticsAPI.userAdd(properties, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void userDel(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            TDAnalyticsAPI.userDelete(appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void userAppend(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            JSONObject properties = convertToJSONObject(readableMap.getMap("properties"));
            TDAnalyticsAPI.userAppend(properties, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void setSuperProperties(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            JSONObject properties = convertToJSONObject(readableMap.getMap("properties"));
            TDAnalyticsAPI.setSuperProperties(properties, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void unsetSuperProperty(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            String property = readableMap.getString("property");
            TDAnalyticsAPI.unsetSuperProperty(property, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void clearSuperProperties(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            TDAnalyticsAPI.clearSuperProperties(appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void identify(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            String distinctId = readableMap.getString("distinctId");
            TDAnalyticsAPI.setDistinctId(distinctId, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void flush(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            TDAnalyticsAPI.flush(appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void enableAutoTrack(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            int types = ( int ) readableMap.getDouble("autoTrackType");
            JSONObject properties = convertToJSONObject(readableMap.getMap("properties"));
            TDAnalyticsAPI.enableAutoTrack(types, properties, appId);
            isEnableAutoTrack = true;
            if ((types & TDAnalytics.TDAutoTrackEventType.APP_VIEW_SCREEN) > 0) {
                isEnablePageView = true;
                for (JSONObject jsonObject : previewPageViewList) {
                    TDAnalytics.track("ta_app_view", jsonObject);
                }
                previewPageViewList.clear();
            } else {
                isEnablePageView = false;
                previewPageViewList.clear();
            }
            isEnableViewClick = (types & TDAnalytics.TDAutoTrackEventType.APP_CLICK) > 0;
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void calibrateTime(ReadableMap readableMap) {
        try {
            double timeStampMillis = readableMap.getDouble("timeStampMillis");
            TDAnalytics.calibrateTime(( long ) timeStampMillis);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void calibrateTimeWithNtp(ReadableMap readableMap) {
        try {
            String ntp_server = readableMap.getString("ntp_server");
            TDAnalytics.calibrateTimeWithNtp(ntp_server);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void enableThirdPartySharing(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            if (readableMap.hasKey("types")) {
                int types = ( int ) readableMap.getDouble("types");
                ReadableMap maps = readableMap.getMap("params");
                if (maps == null) {
                    TDAnalyticsAPI.enableThirdPartySharing(types, appId);
                } else {
                    TDAnalyticsAPI.enableThirdPartySharing(types, maps.toHashMap(), appId);
                }
            }
        } catch (Exception ignore) {

        }
    }

    @ReactMethod
    public void userUniqAppend(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            JSONObject properties = convertToJSONObject(readableMap.getMap("properties"));
            TDAnalyticsAPI.userUniqAppend(properties, appId);
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void setTrackStatus(ReadableMap readableMap) {
        try {
            String appId = readableMap.getString("appId");
            String status = readableMap.getString("status");
            if ("pause".equals(status)) {
                TDAnalyticsAPI.setTrackStatus(TDAnalytics.TDTrackStatus.PAUSE, appId);
            } else if ("stop".equals(status)) {
                TDAnalyticsAPI.setTrackStatus(TDAnalytics.TDTrackStatus.STOP, appId);
            } else if ("saveOnly".equals(status)) {
                TDAnalyticsAPI.setTrackStatus(TDAnalytics.TDTrackStatus.SAVE_ONLY, appId);
            } else {
                TDAnalyticsAPI.setTrackStatus(TDAnalytics.TDTrackStatus.NORMAL, appId);
            }
        } catch (Exception ignore) {
        }
    }

    @ReactMethod
    public void getPresetProperties(ReadableMap readableMap, Promise promise) {
        try {
            String appId = readableMap.getString("appId");
            TDPresetProperties presetProperties = TDAnalyticsAPI.getPresetProperties(appId);
            promise.resolve(convertToMap(presetProperties.toEventPresetProperties()));
        } catch (Exception e) {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getSuperProperties(ReadableMap readableMap, Promise promise) {
        try {
            String appId = readableMap.getString("appId");
            JSONObject superProperties = TDAnalyticsAPI.getSuperProperties(appId);
            promise.resolve(convertToMap(superProperties));
        } catch (Exception e) {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getDistinctId(ReadableMap readableMap, Promise promise) {
        try {
            String appId = readableMap.getString("appId");
            String distinctId = TDAnalyticsAPI.getDistinctId(appId);
            promise.resolve(distinctId);
        } catch (Exception e) {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getAccountId(ReadableMap readableMap, Promise promise) {
        try {
            String appId = readableMap.getString("appId");
            String accountId = TDAnalyticsAPI.getAccountId(appId);
            promise.resolve(accountId);
        } catch (Exception e) {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getDeviceId(ReadableMap readableMap, Promise promise) {
        try {
            String appId = readableMap.getString("appId");
            String deviceId = TDAnalyticsAPI.getDeviceId(appId);
            promise.resolve(deviceId);
        } catch (Exception e) {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void trackViewScreen(ReadableMap params) {
        try {
            JSONObject json = convertToJSONObject(params);
            String tdUrl = json.optString("thinkingdataurl");
            JSONObject properties = json.optJSONObject("thinkingdataparams");
            if (properties == null) {
                properties = new JSONObject();
            }
            boolean isIgnore = properties.optBoolean("TDIgnoreViewScreen");
            if (isIgnore) return;
            properties.remove("TDIgnoreViewScreen");
            if (!properties.has("#title")) {
                properties.put("#title", tdUrl);
            }
            if (!TextUtils.isEmpty(lastScreenName)) {
                properties.put("#referrer", lastScreenName);
            }
            if (!properties.has("#screen_name")) {
                lastScreenName = tdUrl;
                properties.put("#screen_name", tdUrl);
            } else {
                lastScreenName = properties.optString("#screen_name");
            }
            mCurrentTitle = properties.optString("#title");
            mCurrentScreenName = properties.optString("#screen_name");
            if (isEnableAutoTrack) {
                if (isEnablePageView) {
                    TDAnalytics.track("ta_app_view", properties);
                }
            } else {
                if (previewPageViewList.size() < 5) {
                    previewPageViewList.add(properties);
                }
            }
        } catch (Exception ignore) {

        }
    }

    @ReactMethod
    public void trackViewClick(int viewId) {
        try {
            if(!isEnableViewClick) return;
            TDViewProperties viewInfo = viewPropertiesMap.get(viewId);
            if (viewInfo != null && !viewInfo.isIgnore) {
                if (viewInfo.params == null) {
                    viewInfo.params = new JSONObject();
                }
                viewInfo.params.put("#element_content", viewInfo.elementContent);
                viewInfo.params.put("#title",mCurrentTitle);
                viewInfo.params.put("#screen_name",mCurrentScreenName);
                TDAnalytics.track("ta_app_click", viewInfo.params);
            }
        } catch (Exception ignore) {

        }
    }

    @ReactMethod
    public void saveViewProperties(int viewId, String title, ReadableMap viewProperties) {
        viewPropertiesMap.put(viewId, new TDViewProperties(title, readMapToJson(viewProperties)));
    }

    private JSONObject readMapToJson(ReadableMap map) {
        JSONObject params = new JSONObject();
        if (null == map) return params;
        try {
            ReadableMapKeySetIterator iterator = map.keySetIterator();
            while (iterator.hasNextKey()) {
                String key = iterator.nextKey();
                if (map.getType(key) == ReadableType.Map) {
                    ReadableMap newMap = map.getMap(key);
                    if (null != newMap) {
                        params.put(key, readMapToJson(newMap));
                    }
                } else if (map.getType(key) == ReadableType.String) {
                    params.put(key, map.getString(key));
                } else if (map.getType(key) == ReadableType.Boolean) {
                    params.put(key, map.getBoolean(key));
                } else if (map.getType(key) == ReadableType.Number) {
                    params.put(key, map.getDouble(key));
                } else if (map.getType(key) == ReadableType.Array) {
                    ReadableArray newArray = map.getArray(key);
                    if (null != newArray) {
                        params.put(key, readArrayToJsonArray(newArray));
                    }
                }
            }
        } catch (Exception ignore) {
        }
        return params;
    }

    private JSONArray readArrayToJsonArray(ReadableArray array) {
        JSONArray list = new JSONArray();
        if (null == array) return list;
        try {
            for (int i = 0; i < array.size(); i++) {
                if (array.getType(i) == ReadableType.Map) {
                    list.put(readMapToJson(array.getMap(i)));
                } else if (array.getType(i) == ReadableType.String) {
                    list.put(array.getString(i));
                } else if (array.getType(i) == ReadableType.Boolean) {
                    list.put(array.getBoolean(i));
                } else if (array.getType(i) == ReadableType.Number) {
                    list.put(array.getDouble(i));
                } else if (array.getType(i) == ReadableType.Array) {
                    list.put(readArrayToJsonArray(array.getArray(i)));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private WritableMap convertToMap(JSONObject json) {
        if (json == null || json.length() == 0) {
            return Arguments.createMap();
        }
        WritableMap writableMap = Arguments.createMap();
        Iterator<String> it = json.keys();
        while (it.hasNext()) {
            try {
                String key = it.next();
                writableMap.putString(key, json.optString(key));
            } catch (Exception e) {

            }
        }
        return writableMap;
    }

}