package com.example.miku.ipc_messenger_test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ClientActivity extends AppCompatActivity {
    private Button button;
    private Messenger messenger;
    private ServiceConnection src = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
       private Messenger client = new Messenger(new Handler(){
            @Override
            public void handleMessage(Message msg) {
              switch (msg.what){
                  case 2:
                      Log.d("ClientActivity","msg from Service:"+msg.getData().getString("key"));
                      break;
              }
            }
        });
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(src);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        button = (Button)findViewById(R.id.start);
        Intent intent = new Intent(ClientActivity.this,MyService.class);
        bindService(intent,src, Context.BIND_AUTO_CREATE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SendMsg();
            }
        });

    }
    private void SendMsg(){
        Message msg = Message.obtain(null,1);
        Bundle bundle = new Bundle();
        bundle.putString("data","Hello Service");
        msg.setData(bundle);
        //发送时将自己的Messager对象通过replyTo传递给Service.
        msg.replyTo = client;
        try{
            messenger.send(msg);
        }catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
}
