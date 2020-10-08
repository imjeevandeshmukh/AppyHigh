package com.bytelogs.appyhigh.view

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bytelogs.appyhigh.BR
import com.bytelogs.appyhigh.R
import com.bytelogs.appyhigh.data.Article
import com.bytelogs.appyhigh.databinding.ItemAdBinding
import com.bytelogs.appyhigh.databinding.ItemNewsBinding
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.android.synthetic.main.item_ad.view.*


class NewsAdapter(apodList: MutableList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private  var apodList:MutableList<Any>

    private lateinit var context: Context
    init {
        this.apodList = apodList
    }

    private val NEWS_ITEM = 0

    private val UNIFIED_NATIVE_AD_VIEW_TYPE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if(viewType == UNIFIED_NATIVE_AD_VIEW_TYPE){
            val adBinding: ItemAdBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_ad, parent, false)
            return AdViewHolder(adBinding)
        }else{
            val binding: ItemNewsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_news, parent, false)
            return NewsViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if(viewType == UNIFIED_NATIVE_AD_VIEW_TYPE){
            (holder as AdViewHolder).bind(apodList[position] as UnifiedNativeAd)
            }else{
            (holder as NewsViewHolder).bind(apodList[position] as Article)
        }


    }
    override fun getItemViewType(position: Int): Int {
        val recyclerViewItem: Any = apodList.get(position)
        return if (recyclerViewItem is UnifiedNativeAd) {
            UNIFIED_NATIVE_AD_VIEW_TYPE
        } else NEWS_ITEM
    }

    override fun getItemCount(): Int {
        return  apodList.size
    }


    fun updatePostList(apodList: MutableList<Any>){
        this.apodList = apodList
        notifyDataSetChanged()
    }

    class NewsViewHolder(private val binding: ItemNewsBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(obj: Article) {
            binding.setVariable(BR.article, obj)
            binding.cardViewItem.setOnClickListener {
                launchUrl(obj.url,binding.cardViewItem.context)
            }
            binding.executePendingBindings()
        }
        private fun launchUrl(url:String,context: Context){
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(url))
        }
    }
    class AdViewHolder (private val binding: ItemAdBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(unifiedAd: UnifiedNativeAd) {
            (binding.adView.ad_headline as TextView).setText(unifiedAd.getHeadline())
            (binding.adView.ad_body as TextView).setText(unifiedAd.getBody())
            (binding.adView.ad_call_to_action as Button).setText(unifiedAd.getCallToAction())
            binding.adView.setMediaView(binding.adView.findViewById(R.id.ad_media) as MediaView)
            binding.adView.setCallToActionView(binding.adView.findViewById(R.id.ad_call_to_action))

            if (unifiedAd.getIcon() == null) {
                binding.adView.ad_icon.visibility = View.INVISIBLE
            } else {
                Glide.with(binding.adView.ad_icon.context).load(unifiedAd.icon.drawable).into(binding.adView.ad_icon)
                binding.adView.ad_icon.visibility = View.VISIBLE
            }

            if (unifiedAd.getPrice() == null) {
                binding.adView.ad_price.visibility = View.INVISIBLE
            } else {
                binding.adView.ad_price.visibility = View.VISIBLE
                (binding.adView.ad_price as TextView).setText(unifiedAd.getPrice())
            }

            if (unifiedAd.getStore() == null) {
                binding.adView.ad_store.visibility = View.INVISIBLE
            } else {
                binding.adView.ad_store.visibility = View.VISIBLE
                (binding.adView.ad_store as TextView).setText(unifiedAd.getStore())
            }

            if (unifiedAd.getStarRating() == null) {
                binding.adView.ad_stars.visibility = View.INVISIBLE
            } else {
                (binding.adView.ad_stars as RatingBar).rating = unifiedAd.getStarRating().toFloat()
                binding.adView.ad_stars.visibility = View.VISIBLE
            }

            if (unifiedAd.getAdvertiser() == null) {
                binding.adView.ad_advertiser.visibility = View.INVISIBLE
            } else {
                (binding.adView.ad_advertiser as TextView).setText(unifiedAd.getAdvertiser())
                binding.adView.ad_advertiser.visibility = View.VISIBLE
            }
            binding.adView.setNativeAd(unifiedAd)
        }


    }
}