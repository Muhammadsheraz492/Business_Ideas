import React, { useEffect, useState } from 'react';
import { View, Text, Image, FlatList, TouchableOpacity,DeviceEventEmitter } from 'react-native';
import { NativeModules } from 'react-native';

const App = () => {
  const [appList, setAppList] = useState([]);

  useEffect(() => {
    // Call the native Android module to get the list of installed applications with icons
    NativeModules.AppInfoModule.getInstalledApplications()
      .then((result) => {
        console.log(result);
        setAppList(result);
      })
      .catch((error) => {
        console.error('Error retrieving app list:', error);
      });
  }, []);
const Clone=(val)=>{
  NativeModules.AppInfoModule.copyAndOpenAPKFile(val)
  .then((result) => {
    console.log(result);
    // setAppList(result);
  })
  .catch((error) => {
    console.error('Error retrieving app list:', error);
  });
}
useEffect(() => {
  // Listen for an event indicating that the APK file is opened
  DeviceEventEmitter.addListener('APKOpened', () => {
    setAppOpened(true);
  });

  return () => {
    DeviceEventEmitter.removeAllListeners();
  };
}, []);

const openAndCopyAPK = async (val) => {
  console.log(val);
  try {
   var result= await NativeModules.AppInfoModule.cloneAndOpenAPKFile(val,"SherazTest");
   console.log(result);
  } catch (error) {
    console.error(error);
  }
};
  return (
    <View>
      <FlatList
        data={appList}
        renderItem={({ item }) => (
          <TouchableOpacity
          
          onPress={()=>openAndCopyAPK(item.packagename)}
          >
            <Text>{item.name}</Text>
            {item.icon && (
              <Image
                style={{ width: 50, height: 50 }}
                source={{ uri: 'data:image/png;base64,' + item.icon }}
              />
            )}
          </TouchableOpacity>
        )}
        keyExtractor={(item, index) => index.toString()}
      />
    </View>
  );
};

export default App;
