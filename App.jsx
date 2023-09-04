import { View, Text ,NativeModules} from 'react-native'
import React from 'react'

const App = () => {
  const { AppInfoModule } = NativeModules;
  // AppInfoModule.createCalendarEvent('testName', 'testLocation');
  // const onPress = () => {
  //   CalendarModule.createCalendarEvent('testName', 'testLocation');
  // };
// Request the list of installed applications
AppInfoModule.getInstalledApplications()
    .then(applications => {
        // Process the list of installed applications
        console.log(applications);
    })
    
    .catch(error => {
        console.error(error);
    });
  return (
    <View>
      <Text>App</Text>
    </View>
  )
}

export default App