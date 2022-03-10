import {NativeModules, Platform} from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-share-story' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ios: "- You have run 'pod install'\n", default: ''}) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const ShareStory = NativeModules.ShareStory
  ? NativeModules.ShareStory
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      },
    );

export function isInstagramAvailable(): Promise<boolean> {
  return ShareStory.isInstagramAvailable();
}

export function shareInstagramStory({
  imageUrl,
}: {
  imageUrl: string;
}): Promise<boolean> {
  return ShareStory.shareInstagramStory({imageUrl});
}
