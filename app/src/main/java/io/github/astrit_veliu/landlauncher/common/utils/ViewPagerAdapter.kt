package io.github.astrit_veliu.landlauncher.common.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

open class ViewPagerAdapter(fa: FragmentActivity, private val fragments:ArrayList<Fragment>): FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}