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

import cn.thinkingdata.android.TDConfig;
import cn.thinkingdata.android.ThinkingAnalyticsSDK;
import cn.thinkingdata.android.TDUpdatableEvent;
import cn.thinkingdata.android.TDOverWritableEvent;
import cn.thinkingdata.android.TDFirstEvent;
import cn.thinkingdata.android.encrypt.TDSecreteKey;
import cn.thinkingdata.android.thirdparty.TDThirdPartyShareType;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class RNThinkingAnalyticsModule extends ReactContextBaseJavaModule {

    public RNThinkingAnalyticsModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    private static final String MODULE_NAME = "RNThinkingAnalyticsModule";

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
            e.printStackTrace();
        }
        return json;
    }


    @ReactMethod
    public void init(ReadableMap readableMap) {
        try {
            String appid = readableMap.getString("appid");
            String serverUrl = readableMap.getString("serverUrl");

            if (TextUtils.isEmpty(appid) || TextUtils.isEmpty(serverUrl)) return;

            TDConfig config = TDConfig.getInstance(getReactApplicationContext(), appid, serverUrl);

            if (readableMap.hasKey("timeZone")) {
                String timeZoneId = readableMap.getString("timeZone");
                config.setDefaultTimeZone(TimeZone.getTimeZone(timeZoneId));
            }

            if (readableMap.hasKey("mode")) {
                String mode = readableMap.getString("mode");
                if (TextUtils.equals(mode, "debug")) {
                    config.setMode(TDConfig.ModeEnum.DEBUG);
                } else if (TextUtils.equals(mode, "debugOnly")) {
                    config.setMode(TDConfig.ModeEnum.DEBUG_ONLY);
                } else {
                    config.setMode(TDConfig.ModeEnum.NORMAL);
                }
            }

            if (readableMap.hasKey("enableEncrypt")) {
                boolean enableEncrypt = readableMap.getBoolean("enableEncrypt");
                config.enableEncrypt(enableEncrypt);
            }

            if (readableMap.hasKey("secretKey")) {
                ReadableMap secretKey = readableMap.getMap("secretKey");
                if (secretKey != null) {
                    TDSecreteKey key = new TDSecreteKey();
                    key.publicKey = secretKey.getString("publicKey");
                    key.version = secretKey.getInt("version");
                    key.symmetricEncryption = secretKey.getString("symmetricEncryption");
                    key.asymmetricEncryption = secretKey.getString("asymmetricEncryption");
                    config.setSecretKey(key);
                }
            }

            if (readableMap.hasKey("enableLog")) {
                boolean enableLog = readableMap.getBoolean("enableLog");
                ThinkingAnalyticsSDK.enableTrackLog(enableLog);
            }

            ThinkingAnalyticsSDK.setCustomerLibInfo("ReactNative", "2.2.0");
            ThinkingAnalyticsSDK instance = ThinkingAnalyticsSDK.sharedInstance(config);
        }catch (Exception e){
            
        }
    }


    @ReactMethod
    public void track(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            String eventName = null;
            if (readableMap.hasKey("eventName")) {
                eventName = readableMap.getString("eventName");
            }
            JSONObject properties = null;
            if (readableMap.hasKey("properties")) {
                properties = convertToJSONObject(readableMap.getMap("properties"));
            }
            if (readableMap.hasKey("time")) {
                double time = readableMap.getDouble("time");
                Date date = new Date((long) (time));
                TimeZone tz = null;
                if (readableMap.hasKey("timeZone")) {
                    String timeZone = readableMap.getString("timeZone");
                    tz = TimeZone.getTimeZone(timeZone);
                }

                if (null == tz) {
                    ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).track(eventName, properties, date);
                } else {
                    ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).track(eventName, properties, date, tz);
                }
            } else {
                ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).track(eventName, properties);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void trackUpdate(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            String eventName = null;
            if (readableMap.hasKey("eventName")) {
                eventName = readableMap.getString("eventName");
            }
            JSONObject properties = null;
            if (readableMap.hasKey("properties")) {
                properties = convertToJSONObject(readableMap.getMap("properties"));
            }
            String eventId = null;
            if (readableMap.hasKey("eventId")) {
                eventId = readableMap.getString("eventId");
            }

            TDUpdatableEvent updatableEvent = new TDUpdatableEvent(eventName, properties, eventId);
            if (readableMap.hasKey("time")) {
                double time = readableMap.getDouble("time");
                Date date = new Date((long) (time));
                TimeZone tz = null;
                if (readableMap.hasKey("timeZone")) {
                    String timeZone = readableMap.getString("timeZone");
                    tz = TimeZone.getTimeZone(timeZone);
                }

                if (null == tz) {
                    updatableEvent.setEventTime(date);
                    ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).track(updatableEvent);
                } else {
                    updatableEvent.setEventTime(date, tz);
                    ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).track(updatableEvent);
                }
            } else {
                ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).track(updatableEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void trackOverwrite(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            String eventName = null;
            if (readableMap.hasKey("eventName")) {
                eventName = readableMap.getString("eventName");
            }
            JSONObject properties = null;
            if (readableMap.hasKey("properties")) {
                properties = convertToJSONObject(readableMap.getMap("properties"));
            }
            String eventId = null;
            if (readableMap.hasKey("eventId")) {
                eventId = readableMap.getString("eventId");
            }

            TDOverWritableEvent overWritableEvent = new TDOverWritableEvent(eventName, properties, eventId);
            if (readableMap.hasKey("time")) {
                double time = readableMap.getDouble("time");
                Date date = new Date((long) (time));
                TimeZone tz = null;
                if (readableMap.hasKey("timeZone")) {
                    String timeZone = readableMap.getString("timeZone");
                    tz = TimeZone.getTimeZone(timeZone);
                }

                if (null == tz) {
                    overWritableEvent.setEventTime(date);
                    ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).track(overWritableEvent);
                } else {
                    overWritableEvent.setEventTime(date, tz);
                    ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).track(overWritableEvent);
                }
            } else {
                ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).track(overWritableEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void trackFirstEvent(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            String eventName = null;
            if (readableMap.hasKey("eventName")) {
                eventName = readableMap.getString("eventName");
            }
            JSONObject properties = null;
            if (readableMap.hasKey("properties")) {
                properties = convertToJSONObject(readableMap.getMap("properties"));
            }
            String eventId = null;
            if (readableMap.hasKey("eventId")) {
                eventId = readableMap.getString("eventId");
            }

            TDFirstEvent firstEvent = new TDFirstEvent(eventName, properties);
            firstEvent.setFirstCheckId(eventId);
            if (readableMap.hasKey("time")) {
                double time = readableMap.getDouble("time");
                Date date = new Date((long) (time));
                TimeZone tz = null;
                if (readableMap.hasKey("timeZone")) {
                    String timeZone = readableMap.getString("timeZone");
                    tz = TimeZone.getTimeZone(timeZone);
                }

                if (null == tz) {
                    firstEvent.setEventTime(date);
                    ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).track(firstEvent);
                } else {
                    firstEvent.setEventTime(date, tz);
                    ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).track(firstEvent);
                }
            } else {
                ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).track(firstEvent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void timeEvent(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            String eventName = null;
            if (readableMap.hasKey("eventName")) {
                eventName = readableMap.getString("eventName");
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).timeEvent(eventName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void login(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            String loginId = readableMap.getString("loginId");
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).login(loginId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void logout(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void userSet(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            JSONObject properties = null;
            if (readableMap.hasKey("properties")) {
                properties = convertToJSONObject(readableMap.getMap("properties"));
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).user_set(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void userUnset(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            String properties = null;
            if (readableMap.hasKey("properties")) {
                properties = readableMap.getString("properties");
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).user_unset(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void userSetOnce(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            JSONObject properties = null;
            if (readableMap.hasKey("properties")) {
                properties = convertToJSONObject(readableMap.getMap("properties"));
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).user_setOnce(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void userAdd(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            JSONObject properties = null;
            if (readableMap.hasKey("properties")) {
                properties = convertToJSONObject(readableMap.getMap("properties"));
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).user_add(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void userDel(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).user_delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void userAppend(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            JSONObject properties = null;
            if (readableMap.hasKey("properties")) {
                properties = convertToJSONObject(readableMap.getMap("properties"));
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).user_append(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void setSuperProperties(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            JSONObject properties = null;
            if (readableMap.hasKey("properties")) {
                properties = convertToJSONObject(readableMap.getMap("properties"));
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).setSuperProperties(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void unsetSuperProperty(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            String properties = null;
            if (readableMap.hasKey("properties")) {
                properties = readableMap.getString("properties");
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).unsetSuperProperty(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void clearSuperProperties(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).clearSuperProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void identify(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            String distinctId = null;
            if (readableMap.hasKey("distinctId")) {
                distinctId = readableMap.getString("distinctId");
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).identify(distinctId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void flush(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void enableTracking(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            Boolean enableTracking = false;
            if (readableMap.hasKey("enableTracking")) {
                enableTracking = readableMap.getBoolean("enableTracking");
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).enableTracking(enableTracking);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void optInTracking(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).optInTracking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void optOutTracking(ReadableMap readableMap) {
        try {
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).optOutTracking();
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
            String appid = null;
            if (readableMap.hasKey("appid")) {
                appid = readableMap.getString("appid");
            }
            ReadableMap autoTrackType = null;
            if (readableMap.hasKey("autoTrackType")) {
                autoTrackType = readableMap.getMap("autoTrackType");
            }

            List<ThinkingAnalyticsSDK.AutoTrackEventType> eventTypeList = new ArrayList<>();
            if (autoTrackType.hasKey("appStart")) {
                eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_START);
            }
            if (autoTrackType.hasKey("appEnd")) {
                eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_END);
            }
            if (autoTrackType.hasKey("appViewCrash")) {
                eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_CRASH);
            }
            if (autoTrackType.hasKey("appInstall")) {
                eventTypeList.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_INSTALL);
            }

            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).enableAutoTrack(eventTypeList);
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
            ThinkingAnalyticsSDK.calibrateTime((long)timeStampMillis);
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
            ThinkingAnalyticsSDK.calibrateTimeWithNtp(ntp_server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void enableThirdPartySharing(ReadableMap readableMap){
        try {
            String appid = readableMap.getString("appid");
            if(TextUtils.isEmpty(appid)) return;
            if (readableMap.hasKey("types")) {
                ReadableArray shareTypes = readableMap.getArray("types");
                if (null == shareTypes) return;
                int thirdTypes = 0;
                for (int i = 0; i < shareTypes.size(); i++) {
                    switch (shareTypes.getString(i)) {
                        case "AppsFlyer":
                            thirdTypes = thirdTypes | TDThirdPartyShareType.TD_APPS_FLYER;
                            break;
                        case "IronSource":
                            thirdTypes = thirdTypes | TDThirdPartyShareType.TD_IRON_SOURCE;
                            break;
                        case "Adjust":
                            thirdTypes = thirdTypes | TDThirdPartyShareType.TD_ADJUST;
                            break;
                        case "Branch":
                            thirdTypes = thirdTypes | TDThirdPartyShareType.TD_BRANCH;
                            break;
                        case "TopOn":
                            thirdTypes = thirdTypes | TDThirdPartyShareType.TD_TOP_ON;
                            break;
                        case "Tracking":
                            thirdTypes = thirdTypes | TDThirdPartyShareType.TD_TRACKING;
                            break;
                        case "TradPlus":
                            thirdTypes = thirdTypes | TDThirdPartyShareType.TD_TRAD_PLUS;
                            break;
                    }
                }
                ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).enableThirdPartySharing(thirdTypes);
            } else if (readableMap.hasKey("type")) {
                String type = readableMap.getString("type");
                if (type == null) return;
                ReadableMap maps = readableMap.getMap("params");
                int thirdType = 0;
                switch (type) {
                    case "AppsFlyer":
                        thirdType = TDThirdPartyShareType.TD_APPS_FLYER;
                        break;
                    case "IronSource":
                        thirdType = TDThirdPartyShareType.TD_IRON_SOURCE;
                        break;
                    case "Adjust":
                        thirdType = TDThirdPartyShareType.TD_ADJUST;
                        break;
                    case "Branch":
                        thirdType = TDThirdPartyShareType.TD_BRANCH;
                        break;
                    case "TopOn":
                        thirdType = TDThirdPartyShareType.TD_TOP_ON;
                        break;
                    case "Tracking":
                        thirdType = TDThirdPartyShareType.TD_TRACKING;
                        break;
                    case "TradPlus":
                        thirdType = TDThirdPartyShareType.TD_TRAD_PLUS;
                        break;
                }
                ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).enableThirdPartySharing(thirdType, readMapToMap(maps));
            }
        }catch (Exception e){

        }
    }

    @ReactMethod
    public void userUniqAppend(ReadableMap readableMap) {
        try {
            String appid = readableMap.getString("appid");
            if (TextUtils.isEmpty(appid)) return;
            JSONObject properties = null;
            if (readableMap.hasKey("properties")) {
                properties = convertToJSONObject(readableMap.getMap("properties"));
            }
            ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).user_uniqAppend(properties);
        } catch (Exception e) {

        }
    }

    @ReactMethod
    public void setTrackStatus(ReadableMap readableMap) {
        try {
            String appid = readableMap.getString("appid");
            if (TextUtils.isEmpty(appid)) return;
            String status = readableMap.getString("status");
            if (TextUtils.equals(status, "pause")) {
                ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).setTrackStatus(ThinkingAnalyticsSDK.TATrackStatus.PAUSE);
            } else if (TextUtils.equals(status, "stop")) {
                ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).setTrackStatus(ThinkingAnalyticsSDK.TATrackStatus.STOP);
            } else if (TextUtils.equals(status, "saveOnly")) {
                ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).setTrackStatus(ThinkingAnalyticsSDK.TATrackStatus.SAVE_ONLY);
            } else if (TextUtils.equals(status, "normal")) {
                ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).setTrackStatus(ThinkingAnalyticsSDK.TATrackStatus.NORMAL);
            }
        } catch (Exception e) {

        }
    }

    @ReactMethod
    public void getPresetProperties(ReadableMap readableMap, Promise promise){
        try {
            String appid = readableMap.getString("appid");
            JSONObject properties = ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).getPresetProperties().toEventPresetProperties();
            promise.resolve(convertToMap(properties));
        }catch (Exception e){
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getSuperProperties(ReadableMap readableMap, Promise promise){
        try {
            String appid = readableMap.getString("appid");
            JSONObject properties = ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).getSuperProperties();
            promise.resolve(convertToMap(properties));
        }catch (Exception e){
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getDistinctId(ReadableMap readableMap, Promise promise) {
        try {
            String appid = readableMap.getString("appid");
            String distinctId = ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).getDistinctId();
            promise.resolve(distinctId);
        } catch (Exception e) {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void getDeviceId(ReadableMap readableMap, Promise promise){
        try {
            String appid = readableMap.getString("appid");
            String deviceId = ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).getDeviceId();
            promise.resolve(deviceId);
        } catch (Exception e) {
            promise.resolve(null);
        }
    }


    @ReactMethod
    public void setAutoTrackProperties(ReadableMap readableMap) {
        try {
            String appid = readableMap.getString("appid");
            if (TextUtils.isEmpty(appid)) return;
            if (readableMap.hasKey("types") && readableMap.hasKey("properties")) {
                ReadableMap mapProperties = readableMap.getMap("properties");
                ReadableArray autoTrack = readableMap.getArray("types");
                if (null != autoTrack) {
                    List<ThinkingAnalyticsSDK.AutoTrackEventType> eventTypes = new ArrayList<>();
                    for (int i = 0; i < autoTrack.size(); i++) {
                        if (TextUtils.equals(autoTrack.getString(i), "appStart")) {
                            eventTypes.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_START);
                        } else if (TextUtils.equals(autoTrack.getString(i), "appEnd")) {
                            eventTypes.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_END);
                        } else if (TextUtils.equals(autoTrack.getString(i), "appInstall")) {
                            eventTypes.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_INSTALL);
                        } else if (TextUtils.equals(autoTrack.getString(i), "appViewCrash")) {
                            eventTypes.add(ThinkingAnalyticsSDK.AutoTrackEventType.APP_CRASH);
                        }
                    }
                    if (eventTypes.size() > 0) {
                        JSONObject properties = convertToJSONObject(mapProperties);
                        ThinkingAnalyticsSDK.sharedInstance(getReactApplicationContext(), appid).setAutoTrackProperties(eventTypes, properties);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * JSONObject 转换成 WritableMap
     */
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
                params.put(key, readMapToMap(map.getMap(key)));
            } else if (map.getType(key) == ReadableType.String) {
                params.put(key, map.getString(key));
            } else if (map.getType(key) == ReadableType.Boolean) {
                params.put(key, map.getBoolean(key));
            } else if (map.getType(key) == ReadableType.Number) {
                params.put(key, map.getDouble(key));
            } else if (map.getType(key) == ReadableType.Array) {
                params.put(key,readArrayToList(map.getArray(key)));
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
