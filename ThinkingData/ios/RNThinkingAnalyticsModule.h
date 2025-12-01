#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RNThinkingAnalyticsModule : NSObject<RCTBridgeModule>

@property (nonatomic, assign) BOOL isEnableAutoTrack;
@property (nonatomic, assign) BOOL isEnablePageView;
@property (nonatomic, assign) BOOL isEnableViewClick;
@property (nonatomic, strong) NSMutableArray *previewPageViewList;
@property (nonatomic, copy) NSString *lastScreenName;
@property (nonatomic, copy) NSString *mCurrentScreenName;
@property (nonatomic, copy) NSString *mCurrentTitle;
@property (nonatomic, strong) NSMutableDictionary *viewPropertiesMap;

@end
