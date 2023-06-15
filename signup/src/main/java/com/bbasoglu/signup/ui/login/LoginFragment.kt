package com.bbasoglu.signup.ui.login

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bbasoglu.common.R
import com.bbasoglu.core.ui.BaseFragment
import com.bbasoglu.signup.databinding.FragmentLoginBinding
import com.bbasoglu.uimodule.extensions.dp2px
import com.bbasoglu.uimodule.extensions.gone
import com.bbasoglu.uimodule.extensions.visible
import com.bbasoglu.uimodule.toolbar.StandardCustomToolbar
import com.bbasoglu.uimodule.toolbar.StandardToolbarData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment: BaseFragment() {

    private lateinit var binding : FragmentLoginBinding

    override val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        initListener()
    }

    private fun initListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getImagePath().collectLatest {
                    if (it.isEmpty()){
                        binding.image.gone()
                    }else{
                        binding.image.visible()
                        Glide.with(binding.root.context)
                            .asBitmap()
                            .load(it)
                            .placeholder(com.bbasoglu.signup.R.drawable.ic_person)
                            .error(com.bbasoglu.signup.R.drawable.ic_person)
                            .transform(
                                CenterCrop(),
                                RoundedCorners(12.dp2px)
                            )
                            .into(binding.image)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getWebAddress().collectLatest {
                    if (it.isEmpty()){
                        binding.webAddress.gone()
                    }else{
                        binding.webAddress.visible()
                        setupHyperlink(it)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUserName().collectLatest {
                    if (it.isEmpty()){
                        binding.name.gone()
                    }else{
                        binding.name.visible()
                        binding.name.text = it
                        binding.header.text = getString(R.string.hello_name,it)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getEmail().collectLatest {
                    if (it.isEmpty()){
                        binding.email.gone()
                    }else{
                        binding.email.visible()
                        binding.email.text = it
                    }
                }
            }
        }
    }
    fun setupHyperlink(text:String) {
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
        binding.webAddress.text = spannableString
        binding.webAddress.setTextColor(Color.BLUE)
    }

    private fun setToolbar(){
        binding.run {
            toolbar.setToolbar(
                StandardToolbarData(
                    title = getString(R.string.sing_in),
                    titleColorId = com.bbasoglu.uimodule.R.color.black,
                    isTitleCenter = true,
                )
            )
        }
    }
}