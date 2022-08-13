import Foundation
@objc(ShareStory)
class ShareStory: NSObject {
    let domain: String = "RNShareStory"
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
        
        if(UIApplication.shared.canOpenURL(instagramScheme!)){
            do {
                let imageUrl = config["imageUrl"] as! String
                                
                let url = URL(string: imageUrl)
                var pasteboardItems: Dictionary<String, Any> = [:]
                
                let data = try Data(contentsOf: url!) //make sure your image in this url does exist, otherwise unwrap in a if let check / try-catch
                DispatchQueue.main.async {
                    pasteboardItems["com.instagram.sharedSticker.backgroundImage"] = data
                    UIPasteboard.general.items = [pasteboardItems]
                    UIApplication.shared.openURL(self.instagramScheme!)
                }
                
                resolve(true)
            } catch let error {
                reject(domain, "Failed to share", error)
            }
        }
    }
}

