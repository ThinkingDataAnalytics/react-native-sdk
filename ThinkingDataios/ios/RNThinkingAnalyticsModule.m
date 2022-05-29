// Thinkingdata RN SDK v2.1.0
#import "RNThinkingAnalyticsModule.h"
#import <React/RCTBridge.h>
#import <React/RCTEventDispatcher.h>
#import <ThinkingSDK/ThinkingAnalyticsSDK.h>

@implementation RNThinkingAnalyticsModule

RCT_EXPORT_MODULE(RNThinkingAnalyticsModule)

RCT_EXPORT_METHOD(init:(NSDictionary *)options) {
  @try {
      TDConfig *config = [[TDConfig alloc] init];
      if ([options objectForKey:@"timeZone"]) {
          config.defaultTimeZone = [NSTimeZone timeZoneWithName:[options objectForKey:@"timeZone"]];
      }
      if ([[options objectForKey:@"mode"] isEqualToString:@"debug"]) {
          config.debugMode = ThinkingAnalyticsDebug;
      } else if ([[options objectForKey:@"mode"] isEqualToString:@"debugOnly"]) {
          config.debugMode = ThinkingAnalyticsDebugOnly;
      }
      if([options objectForKey:@"enableEncrypt"]){
          NSNumber* enableEncrypt = [options objectForKey:@"enableEncrypt"];
          config.enableEncrypt = enableEncrypt.boolValue;
      }
      if([options objectForKey:@"secretKey"]){
          NSDictionary *secretKey = (NSDictionary *)[options objectForKey:@"secretKey"];
          NSNumber* keyVersion = [secretKey objectForKey:@"version"];
          config.secretKey = [[TDSecretKey alloc] initWithVersion:keyVersion.intValue publicKey:[secretKey objectForKey:@"publicKey"] asymmetricEncryption:[secretKey objectForKey:@"asymmetricEncryption"] symmetricEncryption:[secretKey objectForKey:@"symmetricEncryption"]];
      }
      NSString *appId = [options objectForKey:@"appid"];
      NSString *serverUrl = [options objectForKey:@"serverUrl"];

      if([options objectForKey:@"enableLog"]){
          NSNumber* enableLog = [options objectForKey:@"enableLog"];
          if(enableLog.boolValue){
              [ThinkingAnalyticsSDK setLogLevel:TDLoggingLevelDebug];
          }
      }

      [ThinkingAnalyticsSDK startWithAppId:appId
                                           withUrl:serverUrl
                                        withConfig:config];
      [ThinkingAnalyticsSDK setCustomerLibInfoWithLibName:@"ReactNative" libVersion:@"2.2.0"];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(track:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSString *eventName = [options objectForKey:@"eventName"];
    NSDictionary *properties = [options objectForKey:@"properties"];
    NSDate *time;
    if ([options objectForKey:@"time"]) {
      time = [NSDate dateWithTimeIntervalSince1970:[[options objectForKey:@"time"] doubleValue] / 1000.0];
    }
    NSString *timeZone = [options objectForKey:@"timeZone"];
    NSTimeZone *tz = [NSTimeZone timeZoneWithName:timeZone];
    
    if (appid) {
      if (tz) {
        if (time) {
          [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] track:eventName properties:properties time:time timeZone:tz];
        }
      } else if (time) {
        [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] track:eventName properties:properties time:time];
      } else {
        [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] track:eventName properties:properties];
      }
    } else {
      if (tz) {
        if (time) {
          [[ThinkingAnalyticsSDK sharedInstance] track:eventName properties:properties time:time timeZone:tz];
        }
      } else if (time) {
        [[ThinkingAnalyticsSDK sharedInstance] track:eventName properties:properties time:time];
      } else {
        [[ThinkingAnalyticsSDK sharedInstance] track:eventName properties:properties];
      }
    }
    
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(trackUpdate:(NSDictionary *)options) {
  NSString *appid = [options objectForKey:@"appid"];
  NSString *eventName = [options objectForKey:@"eventName"];
  NSString *eventId = [options objectForKey:@"eventId"];
  NSDictionary *properties = [options objectForKey:@"properties"];
  NSDate *time;
  if ([options objectForKey:@"time"]) {
    time = [NSDate dateWithTimeIntervalSince1970:[[options objectForKey:@"time"] doubleValue] / 1000.0];
  }
  NSString *timeZone = [options objectForKey:@"timeZone"];
  NSTimeZone *tz = [NSTimeZone timeZoneWithName:timeZone];
  
  TDEventModel *updateModel = [[TDUpdateEventModel alloc] initWithEventName:eventName eventID:eventId];
  updateModel.properties = properties;
  [updateModel configTime:time timeZone:tz];
  
  if (appid) {
    [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] trackWithEventModel:updateModel];
  } else {
    [[ThinkingAnalyticsSDK sharedInstance] trackWithEventModel:updateModel];
  }
}

RCT_EXPORT_METHOD(trackOverwrite:(NSDictionary *)options) {
  NSString *appid = [options objectForKey:@"appid"];
  NSString *eventName = [options objectForKey:@"eventName"];
  NSString *eventId = [options objectForKey:@"eventId"];
  NSDictionary *properties = [options objectForKey:@"properties"];
  NSDate *time;
  if ([options objectForKey:@"time"]) {
    time = [NSDate dateWithTimeIntervalSince1970:[[options objectForKey:@"time"] doubleValue] / 1000.0];
  }
  NSString *timeZone = [options objectForKey:@"timeZone"];
  NSTimeZone *tz = [NSTimeZone timeZoneWithName:timeZone];
  
  TDOverwriteEventModel *overwriteModel = [[TDOverwriteEventModel alloc] initWithEventName:eventName eventID:eventId];
  overwriteModel.properties = properties;
  [overwriteModel configTime:time timeZone:tz];
  
  if (appid) {
    [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] trackWithEventModel:overwriteModel];
  } else {
    [[ThinkingAnalyticsSDK sharedInstance] trackWithEventModel:overwriteModel];
  }
}

RCT_EXPORT_METHOD(trackFirstEvent:(NSDictionary *)options) {
  NSString *appid = [options objectForKey:@"appid"];
  NSString *eventName = [options objectForKey:@"eventName"];
  NSString *eventId = [options objectForKey:@"eventId"];
  NSDictionary *properties = [options objectForKey:@"properties"];
  NSDate *time;
  if ([options objectForKey:@"time"]) {
    time = [NSDate dateWithTimeIntervalSince1970:[[options objectForKey:@"time"] doubleValue] / 1000.0];
  }
  NSString *timeZone = [options objectForKey:@"timeZone"];
  NSTimeZone *tz = [NSTimeZone timeZoneWithName:timeZone];
  
  TDFirstEventModel *firstModel = [[TDFirstEventModel alloc] initWithEventName:eventName firstCheckID:eventId];
  firstModel.properties = properties;
  [firstModel configTime:time timeZone:tz];
  
  if (appid) {
    [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] trackWithEventModel:firstModel];
  } else {
    [[ThinkingAnalyticsSDK sharedInstance] trackWithEventModel:firstModel];
  }
}

RCT_EXPORT_METHOD(timeEvent:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSString *eventName = [options objectForKey:@"eventName"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] timeEvent:eventName];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] timeEvent:eventName];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(login:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSString *loginId = [options objectForKey:@"loginId"];
    
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] login:loginId];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] login:loginId];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(logout:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] logout];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] logout];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userSet:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSDictionary *properties = [options objectForKey:@"properties"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] user_set:properties];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] user_set:properties];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userUnset:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSString *property = [options objectForKey:@"properties"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] user_unset:property];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] user_unset:property];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userSetOnce:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSDictionary *properties = [options objectForKey:@"properties"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] user_setOnce:properties];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] user_setOnce:properties];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userAdd:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSDictionary *properties = [options objectForKey:@"properties"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] user_add:properties];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] user_add:properties];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userDel:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] user_delete];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] user_delete];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userAppend:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSDictionary *properties = [options objectForKey:@"properties"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] user_append:properties];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] user_append:properties];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userUniqAppend:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSDictionary *properties = [options objectForKey:@"properties"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] user_uniqAppend:properties];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] user_uniqAppend:properties];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}


RCT_EXPORT_METHOD(setSuperProperties:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSDictionary *properties = [options objectForKey:@"properties"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] setSuperProperties:properties];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] setSuperProperties:properties];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(unsetSuperProperty:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSString *property = [options objectForKey:@"properties"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] unsetSuperProperty:property];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] unsetSuperProperty:property];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(clearSuperProperties:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] clearSuperProperties];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] clearSuperProperties];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(identify:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSString *distinctId = [options objectForKey:@"distinctId"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] identify:distinctId];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] identify:distinctId];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(flush:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] flush];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] flush];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(enableTracking:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    BOOL enableTracking = [[options objectForKey:@"enableTracking"] boolValue];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] enableTracking:enableTracking];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] enableTracking:enableTracking];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(optInTracking:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] optInTracking];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] optInTracking];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(optOutTracking:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] optOutTracking];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] optOutTracking];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(optOutTrackingAndDeleteUser:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] optOutTrackingAndDeleteUser];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] optOutTrackingAndDeleteUser];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(enableAutoTrack:(NSDictionary *)options) {
  @try {
    NSString *appid = [options objectForKey:@"appid"];
    NSDictionary *autoTrackType = [options objectForKey:@"autoTrackType"];
    BOOL appStart = [[autoTrackType objectForKey:@"appStart"] boolValue];
    BOOL appEnd = [[autoTrackType objectForKey:@"appEnd"] boolValue];
    BOOL appViewCrash = [[autoTrackType objectForKey:@"appViewCrash"] boolValue];
    BOOL appInstall = [[autoTrackType objectForKey:@"appInstall"] boolValue];
    int autoTrackEvents = ((appStart==YES) ? 1 : 0) | ((appEnd==YES) ? 2 : 0) | ((appViewCrash==YES) ? 16 : 0) | ((appInstall==YES) ? 32 : 0);
    
    if (appid) {
      [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] enableAutoTrack:autoTrackEvents];
    } else {
      [[ThinkingAnalyticsSDK sharedInstance] enableAutoTrack:autoTrackEvents];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(calibrateTime:(NSDictionary *)options) {
  @try {
    double timeStampMillis = [[options objectForKey:@"timeStampMillis"] doubleValue];
    [ThinkingAnalyticsSDK calibrateTime:timeStampMillis];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(calibrateTimeWithNtp:(NSDictionary *)options) {
  @try {
    NSString *ntp_server = [options objectForKey:@"ntp_server"];
    [ThinkingAnalyticsSDK calibrateTimeWithNtp:ntp_server];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(enableThirdPartySharing:(NSDictionary *)options) {
  @try {
      NSString *appid = [options objectForKey:@"appid"];
      if([options objectForKey:@"types"]){
          NSArray *shareTypes = [options objectForKey:@"types"];
          TDThirdPartyShareType types = TDThirdPartyShareTypeNONE;
          for(int i = 0;i<shareTypes.count;i++){
              NSString* value = shareTypes[i];
              if([@"AppsFlyer" isEqualToString:value]){
                  types |= TDThirdPartyShareTypeAPPSFLYER;
              }else if([@"IronSource" isEqualToString:value]){
                  types |= TDThirdPartyShareTypeIRONSOURCE;
              }else if([@"Adjust" isEqualToString:value]){
                  types |= TDThirdPartyShareTypeADJUST;
              }else if([@"Branch" isEqualToString:value]){
                  types |= TDThirdPartyShareTypeBRANCH;
              }else if([@"TopOn" isEqualToString:value]){
                  types |= TDThirdPartyShareTypeTOPON;
              }else if([@"Tracking" isEqualToString:value]){
                  types |= TDThirdPartyShareTypeTRACKING;
              }else if([@"TradPlus" isEqualToString:value]){
                  types |= TDThirdPartyShareTypeTRADPLUS;
              }
          }
          if (appid) {
              [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] enableThirdPartySharing:types];
          } else {
              [[ThinkingAnalyticsSDK sharedInstance] enableThirdPartySharing:types];
          }
      }else if([options objectForKey:@"type"]){
          NSString* type = [options objectForKey:@"type"];
          NSDictionary* params = [options objectForKey:@"params"];
          TDThirdPartyShareType t = TDThirdPartyShareTypeNONE;
          if([@"AppsFlyer" isEqualToString:type]){
              t = TDThirdPartyShareTypeAPPSFLYER;
          }else if([@"IronSource" isEqualToString:type]){
              t = TDThirdPartyShareTypeIRONSOURCE;
          }else if([@"Adjust" isEqualToString:type]){
              t = TDThirdPartyShareTypeADJUST;
          }else if([@"Branch" isEqualToString:type]){
              t = TDThirdPartyShareTypeBRANCH;
          }else if([@"TopOn" isEqualToString:type]){
              t = TDThirdPartyShareTypeTOPON;
          }else if([@"Tracking" isEqualToString:type]){
              t = TDThirdPartyShareTypeTRACKING;
          }else if([@"TradPlus" isEqualToString:type]){
              t = TDThirdPartyShareTypeTRADPLUS;
          }
          if (appid) {
              [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] enableThirdPartySharing:t customMap:params];
          } else {
              [[ThinkingAnalyticsSDK sharedInstance] enableThirdPartySharing:t customMap:params];
          }
      }
      
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(setTrackStatus:(NSDictionary *)options) {
  @try {
      NSString *appid = [options objectForKey:@"appid"];
      if([options objectForKey:@"status"]){
          NSString *status = [options objectForKey:@"status"];
          TATrackStatus ta_status;
          if([@"pause" isEqualToString:status]){
              ta_status = TATrackStatusPause;
          }else if([@"stop" isEqualToString:status]){
              ta_status = TATrackStatusStop;
          }else if([@"saveOnly" isEqualToString:status]){
              ta_status = TATrackStatusSaveOnly;
          }else if([@"normal" isEqualToString:status]){
              ta_status = TATrackStatusNormal;
          }
          if (appid) {
              [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] setTrackStatus:ta_status];
          } else {
              [[ThinkingAnalyticsSDK sharedInstance] setTrackStatus:ta_status];
          }
      }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(getPresetProperties:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
  @try {
      NSString *appid = [options objectForKey:@"appid"];
      if(appid){
          NSDictionary *presetProperties = [[[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] getPresetProperties] toEventPresetProperties];
          resolve(presetProperties);
      }else{
          NSDictionary *presetProperties = [[[ThinkingAnalyticsSDK sharedInstance] getPresetProperties] toEventPresetProperties];
          resolve(presetProperties);
      }
  } @catch (NSException *exception) {
    resolve(nil);
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(getSuperProperties:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
  @try {
      NSString *appid = [options objectForKey:@"appid"];
      if(appid){
          NSDictionary *superProperties = [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] currentSuperProperties];
          resolve(superProperties);
      }else{
          NSDictionary *superProperties = [[ThinkingAnalyticsSDK sharedInstance] currentSuperProperties];
          resolve(superProperties);
      }
  } @catch (NSException *exception) {
    resolve(nil);
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(getDistinctId:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
  @try {
      NSString *appid = [options objectForKey:@"appid"];
      if(appid){
          NSString *distinctId = [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] getDistinctId];
          resolve(distinctId);
      }else{
          NSString *distinctId = [[ThinkingAnalyticsSDK sharedInstance] getDistinctId];
          resolve(distinctId);
      }
  } @catch (NSException *exception) {
    resolve(nil);
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(getDeviceId:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
  @try {
      NSString *appid = [options objectForKey:@"appid"];
      if(appid){
          NSString *deviceId = [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] getDeviceId];
          resolve(deviceId);
      }else{
          NSString *deviceId = [[ThinkingAnalyticsSDK sharedInstance] getDeviceId];
          resolve(deviceId);
      }
  } @catch (NSException *exception) {
    resolve(nil);
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(setAutoTrackProperties:(NSDictionary *)options) {
  @try {
      NSString *appid = [options objectForKey:@"appid"];
      NSArray *autoTrackTypes = [options objectForKey:@"types"];
      ThinkingAnalyticsAutoTrackEventType iOSAutoTrackType = ThinkingAnalyticsEventTypeNone;
      NSDictionary* autoProperties = [options objectForKey:@"properties"];
      for(int i=0; i < autoTrackTypes.count; i++) {
          NSString* value = autoTrackTypes[i];
          if ([@"appStart" isEqualToString:value]) {
              iOSAutoTrackType |= ThinkingAnalyticsEventTypeAppStart;
          } else if ([@"appEnd" isEqualToString:value]) {
              iOSAutoTrackType |= ThinkingAnalyticsEventTypeAppEnd;
          } else if ([@"appInstall" isEqualToString:value]) {
              iOSAutoTrackType |= ThinkingAnalyticsEventTypeAppInstall;
          } else if ([@"appViewCrash" isEqualToString:value]) {
              iOSAutoTrackType |= ThinkingAnalyticsEventTypeAppViewCrash;
          }
      }
      if(appid){
          [[ThinkingAnalyticsSDK sharedInstanceWithAppid:appid] setAutoTrackProperties:iOSAutoTrackType properties:autoProperties];
      }else{
          [[ThinkingAnalyticsSDK sharedInstance] setAutoTrackProperties:iOSAutoTrackType properties:autoProperties];
      }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

@end
