package com.dualspace;

import android.content.Intent;
import android.net.VpnService;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyVpnService extends VpnService {
private static  final  String TAG="mYvpnsERVICE";
private final AtomicBoolean isRunning=new AtomicBoolean(false);
private ParcelFileDescriptor VpnInterface;
private String ServerIp;
private int ServerPortNumber;
private Handler handler=new Handler(Looper.getMainLooper());
public  ParcelFileDescriptor getVpnInterface(){
return  VpnInterface;
};
@Override
    public  int onStartCommand(Intent intent,int flag,int starId){
    if(intent!=null){
        Thread VpnThread=new Thread(new Runnable() {
            @Override
            public void run() {
                MyVpnService.this.runVpnConnection();

            }
        });
    }
    return  START_STICKY;
}

    private void runVpnConnection() {
  try{
      if(establishedVpnConnection()){

      }
  }catch (Exception e){
      Log.d(TAG,"Error during Vpn Connection"+e.getMessage());
  }finally {
      StopVpnConnection();
  }

}

    private void StopVpnConnection() {
    }

    private boolean establishedVpnConnection() throws IOException {

if(VpnInterface!=null){
    Builder builder=new Builder();
    builder.addAddress(ServerIp,32);
    builder.addRoute("0.0.0.0",0);
    VpnInterface= builder.setSession(getString(R.string.app_name)).setConfigureIntent(null).establish();
    return  VpnInterface!=null;
}else {
    handler.post(new Runnable() {
        @Override
        public void run() {
            Toast.makeText(MyVpnService.this, "Vpn ConnectionAlready Established", Toast.LENGTH_SHORT).show();
        }
    });
}
return true;
}

private void readFromVpnInterface() throws  IOException{
    isRunning.set(true);
    ByteBuffer buffer=ByteBuffer.allocate(32767);
    while (isRunning.get()){
        try{
            FileInputStream inputStream=new FileInputStream(VpnInterface.getFileDescriptor());
            int length=inputStream.read(buffer.array());
            if(length>0){
                String recivedData=new String(buffer.array(),0,length);
                Intent intent=new Intent("received_data_from_vpn");
                intent.putExtra("data",recivedData);
//                LocalBroadcasrManager.
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
}