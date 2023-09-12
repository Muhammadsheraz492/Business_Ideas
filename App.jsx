import React from 'react';
import { View, StyleSheet, Button } from 'react-native';
import { NativeModules } from 'react-native';

const App = () => {
  const OpenVPNModule = NativeModules.OpenVPNModule; // Assuming OpenVPNModule is the name of your native module
   
  const Connect=()=>{
    // let file=require('./home/muhammad/Documents/GitHub/Business_ideas/DualSpace/')
    OpenVPNModule.connect("/home/muhammad/Documents/GitHub/Business_ideas/DualSpace/profile-11.ovpn")
  .then(response => {
    console.log(response); // Handle success
  })
  .catch(error => {
    console.error(error); // Handle error
  });
  }
  return (
    <View style={styles.container}>
      <Button title="Centered Button" onPress={Connect} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default App;
