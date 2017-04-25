package com.example.miku.ipc_messenger_test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by miku on 2017/4/25.
 */
public class MyService extends Service {
    public MyService() {
        super();
    }

    private final Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.d("MyService", "receive msg from client:" + msg.getData().getString("data"));
                    //得到client的Messager对象
                    Messenger client = msg.replyTo;
                    Message message = Message.obtain(null,2);
                    Bundle bundle = new Bundle();
                    bundle.putString("key","Hello Client");
                    message.setData(bundle);
                    try {
                            Thread.sleep(1000);
                            client.send(message);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


}
