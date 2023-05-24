package com.example.quizdone.main.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quizdone.R
import com.example.quizdone.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    //Google Sign in request code
    private val rcSignIn = 123
    //Firebase Authentication
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireContext(),gso)
        binding.btnGoogle.setOnClickListener{
            signInWithGoogle(googleSignInClient,firebaseAuth)
        }
        setUpUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpUI(){

        binding.btnLogin.setOnClickListener{
            if (binding.etMail.text.isEmpty() || binding.etPassword.text.isEmpty()) {
                Toast.makeText(context, "Lütfen Gerekli Yerleri Doldurunuz", Toast.LENGTH_SHORT).show()
            }
            else if(binding.cbHatirla.isChecked){

                val Mail = binding.etMail.text.toString()
                val Password = binding.etPassword.text.toString()
                //firebase + ana ekrana giriş
            }
            else{
                //ana ekrana giriş
            }
        }
    }
    private fun signInWithGoogle(googleSignInClient: GoogleSignInClient, firebaseAuth: FirebaseAuth){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,rcSignIn)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == rcSignIn) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                Snackbar.make(requireView(), "Google sign-in failed", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    // TODO: Handle successful sign-in
                } else {
                    Snackbar.make(requireView(), "Authentication failed", Snackbar.LENGTH_SHORT).show()
                }
            }
    }
}