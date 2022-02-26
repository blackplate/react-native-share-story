#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ShareStory, NSObject)

RCT_EXTERN_METHOD(isInstagramAvailable: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(shareInstagramStory:(NSDictionary *)config
                  resolver: (RCTPromiseResolveBlock)resolve
                  rejecter: (RCTPromiseRejectBlock)reject)

@end
