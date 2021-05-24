# PhotoGallery

Simple app to display photos in an image gallery

PhotoGallery is designed in Kotlin, with MVVM and Jetpack architecture such as Room, LiveData, Courotines, Databinding, ktx. Piccasso, JUnit5 test, Retrofit2 and Gson have also been used.

The application is in Spanish and English, it changes automatically according to the regional settings of your smartphone.

The App is prepared to work in both landscape and portrait mode.



## Considerations

The CRUD operations are mocked with the local repository (Room SQL), since the API does not provide these operations.

URL SERVICE:  https://jsonplaceholder.typicode.com/photos/



## Technical specifications


	- Kotlin 1.4 programming language.
	
	- Development enviroment:
	    - Android Studio 4.2
	    - Build #AI-202.7660.26.42.7322048, built on April 29, 2021
        - Runtime version: 11.0.8+10-b944.6842174 amd64
        - VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
        - OS: Windows 10 10.0
        
		
	- SDK: minSdkVersion 23 , targetSdkVersion 30
	
	- Libraries used:
       - lifecycle-viewmodel-ktx:2.3.1 : Live Data - Databinding - Jetpack 
       - retrofit:2.9.0 : Retrofit - Https services library
       - gson:2.8.6 : Gson - Parser library
       - picasso:2.71828 : To get images from URL.
       - lifecycle-extensions:2.2.0 : Lifecycle
       - kotlinx-coroutines-core:1.3.9 : Kotlin coroutines - Jetpack 
       - androidx.room:room-ktx:2.3.0 : Room Ktx - Jetpack
       - junit-jupiter-api:5.7.1 : JUnit 5 - Testing

## Storyboard
**NOTE:**  Screenshots in Spanish


**Step 1:** Open the App (A.Morales PhotoGallery)

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<img src=https://github.com/antoniomy82/PhotoGallery/blob/master/screenshots/00.JPG>

***
**Step 2 :** Add a photo from the gallery or using the photo camera.

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<img src=https://github.com/antoniomy82/PhotoGallery/blob/master/screenshots/01.JPG>

***

**Step 3 :** You can edit the title of a photo by clicking on the edit icon (pencil).

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<img src=https://github.com/antoniomy82/PhotoGallery/blob/master/screenshots/02.JPG>

***

**Step 4 :** You can add a photo from gallery by pressing add from gallery.

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<img src=https://github.com/antoniomy82/PhotoGallery/blob/master/screenshots/03.JPG>

***

**Simple Repository Test**

&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<img src=https://github.com/antoniomy82/PhotoGallery/blob/master/screenshots/simple_test.JPG>

