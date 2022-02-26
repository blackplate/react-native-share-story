# react-native-share-story

Share to an Instagram Story

## Installation

```sh
yarn add react-native-share-story
```

#### Info.plist

+ add `instagram-stories` to the `LSApplicationQueriesSchemes` key in your app's Info.plist.

```diff
...
<key>LSApplicationQueriesSchemes</key>
<array>
	...
+	<string>instagram-stories</string>
</array>
...
```

## Usage

```js
import { isInstagramAvailable } from "react-native-share-story";

// ...

const result = await isInstagramAvailable();
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
