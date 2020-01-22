package com.spur.io.pakhealth.ui.Login

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.spur.io.pakhealth.R
import com.spur.io.pakhealth.ui.Reports.ReportsFragment
import com.spur.io.pakhealth.ui.SearchDoctor.SearchDoctorFragment
import com.spur.io.pakhealth.ui.history.HistoryFragment
import com.spur.io.pakhealth.ui.home.DocHomeFragment
import com.spur.io.pakhealth.ui.home.HomeFragment
import com.spur.io.pakhealth.ui.settings.SettingsFragment

import com.spur.io.pakhealth.ui.signup.SignUpFragment
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.net.URI


class LoginFragment : Fragment() {

    // private lateinit var loginViewModel: LoginViewModel
    private lateinit var auth: FirebaseAuth
    var role: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container!!.removeAllViews()
//        loginViewModel =
//            ViewModelProviders.of(this).get(LoginViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        auth = FirebaseAuth.getInstance()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //if (auth.currentUser == null) {
            super.onViewCreated(view, savedInstanceState)


            BtnSignUp.setOnClickListener {
                val transaction = fragmentManager!!.beginTransaction()
                val frag: Fragment = SignUpFragment()
                transaction.replace(
                    R.id.nav_host_fragment, frag
                ) // give your fragment container id in first parameter
                transaction.addToBackStack(null)  // if written, this transaction will be added to backstack
                transaction.commit()


            }

            BtnLogin.setOnClickListener {
                val currentUser = auth.currentUser
                var role = ""
                if (RbDoctor.isChecked) {
                    role = "Doctor"
                } else if (RBPatient.isChecked) {
                    role = "Patient"
                }

               if (!role.equals("")) {
                   imgloading.isVisible = true
                    auth.signInWithEmailAndPassword(
                        TxtUserName.text.toString(),
                        TxtPassword.text.toString()
                    )
                        .addOnCompleteListener(this.activity!!) { task ->
                            if (task.isSuccessful) {

                                var frag: Fragment? = null
                                if(role.equals("Patient")) {
                                    val s:String=  readProfile("Patient")

                                    frag = HomeFragment()
                                }
                                else if(role.equals("Doctor")) {
                                    frag = DocHomeFragment()
                                }
                                txtmsg.addTextChangedListener(object : TextWatcher {
                                    override fun afterTextChanged(p0: Editable?) {
                                        if(!txtmsg.text.toString().equals("")){
                                            if(frag!=null)
                                              SwitchFragment(frag)

                                        }
                                    }

                                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                    }

                                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                                    }
                                })


                            } else {

                                Toast.makeText(
                                    this.context, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
                else{
                   txtmsg.text = "You must select a role to login"
               }
            }


       // }
//        (activity as AppCompatActivity).supportActionBar!!.setTitle("Home")
//
//
//        BtnReports.setOnClickListener {
//
//            val frag: Fragment = ReportsFragment()
//            SwitchFragment(frag)
//        }
//        BtnAppointments.setOnClickListener {
//
//            val frag: Fragment = SearchDoctorFragment()
//            SwitchFragment(frag)
//        }
//        btnHistory.setOnClickListener {
//            val frag = HistoryFragment()
//            SwitchFragment(frag)
//        }





      // lbltest.text = role







    }
    fun SwitchFragment(fragment: Fragment){
        val transaction = fragmentManager!!.beginTransaction()

        transaction.replace(
            R.id.nav_host_fragment,fragment
        ) // give your fragment container id in first parameter
        transaction.addToBackStack(null)  // if written, this transaction will be added to backstack
        transaction.commit()
    }

    fun readProfile(Role:String):String{
        var ret:String  = ""
        val db = FirebaseFirestore.getInstance()
        db.collection(Role)

            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val user = db.collection(Role).document(auth.currentUser!!.uid)
                    user.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        if (task.isSuccessful) {
                            val doc = task.result
                          txtmsg.isVisible = false
                          txtmsg.setText(doc!!.get("FirstName").toString())




                        }
                    })
                        .addOnFailureListener(OnFailureListener { })
                }
            }
            .addOnFailureListener { exception ->

            }

   return ret }
}