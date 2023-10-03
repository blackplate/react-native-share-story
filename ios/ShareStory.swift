@objc(ShareStory)
class ShareStory: NSObject {
    let instagramScheme = URL(string: "instagram-stories://share?source_application")

    @objc
    func isInstagramAvailable(_ resolve: RCTPromiseResolveBlock,
                              rejecter reject: RCTPromiseRejectBlock) -> Void {
        resolve(UIApplication.shared.canOpenURL(instagramScheme!))
    }

    @objc
    func shareInstagramStory(_ config: NSDictionary,
               resolver resolve: RCTPromiseResolveBlock,
               rejecter reject: RCTPromiseRejectBlock) -> Void {
        if(UIApplication.shared.canOpenURL(instagramScheme!) && config["imageUrl"] != nil && config["appId"] != nil){

            let appID = config["appId"]
            let instagramShareScheme = URL(string: "instagram-stories://share?source_application=\(appID)")

            let url = URL(string: config["imageUrl"] as! String)
            var pasteboardItems: Dictionary<String, Any> = [:]

            let data = try? Data(contentsOf: url!) //make sure your image in this url does exist, otherwise unwrap in a if let check / try-catch
            DispatchQueue.main.async {
                pasteboardItems["com.instagram.sharedSticker.backgroundImage"] = data!
                UIPasteboard.general.items = [pasteboardItems]
                UIApplication.shared.openURL(instagramShareScheme!)
            }

            resolve(true)
        }
    }
}

