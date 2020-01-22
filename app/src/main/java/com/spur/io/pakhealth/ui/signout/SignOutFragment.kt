package com.spur.io.pakhealth.ui.signout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.spur.io.pakhealth.R
import kotlinx.android.synthetic.main.fragment_signout.*

class SignOutFragment : Fragment() {

    private lateinit var signOutViewModel: SignOutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container!!.removeAllViews()
        signOutViewModel =
            ViewModelProviders.of(this).get(SignOutViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_signout, container, false)
        val textView: TextView = root.findViewById(R.id.text_tools)
        signOutViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.setTitle("Sign Out")
        btnSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }
    }
}