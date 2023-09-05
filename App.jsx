import React, { useEffect, useState } from 'react';
import { View, Text, Image, FlatList } from 'react-native';
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

  return (
    <View>
      <FlatList
        data={appList}
        renderItem={({ item }) => (
          <View>
            <Text>{item.name}</Text>
            {item.icon && (
              <Image
                style={{ width: 50, height: 50 }}
                source={{ uri: 'data:image/png;base64,' + item.icon }}
              />
            )}
          </View>
        )}
        keyExtractor={(item, index) => index.toString()}
      />
    </View>
  );
};

export default App;
