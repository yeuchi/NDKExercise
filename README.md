# NDKExercise
Native development kit in C++ exercise is based on Frank Ableson's example.  Expensive computations such as image processing is much more efficient in C/C++ than Java.  This exercise demonstrates those common computations: spatial convolution and hough transforms. 

Additionally, I am using Jetpack data-binding and view-models to handle UI events and data.

### Convolution filters 
- 7x7 blur RECT filter
- 3x3 sharpen
- 1 identity filter
- 3x3 derivative horizontal
- 3x3 derivate vertical
- 3x3 edge detection - isotropic

<img width="280" src="https://user-images.githubusercontent.com/1282659/49413992-6c7a0c80-f737-11e8-8dcd-e1160f64513f.png"><img width="280" src="https://user-images.githubusercontent.com/1282659/49452005-feb8f980-f7a5-11e8-92bf-1c1add07f123.png"><img width="280" src="https://user-images.githubusercontent.com/1282659/49452012-0082bd00-f7a6-11e8-980f-2d7821e9aa18.png">

### Hough Circle

Cyan border around yellow marks the circle found.  The algorithm is as follows.
1. convolution -> edge detection [image #1].
2. create rho-theta plot of circle candidates.
3. threshold peak value as circle found.
4. draw highlight around circle on [image #2].

<img width="400" src="https://user-images.githubusercontent.com/1282659/50365554-c6057800-053a-11e9-906f-c6140bf3691d.png"><img width="400" src="https://user-images.githubusercontent.com/1282659/50389139-9e094680-06eb-11e9-876f-cb443b566eca.png">

### Hough Line

Black line border on yellow + cyan hexagon for line found.  
<img width="400" src="https://user-images.githubusercontent.com/1282659/50387177-ef014680-06bb-11e9-8f2c-c0dbc824b0ad.png"><img width="400" src="https://user-images.githubusercontent.com/1282659/50389140-9ea1dd00-06eb-11e9-98d5-cb0807cde807.png">

### Jetpack Architecture - Data binding
User interface event handlers following the latest recommended android architecture data binding and viewModel classes.
https://developer.android.com/topic/libraries/data-binding/

### Devices
Tested on the following devices.
1. Tablet emulator, Pixel C API 28 Android 9
2. Samsung S9

### References

1. Reuse existing C code with the Android NDK by Frank Ableson
   https://www.ibm.com/developerworks/opensource/tutorials/os-androidndk/os-androidndk-pdf.pdf
   
2. Sending int[]s between Java and C                                     
   https://stackoverflow.com/questions/4841345/sending-ints-between-java-and-c

3. Digital Image Processing by Gonzalez and Woods, 1993. ISBN:0-201-50803-6
   - Convolution pg 189 - 215 
   - Hough pg 432

4. NDK-stack tool for debugging
   https://developer.android.com/ndk/guides/ndk-stack
   
5. Hough transform [my Adobe Flex implementation - right click for source code]
   http://www.ctyeung.com/flex/hough/srcview/index.html
   
