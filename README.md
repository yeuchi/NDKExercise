# NDKExercise
NDK exercise base on Frank Ableson's example.  Added spatial convolution capability with the following examples.  User may define custom kernel.

### Convolution filters 
- 7x7 blur RECT filter
- 3x3 sharpen
- 1 identity filter
- 3x3 derivative horizontal
- 3x3 derivate vertical
- 3x3 edge detection - isotropic

<img width="280" src="https://user-images.githubusercontent.com/1282659/49413992-6c7a0c80-f737-11e8-8dcd-e1160f64513f.png"><img width="280" src="https://user-images.githubusercontent.com/1282659/49452005-feb8f980-f7a5-11e8-92bf-1c1add07f123.png"><img width="280" src="https://user-images.githubusercontent.com/1282659/49452012-0082bd00-f7a6-11e8-980f-2d7821e9aa18.png">

### Device
Tested on Tablet emulator, Pixel C API 28 Android 9

### Reference

1. Reuse existing C code with the Android NDK by Frank Ableson
   https://www.ibm.com/developerworks/opensource/tutorials/os-androidndk/os-androidndk-pdf.pdf
   
2. Sending int[]s between Java and C                                     
   https://stackoverflow.com/questions/4841345/sending-ints-between-java-and-c

3. Digital Image Processing by Gonzalez and Woods, 1993. pg 189 - 215 ISBN:0-201-50803-6

4. NDK-stack tool for debugging
https://developer.android.com/ndk/guides/ndk-stack
