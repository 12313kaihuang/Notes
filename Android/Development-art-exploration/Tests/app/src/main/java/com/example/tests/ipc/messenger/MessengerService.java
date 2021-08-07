package com.example.tests.ipc.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @auther hy
 * create on 2021/08/01 下午4:56
 * <p>
 * Service运行在另一个进程中，过滤log的时候注意要选中no Filter
 */
public class MessengerService extends Service {

    private static class MessengerHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d(MessengerConstants.TAG, "server handleMessage: " + msg.what);
            if (msg.what == MessengerConstants.MSG_FROM_CLIENT) {
                Bundle data = msg.getData();
                String msg1 = data.getString("msg", null);
                Log.d(MessengerConstants.TAG, "received msg from client: " + msg1);

                //服务端回传数据
                Message message = Message.obtain(null, MessengerConstants.MSG_FROM_SERVER);
                Bundle bundle = new Bundle();
                bundle.putString("msg", "got it");
                message.setData(bundle);
                try {
                    Log.d(MessengerConstants.TAG, "send to client ");
                    msg.replyTo.send(message); //注意这里
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e(MessengerConstants.TAG, "send to client error", e);
                }
            } else {
                super.handleMessage(msg);
            }
        }
    }

    private Messenger messenger;

    @Override
    public void onCreate() {
        super.onCreate();
        messenger = new Messenger(new MessengerHandler());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
