package com.example.umborno;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.example.umborno.http.ApiInterface;

import java.util.ArrayList;

public class LocationService extends Service {
    /** Keeps track of all current registered clients. */
    ArrayList<Messenger> mClients = new ArrayList<>();
    /** Holds last value set by a client. */
    int mValue = 0;

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_SET_VALUE = 3;

    public LocationService() {
    }

    //Handler of incoming msgs from clients
    class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_SET_VALUE:
                    mValue = msg.arg1;
                    for(int i = mClients.size()-1;i>=0;i--){
                        try{
                            Message message = Message.obtain(null,MSG_SET_VALUE,mValue,0);
                            mClients.get(i).send(message);
                        }catch (RemoteException e){
                            // The client is dead.  Remove it from the list;
                            // we are going through the list from back to front
                            // so this is safe to do inside the loop.
                            mClients.remove(i);
                        }
                    }
                    break;
                    default:
                        super.handleMessage(msg);
            }

        }
    }
    final Messenger mMessenger = new Messenger(new IncomingHandler());


    //for local service
    /*public class LocationBinder extends Binder{
        LocationService getService(){
            return LocationService.this;
        }
    }

    private final IBinder mBinder = new LocationBinder();*/

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //todo: move locationhelper here

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //todo
    }
}
