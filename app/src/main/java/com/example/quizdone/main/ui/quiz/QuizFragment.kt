package com.example.quizdone.main.ui.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quizdone.R
import com.example.quizdone.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {
    private var _binding : FragmentQuizBinding? = null
     private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }
}