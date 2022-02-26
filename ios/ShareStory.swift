@objc(ShareStory)
class ShareStory: NSObject {
    let instagramScheme = URL(string: "instagram-stories://share")

    @objc
    func isInstagramAvailable(_ resolve: RCTPromiseResolveBlock,
                              rejecter reject: RCTPromiseRejectBlock) -> Void {
        resolve(UIApplication.shared.canOpenURL(instagramScheme!))
    }

    @objc
    func shareInstagramStory(_ config: NSDictionary,
               resolver resolve: RCTPromiseResolveBlock,
               rejecter reject: RCTPromiseRejectBlock) -> Void {
        resolve(true)
    }
}

