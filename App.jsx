import { View, Text ,NativeModules,Button} from 'react-native'
import React from 'react'

const App = () => {
const {AppInfoModule} = NativeModules;
const onPress = () => {
  AppInfoModule.getInstalledApplications()
    .then(result => {
        // Handle success if needed
        console.log(result);
    })
    .catch(error => {
        // Handle error if needed
        console.error(error);
    });

};
  return (
    <View>
      <Button
      title="Click to invoke your native module!"
      color="#841584"
      onPress={onPress}
    />
    </View>
  )
}

export default App