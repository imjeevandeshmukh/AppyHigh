package com.bytelogs.appyhigh.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bytelogs.appyhigh.App
import com.bytelogs.appyhigh.R
import com.bytelogs.appyhigh.data.NewsResponse
import com.bytelogs.appyhigh.data.Status
import com.bytelogs.appyhigh.databinding.ActivityMainBinding
import com.bytelogs.appyhigh.repository.RemoteConfigRepository
import com.bytelogs.appyhigh.viewmodel.MainViewModel
import com.bytelogs.appyhigh.viewmodel.ViewModelFactory
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var apodList:MutableList<Any>
    private lateinit var apodAdapter: NewsAdapter
    private val NUMBER_OF_ADS = 5
    private var adLoader: AdLoader? = null
    private lateinit var  mNativeAds: MutableList<UnifiedNativeAd>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        (application as App).getApplicationComponent().inject(this)
        init()


    }
    private fun init(){
        mNativeAds = mutableListOf();
        apodList = mutableListOf()
        mainViewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel::class.java)
        binding.mainViewModel = mainViewModel
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        apodAdapter = NewsAdapter(apodList)
        mainViewModel.onRemoteInit()
        binding.mainRecyclerView.adapter = apodAdapter
        binding.tryAgain.setOnClickListener(this)
        listenRemoteConfig()

    }
    private fun listenRemoteConfig(){
        mainViewModel.getOnCompleteSyncLiveData().observe(this, Observer {
            if(it){
                setupObservers()
            }

        })
    }



    private fun setupObservers() {
        mainViewModel.onFetchNews(getLocaleCountry()).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { news ->  
                           onSuccessNewsFetch(news)
                        }
                        binding.tryAgain.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        binding.mainRecyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        binding.tryAgain.visibility = View.VISIBLE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        binding.mainRecyclerView.visibility = View.GONE
                        binding.tryAgain.visibility = View.GONE

                    }
                }
            }
        })
    }
    private fun  onSuccessNewsFetch(newsResponse: NewsResponse){
        apodList.addAll(newsResponse.articles)
        if(mainViewModel.getDoLoadAds()){
            loadNativeAds()
        }else{
            apodAdapter.notifyDataSetChanged()
            binding.mainRecyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
    private fun getLocaleCountry():String{
        return Locale.getDefault().country;
    }
    private fun loadNativeAds() {
        val builder = AdLoader.Builder(this, getString(R.string.ad_unit_id))
        adLoader = builder.forUnifiedNativeAd { unifiedNativeAd ->
                mNativeAds.add(unifiedNativeAd)
                if (!adLoader!!.isLoading) {
                    insertAds()
                }
            }.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(errorCode: Int) {
                        if (!adLoader!!.isLoading) {
                            insertAds()
                        }
                    }
                }).build()
        adLoader?.let { it -> it.loadAds(AdRequest.Builder().build(), NUMBER_OF_ADS) }

    }
    private fun insertAds() {
        if (mNativeAds.size <= 0) {
            return
        }
        val offset = apodList.size / mNativeAds.size + 1
        var index = 0
        for (ad in mNativeAds) {
            apodList.add(index, ad)
            index = index + offset
        }
        apodAdapter.notifyDataSetChanged()
        binding.mainRecyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun onClick(p0: View?) {
        if(p0?.id == R.id.try_again){
            setupObservers()
        }
    }


}





