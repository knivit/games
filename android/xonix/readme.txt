# How To Build
--------------
1. Use maven >= 3.0.3
2. Install android.jar
   mvn install:install-file -DgroupId=com.google.android -DartifactId=android -Dversion=4.1 -Dpackaging=jar -Dfile=./android.jar
3. mvn clean package

# Android Installation
----------------------
1. Install VirtualBox
2. Get android-x86 iso
3. Create AndroidVM & install
4. To connect adb utility to VM,
  - close android VM if it is running
  (Windows)
  - VBoxManage modifyvm android --natpf1 adb,tcp,*,5555,*,5555
  (Ubuntu)
  /usr/lib/virtualbox/VBoxManage modifyvm android --natpf1 adb,tcp,*,5555,*,5555
  or through UI:
    - Network -> Adapter1 -> Port Forwarding
    - add new rule:
      Name: adb
      Protocol: TCP
      Host IP: *
      Host Port: 5555
      Guest IP: *
      Guest Port: 5555
  - start VM

# Developing the App
--------------------
1. adb connect localhost:5555
2. adb install xonix.apk
3. adb uninstall adb uninstall com.tsoft.game.xonix

# HowTo Debug (Dalvik Debug Monitor)
------------------------------------
$ ddms &
Get the port number (before /) and use it in Idea Remote Debugging

# HowTo Get a Logging from the Application
------------------------------------------
$ adb shell
# setprop
usage: setprop <key> <value>
# setprop log.tag.GAME_TIMING_TAG VERBOSE
