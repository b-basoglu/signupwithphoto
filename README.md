# signupwithphoto


Platform = Android 
Software Development kit (SDK): Android SDK (Target SDK 33 Minimum SDK 24)
Language = Kotlin
Device Emulator = PIXEL 4 API 33 (Tests are tested on real device)
Used Tools and Technologies =
    - Developed using MVVM architecture (with usecase)
    - Kotlin coroutines used as concurrency design pattern
    - Hilt for dependecy injection
    - Datastore preferences used for user data storing
    - Kotlin Flows (and State Flows)
    - Navigation Component
    - View binding
    - Glide (Image loading library)
    - afollestad.materialdialogs library used for a generic dialog
    - Unit tests for SignUpUseCaseTest SignUpViewModelTest (more detailed than UI test)
    - Expresso for ui testing (because of tight schedule, I gave an example implementation MainFragmentTest and MainActivityTest) 


Structure
    This multi module application is implemented using single Activity mutiple fragment.

    "app" module is our application module which keeps Application class and MainActivity class. Navigation component is inialized and navigation controller provided to BaseActivity by MainActivity. Base activity and Base Fragment communicates between each other using ActivityListener for navigation. (We may add more functionality for this listener but for now it is sufficient)

    "uimodule" is where we implement ui elements to use in our features. We hold everthing that is related with UI(colors, dimensions, styles, custom UI...) that can be reused in here. We reduce boiler plate code and create less resources by this structure.

    "core" module contain core base elements such as data store instance, extensions, base network classes, file operations... (And also it can provide base instances such as base okhttp which should(must) be reused by different feature modules.)

    "signup" module where the feature is implemented. There is 2 fragment, 2 viewmodel, 1 usecase and 1 adapter. Data and domain models separated. A mapper used to map data model to ui model. UI states checked using coroutines. There is a Generic BaseAdapter(uimodule) implementation which reduce boiler plate code for recyclerview adapters. Glide used as an image loading library. User can take photo or pick photo from gallery.

    "common" module includes just strings for now. Common will never include any module. It will be used for preventing dependecy cycles.

Additional features ->
    When camera intent called some of the devices destroy activity so I saved data to saved instance state to recover
    Multi Module architexture is used.
    User not just take photo from camera, user can take phtoto from phones gallery. 
    A dialog which navigates photo or gallery
    Snackbar is shown to warn user for required fields when user press submit button.
    Android 33 permissions are considered with backward compatibility for gallery and camera.
    Sign up page is a recyclerview.
    Nearly every element is a custom view (including recyclerview rows that can be reused)
    Some tests are implemented because of lack of time I just can implement SignUpUseCaseTest SignUpViewModelTest and MainFragmentTest(just brief example of ui test) tests.
