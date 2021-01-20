package io.github.astrit_veliu.landlauncher.ui.launcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.astrit_veliu.landlauncher.R
import io.github.astrit_veliu.landlauncher.ui.home.adapter.ApplicationsAdapter
import io.github.astrit_veliu.landlauncher.utils.extractAppLaunchInfo
import io.github.astrit_veliu.landlauncher.utils.getInstalledApps
import kotlinx.android.synthetic.main.activity_main.*

class LandLauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        applicationsRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false)

        applicationsRecyclerView.enableViewScaling(true)

        applicationsRecyclerView.adapter = ApplicationsAdapter(extractAppLaunchInfo(applicationContext))
    }
}