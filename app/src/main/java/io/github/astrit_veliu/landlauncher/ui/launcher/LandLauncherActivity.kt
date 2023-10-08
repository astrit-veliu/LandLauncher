package io.github.astrit_veliu.landlauncher.ui.launcher

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.astrit_veliu.landlauncher.common.utils.handlePackage
import io.github.astrit_veliu.landlauncher.common.utils.openPackage
import io.github.astrit_veliu.landlauncher.common.utils.openPlayStore
import io.github.astrit_veliu.landlauncher.databinding.ActivityMainBinding
import io.github.astrit_veliu.landlauncher.ui.home.adapter.ApplicationsAdapter
import io.github.astrit_veliu.landlauncher.ui.home.adapter.RecommendationsAdapter

@AndroidEntryPoint
class LandLauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LauncherViewModel by viewModels()
    private var drawerState = 5
    private var menuAdapter: ApplicationsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        refreshUI()
        initObservers()
    }

    private fun initObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            isLoading?.let { print(it) }
        }

/*        viewModel.allAppList.observe(this) { apps ->
            apps?.let {
                binding.menuContainer.drawerRecyclerView.layoutManager = GridLayoutManager(this, 4)
                menuAdapter = ApplicationsAdapter(it, applicationContext)
                binding.menuContainer.drawerRecyclerView.adapter = menuAdapter
                menuAdapter?.setOnApplicationClick { app ->
                    app?.let { app -> openPackage(app.packageName) }
                }
            }
        }

        viewModel.homeAppList.observe(this) { homeApps ->
            homeApps?.let {
                val adapter = RecommendationsAdapter(it, applicationContext)
                binding.applicationsRecyclerView.apply {
                    layoutManager = LinearLayoutManager(
                        this@LandLauncherActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    //enableViewScaling(true)
                    setAdapter(adapter)
                }

                adapter.setOnApplicationClick { app ->
                    app?.packageName?.let { packageName -> openPackage(packageName) }
                }
                adapter.setOnApplicationFocused { focusedApp ->
                    //todo handle the focused app to show info on home
                    Log.d("FOCUSED_APP: ", focusedApp?.appName ?: "no focused app")
                }
            }
        }*/
    }

    private fun initViews() {
        binding.imageButtonDrawer.setOnApplicationClick { handleDrawerClick(binding.imageButtonDrawer) }
        //binding.imageButtonProfile.setOnApplicationClick { changePage(1) }
        binding.imageButtonSettings.setOnApplicationClick {
            clearHomeAppsFocus()
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
        binding.cardBrowser.setOnApplicationClick {
            clearHomeAppsFocus()
            handlePackage(Intent.CATEGORY_APP_BROWSER)
        }
        binding.cardPlayStore.setOnApplicationClick {
            clearHomeAppsFocus()
            openPlayStore(this)
        }
        binding.cardMusic.setOnApplicationClick {
            clearHomeAppsFocus()
            handlePackage(Intent.CATEGORY_APP_MUSIC)
        }
        binding.cardGallery.setOnApplicationClick {
            clearHomeAppsFocus()
            handlePackage(Intent.CATEGORY_APP_GALLERY)
        }

        binding.drawerLayout.addDrawerListener(object :
            DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // will zoom out the menu as mentioned
                binding.applicationsRecyclerView.alpha = 1.2f - slideOffset
                binding.bottomHintsContainer.alpha = 1.2f - slideOffset
                binding.textClock.alpha = 1.2f - slideOffset
                binding.applicationsRecyclerView.scaleY = 1f - (slideOffset / 50)
                binding.applicationsRecyclerView.scaleX = 1f - (slideOffset / 50)
            }

            override fun onDrawerOpened(drawerView: View) {
                // Whatever you want
                if (binding.root.findFocus() != null) binding.root.findFocus().clearFocus()
                binding.menuContainer.drawerRecyclerView.requestFocus()
            }

            override fun onDrawerClosed(drawerView: View) {
                clearMenuAppsFocus()
            }

            override fun onDrawerStateChanged(newState: Int) {
                // Whatever you want
            }
        })
    }

    @SuppressLint("WrongConstant")
    open fun handleDrawerClick(view: View) {
        // if (view.id == R.id.imageButtonDrawer && binding.viewPager.currentItem != 0) changePage(0)
        // else {it
        clearHomeAppsFocus()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) binding.drawerLayout.closeDrawer(
            GravityCompat.END
        )
        else binding.drawerLayout.openDrawer(GravityCompat.END)
        //  }
    }

    private fun clearHomeAppsFocus() {
        if (binding.applicationsRecyclerView.hasFocus()) binding.applicationsRecyclerView.clearFocus()
    }

    private fun clearMenuAppsFocus() {
        if (binding.menuContainer.drawerRecyclerView.hasFocus()) binding.menuContainer.drawerRecyclerView.clearFocus()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun refreshUI() {
        //todo handle with scale animation home when drawer change listener open close
        binding.homeContent.alpha = 1f
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) binding.drawerLayout.closeDrawer(
            GravityCompat.END
        )
        if (binding.root.findFocus() != null) {
            binding.root.findFocus().clearFocus()
            binding.dockButtons.requestFocus()
            binding.applicationsRecyclerView.smoothScrollToPosition(0)
        }
    }
}