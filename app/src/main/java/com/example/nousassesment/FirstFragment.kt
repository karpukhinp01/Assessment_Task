package com.example.nousassesment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nousassesment.databinding.FragmentFirstBinding
import com.example.nousassesment.viewmodels.MainViewModel

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}