package io.github.astrit_veliu.landlauncher.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.astritveliu.boom.Boom
import io.github.astrit_veliu.landlauncher.R
import io.github.astrit_veliu.landlauncher.data.ApplicationEntity
import io.github.astrit_veliu.landlauncher.ui.home.adapter.RecommendationsAdapter
import io.github.astrit_veliu.landlauncher.utils.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    lateinit var applications: List<ApplicationEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applicationsRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false)

        applicationsRecyclerView.enableViewScaling(true)
        applications = getGames(requireContext())
        applicationsRecyclerView.adapter = RecommendationsAdapter(applications)

        setUpTopCards()
    }

    private fun setUpTopCards() {
        Boom(cardBrowser)
        Boom(cardPlayStore)
        Boom(cardMusic)
        Boom(cardGallery)
        cardBrowser.setOnClickListener { openBrowser(requireContext()) }
        cardPlayStore.setOnClickListener { openPlayStore(requireContext()) }
        cardMusic.setOnClickListener { openMusic(requireContext()) }
        cardGallery.setOnClickListener { openGallery(requireContext()) }
    }


    internal class ViewHolder {
        var imageView: ImageView? = null
    }

    override fun onResume() {
        super.onResume()
    }

    companion object{
        fun newInstance() = HomeFragment()
    }

}