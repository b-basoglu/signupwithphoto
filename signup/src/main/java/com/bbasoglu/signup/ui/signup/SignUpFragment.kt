package com.bbasoglu.signup.ui.signup

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bbasoglu.core.extensions.STORAGE_PERMISSION
import com.bbasoglu.core.extensions.hideKeyBoard
import com.bbasoglu.core.extensions.showPopup
import com.bbasoglu.core.ui.BaseFragment
import com.bbasoglu.core.utils.FileUtils
import com.bbasoglu.signup.data.model.ui.SignUpFragmentUiModel
import com.bbasoglu.signup.databinding.FragmentSignupBinding
import com.bbasoglu.signup.ui.signup.adapter.CustomAdapterEvent
import com.bbasoglu.signup.ui.signup.adapter.InputTextType
import com.bbasoglu.signup.ui.signup.adapter.SignUpAdapter
import com.bbasoglu.signup.ui.signup.adapter.data.SignUpHeaderData
import com.bbasoglu.signup.ui.signup.adapter.data.SignUpInputViewData
import com.bbasoglu.uimodule.adapter.BaseAdapterData
import com.bbasoglu.uimodule.dialog.generic.BottomSheetGenericDialog
import com.bbasoglu.uimodule.dialog.generic.SelectItemData
import com.bbasoglu.uimodule.dialog.popup.Popup
import com.bbasoglu.uimodule.toolbar.StandardToolbarData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class SignUpFragment: BaseFragment() {
    private val signupListAdapter by lazy {
        SignUpAdapter(::signUpListener)
    }
    companion object {
        private const val SAVED_DATA = "SAVED_DATA"
        private const val MIME_TYPE_IMAGE = "image/*"
        private const val MAX_FILE_SIZE = 2_097_152 * 2L
    }

    private var currentPhotoPath: String? = null
    private var currentFileName: String? = null
    private var shareUri: Uri?=null
    private var cameraPermissionGranted = false
    private var readPermissionGranted = false
    private var writePermissionGranted = false


    private lateinit var binding : FragmentSignupBinding

    override val viewModel by viewModels<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentSignupBinding.inflate(inflater)
        val signUpFragmentUiModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            savedInstanceState?.getParcelable(SAVED_DATA, SignUpFragmentUiModel::class.java)
        } else {
            savedInstanceState?.getParcelable<SignUpFragmentUiModel>(SAVED_DATA)
        }
        viewModel.signUpFragmentUiModel.value.path?.let {
            signUpFragmentUiModel?.path = it
        }
        signUpFragmentUiModel?.let {
            viewModel.setSignUpFragmentUiModel(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerview()
        initListeners()
        initViewListeners()
        setToolbar()

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putParcelable(SAVED_DATA, viewModel.signUpFragmentUiModel.value)
    }


    private fun initViewListeners() {
        binding.continueBtn.setOnClickListener {
            viewModel.saveUserData(signupListAdapter.getLoginData())
        }
        binding.root.setOnClickListener {
            hideKeyBoard()
        }
    }

    private fun initListeners() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorStateFlow.collectLatest {
                    if (it != null) {
                        viewModel.clearErrorState()
                        binding.snackbarAction.setText(
                            getString(com.bbasoglu.common.R.string.please_fill,
                            when(it){
                                    ErrorType.FILL_EMAIL -> {
                                        getString(com.bbasoglu.common.R.string.email_address)
                                    }
                                    ErrorType.FILL_PASSWORD -> {
                                        getString(com.bbasoglu.common.R.string.password)
                                    }
                                    else -> {
                                        getString(com.bbasoglu.common.R.string.email_address)+" & "+getString(com.bbasoglu.common.R.string.password)
                                    }
                                }
                            )
                        )
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigatorStateFlow.collectLatest {
                    if (it != null) {
                        viewModel.clearNavigateState()
                        navigateTo(
                            SignUpFragmentDirections.actionSignUpToLogin()
                        )
                    }
                }
            }
        }
    }

    private fun setRecyclerview() {
        binding.run {
            listSignUp.layoutManager = LinearLayoutManager(requireContext())
            listSignUp.adapter = signupListAdapter
            signupListAdapter.submitList(createList())
        }
    }

    private fun createList() = arrayListOf<BaseAdapterData>(
        SignUpHeaderData(
            id ="1",
            getString(com.bbasoglu.common.R.string.profile_creation),
            getString(com.bbasoglu.common.R.string.use_form_exp),
            getString(com.bbasoglu.common.R.string.add_avatar),
            path = viewModel.signUpFragmentUiModel.value.path
        ),
        SignUpInputViewData(
            id = "2",
            hint = getString(com.bbasoglu.common.R.string.first_name),
            input = InputTextType.FIRST_NAME,
            text = viewModel.signUpFragmentUiModel.value.name
        ),
        SignUpInputViewData(
            id = "3",
            hint = getString(com.bbasoglu.common.R.string.email_address),
            input = InputTextType.EMAIL,
            isRequired = true,
            text = viewModel.signUpFragmentUiModel.value.email
        ),
        SignUpInputViewData(
            id = "4",
            hint = getString(com.bbasoglu.common.R.string.password),
            input = InputTextType.PASSWORD,
            isRequired = true,
            text = viewModel.signUpFragmentUiModel.value.password
        ),
        SignUpInputViewData(
            id = "5",
            hint = getString(com.bbasoglu.common.R.string.website),
            input = InputTextType.WEBSITE,
            text = viewModel.signUpFragmentUiModel.value.website
        )
    )


    private fun selectImage() {
        val choice = arrayListOf<Pair<@DrawableRes Int,String>>(
            Pair(com.bbasoglu.uimodule.R.drawable.ic_gallery,getString(com.bbasoglu.common.R.string.gallery)),
        )
        if(requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            choice.add(0,Pair(com.bbasoglu.uimodule.R.drawable.ic_take_photo,getString(com.bbasoglu.common.R.string.camera)))
        }
        BottomSheetGenericDialog(requireContext()).show {
            setTitle(getString(com.bbasoglu.common.R.string.select_from))
            setData(
                choice.map { pair ->
                    SelectItemData(id = pair.second,leftDrawable = pair.first)
                }
            )
            setSelectedListener{ position, key ->
                when (key?.id) {
                    // Select "Choose from Gallery" to pick image from gallery
                    getString(com.bbasoglu.common.R.string.camera) -> {
                        takePicture()
                    }
                    // Select "Take Photo" to take a photo
                    getString(com.bbasoglu.common.R.string.gallery) -> {
                        openGallery()
                    }
                }
            }
        }
    }
    fun getImageDirectoryPath(): String{
        return Environment.DIRECTORY_PICTURES + File.separator + getString(com.bbasoglu.common.R.string.image_file_path)
    }

    fun getAppSpecificAlbumStorageDir(context: Context, albumName: String, subAlbumName: String): File? {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        val file = File(
            context.getExternalFilesDir(
                albumName
            ), subAlbumName
        )
        if (!file.mkdirs()) {
            Log.d("Error ", "Directory not created")
        }

        return file
    }
    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getAppSpecificAlbumStorageDir(requireContext(), Environment.DIRECTORY_PICTURES,getString(com.bbasoglu.common.R.string.image_file_path))
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            currentFileName = "JPEG_${timeStamp}_.jpg"
        }
    }
    private fun addImageInGallery() {
        currentPhotoPath?.let {
            val file = File(it)
            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    MediaStore.Images.Media.insertImage(
                        requireActivity().contentResolver,
                        file.absolutePath, file.name, null
                    )
                    requireContext().applicationContext.sendBroadcast(
                        Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)
                        )
                    )
                }else{
                    MediaScannerConnection.scanFile(requireActivity().applicationContext, arrayOf(file.absolutePath),
                        null, MediaScannerConnection.OnScanCompletedListener { path, uri ->
                            Log.i("ExternalStorage", "Scanned " + path + ":")
                            Log.i("ExternalStorage", "-> uri=" + uri)
                        })
                }
            } catch (e: FileNotFoundException) {
                Log.e("Could not share to gallery", e.toString())
            }
        }

    }

    private fun takePicture() {
        viewModel.setSignUpFragmentUiModel(signupListAdapter.getLoginData())
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create the File where the photo should go
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            Log.e("File create error ", ex.toString())
            null
        }
        // Continue only if the File was successfully created
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireActivity().applicationContext,
                requireActivity().applicationContext.packageName + ".provider",
                it
            )

            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, currentFileName)
                put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE_IMAGE)
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    getImageDirectoryPath()
                )
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            } else {
                val imageUri =
                    requireActivity().contentResolver.insert(
                        MediaStore.Images.Media.getContentUri(
                            MediaStore.VOLUME_EXTERNAL), values)
                if (imageUri != null) {
                    currentPhotoPath = imageUri.toString()
                    shareUri = imageUri
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            }
            cameraLauncher.launch(takePictureIntent)
        }
    }

    private fun openGallery() {
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }


        try {
            val intent = Intent(Intent.ACTION_PICK, collection).apply {
                type = MIME_TYPE_IMAGE
            }
            photoLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e("Photo pick intent fail", e.toString())
        }
    }
    private fun getFilePath(imageFile: File){
        val filePath: String = imageFile.path
        setImage(filePath)
    }

    private fun setImage(path:String){
        viewModel.alterPathSignUpFragmentUiModel(path)
        signupListAdapter.insertImage(path)
    }
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            currentPhotoPath?.let { currentPath ->
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    val file: File = File(currentPath)
                    shareUri = FileProvider.getUriForFile(
                        requireActivity().applicationContext,
                        requireActivity().applicationContext.packageName + ".provider",
                        file
                    )
                    if (file.exists()){
                        getFilePath(file)
                    }

                } else {
                    //val bitmap: Bitmap = getBitmapFromContentResolver(Uri.parse(currentPath))
                    //bitmap.reduceBitmapSize(MAX_FILE_SIZE)
                    FileUtils.getFileFromUri(Uri.parse(currentPath),requireContext())?.let { myFile->
                        if (myFile.exists()){
                            getFilePath(myFile)
                        }
                    }
                }
                //to show image in gallery
                addImageInGallery()
            }
        }
    }
    private val photoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            it?.data?.data?.also { uri ->
                shareUri = uri
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    FileUtils.getFileFromUri(uri,requireContext())?.let { myFile->
                        if (myFile.exists()){
                            getFilePath(myFile)
                        }
                    }
                }else{
                    FileUtils.getFileFromUri(uri,requireContext())?.let { myFile->
                        getFilePath(myFile)
                    }
                }
            }
        }
    }

    private fun updateOrRequestPermissions() {
        val hasCameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val hasReadPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            STORAGE_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        val minSdk33 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

        cameraPermissionGranted = hasCameraPermission
        readPermissionGranted = hasReadPermission || minSdk33
        writePermissionGranted = if (minSdk29 && !minSdk33){
            true
        }else{
            hasWritePermission
        }

        val permissionsToRequest = mutableListOf<String>()
        if(!cameraPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }
        if(!readPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if(!writePermissionGranted) {
            permissionsToRequest.add(STORAGE_PERMISSION)
        }
        if(permissionsToRequest.isNotEmpty()) {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }else{
            selectImage()
        }
    }

    private val permissionsLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        cameraPermissionGranted = permissions[Manifest.permission.CAMERA] ?: cameraPermissionGranted
        readPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
        writePermissionGranted = permissions[STORAGE_PERMISSION] ?: writePermissionGranted

        if(cameraPermissionGranted && writePermissionGranted && readPermissionGranted) {
            selectImage()
        } else {
            val popup = Popup(
                title = getString(com.bbasoglu.common.R.string.select_image_error),
                message = getString(com.bbasoglu.common.R.string.select_image_error_explain),
            )
            requireContext().showPopup(popup){
                Log.d("Request:","Permission reason explained to user")
            }
        }
    }


    private fun signUpListener(data: Any) {
        when (data) {
            is SignUpHeaderData -> {
                updateOrRequestPermissions()
            }
            is SignUpInputViewData -> {

            }
            is CustomAdapterEvent->{
                if (data == CustomAdapterEvent.HIDE_KEYBOARD){
                    hideKeyBoard()
                }
            }
        }
    }

    private fun setToolbar(){
        binding.run {
            toolbar.setToolbar(
                StandardToolbarData(
                    title = getString(com.bbasoglu.common.R.string.sing_up),
                    titleColorId = com.bbasoglu.uimodule.R.color.black,
                    isTitleCenter = true,
                )
            )
        }
    }

}