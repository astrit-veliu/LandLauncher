package io.github.astrit_veliu.landlauncher.ui.launcher

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.astritveliu.boom.Boom
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import io.github.astrit_veliu.landlauncher.R
import io.github.astrit_veliu.landlauncher.ui.home.HomeFragment
import io.github.astrit_veliu.landlauncher.ui.home.adapter.ApplicationsAdapter
import io.github.astrit_veliu.landlauncher.ui.profile.UserProfileFragment
import io.github.astrit_veliu.landlauncher.utils.ViewPagerAdapter
import io.github.astrit_veliu.landlauncher.utils.extractAppLaunchInfo
import kotlinx.android.synthetic.main.activity_main.*


class LandLauncherActivity : AppCompatActivity() {

    var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    var drawerState = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //when (position) {
                //    0 -> // home fragment
                //    1 -> //profile fragment
                //}
            }
        })

    }

    private fun initViews() {
        initializeBottomSheet()
        setupViewPager()
        Boom(imageButtonDrawer)
        Boom(imageButtonProfile)
        Boom(imageButtonSettings)
    }

    private fun setupViewPager() {
        val fragmentList = arrayListOf(
            HomeFragment.newInstance(),
            UserProfileFragment.newInstance(),
        )
        viewPager.adapter = ViewPagerAdapter(this, fragmentList)
        viewPager.isUserInputEnabled = false
    }

    private fun setUpDrawer(){
        drawerRecyclerView.layoutManager = GridLayoutManager(this, 5)
        drawerRecyclerView.adapter = ApplicationsAdapter(extractAppLaunchInfo(applicationContext))
    }

    private fun initializeBottomSheet() {
        bottomSheetBehavior = from<View>(bottom_relative)
        bottomSheetBehavior?.let {
            it.state = drawerState
            it.addBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {}
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    animateBottomSheetArrows(slideOffset)
                }
            })
        }

    }

    private fun animateBottomSheetArrows(slideOffset: Float) {
        imgArrow.rotation = slideOffset * -180
        animateTopTextviews(slideOffset)
    }


    private fun animateTopTextviews(value: Float) {
        //hide or blurry main screen & show drawer with searchbar at the top
        homeContent.alpha = 1 - value
        txt_title_collapse.alpha = 1 - value
        txt_subtitle_collapse.alpha = 1 - value
        txt_title_collapse_below.alpha = value
        txt_subtitle_collapse_below.alpha = value
    }

    open fun handleClick(view: View){
        when(view.id){
            R.id.imageButtonDrawer -> handleDrawerClick(view)
            R.id.imageButtonProfile -> changePage(1)
            R.id.imageButtonSettings ->startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }

    open fun handleDrawerClick(view: View){
        if(view.id == R.id.imageButtonDrawer && viewPager.currentItem != 0) changePage(0)
        else {
            bottomSheetBehavior?.let {
                drawerState = when (it.state) {
                    STATE_EXPANDED -> STATE_HIDDEN
                    STATE_COLLAPSED or STATE_HIDDEN -> STATE_EXPANDED
                    else -> STATE_EXPANDED
                }
                it.state = drawerState
            }
        }
    }

    private fun changePage(page: Int){
        viewPager.setCurrentItem(page, true)
    }

    override fun onResume() {
        super.onResume()
        setUpDrawer()
        refreshUI()
    }

    private fun refreshUI() {
        bottomSheetBehavior?.state = STATE_HIDDEN
        homeContent.alpha = 1f
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior?.state != STATE_HIDDEN) bottomSheetBehavior?.state = STATE_HIDDEN
        else super.onBackPressed()
    }
}