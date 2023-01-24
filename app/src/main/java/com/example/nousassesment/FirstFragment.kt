package com.example.nousassesment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nousassesment.data.LoadStatus
import com.example.nousassesment.databinding.FragmentFirstBinding
import com.example.nousassesment.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var  RVAdapter: RecyclerViewAdapter
    private val mMainViewModel: MainViewModel by viewModel()
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerview = binding.recyclerView
        recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        RVAdapter = RecyclerViewAdapter(requireContext())
        recyclerview.adapter = RVAdapter

        mMainViewModel.programmeList.observe(viewLifecycleOwner) {
            RVAdapter.updatePosts(it)
        }

        mMainViewModel.loadStatus.observe(viewLifecycleOwner) {
            when (it) {
                LoadStatus.LOADING -> {
                    binding.retryButton.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.statusImageView.visibility = View.VISIBLE
                    binding.statusImageView.setImageResource(R.drawable.ic_baseline_cloud_download_24)
                }
                LoadStatus.SUCCESS -> {
                    binding.retryButton.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.statusImageView.visibility = View.GONE
                }
                LoadStatus.ERROR -> {
                    binding.retryButton.visibility = View.VISIBLE
                    binding.statusImageView.visibility = View.VISIBLE
                    binding.statusImageView.setImageResource(R.drawable.ic_broken_image)
                }
            }
        }

        binding.listToolbar.inflateMenu(R.menu.menu_main)

        binding.retryButton.setOnClickListener {
            mMainViewModel.getItemsList()
        }

        binding.listToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_theme)  {
                if (MyPreferences(this.requireContext()).darkMode == 0) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    MyPreferences(this.requireContext()).darkMode = 1
                    Toast.makeText(this.requireContext(), "Set to dark mode!", Toast.LENGTH_LONG).show()
                } else if (MyPreferences(this.requireContext()).darkMode == 1) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    MyPreferences(this.requireContext()).darkMode = 2
                    Toast.makeText(this.requireContext(), "Set to system mode!", Toast.LENGTH_LONG).show()
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    MyPreferences(this.requireContext()).darkMode = 0
                    Toast.makeText(this.requireContext(), "Set to light mode!", Toast.LENGTH_LONG).show()
                }
                true
            } else false
        }

        val searchItem = binding.listToolbar.menu.findItem(R.id.actionSearch)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                RVAdapter.updatePosts(mMainViewModel.filter(query!!))
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}