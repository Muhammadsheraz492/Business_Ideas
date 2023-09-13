import React, { useState, useEffect } from 'react';
import { View, Text, Button, FlatList, TouchableOpacity,PermissionsAndroid } from 'react-native';
import axios from 'axios';
import { Converter } from 'csvtojson';
import { NativeModules } from 'react-native';
import { request, PERMISSIONS } from 'react-native-permissions';
import * as Keychain from 'react-native-keychain';
// Call the prepare method
export default function App() {
  const { OpenVPNModule,AppInfoModule } = NativeModules;
  const [vpnList, setVpnList] = useState([]);
  const [Package_list,SetPackage_list]=useState([])
  const EstablishConnection=async(item)=>{
  
             OpenVPNModule.prepare(item.OpenVPN_ConfigData_Base64, item.CountryLong, 'vpn', 'vpn',)
      .then(result => {
          console.log('Success:', result);
      })
      .catch(error => {
          console.error('Error:', error);
      }); 
  }
  const getVPNServers = async () => {
    try {
      const response = await axios.get('http://www.vpngate.net/api/iphone/');
      const csvString = response.data.split('#')[1].replace(/\*/g, '');

      const csvConverter = new Converter();
      const list = await csvConverter.fromString(csvString);
//  console.log(list[0]);
      setVpnList(list); // Update the state with the server list
    console.log("Get Second");

    } catch (e) {
      console.error('getVPNServers Error:', e);
      // Handle the error as needed
    }
  };
const Packages=()=>{
  let dat=[];
  AppInfoModule.getInstalledApplications().then((data)=>{
    // data.
    data.filter((e)=>{
      dat.push(e.packagename);
    })
    SetPackage_list(dat);
    console.log("Get First");
    getVPNServers();
  }).catch((err)=>{
    console.log(err);
  })
}
  useEffect(() => {
    // Call getVPNServers when the component mounts

    // getVPNServers();
    Packages();
  }, []);

  const renderItem = ({ item }) => (
    <TouchableOpacity
    
    onPress={()=>EstablishConnection(item)}
    >

    <View style={{ margin: 10 }}>
      <Text>{JSON.stringify(item)}</Text>
    </View>
    </TouchableOpacity>
  );

  return (
    <View>
      <Text>VPN Server List:</Text>
      <FlatList
        data={vpnList}
        renderItem={renderItem}
        keyExtractor={(item, index) => index.toString()}
      />
      <Button title="Refresh" onPress={getVPNServers} />
    </View>
  );
}
