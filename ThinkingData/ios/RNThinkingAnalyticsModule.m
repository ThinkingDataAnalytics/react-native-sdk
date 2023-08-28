// Thinkingdata RN SDK v2.1.0
#import "RNThinkingAnalyticsModule.h"
#import <React/RCTBridge.h>
#import <React/RCTEventDispatcher.h>
#import <ThinkingSDK/ThinkingAnalyticsSDK.h>
#import <TAGameEngine/ThinkingGameEngineApi.h>

ThinkingGameEngineApi* mApi = nil;

@implementation RNThinkingAnalyticsModule

RCT_EXPORT_MODULE(RNThinkingAnalyticsModule)

RCT_EXPORT_METHOD(init:(NSDictionary *)options libVersion:(NSString *)libVersion) {
  @try {
      mApi = [[ThinkingGameEngineApi alloc]init];
      NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
      if ([options objectForKey:@"timeZone"]) {
          [mDict setValue:[options objectForKey:@"timeZone"] forKey:@"timeZone"];
      }
      if ([[options objectForKey:@"mode"] isEqualToString:@"debug"]) {
          [mDict setValue:@"1" forKey:@"mode"];
      } else if ([[options objectForKey:@"mode"] isEqualToString:@"debugOnly"]) {
          [mDict setValue:@"2" forKey:@"mode"];
      }else{
          [mDict setValue:@"0" forKey:@"mode"];
      }
      if([options objectForKey:@"enableEncrypt"]){
          [mDict setValue:[options objectForKey:@"enableEncrypt"] forKey:@"enableEncrypt"];
      }
      if([options objectForKey:@"secretKey"]){
          NSDictionary *secretKey = (NSDictionary *)[options objectForKey:@"secretKey"];
          [mDict setValue:secretKey forKey:@"secretKey"];
      }
      NSString *appId = [options objectForKey:@"appid"];
      NSString *serverUrl = [options objectForKey:@"serverUrl"];
      [mDict setValue:appId forKey:@"appId"];
      [mDict setValue:serverUrl forKey:@"serverUrl"];
      
      if([options objectForKey:@"enableLog"]){
          NSNumber* enableLog = [options objectForKey:@"enableLog"];
          if(enableLog.boolValue){
              [mApi enableTrackLog:true];
          }
      }

      [mApi sharedInstance:[self toJSONData:mDict]];
    
      [mApi setCustomerLibInfo:@"ReactNative" libVersion:libVersion];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(track:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    NSString *appid = [options objectForKey:@"appid"];
    [mDict setValue:appid forKey:@"appId"];
    NSString *eventName = [options objectForKey:@"eventName"];
    [mDict setValue:eventName forKey:@"eventName"];
    NSDictionary *properties = [options objectForKey:@"properties"];
    [mDict setValue:properties forKey:@"properties"];
    if ([options objectForKey:@"time"]) {
        [mDict setValue:[options objectForKey:@"time"] forKey:@"time"];
    }
    NSString *timeZone = [options objectForKey:@"timeZone"];
    [mDict setValue:timeZone forKey:@"timeZone"];
    [mApi track:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(trackUpdate:(NSDictionary *)options) {
  @try {
      NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
      [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
      [mDict setValue:[options objectForKey:@"eventName"] forKey:@"eventName"];
      [mDict setValue:[options objectForKey:@"eventId"] forKey:@"eventId"];
      [mDict setValue:[options objectForKey:@"properties"] forKey:@"properties"];
      if ([options objectForKey:@"time"]) {
          [mDict setValue:[options objectForKey:@"time"] forKey:@"time"];
      }
      [mDict setValue:[options objectForKey:@"timeZone"] forKey:@"timeZone"];
      [mDict setValue:@"1" forKey:@"type"];
      [mApi trackEvent:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
      NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(trackOverwrite:(NSDictionary *)options) {
  @try {
        NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
        [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
        [mDict setValue:[options objectForKey:@"eventName"] forKey:@"eventName"];
        [mDict setValue:[options objectForKey:@"eventId"] forKey:@"eventId"];
        [mDict setValue:[options objectForKey:@"properties"] forKey:@"properties"];
        if ([options objectForKey:@"time"]) {
            [mDict setValue:[options objectForKey:@"time"] forKey:@"time"];
        }
        [mDict setValue:[options objectForKey:@"timeZone"] forKey:@"timeZone"];
        [mDict setValue:@"2" forKey:@"type"];
        [mApi trackEvent:[self toJSONData:mDict]];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(trackFirstEvent:(NSDictionary *)options) {
  @try {
        NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
        [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
        [mDict setValue:[options objectForKey:@"eventName"] forKey:@"eventName"];
        [mDict setValue:[options objectForKey:@"eventId"] forKey:@"eventId"];
        [mDict setValue:[options objectForKey:@"properties"] forKey:@"properties"];
        if ([options objectForKey:@"time"]) {
            [mDict setValue:[options objectForKey:@"time"] forKey:@"time"];
        }
        [mDict setValue:[options objectForKey:@"timeZone"] forKey:@"timeZone"];
        [mDict setValue:@"0" forKey:@"type"];
        [mApi trackEvent:[self toJSONData:mDict]];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(timeEvent:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:[options objectForKey:@"eventName"] forKey:@"eventName"];
    [mApi timeEvent:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(login:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:[options objectForKey:@"loginId"] forKey:@"loginId"];
    [mApi login:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(logout:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mApi logout:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userSet:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:[options objectForKey:@"properties"] forKey:@"properties"];
    [mApi userSet:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userUnset:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    NSString *property = [options objectForKey:@"properties"];
    NSMutableArray* array = [[NSMutableArray alloc]init];
    [array addObject:property];
    [mDict setValue:array forKey:@"properties"];
    [mApi userUnset:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userSetOnce:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:[options objectForKey:@"properties"] forKey:@"properties"];
    [mApi userSetOnce:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userAdd:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:[options objectForKey:@"properties"] forKey:@"properties"];
    [mApi userAdd:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userDel:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mApi userDel:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userAppend:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:[options objectForKey:@"properties"] forKey:@"properties"];
    [mApi userAppend:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(userUniqAppend:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:[options objectForKey:@"properties"] forKey:@"properties"];
    [mApi userUniqAppend:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}


RCT_EXPORT_METHOD(setSuperProperties:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:[options objectForKey:@"properties"] forKey:@"properties"];
    [mApi setSuperProperties:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(unsetSuperProperty:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:[options objectForKey:@"properties"] forKey:@"property"];
    [mApi unsetSuperProperty:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(clearSuperProperties:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mApi clearSuperProperties:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(identify:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:[options objectForKey:@"distinctId"] forKey:@"distinctId"];
    [mApi identify:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(flush:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mApi flush:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(enableTracking:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    BOOL enableTracking = [[options objectForKey:@"enableTracking"] boolValue];
    if(enableTracking){
        [mDict setValue:@"normal" forKey:@"status"];
    }else{
        [mDict setValue:@"pause" forKey:@"status"];
    }
    [mApi setTrackStatus:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(optInTracking:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:@"normal" forKey:@"status"];
    [mApi setTrackStatus:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(optOutTracking:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:@"stop" forKey:@"status"];
    [mApi setTrackStatus:[self toJSONData:mDict]];
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
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
      [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
      NSDictionary *autoTrackType = [options objectForKey:@"autoTrackType"];
      BOOL appStart = [[autoTrackType objectForKey:@"appStart"] boolValue];
      BOOL appEnd = [[autoTrackType objectForKey:@"appEnd"] boolValue];
      BOOL appViewCrash = [[autoTrackType objectForKey:@"appViewCrash"] boolValue];
      BOOL appInstall = [[autoTrackType objectForKey:@"appInstall"] boolValue];
      NSMutableArray* array = [[NSMutableArray alloc]init];
      if(appStart){
          [array addObject:@"appStart"];
      }
      if(appEnd){
          [array addObject:@"appEnd"];
      }
      if(appViewCrash){
          [array addObject:@"appCrash"];
      }
      if(appInstall){
          [array addObject:@"appInstall"];
      }
      [mDict setValue:array forKey:@"autoTrack"];
      [mApi enableAutoTrack:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(calibrateTime:(NSDictionary *)options) {
  @try {
    double timeStampMillis = [[options objectForKey:@"timeStampMillis"] doubleValue];
    [mApi calibrateTime:timeStampMillis];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(calibrateTimeWithNtp:(NSDictionary *)options) {
  @try {
    NSString *ntp_server = [options objectForKey:@"ntp_server"];
    [mApi calibrateTimeWithNtp:ntp_server];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(enableThirdPartySharing:(NSDictionary *)options) {
  @try {
      NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
      [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
      if([options objectForKey:@"types"]){
          NSArray *shareTypes = [options objectForKey:@"types"];
          [mDict setValue:shareTypes forKey:@"types"];
      }else if([options objectForKey:@"type"]){
          NSString* type = [options objectForKey:@"type"];
          NSDictionary* params = [options objectForKey:@"params"];
          [mDict setValue:params forKey:@"params"];
          NSArray* array = [[NSArray alloc]initWithObjects:type, nil];
          [mDict setValue:array forKey:@"type"];
      }
      [mApi enableThirdPartySharing:[self toJSONData:mDict]];  
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(setTrackStatus:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    if([options objectForKey:@"status"]){
        NSString *status = [options objectForKey:@"status"];
        [mDict setValue:status forKey:@"status"];
        [mApi setTrackStatus:[self toJSONData:mDict]];
    }
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(getPresetProperties:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    NSString* preset = [mApi getPresetProperties:[self toJSONData:mDict]];
    resolve([self toDictionary:preset]);
  } @catch (NSException *exception) {
    resolve(nil);
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(getSuperProperties:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    NSString* superProperties = [mApi getSuperProperties:[self toJSONData:mDict]];
    resolve([self toDictionary:superProperties]);
  } @catch (NSException *exception) {
    resolve(nil);
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(getDistinctId:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    NSString *distinctId = [mApi getDistinctId:[self toJSONData:mDict]];
    resolve(distinctId);
  } @catch (NSException *exception) {
    resolve(nil);
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(getDeviceId:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    NSString *deviceId  = [mApi getDeviceId:[self toJSONData:mDict]];
    resolve(deviceId);
  } @catch (NSException *exception) {
    resolve(nil);
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

RCT_EXPORT_METHOD(setAutoTrackProperties:(NSDictionary *)options) {
  @try {
    NSMutableDictionary *mDict =[[NSMutableDictionary alloc]init];
    [mDict setValue:[options objectForKey:@"appid"] forKey:@"appId"];
    [mDict setValue:[options objectForKey:@"properties"] forKey:@"properties"];
    NSArray *autoTrackTypes = [options objectForKey:@"types"];
    NSMutableArray *array = [[NSMutableArray alloc]init];
    for(int i=0; i < autoTrackTypes.count; i++) {
        NSString* value = autoTrackTypes[i];
        if ([@"appStart" isEqualToString:value]) {
            [array addObject:@"appStart"];
        } else if ([@"appEnd" isEqualToString:value]) {
            [array addObject:@"appEnd"];
        } else if ([@"appInstall" isEqualToString:value]) {
            [array addObject:@"appInstall"];
        } else if ([@"appViewCrash" isEqualToString:value]) {
            [array addObject:@"appCrash"];
        }
    }
    [mDict setValue:array forKey:@"autoTrack"];
    [mApi setAutoTrackProperties:[self toJSONData:mDict]];
  } @catch (NSException *exception) {
    NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
  }
}

- (NSString *)toJSONData:(NSDictionary *)dict{
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict options:NSJSONWritingPrettyPrinted error:nil];
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    return jsonString;
}

- (NSDictionary*) toDictionary:(NSString*)jsonString{
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *error;
    NSDictionary *dict = [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers error:&error];
    if (error == nil) {
        return  dict;
    }else{
        return [[NSDictionary alloc]init];
    }
}

@end
