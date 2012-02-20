Bluetooth Always Discoverable
=============================================================

This app will provide a support for leave the bluetooth discoverability of Android phones prior to 2.3.3.
This application works only in rooted phones.
You also need the permission to write the /system/app folder


Get started
-------------------------------------------------------------

Get the source

	git clone git://github.com/markov00/Bluetooth-Discoverability.git

Create a signed apk with your preferred IDE

Push the apk inside /system/app folder on your rooted phone

if you need to mount the file system in write mode you can use command

     mount -o remount,rw -t yaffs2 /dev/block/mtdblock3 /system


