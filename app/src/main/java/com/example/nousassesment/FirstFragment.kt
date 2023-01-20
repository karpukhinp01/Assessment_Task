package com.example.nousassesment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nousassesment.databinding.FragmentFirstBinding
import com.example.nousassesment.viewmodels.MainViewModel
import java.util.Locale.filter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var  RVAdapter: RecyclerViewAdapter
    private lateinit var mMainViewModel: MainViewModel
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerview = binding.recyclerView
        recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
        RVAdapter = RecyclerViewAdapter(requireContext())
        recyclerview.adapter = RVAdapter

        mMainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mMainViewModel.programmeList.observe(viewLifecycleOwner) {
            RVAdapter.updatePosts(it)
        }

        binding.listToolbar.inflateMenu(R.menu.menu_main)

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