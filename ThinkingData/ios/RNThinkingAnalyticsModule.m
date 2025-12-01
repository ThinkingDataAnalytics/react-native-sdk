#import "RNThinkingAnalyticsModule.h"
#import <React/RCTBridge.h>
#import <React/RCTEventDispatcher.h>
#import <ThinkingSDK/ThinkingSDK.h>

@implementation RNThinkingAnalyticsModule

RCT_EXPORT_MODULE(RNThinkingAnalyticsModule)

- (instancetype)init {
    self = [super init];
    if (self) {
        _previewPageViewList = [NSMutableArray array];
        _isEnableAutoTrack = NO;
        _isEnablePageView = NO;
        _lastScreenName = @"";
        _mCurrentScreenName = @"";
        _mCurrentTitle = @"";
        _viewPropertiesMap = [NSMutableDictionary dictionary];
        _isEnableViewClick = NO;
    }
    return self;
}

- (NSTimeZone *)getTimeZone:(NSNumber *)number {
    if (!number || [number doubleValue] <= 0) {
        return [NSTimeZone defaultTimeZone];
    }
    NSTimeInterval hoursOffset = [number doubleValue];
    NSTimeInterval secondsOffset = hoursOffset * 3600;
    return [NSTimeZone timeZoneForSecondsFromGMT:secondsOffset];
}

RCT_EXPORT_METHOD(init:(NSDictionary *)options libVersion:(NSString *)libVersion) {
    @try {
        
        TDConfig *config = [[TDConfig alloc]init];
        config.appid = options[@"appId"];
        config.serverUrl = options[@"serverUrl"];
        if([@"debug" isEqualToString: options[@"mode"]]){
            config.mode = TDModeDebug;
        }else if([@"debugOnly" isEqualToString:options[@"mode"]]){
            config.mode = TDModeDebugOnly;
        }
        if (options[@"timeZone"]) {
            config.defaultTimeZone = [self getTimeZone:options[@"timeZone"]];
        }
        if(options[@"enableEncrypt"] && options[@"secretKey"]){
            bool enableEncrypt = options[@"enableEncrypt"];
            if(enableEncrypt){
                NSDictionary *secretKey = (NSDictionary *)options[@"secretKey"];
                if(secretKey){
                    NSUInteger version = [secretKey[@"version"] unsignedIntegerValue];
                    [config enableEncryptWithVersion:version publicKey:secretKey[@"publicKey"]];
                }
            }
        }
        
        if(options[@"enableLog"]){
            NSNumber* enableLog = options[@"enableLog"];
            [TDAnalytics enableLog:enableLog.boolValue];
        }
        [TDAnalytics setCustomerLibInfoWithLibName:@"ReactNative" libVersion:libVersion];
        [TDAnalytics startAnalyticsWithConfig:config];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(track:(NSDictionary *)options) {
    @try {
        
        NSString *appId = options[@"appId"];
        NSString *eventName = options[@"eventName"];
        NSDictionary *properties = options[@"properties"];
        NSDate *date;
        NSTimeZone *timeZone;
        if (options[@"time"]) {
            NSTimeInterval timestamp = [options[@"time"] doubleValue];
            if(timestamp > 0){
                date = [NSDate dateWithTimeIntervalSince1970:timestamp / 1000.0];
            }
        }
        if (options[@"timeZone"]) {
            timeZone = [self getTimeZone:options[@"timeZone"]];
        }
        [TDAnalytics track:eventName properties:properties time:date timeZone:timeZone withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(trackUpdate:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        NSString *eventName = options[@"eventName"];
        NSString *eventId = options[@"eventId"];
        NSDate *date;
        NSTimeZone *timeZone;
        if (options[@"time"]) {
            NSTimeInterval timestamp = [options[@"time"] doubleValue];
            if(timestamp > 0){
                date = [NSDate dateWithTimeIntervalSince1970:timestamp / 1000.0];
            }
        }
        if (options[@"timeZone"]) {
            timeZone = [self getTimeZone:options[@"timeZone"]];
        }
        TDUpdateEventModel *model = [[TDUpdateEventModel alloc] initWithEventName:eventName eventID:eventId];
        model.properties = options[@"properties"];
        [model configTime:date timeZone:timeZone];
        [TDAnalytics trackWithEventModel:model withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(trackOverwrite:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        NSString *eventName = options[@"eventName"];
        NSString *eventId = options[@"eventId"];
        NSDate *date;
        NSTimeZone *timeZone;
        if (options[@"time"]) {
            NSTimeInterval timestamp = [options[@"time"] doubleValue];
            if(timestamp > 0){
                date = [NSDate dateWithTimeIntervalSince1970:timestamp / 1000.0];
            }
        }
        if (options[@"timeZone"]) {
            timeZone = [self getTimeZone:options[@"timeZone"]];
        }
        TDOverwriteEventModel *model = [[TDOverwriteEventModel alloc]initWithEventName:eventName eventID:eventId];
        model.properties = options[@"properties"];
        [model configTime:date timeZone:timeZone];
        [TDAnalytics trackWithEventModel:model withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(trackFirstEvent:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        NSString *eventName = options[@"eventName"];
        NSString *eventId = options[@"eventId"];
        NSDate *date;
        NSTimeZone *timeZone;
        if (options[@"time"]) {
            NSTimeInterval timestamp = [options[@"time"] doubleValue];
            if(timestamp > 0){
                date = [NSDate dateWithTimeIntervalSince1970:timestamp / 1000.0];
            }
        }
        if (options[@"timeZone"]) {
            timeZone = [self getTimeZone:options[@"timeZone"]];
        }
        TDFirstEventModel *model = [[TDFirstEventModel alloc]initWithEventName:eventName firstCheckID:eventId];
        model.properties = options[@"properties"];
        [model configTime:date timeZone:timeZone];
        [TDAnalytics trackWithEventModel:model withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(timeEvent:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        NSString *eventName = options[@"eventName"];
        [TDAnalytics timeEvent:eventName withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(login:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        NSString *loginId = options[@"loginId"];
        [TDAnalytics login:loginId withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(logout:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics logoutWithAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(userSet:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics userSet:options[@"properties"] withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(userUnset:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics userUnset:options[@"property"] withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(userSetOnce:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics userSetOnce:options[@"properties"] withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(userAdd:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics userAdd:options[@"properties"] withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(userDel:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics userDeleteWithAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(userAppend:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics userAppend:options[@"properties"] withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(userUniqAppend:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics userUniqAppend:options[@"properties"] withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}


RCT_EXPORT_METHOD(setSuperProperties:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics setSuperProperties:options[@"properties"] withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(unsetSuperProperty:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics unsetSuperProperty:options[@"property"] withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(clearSuperProperties:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics clearSuperPropertiesWithAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(identify:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics setDistinctId:options[@"distinctId"] withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(flush:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        [TDAnalytics flushWithAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(enableAutoTrack:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        NSInteger types = [options[@"autoTrackType"]integerValue];
        self.isEnableAutoTrack = YES;
        if ((types & TDAutoTrackEventTypeAppViewScreen) > 0) {
            self.isEnablePageView = YES;
            types &= ~TDAutoTrackEventTypeAppViewScreen;
            for (NSDictionary *event in self.previewPageViewList) {
                [TDAnalytics track:@"ta_app_view" properties:event];
            }
            [self.previewPageViewList removeAllObjects];
        } else {
            [self.previewPageViewList removeAllObjects];
        }
        self.isEnableViewClick = (types & TDAutoTrackEventTypeAppClick) > 0;
        [TDAnalytics enableAutoTrack:types properties:options[@"properties"] withAppId:appId];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(calibrateTime:(NSDictionary *)options) {
    @try {
        double timeStampMillis = [options[@"timeStampMillis"] doubleValue];
        [TDAnalytics calibrateTime:timeStampMillis];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(calibrateTimeWithNtp:(NSDictionary *)options) {
    @try {
        NSString *ntp_server = options[@"ntp_server"];
        [TDAnalytics calibrateTimeWithNtp:ntp_server];
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(enableThirdPartySharing:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        if(options[@"types"]){
            NSInteger types = [options[@"types"]integerValue];
            if(options[@"params"]){
                [TDAnalytics enableThirdPartySharing:types properties:options[@"params"] withAppId:appId];
            }else{
                [TDAnalytics enableThirdPartySharing:types withAppId:appId];
            }
        }
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(setTrackStatus:(NSDictionary *)options) {
    @try {
        NSString *appId = options[@"appId"];
        NSString *status = options[@"status"];
        if([@"pause" isEqualToString: status]){
            [TDAnalytics setTrackStatus:TDTrackStatusPause withAppId:appId];
        }else if([@"stop" isEqualToString:status]){
            [TDAnalytics setTrackStatus:TDTrackStatusStop withAppId:appId];
        }else if([@"saveOnly" isEqualToString:status]){
            [TDAnalytics setTrackStatus:TDTrackStatusSaveOnly withAppId:appId];
        }else{
            [TDAnalytics setTrackStatus:TDTrackStatusNormal withAppId:appId];
        }
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(getPresetProperties:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
    @try {
        NSString *appId = options[@"appId"];
        TDPresetProperties *presetProperties = [TDAnalytics getPresetPropertiesWithAppId:appId];
        resolve([presetProperties toEventPresetProperties]);
    } @catch (NSException *exception) {
        resolve(nil);
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(getSuperProperties:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
    @try {
        NSString *appId = options[@"appId"];
        NSDictionary *superProperties = [TDAnalytics getSuperPropertiesWithAppId:appId];
        resolve(superProperties);
    } @catch (NSException *exception) {
        resolve(nil);
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(getDistinctId:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
    @try {
        resolve([TDAnalytics getDistinctIdWithAppId:options[@"appId"]]);
    } @catch (NSException *exception) {
        resolve(nil);
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(getAccountId:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
    @try {
        resolve([TDAnalytics getAccountIdWithAppId:options[@"appId"]]);
    } @catch (NSException *exception) {
        resolve(nil);
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(getDeviceId:(NSDictionary *)options :(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject) {
    @try {
        resolve([TDAnalytics getDeviceId]);
    } @catch (NSException *exception) {
        resolve(nil);
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(trackViewScreen:(NSDictionary *)options) {
    @try {
        NSString *tdUrl = options[@"thinkingdataurl"];
        NSDictionary *thinkingDataParams = options[@"thinkingdataparams"];
        NSMutableDictionary *properties = [NSMutableDictionary dictionary];
        if (thinkingDataParams) {
            [properties addEntriesFromDictionary:thinkingDataParams];
        }
        BOOL isIgnore = [[properties objectForKey:@"TDIgnoreViewScreen"] boolValue];
        if (isIgnore) {
            return;
        }
        [properties removeObjectForKey:@"TDIgnoreViewScreen"];
        if (!properties[@"#title"] && tdUrl) {
            properties[@"#title"] = tdUrl;
        }
        if (self.lastScreenName.length > 0) {
            properties[@"#referrer"] = self.lastScreenName;
        }
        if (!properties[@"#screen_name"] && tdUrl) {
            self.lastScreenName = tdUrl;
            properties[@"#screen_name"] = tdUrl;
        } else if (properties[@"#screen_name"]) {
            self.lastScreenName = properties[@"#screen_name"];
        }
        self.mCurrentTitle = properties[@"#title"];
        self.mCurrentScreenName = properties[@"#screen_name"];
        if (self.isEnableAutoTrack) {
            if (self.isEnablePageView) {
                [TDAnalytics track:@"ta_app_view" properties:properties];
            }
        } else {
            if (self.previewPageViewList.count < 5) {
                [self.previewPageViewList addObject:[properties copy]];
            }
        }
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(trackViewClick:(NSInteger)viewId) {
    @try {
        if(!self.isEnableViewClick) return;
        NSNumber *viewIdKey = @(viewId);
        NSDictionary *dict = self.viewPropertiesMap[viewIdKey];
        if(dict && ![dict[@"isIgnore"] boolValue]){
            NSMutableDictionary *params = dict[@"params"];
            params[@"#element_content"] = dict[@"elementContent"];
            params[@"#title"] = self.mCurrentTitle;
            params[@"#screen_name"] = self.mCurrentScreenName;
            [TDAnalytics track:@"ta_app_click" properties:params];
        }
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(saveViewProperties:(NSInteger)viewId
                  title:(NSString *)title
                  viewProperties:(NSDictionary *)viewProperties) {
    @try {
        NSMutableDictionary *mutableProps = [viewProperties mutableCopy] ?: [NSMutableDictionary dictionary];
        BOOL isIgnore = NO;
        if(mutableProps[@"TDIgnoreViewClick"]){
            isIgnore = mutableProps[@"TDIgnoreViewClick"];
            [mutableProps removeObjectForKey:@"TDIgnoreViewClick"];
        }
        self.viewPropertiesMap[@(viewId)] = @{
            @"elementContent": title ?: @"",
            @"params": mutableProps ?: @{},
            @"isIgnore":@(isIgnore)
        };
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

RCT_EXPORT_METHOD(saveRootViewProperties:(NSInteger)viewId title:(NSString *)title viewProperties:(NSDictionary *)viewProperties rootTag:(NSInteger)rootTag) {
    @try {
        NSMutableDictionary *mutableProps = [viewProperties mutableCopy] ?: [NSMutableDictionary dictionary];
        BOOL isIgnore = NO;
        if(mutableProps[@"TDIgnoreViewClick"]){
            isIgnore = mutableProps[@"TDIgnoreViewClick"];
            [mutableProps removeObjectForKey:@"TDIgnoreViewClick"];
        }
        self.viewPropertiesMap[@(viewId)] = @{
            @"elementContent": title ?: @"",
            @"params": mutableProps ?: @{},
            @"isIgnore":@(isIgnore)
        };
    } @catch (NSException *exception) {
        NSLog(@"[ThinkingAnalyticsSDK] error:%@",exception);
    }
}

@end