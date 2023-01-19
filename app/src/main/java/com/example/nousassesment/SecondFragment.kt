package com.example.nousassesment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nousassesment.databinding.FragmentSecondBinding
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private val args by navArgs<SecondFragmentArgs>()
    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailToolbar.inflateMenu(R.menu.menu_main)

        binding.detailToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_theme)  {
                if (MyPreferences(this.requireContext()).darkMode == 0) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    MyPreferences(this.requireContext()).darkMode = 1
                    Toast.makeText(this.requireContext(), "Set to dark mode!", Toast.LENGTH_LONG).show()
                    binding.backButton.setImageResource(R.drawable.ic_baseline_arrow_back_24_light)
                } else if (MyPreferences(this.requireContext()).darkMode == 1) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    MyPreferences(this.requireContext()).darkMode = 2
                    Toast.makeText(this.requireContext(), "Set to system mode!", Toast.LENGTH_LONG).show()
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    MyPreferences(this.requireContext()).darkMode = 0
                    Toast.makeText(this.requireContext(), "Set to light mode!", Toast.LENGTH_LONG).show()
                    binding.backButton.setBackgroundResource(R.drawable.ic_baseline_arrow_back_24)
                }
                true
            } else false
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        val formattedDesc = args.item.description.replace("| ", "\n")

        val imageCorr = if (args.item.imageUrl.endsWith("/preview")) args.item.imageUrl else args.item.imageUrl.plus("/preview")
        Picasso.with(requireContext()).load(imageCorr).into(binding.image)
        binding.title.text = args.item.title
        binding.description.text = formattedDesc

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}