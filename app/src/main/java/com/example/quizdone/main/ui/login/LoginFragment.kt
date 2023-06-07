package com.example.quizdone.main.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quizdone.R
import com.example.quizdone.databinding.FragmentLoginBinding
import com.example.quizdone.main.ui.category.CategoryFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val TAG = "LoginFragment"
        private const val RC_SIGN_IN = 9001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(),gso)
        auth = Firebase.auth

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload()
        }
        updateUI(currentUser)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG,"firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e:ApiException) {
                Log.w(TAG,"Google Sign in failed",e)
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoogle.setOnClickListener {
            signInGoogle()
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.etMail.text.toString()
            val password = binding.etPassword.text.toString()
            signInEmail(email,password)

        }
        binding.btnSignUp.setOnClickListener {
            //Navigate to Category Fragment if sign in successful
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.navContainer,RegisterFragment())?.commit()
/*            val email = binding.etMail.text.toString()
            val password = binding.etPassword.text.toString()
            createAccount(email, password)*/
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        updateUI(user)
                        //Navigate to Category Fragment if sign in successful
                        val transaction = fragmentManager?.beginTransaction()
                        transaction?.replace(R.id.navContainer,CategoryFragment())?.commit()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        updateUI(null)
                    }
                }
        }
    private fun updateUI(user: FirebaseUser?) {
    }
    private fun sendEmailVerification(){
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(requireActivity()) { task ->

            }
    }
    private fun reload(){

    }
    private fun signInEmail(email: String,password: String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    // Giriş başarılı olduktan sonra ne yapılacağını yaz ( Diğer sayfaya geçilecek)
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    //Navigate to Category Fragment if sign in successful
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.navContainer,CategoryFragment())?.commit()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Please check your mail address and password.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}