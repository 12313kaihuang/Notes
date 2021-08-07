package com.example.tests.ipc.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * @auther hy
 * create on 2021/08/01 下午5:01
 */
public class MessengerClient {

    private static class ClientMessengerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d(MessengerConstants.TAG, "client handleMessage: " + msg.what);
            if (msg.what == MessengerConstants.MSG_FROM_SERVER) {
                String msg1 = msg.getData().getString("msg", null);
                Log.d(MessengerConstants.TAG, "received msg from server: " + msg1);
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new ClientMessengerHandler());

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(MessengerConstants.TAG, "onServiceConnected: " + name);
            Messenger messenger = new Messenger(service);
            Message message = Message.obtain(null, MessengerConstants.MSG_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "hello, this is client.");
            message.setData(bundle);
            message.replyTo = mMessenger; //用于服务端回传
            try {
                Log.d(MessengerConstants.TAG, "send to server ");
                messenger.send(message);
            } catch (RemoteException e) {
                Log.e(MessengerConstants.TAG, "send to server  error", e);
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void bindService(Context context) {
        Intent intent = new Intent(context, MessengerService.class);
        //flag
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unBindService(Context context) {
        context.unbindService(connection);
    }
}
