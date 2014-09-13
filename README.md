# README #

## FFmpeg video manipulation app for android ##

### What is this repository for? ###

* App for Android. Change Resolution and Frame Rate of existing videos. 
* Using the FFMPEG Library for Android by Guardian Project
* Using the aFileChooser library by iPaulPro for file browsing

### How do I get set up? ###

* Clone Repository
* Clone [guardianproject](https://github.com/guardianproject/android-ffmpeg-java) and add it as a library
* Clone [aFileChooser](https://github.com/iPaulPro/aFileChooser), add it as a library, and change the following code of FileUtils.java:
```
#!java
public static Intent createGetContentIntent() {
    ...
    // The MIME data type filter
    intent.setType(MIME_TYPE_VIDEO); // to browse

    ...
}
```
.

![](http://imgur.com/PuS5wtc)
![](https://github.com/hpimentel/Android_ffmpeg/github/Navigation.png)
![](https://github.com/hpimentel/Android_ffmpeg/github/Output.png)


### Programming ###

* Tutorial for Navigation Drawer Menu: [link](http://www.tutecentral.com/android-custom-navigation-drawer/)

### TODO ###

* Show a progress bar when videos are being converted, problably undeterministic
* Have a history of the videos converted, maybe even a log for errors
* Fix videos orientation bug