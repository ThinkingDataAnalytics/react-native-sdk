// Thinkingdata RN SDK v2.1.0
package cn.thinkingdata;

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

import cn.thinkingdata.analytics.ThinkingAnalyticsSDK;
import cn.thinkingdata.engine.ThinkingGameEngineApi;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RNThinkingAnalyticsModule extends ReactContextBaseJavaModule {

    public RNThinkingAnalyticsModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    private static final String MODULE_NAME = "RNThinkingAnalyticsModule";

    private final ThinkingGameEngineApi mApi = new ThinkingGameEngineApi();

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
            try{
                json = new JSONObject(properties.toString());
            }catch(Exception e1){

            }
        }
        return json;
    }


    @ReactMethod
    public void init(ReadableMap readableMap, String libVersion) {
        try {
            JSONObject json = new JSONObject();
            String appid = readableMap.getString("appid");
            String serverUrl = readableMap.getString("serverUrl");
            if (TextUtils.isEmpty(appid) || TextUtils.isEmpty(serverUrl)) return;
            json.put("appId",appid);
            json.put("serverUrl",serverUrl);

            if (readableMap.hasKey("timeZone")) {
                String timeZoneId = readableMap.getString("timeZone");
                json.put("timeZone",timeZoneId);
            }

            if (readableMap.hasKey("mode")) {
                String mode = readableMap.getString("mode");
                if (TextUtils.equals(mode, "debug")) {
                    json.put("mode",1);
                } else if (TextUtils.equals(mode, "debugOnly")) {
                    json.put("mode",2);
                } else {
                    json.put("mode",0);
                }
            }

            if (readableMap.hasKey("enableEncrypt")) {
                boolean enableEncrypt = readableMap.getBoolean("enableEncrypt");
                json.put("enableEncrypt",enableEncrypt);
            }

            if (readableMap.hasKey("secretKey")) {
                JSONObject secretJson = new JSONObject();
                ReadableMap secretKey = readableMap.getMap("secretKey");
                if (secretKey != null) {
                    secretJson.put("publicKey",secretKey.getString("publicKey"));
                    secretJson.put("version",secretKey.getInt("version"));
                    secretJson.put("symmetricEncryption",secretKey.getString("symmetricEncryption"));
                    secretJson.put("asymmetricEncryption",secretKey.getString("asymmetricEncryption"));
                    json.put("secretKey",secretJson);
                }
            }

            if (readableMap.hasKey("enableLog")) {
                boolean enableLog = readableMap.getBoolean("enableLog");
                mApi.enableTrackLog(enableLog);
            }
            mApi.setCustomerLibInfo("ReactNative", libVersion);
            mApi.sharedInstance(getReactApplicationContext(),json.toString());
        }catch (Exception e){
            
        }
    }


    @ReactMethod
    public void track(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId",readableMap.getString("appid"));
            }
            if (readableMap.hasKey("eventName")) {
                json.put("eventName",readableMap.getString("eventName"));
            }
            if (readableMap.hasKey("properties")) {
                json.put("properties",convertToJSONObject(readableMap.getMap("properties")));
            }
            if (readableMap.hasKey("time")) {
                double time = readableMap.getDouble("time");
                json.put("time",time);
                if (readableMap.hasKey("timeZone")) {
                    String timeZone = readableMap.getString("timeZone");
                    json.put("timeZone",timeZone);
                }
            }
            mApi.track(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void trackUpdate(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId", readableMap.getString("appid"));
            }
            if (readableMap.hasKey("eventName")) {
                json.put("eventName", readableMap.getString("eventName"));
            }
            if (readableMap.hasKey("properties")) {
                json.put("properties",convertToJSONObject(readableMap.getMap("properties")));
            }
            if (readableMap.hasKey("eventId")) {
                json.put("eventId",readableMap.getString("eventId"));
            }
            json.put("type",1);

            if (readableMap.hasKey("time")) {
                double time = readableMap.getDouble("time");
                json.put("time",time);
                if (readableMap.hasKey("timeZone")) {
                    String timeZone = readableMap.getString("timeZone");
                    json.put("timeZone",timeZone);
                }
            }
            mApi.trackEvent(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void trackOverwrite(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId", readableMap.getString("appid"));
            }
            if (readableMap.hasKey("eventName")) {
                json.put("eventName", readableMap.getString("eventName"));
            }
            if (readableMap.hasKey("properties")) {
                json.put("properties", convertToJSONObject(readableMap.getMap("properties")));
            }
            if (readableMap.hasKey("eventId")) {
                json.put("eventId",readableMap.getString("eventId"));
            }
            json.put("type",2);
            if (readableMap.hasKey("time")) {
                double time = readableMap.getDouble("time");
                json.put("time",time);
                if (readableMap.hasKey("timeZone")) {
                    String timeZone = readableMap.getString("timeZone");
                    json.put("timeZone",timeZone);
                }
            }
            mApi.trackEvent(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void trackFirstEvent(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId", readableMap.getString("appid"));
            }
            if (readableMap.hasKey("eventName")) {
                json.put("eventName", readableMap.getString("eventName"));
            }
            if (readableMap.hasKey("properties")) {
                json.put("properties",convertToJSONObject(readableMap.getMap("properties")));
            }
            if (readableMap.hasKey("eventId")) {
                json.put("eventId",readableMap.getString("eventId"));
            }
            json.put("type",0);
            if (readableMap.hasKey("time")) {
                double time = readableMap.getDouble("time");
                json.put("time",time);
                if (readableMap.hasKey("timeZone")) {
                    String timeZone = readableMap.getString("timeZone");
                    json.put("timeZone",timeZone);
                }
            }
            mApi.trackEvent(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void timeEvent(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId",readableMap.getString("appid"));
            }
            if (readableMap.hasKey("eventName")) {
                json.put("eventName",readableMap.getString("eventName"));
            }
            mApi.timeEvent(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void login(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId",readableMap.getString("appid"));
            }
            String loginId = readableMap.getString("loginId");
            json.put("loginId",loginId);
            mApi.login(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void logout(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId",readableMap.getString("appid"));
            }
            mApi.logout(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void userSet(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId", readableMap.getString("appid"));
            }
            if (readableMap.hasKey("properties")) {
                json.put("properties", convertToJSONObject(readableMap.getMap("properties")));
            }
            mApi.userSet(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void userUnset(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId",readableMap.getString("appid"));
            }
            if (readableMap.hasKey("properties")) {
                JSONArray jsonArray = new JSONArray();
                json.put("properties",jsonArray.put(readableMap.getString("properties")));
            }
            mApi.userUnset(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void userSetOnce(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId", readableMap.getString("appid"));
            }
            if (readableMap.hasKey("properties")) {
                json.put("properties", convertToJSONObject(readableMap.getMap("properties")));
            }
            mApi.userSetOnce(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void userAdd(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId", readableMap.getString("appid"));
            }
            if (readableMap.hasKey("properties")) {
                json.put("properties", convertToJSONObject(readableMap.getMap("properties")));
            }
            mApi.userAdd(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void userDel(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId",readableMap.getString("appid"));
            }
            mApi.userDel(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void userAppend(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId", readableMap.getString("appid"));
            }
            if (readableMap.hasKey("properties")) {
                json.put("properties",convertToJSONObject(readableMap.getMap("properties")));
            }
            mApi.userAppend(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void setSuperProperties(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId", readableMap.getString("appid"));
            }
            if (readableMap.hasKey("properties")) {
                json.put("properties", convertToJSONObject(readableMap.getMap("properties")));
            }
            mApi.setSuperProperties(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void unsetSuperProperty(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId", readableMap.getString("appid"));
            }
            if (readableMap.hasKey("properties")) {
                json.put("property", readableMap.getString("properties"));
            }
            mApi.unsetSuperProperty(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void clearSuperProperties(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId", readableMap.getString("appid"));
            }
            mApi.clearSuperProperties(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void identify(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId", readableMap.getString("appid"));
            }
            if (readableMap.hasKey("distinctId")) {
                json.put("distinctId", readableMap.getString("distinctId"));
            }
            mApi.identify(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void flush(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId",readableMap.getString("appid"));
            }
            mApi.flush(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void enableTracking(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId",readableMap.getString("appid"));
            }
            boolean enableTracking = false;
            if (readableMap.hasKey("enableTracking")) {
                enableTracking = readableMap.getBoolean("enableTracking");
                if (enableTracking) {
                    json.put("status", "normal");
                } else {
                    json.put("status", "pause");
                }
            }
            mApi.setTrackStatus(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void optInTracking(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId",readableMap.getString("appid"));
            }
            json.put("status", "normal");
            mApi.setTrackStatus(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void optOutTracking(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId",readableMap.getString("appid"));
            }
            json.put("status", "stop");
            mApi.setTrackStatus(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void optOutTrackingAndDeleteUser(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).optOutTrackingAndDeleteUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void enableAutoTrack(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            if (readableMap.hasKey("appid")) {
                json.put("appId",readableMap.getString("appid"));
            }
            ReadableMap autoTrackType = null;
            if (readableMap.hasKey("autoTrackType")) {
                autoTrackType = readableMap.getMap("autoTrackType");
            }
            if(autoTrackType == null) return;

            JSONArray jsonArray = new JSONArray();
            if (autoTrackType.hasKey("appStart")) {
                jsonArray.put("appStart");
            }
            if (autoTrackType.hasKey("appEnd")) {
                jsonArray.put("appEnd");
            }
            if (autoTrackType.hasKey("appViewCrash")) {
                jsonArray.put("appCrash");
            }
            if (autoTrackType.hasKey("appInstall")) {
                jsonArray.put("appInstall");
            }
            json.put("autoTrack",jsonArray);
            mApi.enableAutoTrack(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void calibrateTime(ReadableMap readableMap) {
        try {
            double timeStampMillis = 0;
            if (readableMap.hasKey("timeStampMillis")) {
                timeStampMillis = readableMap.getDouble("timeStampMillis");
            }
            mApi.calibrateTime(( long ) timeStampMillis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void calibrateTimeWithNtp(ReadableMap readableMap) {
        try {
            String ntp_server = null;
            if (readableMap.hasKey("ntp_server")) {
                ntp_server = readableMap.getString("ntp_server");
            }
            mApi.calibrateTimeWithNtp(ntp_server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void enableThirdPartySharing(ReadableMap readableMap){
        try {
            JSONObject json = new JSONObject();
            String appid = readableMap.getString("appid");
            if(TextUtils.isEmpty(appid)) return;
            json.put("appId",appid);
            if (readableMap.hasKey("types")) {
                ReadableArray shareTypes = readableMap.getArray("types");
                if (null == shareTypes) return;
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < shareTypes.size(); i++) {
                    switch (shareTypes.getString(i)) {
                        case "AppsFlyer":
                            jsonArray.put("AppsFlyer");
                            break;
                        case "IronSource":
                            jsonArray.put("IronSource");
                            break;
                        case "Adjust":
                            jsonArray.put("Adjust");
                            break;
                        case "Branch":
                            jsonArray.put("Branch");
                            break;
                        case "TopOn":
                            jsonArray.put("TopOn");
                            break;
                        case "Tracking":
                            jsonArray.put("Tracking");
                            break;
                        case "TradPlus":
                            jsonArray.put("TradPlus");
                            break;
                    }
                }
                json.put("types",jsonArray);
            } else if (readableMap.hasKey("type")) {
                String type = readableMap.getString("type");
                if (type == null) return;
                ReadableMap maps = readableMap.getMap("params");
                json.put("params",convertToJSONObject(maps));
                json.put("type",type);
            }
            mApi.enableThirdPartySharing(json.toString());
        }catch (Exception e){

        }
    }

    @ReactMethod
    public void userUniqAppend(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            String appid = readableMap.getString("appid");
            if (TextUtils.isEmpty(appid)) return;
            json.put("appId", appid);
            if (readableMap.hasKey("properties")) {
                json.put("properties", convertToJSONObject(readableMap.getMap("properties")));
            }
            mApi.userUniqAppend(json.toString());
        } catch (Exception e) {

        }
    }

    @ReactMethod
    public void setTrackStatus(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            String appid = readableMap.getString("appid");
            if (TextUtils.isEmpty(appid)) return;
            json.put("appId",appid);
            String status = readableMap.getString("status");
            json.put("status",status);
            mApi.setTrackStatus(json.toString());
        } catch (Exception e) {

        }
    }

    @ReactMethod
    public void getPresetProperties(ReadableMap readableMap, Promise promise){
        try {
            JSONObject json = new JSONObject();
            String appid = readableMap.getString("appid");
            json.put("appId",appid);
            String properties = mApi.getPresetProperties(json.toString());
            promise.resolve(convertToMap(new JSONObject(properties)));
        }catch (Exception e){
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getSuperProperties(ReadableMap readableMap, Promise promise){
        try {
            JSONObject json = new JSONObject();
            String appid = readableMap.getString("appid");
            json.put("appId",appid);
            String properties = mApi.getSuperProperties(json.toString());
            promise.resolve(convertToMap(new JSONObject(properties)));
        }catch (Exception e){
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getDistinctId(ReadableMap readableMap, Promise promise) {
        try {
            JSONObject json = new JSONObject();
            String appid = readableMap.getString("appid");
            json.put("appId",appid);
            String distinctId = mApi.getDistinctId(json.toString());
            promise.resolve(distinctId);
        } catch (Exception e) {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getDeviceId(ReadableMap readableMap, Promise promise){
        try {
            JSONObject json = new JSONObject();
            String appid = readableMap.getString("appid");
            json.put("appId",appid);
            String deviceId = mApi.getDeviceId(json.toString());
            promise.resolve(deviceId);
        } catch (Exception e) {
            promise.resolve(null);
        }
    }


    @ReactMethod
    public void setAutoTrackProperties(ReadableMap readableMap) {
        try {
            JSONObject json = new JSONObject();
            String appid = readableMap.getString("appid");
            if (TextUtils.isEmpty(appid)) return;
            json.put("appId",appid);
            if (readableMap.hasKey("types") && readableMap.hasKey("properties")) {
                ReadableMap mapProperties = readableMap.getMap("properties");
                json.put("properties",convertToJSONObject(mapProperties));
                ReadableArray autoTrack = readableMap.getArray("types");
                JSONArray jsonArray = new JSONArray();
                if (null != autoTrack) {
                    for (int i = 0; i < autoTrack.size(); i++) {
                        if (TextUtils.equals(autoTrack.getString(i), "appStart")) {
                            jsonArray.put("appStart");
                        } else if (TextUtils.equals(autoTrack.getString(i), "appEnd")) {
                            jsonArray.put("appEnd");
                        } else if (TextUtils.equals(autoTrack.getString(i), "appInstall")) {
                            jsonArray.put("appInstall");
                        } else if (TextUtils.equals(autoTrack.getString(i), "appViewCrash")) {
                            jsonArray.put("appCrash");
                        }
                    }
                    if(jsonArray.length() >0){
                        json.put("autoTrack",jsonArray);
                        mApi.setAutoTrackProperties(json.toString());
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private WritableMap convertToMap(JSONObject json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        WritableMap writableMap = Arguments.createMap();
        Iterator<String> it = json.keys();
        while(it.hasNext()){
            try {
                String key = it.next();
                writableMap.putString(key, json.optString(key));
            } catch (Exception e) {

            }
        }
        return writableMap;
    }

    private Map<String, Object> readMapToMap(ReadableMap map) {
        Map<String, Object> params = new HashMap<>();
        ReadableMapKeySetIterator iterator = map.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            if (map.getType(key) == ReadableType.Map) {
                ReadableMap newMap = map.getMap(key);
                if (null != newMap) {
                    params.put(key, readMapToMap(newMap));
                }
            } else if (map.getType(key) == ReadableType.String) {
                params.put(key, map.getString(key));
            } else if (map.getType(key) == ReadableType.Boolean) {
                params.put(key, map.getBoolean(key));
            } else if (map.getType(key) == ReadableType.Number) {
                params.put(key, map.getDouble(key));
            } else if (map.getType(key) == ReadableType.Array) {
                ReadableArray newArray = map.getArray(key);
                if(null != newArray) {
                    params.put(key, readArrayToList(newArray));
                }
            }
        }
        return params;
    }

    private List<Object> readArrayToList(ReadableArray array) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            if (array.getType(i) == ReadableType.Map) {
                list.add(readMapToMap(array.getMap(i)));
            } else if (array.getType(i) == ReadableType.String) {
                list.add(array.getString(i));
            } else if (array.getType(i) == ReadableType.Boolean) {
                list.add(array.getBoolean(i));
            } else if (array.getType(i) == ReadableType.Number) {
                list.add(array.getDouble(i));
            } else if (array.getType(i) == ReadableType.Array) {
                list.add(readArrayToList(array.getArray(i)));
            }
        }
        return list;
    }

}
