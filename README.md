# NDKExercise
This exercise is based on Frank Ableson's article<sup>[1]</sup> on native development kit in C++.
Java iterators are slow compared to pointer arithemtics.  Hence, image processing presents an excellent usage of native development in C++ through java native interface (JNI).  This exercise demonstrates spatial convolution, hough transforms as well as Jetpack data-binding, view-models, UI navigation drawer.

### Data binding, Navigation drawer (java)
The navigation drawer is based on Rohit Kumar Kanojia's article<sup>[7]</sup>.  All one-way xml data/event handler binding and view-models are based on Mitch Tabian's tutorial<sup>[6]</sup>.

<img width="280" src="https://user-images.githubusercontent.com/1282659/50551474-68fa7800-0c46-11e9-913e-b294929ad9d2.png"><img width="280" src="https://user-images.githubusercontent.com/1282659/50551471-68fa7800-0c46-11e9-9d95-56cac1fb0a7b.png">

### Convolution filters (C++)
Implementation of kernenls and convolution is well described in Gonzolas & Woods' book<sup>[3]</sup>.
For the sharpen filter, out-of-bound pixels are intentionally not corrected to show pixel locations and highlight.

- 7x7 blur RECT filter
- 3x3 sharpen
- 1 identity filter
- 3x3 derivative horizontal
- 3x3 derivate vertical
- 3x3 edge detection - isotropic

<img width="280" src="https://user-images.githubusercontent.com/1282659/50551468-68fa7800-0c46-11e9-9756-d25e369e71a7.png"><img width="280" src="https://user-images.githubusercontent.com/1282659/50551470-68fa7800-0c46-11e9-8c26-edd805ca1ef8.png">
<img width="280" src="https://user-images.githubusercontent.com/1282659/50551469-68fa7800-0c46-11e9-9e6b-e2c59afc0cc9.png">

### Hough Circle (C++)
Implementation of hough transform for circle detection is described in my old Flex example<sup>[5]</sup>
Cyan border around yellow marks the circle found.  The algorithm is as follows.
1. convolution -> edge detection [image #1].
2. create rho-theta plot of circle candidates.
3. threshold peak value as circle found.
4. draw highlight around circle on [image #2].

<img width="280" src="https://user-images.githubusercontent.com/1282659/50551475-6b5cd200-0c46-11e9-96ae-a2e96b4a6b39.png"><img width="280" src="https://user-images.githubusercontent.com/1282659/50551476-6b5cd200-0c46-11e9-92c0-9199217e6d90.png">

### Hough Line (C++)
Implementation of hough transform for line detection is described in Gonzolas & Woods' book<sup>[3]</sup>.
Black line border on yellow + cyan hexagon for line found.  
<img width="280" src="https://user-images.githubusercontent.com/1282659/50551472-68fa7800-0c46-11e9-9318-863ae29e29f0.png"><img width="280" src="https://user-images.githubusercontent.com/1282659/50551473-68fa7800-0c46-11e9-8a3c-e319ae19fdce.png">

### Devices
Tested on the following devices.
1. Tablet emulator, Pixel C API 28 Android 9
2. Samsung S9

### Unit, Espresso tests
none yet

### Configuration ../src/CMakeList.txt
Below addition is <bold>CRITICAL</bold> to JNI referencing to C++ classes.

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
```diff
+       src/main/cpp/CirclePoint.cpp
+       src/main/cpp/HoughCircle.cpp
+       src/main/cpp/HoughLine.cpp
+       src/main/cpp/Convolution.cpp
```
        src/main/cpp/native-lib.cpp)

### References

1. Extend/edit Frank Ableson's C++ (correct RGBA alignment and)
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
   
6. Jetpack architecture - databinding tutorial, Mitch Tabian                                                       
   https://www.youtube.com/watch?v=v4XO_y3RErI
   
7. Android Navigation Drawer by Rohit Kumar Kanojia
   https://medium.com/quick-code/android-navigation-drawer-e80f7fc2594f
   
