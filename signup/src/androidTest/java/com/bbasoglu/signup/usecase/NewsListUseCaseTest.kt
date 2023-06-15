package com.bbasoglu.signup.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bbasoglu.core.data.SignUpCoreDataStore
import com.bbasoglu.core.network.NetworkResponse
import com.bbasoglu.signup.data.model.data.LoginFragmentDataModel
import com.bbasoglu.signup.ui.login.LoginViewModel
import com.bbasoglu.signup.utils.MainCoroutineRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import javax.inject.Inject
import kotlin.jvm.Throws

@HiltAndroidTest
class SignUpUseCaseTest {
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()
    private lateinit var useCase: SignUpSaveUseCase
    private lateinit var viewModel: LoginViewModel
    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(coroutineRule)
        .around(instantTaskExecutorRule)

    @Inject
    lateinit var signUpCoreDataStore: SignUpCoreDataStore


    @Before
    fun setUp() {
        hiltRule.inject()
        useCase = SignUpSaveUseCase(signUpCoreDataStore)
        viewModel = LoginViewModel(signUpCoreDataStore)

    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    @Throws(InterruptedException::class)
    fun isValidParam() = run {
        val param1 = SignUpSaveUseCase.Param(LoginFragmentDataModel("1","2","2","2","2"))
        val param2 = SignUpSaveUseCase.Param(LoginFragmentDataModel())
        val param3 = SignUpSaveUseCase.Param(LoginFragmentDataModel("1","2","2","2",null))
        val param4 = SignUpSaveUseCase.Param(LoginFragmentDataModel("1","2","2",null,null))
        val param5 = SignUpSaveUseCase.Param(LoginFragmentDataModel("1","2","2",null,"2"))
        Assert.assertTrue(useCase.isValidParam(param1.body))
        Assert.assertFalse(useCase.isValidParam(param2.body))
        Assert.assertFalse(useCase.isValidParam(param3.body))
        Assert.assertFalse(useCase.isValidParam(param4.body))
        Assert.assertFalse(useCase.isValidParam(param5.body))
    }
    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    @Throws(InterruptedException::class)
    fun execute() = runBlocking {

        val newsListResult = useCase.execute(
            SignUpSaveUseCase.Param(
                LoginFragmentDataModel("1","2","3","4","5")
            )
        )
        when(newsListResult) {
            is NetworkResponse.Success -> {
                Assert.assertTrue(true)
                Assert.assertTrue(viewModel.getImagePath().first() =="1")
                Assert.assertTrue(viewModel.getWebAddress().first() =="2")
                Assert.assertTrue(viewModel.getUserName().first() =="3")
                Assert.assertTrue(viewModel.getEmail().first() =="4")
            }
            is NetworkResponse.Loading -> {
            }
            is NetworkResponse.Error -> {
                Assert.assertTrue(false)
            }
        }
    }
}