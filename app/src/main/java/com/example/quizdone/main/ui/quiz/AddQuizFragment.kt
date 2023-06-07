package com.example.quizdone.main.ui.quiz

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.quizdone.R
import com.example.quizdone.databinding.FragmentAddQuizBinding
import com.example.quizdone.main.ui.category.CategoryFragment
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddQuizFragment : Fragment() {
    private var _binding: FragmentAddQuizBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.navContainer, CategoryFragment())?.commit()
        }

        binding.btnAdd.setOnClickListener {
            val question = binding.etQuestion.text.toString()
            val category = binding.etCategory.text.toString()
            val option1 = binding.etOption1.text.toString()
            val option2 = binding.etOption2.text.toString()
            val option3 = binding.etOption3.text.toString()
            val option4 = binding.etOption4.text.toString()

            val options = listOf(option1, option2, option3, option4)
            val shuffledOptions = shuffleOptions(options)

            val quizOptions = mapOf(
                "option1" to shuffledOptions[0],
                "option2" to shuffledOptions[1],
                "option3" to shuffledOptions[2],
                "option4" to shuffledOptions[3]
            )

            addQuizToFirestore(question, category, quizOptions)
        }
    }

    private fun shuffleOptions(options: List<String>): List<String> {
        val shuffledOptions = options.toMutableList()
        Collections.shuffle(shuffledOptions)
        return shuffledOptions
    }

    private fun addQuizToFirestore(question: String, category: String, quizOptions: Map<String, String>) {
        val quiz = hashMapOf(
            "question" to question,
            "category" to category,
            "quizOptions" to quizOptions
        )

        db.collection("QuizSuggestions")
            .add(quiz)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(requireContext(), "Quiz Added Successfully!", Toast.LENGTH_SHORT).show()
                clearInputFields()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error Adding Quiz", e)
                Toast.makeText(requireContext(), "Failed to add quiz.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearInputFields() {
        binding.etQuestion.text.clear()
        binding.etCategory.text.clear()
        binding.etOption1.text.clear()
        binding.etOption2.text.clear()
        binding.etOption3.text.clear()
        binding.etOption4.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
