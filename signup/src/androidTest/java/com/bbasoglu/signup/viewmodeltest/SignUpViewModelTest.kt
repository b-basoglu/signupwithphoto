package com.bbasoglu.signup.viewmodeltest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bbasoglu.signup.data.model.data.LoginFragmentDataModel
import com.bbasoglu.signup.ui.signup.ErrorType
import com.bbasoglu.signup.ui.signup.SignUpViewModel
import com.bbasoglu.signup.usecase.SignUpSaveUseCase
import com.bbasoglu.signup.utils.MainCoroutineRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.*
import org.junit.rules.RuleChain
import javax.inject.Inject
import kotlin.jvm.Throws

@HiltAndroidTest
class SignUpViewModelTest {

    private lateinit var viewModel: SignUpViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()

    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)
        .around(coroutineRule)

    @Inject
    lateinit var signUpSaveUseCase: SignUpSaveUseCase



    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = SignUpViewModel(signUpSaveUseCase)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    @Throws(InterruptedException::class)
    fun testInitialStateFlow() = run {
        Assert.assertNull(viewModel.errorStateFlow.value)
        Assert.assertNull(viewModel.navigatorStateFlow.value)
    }

    @Test
    @Throws(InterruptedException::class)
    fun setEmptyUserData() = coroutineRule.run {
        viewModel.saveUserData(LoginFragmentDataModel())
        Assert.assertTrue(viewModel.errorStateFlow.value == ErrorType.FILL_REMAINING)
    }

    @Test
    @Throws(InterruptedException::class)
    fun setEmptyPasswordEmailUserData() = coroutineRule.run {
        viewModel.saveUserData(LoginFragmentDataModel())
        Assert.assertTrue(viewModel.errorStateFlow.value == ErrorType.FILL_REMAINING)
    }

    @Test
    @Throws(InterruptedException::class)
    fun setEmptyPasswordUserData() = coroutineRule.run {
        viewModel.saveUserData(LoginFragmentDataModel("1","1","1","1",null))
        Assert.assertTrue(viewModel.errorStateFlow.value == ErrorType.FILL_PASSWORD)
    }

    @Test
    @Throws(InterruptedException::class)
    fun setEmptyEmailUserData() = coroutineRule.run {
        viewModel.saveUserData(LoginFragmentDataModel("1","1","1",null,"1"))
        Assert.assertTrue(viewModel.errorStateFlow.value == ErrorType.FILL_EMAIL)
    }
    @Test
    @Throws(InterruptedException::class)
    fun setUserData() = coroutineRule.run {
        viewModel.saveUserData(LoginFragmentDataModel("1","1","1","1","1"))
        Assert.assertTrue(viewModel.errorStateFlow.value == null)
    }
}