package com.example.muapp.ui.MyPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.muapp.R
import com.example.muapp.databinding.FragmentMypageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MypageFragment : Fragment() {

    private var _binding : FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ibMyFavorite.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_my_page_to_navigation_favorite)
        }
        binding.tvUserid.text = auth.currentUser!!.email
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}