/*
 *
 *   Created by Amitskr & VnjVibhash on 2/21/24, 10:32 AM
 *   Copyright Ⓒ 2024. All rights reserved Ⓒ 2024 http://vivekajee.in/
 *   Last modified: 3/2/24, 12:59 PM
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 *   except in compliance with the License. You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENS... Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *    either express or implied. See the License for the specific language governing permissions and
 *    limitations under the License.
 * /
 */

package com.asvk.urlshield.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.asvk.urlshield.utilities.generics.GenericPref;

/**
 * Empty service for fake custom tabs.
 * <p>
 * referrer: <a href="https://chromium.googlesource.com/chromium/src/+/b71e98cdf14f18cb967a73857826f6e8c568cea0/chrome/android/java/src/org/chromium/chrome/browser/customtabs/CustomTabsConnectionService.java">...</a>
 */

public class CustomTabs extends Service {

    public static GenericPref.Bool SHOWTOAST_PREF(Context cntx) {
        return new GenericPref.Bool("ctabs_toast", false, cntx);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        log("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("onStartCommand\n" + intent.toUri(0));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        log("onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        log("onBind\n" + intent.toUri(0)); // a toast here, for some reason, isn't shown and later it crashes
        return null;
//        return new IBinder() {
//            @Override
//            public String getInterfaceDescriptor() throws RemoteException {
//                log("getInterfaceDescriptor");
//                return null;
//            }
//
//            @Override
//            public boolean pingBinder() {
//                log("pingBinder");
//                return false;
//            }
//
//            @Override
//            public boolean isBinderAlive() {
//                log("isBinderAlive");
//                return false;
//            }
//
//            @Override
//            public IInterface queryLocalInterface(String descriptor) {
//                log("queryLocalInterface");
//                return null;
//            }
//
//            @Override
//            public void dump(FileDescriptor fd, String[] args) throws RemoteException {
//                log("dump");
//            }
//
//            @Override
//            public void dumpAsync(FileDescriptor fd, String[] args) throws RemoteException {
//                log("dumpAsync");
//            }
//
//            @Override
//            public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
//                log("transact");
//                return false;
//            }
//
//            @Override
//            public void linkToDeath(DeathRecipient recipient, int flags) throws RemoteException {
//                log("linkToDeath");
//            }
//
//            @Override
//            public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
//                log("unlinkToDeath");
//                return false;
//            }
//        };
    }

    // ------------------- logging -------------------

    private static final String TAG = "CUSTOMTABS";

    private void log(String message) {
        Log.d(TAG, message);
        if (SHOWTOAST_PREF(this).get()) {
            Toast.makeText(this, TAG + ": " + message, Toast.LENGTH_LONG).show();
        }
    }
}
