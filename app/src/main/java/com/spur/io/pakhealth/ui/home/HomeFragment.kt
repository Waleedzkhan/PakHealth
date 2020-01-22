package com.spur.io.pakhealth.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.spur.io.pakhealth.R
import com.spur.io.pakhealth.ui.Reports.ReportsFragment
import com.spur.io.pakhealth.ui.SearchDoctor.SearchDoctorFragment
import com.spur.io.pakhealth.ui.history.HistoryFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container!!.removeAllViews()
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val login = inflater.inflate(R.layout.fragment_login,container,false)
        //val textView: TextView = root.findViewById(R.id.text_gallery)
        homeViewModel.text.observe(this, Observer {
           // textView.text = it
        })
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if(user!=null) {

            return root

        }
        else {
            return login
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.hideKeyboard()
        (activity as AppCompatActivity).supportActionBar!!.setTitle("Home")
        BtnReports.setOnClickListener {

            val frag: Fragment = ReportsFragment()
            switchFragment(frag)
        }
        BtnAppointments.setOnClickListener {

            val frag: Fragment = SearchDoctorFragment()
            switchFragment(frag)
        }
        btnHistory.setOnClickListener {
            val frag = HistoryFragment()
            switchFragment(frag)
        }


        val ispat: Boolean = getRole("Patient")
        var isdoc: Boolean = false


        if (ispat == false) {
            isdoc = getRole("Doctor")
        } else {
            (activity as AppCompatActivity).supportActionBar!!.setSubtitle("Patient")

        }

        if (isdoc) {
            (activity as AppCompatActivity).supportActionBar!!.setSubtitle("Doctor")
        }




    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun switchFragment( frag: Fragment){
        val transaction = fragmentManager!!.beginTransaction()

        transaction.replace(
            R.id.nav_host_fragment,frag
        ) // give your fragment container id in first parameter
        transaction.addToBackStack(null)  // if written, this transaction will be added to backstack
        transaction.commit()
    }

    fun  getRole(role: String):Boolean{
        var ret:Boolean = false
        val db = FirebaseFirestore.getInstance()
        db.collection(role).document(auth.currentUser!!.uid).get().addOnSuccessListener { documentReference ->
            Toast.makeText(this.context!!,"Success", Toast.LENGTH_SHORT)
            ret = true
//
        }.addOnFailureListener { e ->
            Toast.makeText(this.context!!,"Failure", Toast.LENGTH_SHORT)
            ret = false
        }
    return ret}
}