package com.example.quizdone.main.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizdone.R
import com.example.quizdone.adapter.CategoryAdapter
import com.example.quizdone.databinding.FragmentCategoryBinding
import com.example.quizdone.main.ui.login.LoginFragment
import com.example.quizdone.main.ui.quiz.AddQuizFragment
import com.example.quizdone.main.ui.quiz.QuizFragment
import com.example.quizdone.model.CategoryModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class CategoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var categoryArrayList: ArrayList<CategoryModel>
    var questionIndex : String? = null
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryArrayList = arrayListOf()
        categoryAdapter = CategoryAdapter(categoryArrayList)
        EventChangeListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        recyclerView = binding.rvCategory
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = categoryAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fbAddQuestion.setOnClickListener{
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.navContainer,AddQuizFragment())?.commit()
        }
        binding.ivOut.setOnClickListener{
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.navContainer,LoginFragment())?.commit()
        }
        goToQuiz()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun EventChangeListener() {
        // Firebase'den veri al ve ArrayList'e ekle
        db = FirebaseFirestore.getInstance()
        db.collection("Quiz").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return
                }

                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        categoryArrayList.add(dc.document.toObject(CategoryModel::class.java))
                    }
                }

                categoryAdapter.notifyDataSetChanged()
            }
        })
    }
    private fun goToQuiz() {

        categoryAdapter.setOnItemClickListener(object : CategoryAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                val questionIndex = categoryArrayList[position].quizId
                val bundle = Bundle()
                bundle.putString("questionIndex", questionIndex)
                val quizFragment = QuizFragment()
                quizFragment.arguments = bundle
                val transaction = requireFragmentManager().beginTransaction()
                transaction.replace(R.id.navContainer, quizFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })
    }

}
