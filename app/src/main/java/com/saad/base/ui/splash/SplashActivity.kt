package com.saad.base.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.saad.base.R
import com.saad.base.common.MyApplication
import com.saad.base.data.api.response.APIResource
import com.saad.base.data.api.response.RequestStatusEnum
import com.saad.base.data.api.response.ResponseSubErrorsCodeEnum
import com.saad.base.data.common.CustomObserverResponse
import com.saad.base.data.models.configuration.ConfigurationWrapperResponse
import com.saad.base.databinding.ActivitySplashBinding
import com.saad.base.ui.MainActivity
import com.saad.base.ui.auth.AuthActivity
import com.saad.base.ui.base.activity.BaseBindingActivity
import com.saad.base.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import io.branch.referral.Branch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseBindingActivity<ActivitySplashBinding>() {

    private val viewModel: SplashViewModel by viewModels { defaultViewModelProviderFactory }

    @Inject
    lateinit var myApp: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            layoutResID = R.layout.activity_splash,
            hasToolbar = false
        )
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getConfigurationData().observe(this, configurationResultObserver())
        }, 3000)

        RuntimeException("This is a RUNTIME EXCEPTION")
    }

    private fun configurationResultObserver(): CustomObserverResponse<ConfigurationWrapperResponse> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<ConfigurationWrapperResponse> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ConfigurationWrapperResponse?
                ) {
                    when {
                        subErrorCode == ResponseSubErrorsCodeEnum.Success -> {
                            SharedPreferencesUtil.getInstance(this@SplashActivity)
                                .setConfigurationPreferences(data)
                            goToNextPage()
                        }
                    }
                }
            })
    }

    private val configurationResultObserver =
        Observer<APIResource<ConfigurationWrapperResponse>> {
            when (it.status) {
                RequestStatusEnum.SUCCESS -> {
                    hideLoadingView()
                    when {
                        it.statusSubCode == ResponseSubErrorsCodeEnum.Success -> {
                            SharedPreferencesUtil.getInstance(this)
                                .setConfigurationPreferences(it.data)
                            goToNextPage()
                        }
                        else -> {
                            handleRequestFailedMessages(
                                it.statusCode,
                                it?.statusSubCode,
                                it.messages ?: ""
                            )
                        }
                    }
                }
                RequestStatusEnum.FAILED -> {
                    hideLoadingView()
                    handleRequestFailedMessages(
                        it.statusCode,
                        it.statusSubCode,
                        it.messages
                    )
                }
                RequestStatusEnum.LOADING -> showLoadingView()
            }
        }

    private fun goToNextPage() {
        if (!viewModel.isUserLoggedIn()) {
            AuthActivity.start(this)
        } else
            MainActivity.start(this)

    }

    override fun onStart() {
        super.onStart()
        val branch = Branch.getInstance()
        // Branch init
//        branch.initSession({ referringParams: JSONObject?, error: BranchError? ->
//            if (error == null && referringParams != null) {
//                try {
//                    myApp.deeplink_id =
//                        referringParams.optString(Constants.BundleData.OpId, "")
//                } catch (e: Exception) {
//                    Log.i(
//                        "DIDNT_PARSED_LINK",
//                        "DIDNT_PARSED_LINK " + error!!.message + referringParams.toString()
//                    )
//                }
//            } else {
//                Log.i(
//                    "DIDNT_PARSED_LINK_NULL",
//                    "DIDNT_PARSED_LINK_NULL" + error!!.message + referringParams.toString()
//                )
//            }
//        }, this.intent.data, this)
    }

    override fun onNewIntent(intent: Intent?) {
        this.intent = intent
        super.onNewIntent(intent)
    }


    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }

    }

}