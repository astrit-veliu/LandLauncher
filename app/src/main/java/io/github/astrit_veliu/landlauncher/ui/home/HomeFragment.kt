package io.github.astrit_veliu.landlauncher.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.astrit_veliu.landlauncher.data.ApplicationEntity
import io.github.astrit_veliu.landlauncher.databinding.FragmentHomeBinding
import io.github.astrit_veliu.landlauncher.ui.home.adapter.RecommendationsAdapter
import io.github.astrit_veliu.landlauncher.common.utils.getGames
import io.github.astrit_veliu.landlauncher.common.utils.openPackage

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var applications: List<ApplicationEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.applicationsRecyclerView.layoutManager = CardLayoutManager(requireContext())
        //CardHelper().attachToRecyclerView(binding.applicationsRecyclerView)

        /*applications = getGames(requireContext())
        val adapter = RecommendationsAdapter(applications, requireContext())
        binding.applicationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            //enableViewScaling(true)
            setAdapter(adapter)
        }

        adapter.setOnApplicationClick { it?.packageName?.let { packageName -> requireContext().openPackage(packageName) } }*/
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

}