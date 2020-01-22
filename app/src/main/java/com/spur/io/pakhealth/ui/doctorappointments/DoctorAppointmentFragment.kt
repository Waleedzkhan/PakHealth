package com.spur.io.pakhealth.ui.doctorappointments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

import com.spur.io.pakhealth.R
import kotlinx.android.synthetic.main.fragment_doctor_appointment.*

/**
 * A simple [Fragment] subclass.
 */
class DoctorAppointmentFragment : Fragment() {
    var listItems: ArrayList<String> = ArrayList<String>()
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_doctor_appointment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun ReadSingleContact() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Appointment")
            .whereEqualTo("Email", auth.currentUser!!.email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val user = db.collection("Appointment").document(document.id)
                    user.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        if (task.isSuccessful) {
                            val doc = task.result

                            val fields = StringBuilder("")
                            fields.append(doc!!.get("PatientDetails"))


                            val adapter = ArrayAdapter(
                                this.context!!,
                                android.R.layout.simple_list_item_1,
                                listItems
                            )
                            lstdocapt.adapter = adapter

                        }
                    })
                        .addOnFailureListener(OnFailureListener { })
                }
            }
            .addOnFailureListener { exception ->

            }


    }
}
