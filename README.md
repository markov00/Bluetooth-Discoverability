Bluetooth Always Discoverable
=============================================================

This app will provide a support for leave the bluetooth discoverability of Android phones prior to 2.3.3.

This application works only with *rooted* phones. You need the write permission for the /system/app folder.

The application start a service (even at a system startup time) that, will maintain discoverable the bluetooth adapter.
Using reflection the app will use methods *setDiscoverableTimeout* and *setScanMode* from BluetoothAdapter class, using reflection, and resetting the timer each 100 seconds.


Get started
-------------------------------------------------------------

1. Get the source

    git clone git://github.com/markov00/Bluetooth-Discoverability.git
    

1. Compile and create an apk with your preferred IDE


1. Push the apk inside /system/app folder on your rooted phone. 


If you need to mount the file system in write mode you can use command

    mount -o remount,rw -t yaffs2 /dev/block/mtdblock3 /system


Tested on
-------------------------------------------------------------
This app was tested on a rooted LG P500 2.2 android phone.

