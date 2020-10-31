// Thinkingdata RN SDK v2.1.0
package cn.thinkingdata;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import cn.thinkingdata.android.ThinkingAnalyticsSDK;
import cn.thinkingdata.android.TDUpdatableEvent;
import cn.thinkingdata.android.TDOverWritableEvent;
import cn.thinkingdata.android.TDFirstEvent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

}
