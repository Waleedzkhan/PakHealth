package com.spur.io.pakhealth.ui.userprofile


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

import com.spur.io.pakhealth.R
import kotlinx.android.synthetic.main.fragment_search_doctor.*
import kotlinx.android.synthetic.main.fragment_user_profile.*

/**
 * A simple [Fragment] subclass.
 */
class UserProfileFragment : Fragment() {
      private lateinit  var auth: FirebaseAuth
      private  lateinit var view1: View
    private lateinit var role:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        view1 = inflater.inflate(R.layout.fragment_user_profile, container, false)
        return view1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.setTitle("Profile")


        readProfile("Patient")

        if(txtLname.text.toString().equals("")){
            readProfile("Doctor")
        }
        else{
            role = "Patient"
        }

        if(txtfname.text.toString().equals("")) {
            role = ""

        }

        btnEdit.setOnClickListener {
            lblSave.isVisible = true
            btnSave.isVisible = true



            txtLname.isFocusable = true
            txtLname.isEnabled = true
            txtLname.isFocusableInTouchMode = true
            txtLname.requestFocus()

            txtAddress.isFocusable = true
            txtAddress.isEnabled = true
            txtAddress.isFocusableInTouchMode = true
            txtAddress.requestFocus()

            txtAge.isFocusable = true
            txtAge.isEnabled = true
            txtAge.isFocusableInTouchMode = true
            txtAge.requestFocus()

            txtGender.isFocusable = true
            txtGender.isEnabled = true
            txtGender.isFocusableInTouchMode = true
            txtGender.requestFocus()

            txtPhoneNo.isFocusable = true
            txtPhoneNo.isEnabled = true
            txtPhoneNo.isFocusableInTouchMode = true
            txtPhoneNo.requestFocus()

            txtfname.isFocusable = true
            txtfname.isEnabled = true
            txtfname.isFocusableInTouchMode = true
            txtfname.requestFocus()

            view1.showkeyboard()

        }
        btnSave.setOnClickListener {
            lblSave.isVisible = false
            btnSave.isVisible = false

            txtfname.isFocusable = false
            txtfname.isFocusableInTouchMode = false

            txtLname.isFocusable = false
            txtfname.isFocusableInTouchMode = false

            txtAddress.isFocusable = false
            txtAddress.isFocusableInTouchMode = false


            txtAge.isFocusable = false
            txtAddress.isFocusableInTouchMode = false

            txtGender.isFocusable = false
            txtGender.isFocusableInTouchMode = false

            txtPhoneNo.isFocusable = false
            txtPhoneNo.isFocusableInTouchMode = false

            view1.hideKeyboard()

            if(!role.equals("")){

                val record = hashMapOf(
                    "UserID" to auth.currentUser!!.uid,
                    "FirstName" to txtfname.text.toString(),
                    "LastName" to txtLname.text.toString(),
                    "Age" to txtAge.text.toString(),
                    "Gender" to txtGender.text.toString(),
                    "PhoneNo" to txtPhoneNo.text.toString(),
                    "Address" to txtAddress.text.toString(),

                    "Role" to role

                )

                val db = FirebaseFirestore.getInstance()
                db.collection(role).document(auth.currentUser!!.uid).set(record).addOnSuccessListener { documentReference ->
                    Toast.makeText(this.context!!,"Success",Toast.LENGTH_SHORT)
//                    val transaction = fragmentManager!!.beginTransaction()
//                    val frag : Fragment = HomeFragment()
//                    transaction.replace(
//                        R.id.nav_host_fragment,frag
//                    ) // give your fragment container id in first parameter
//                    transaction.addToBackStack(null)  // if written, this transaction will be added to backstack
//                    transaction.commit()
                }.addOnFailureListener { e ->
                    Toast.makeText(this.context!!,"Failure",Toast.LENGTH_SHORT)
                }
            }
            }


    }

    fun readProfile(Role:String){
        val db = FirebaseFirestore.getInstance()
        db.collection(Role)
            .whereEqualTo("Role", Role)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val user = db.collection(Role).document(auth.currentUser!!.uid)
                    user.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        if (task.isSuccessful) {
                            val doc = task.result



                           txtfname.setText(doc!!.get("FirstName").toString())
                            txtLname.setText(doc!!.get("LastName").toString())
                            txtGender.setText(doc.get("Gender").toString())
                            //fields.append("\n").append(doc.get("Specialization"))
                            txtAge.setText(doc!!.get("Age").toString())
                            txtPhoneNo.setText(doc.get("PhoneNo").toString())
                            txtAddress.setText(doc.get("Address").toString())


                        }
                    })
                        .addOnFailureListener(OnFailureListener { })
                }
            }
            .addOnFailureListener { exception ->

            }
    }

    fun View.showkeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
