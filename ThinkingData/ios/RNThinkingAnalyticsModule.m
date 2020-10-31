// Thinkingdata RN SDK v2.1.0
#import "RNThinkingAnalyticsModule.h"
#import <React/RCTBridge.h>
#import <React/RCTEventDispatcher.h>
#import <ThinkingSDK/ThinkingAnalyticsSDK.h>

@implementation RNThinkingAnalyticsModule

RCT_EXPORT_MODULE(RNThinkingAnalyticsModule)

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

@end
