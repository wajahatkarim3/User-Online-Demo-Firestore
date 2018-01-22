# User-Online-Demo-Firestore
This is a demo project to managing users' presence like users' online and offline status using Firestore, Firebase Realtime Database and Cloud Functions.

You can read about how it works at Medium on: https://android.jlelse.eu/fmanaging-user-presence-with-firestore-in-android-a1146bdf101d

Firestore stores all the users in Users collections with each document as different user. These documents contain name, status (online/offline), last active (timestamp), and about line.

Firebase Realtime Database stores each user's online or offline status. The purpose of this is to trigger cloud function in case of network failure or app exit through `onDisconnect()` method.

Following is the demo run on two devices. Left device is using `Zeeshan` as logged user, and right device is using `Wajahat` as logged user. When `Zeeshan` exits the app, right device can see his status change from `Online` to `Few Seconds Ago` and vice versa for `Wajahat` as well.

![](https://github.com/wajahatkarim3/User-Online-Demo-Firestore/blob/master/demo.gif)
