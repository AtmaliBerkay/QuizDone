package com.example.quizdone.main.ui.quiz

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.quizdone.R
import com.example.quizdone.databinding.FragmentQuizBinding
import com.example.quizdone.main.ui.category.CategoryFragment
import com.example.quizdone.model.QuestionModel
import com.google.firebase.database.*


class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private lateinit var questionModel: QuestionModel
    //private lateinit var tvScore: TextView
    private lateinit var database: DatabaseReference
    private var questionsArray: ArrayList<QuestionModel> = ArrayList()
    private var questionCount: Int = 0
    private var userScore: Int = 0
    private var countQuestions: Int = 1
    private var categoryFragment: CategoryFragment? = null
    private var questionIndex : String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentQuizBinding.inflate(inflater, container, false)


        getQuestions()

        binding.btnOption1.setOnClickListener {
            pressAnswer(binding.btnOption1)
        }
        binding.btnOption2.setOnClickListener {
            pressAnswer(binding.btnOption2)
        }
        binding.btnOption3.setOnClickListener {
            pressAnswer(binding.btnOption3)
        }
        binding.btnOption4.setOnClickListener {
            pressAnswer(binding.btnOption4)
        }
        binding.btnNext.setOnClickListener {
            loadNextQuestion()
        }
        binding.btnFinish.setOnClickListener {
            finishQuiz()
        }
        binding.ivBack.setOnClickListener{
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.navContainer,CategoryFragment())?.commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionIndex = arguments?.getString("questionIndex")
        binding.btnNext.isEnabled = false

    }
    private fun getQuestions() {
        database = FirebaseDatabase.getInstance().reference.child("questions")
        questionsArray = ArrayList()

        database.get()
            .addOnSuccessListener { snapshot ->
                for (childSnapshot in snapshot.children) {
                    val subject = childSnapshot.child("subject").value as String
                    val title = childSnapshot.child("title").value as String
                    val options = childSnapshot.child("options").value as HashMap<String, Boolean>
                    if (subject == questionIndex){
                        binding.tvQuestion.text = title
                        val question = QuestionModel(title = title, subject = subject, options = options)
                        questionsArray.add(question)
                    }
                }
                displayCurrentQuestion()
            }
            .addOnFailureListener { error ->
                Log.e(TAG, "Firebase Error: ${error.message}")
            }
    }
    fun pressAnswer(sender: Button) {
        val answerTitles = takingAnswerTitles()
        val myAnswer = sender.tag?.toString()?.toIntOrNull() ?: 0

        val isCorrectAnswer = questionsArray[questionCount].options[answerTitles[myAnswer]] ?: false
        val correctAnswerIndex = answerTitles.indexOfFirst { questionsArray[questionCount].options[it] == true }

        if (isCorrectAnswer) {
            userScore++
            sender.setBackgroundColor(Color.GREEN)
        } else {
            sender.setBackgroundColor(Color.RED)
            if (correctAnswerIndex != -1) {
                val correctAnswerButton = getAnswerButtonByIndex(correctAnswerIndex)
                correctAnswerButton?.setBackgroundColor(Color.GREEN)
            }
        }

        binding.btnOption1.isEnabled = false
        binding.btnOption2.isEnabled = false
        binding.btnOption3.isEnabled = false
        binding.btnOption4.isEnabled = false
        binding.btnNext.isEnabled = true

        if (questionCount != questionsArray.size - 1) {
            questionCount++
            countQuestions++
        } else {
            // this.performSegue(C.quizToScore, this)
            // dismiss(animated = true) { }
        }
    }

    private fun getAnswerButtonByIndex(index: Int): Button? {
        return when (index) {
            0 -> binding.btnOption1
            1 -> binding.btnOption2
            2 -> binding.btnOption3
            3 -> binding.btnOption4
            else -> null
        }
    }

    private fun displayCurrentQuestion() {
        val purple200Color = ContextCompat.getColor(requireContext(), R.color.purple_200)
        val answerTitles = takingAnswerTitles()
        binding.tvQuestion.text = questionsArray[questionCount].title
        binding.btnOption1.text = answerTitles[0]
        binding.btnOption2.text = answerTitles[1]
        binding.btnOption3.text = answerTitles[2]
        binding.btnOption4.text = answerTitles[3]
        binding.btnOption1.setBackgroundColor(purple200Color)
        binding.btnOption2.setBackgroundColor(purple200Color)
        binding.btnOption3.setBackgroundColor(purple200Color)
        binding.btnOption4.setBackgroundColor(purple200Color)
        binding.tvScore.text = "Score: $userScore"

        binding.btnOption1.isEnabled = true
        binding.btnOption2.isEnabled = true
        binding.btnOption3.isEnabled = true
        binding.btnOption4.isEnabled = true
    }

    fun takingAnswerTitles(): List<String> {
        val answerTitles: MutableList<String> = mutableListOf()
        for (answers in questionsArray[questionCount].options.keys) {
            answerTitles.add(answers)
        }
        return answerTitles
    }

    fun answerCheck(sender: Button): List<Boolean> {
        val checker: MutableList<Boolean> = mutableListOf()
        val answerTitles = takingAnswerTitles()
        for (index in answerTitles) {
            val checked = questionsArray[questionCount].options[index] ?: false
            checker.add(checked)
        }
        return checker
    }


    private fun loadNextQuestion() {
        questionCount++
        if (questionCount < questionsArray.size) {
            displayCurrentQuestion()
        } else {
            Toast.makeText(context,"Questions Finished. Your Score is: $userScore ",Toast.LENGTH_SHORT).show()
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.navContainer,CategoryFragment())?.commit()
        // Quiz tamamlandığında yapılacak işlemleri burada gerçekleştirin
    }
}