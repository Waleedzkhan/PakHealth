package com.spur.io.pakhealth.ui.history


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth

import com.spur.io.pakhealth.R
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {
    lateinit var auth : FirebaseAuth
    var listItems: ArrayList<String> = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        val view:View =  inflater.inflate(R.layout.fragment_history, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.setTitle("Appointments")
        readAppointments()
    }


    fun readAppointments(){

        val db = FirebaseFirestore.getInstance()
        db.collection("Appointment")
            .whereEqualTo("PatientID", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val user = db.collection("Appointment").document(document.id)
                    user.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        if (task.isSuccessful) {
                            val doc = task.result

                            val fields = StringBuilder("")
                            fields.append(doc!!.get("Date"))
                            fields.append("\n With ").append(doc.get("With"))




                                listItems.add(0, fields.toString())

                            val adapter = ArrayAdapter(this.context!!, android.R.layout.simple_list_item_1, listItems)
                            lstApt.adapter = adapter
                            if(listItems.size == 0){
                                textView13.setText("No Appointments Found")
                            }

                        }
                    })
                        .addOnFailureListener(OnFailureListener { })
                }
            }
            .addOnFailureListener { exception ->

            }

    }
}
