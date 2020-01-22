package com.spur.io.pakhealth.ui.Reports


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.spur.io.pakhealth.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_reports.*

class ReportsFragment : Fragment() {

    var listItems: ArrayList<String> = ArrayList<String>()
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()


        return inflater.inflate(R.layout.fragment_reports, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            updateInfo()
            readReports()

    }
    fun updateInfo(){
        (activity as AppCompatActivity).supportActionBar!!.setTitle("Reports History")
        val db = FirebaseFirestore.getInstance()
        db.collection("Patient").document("RJnwV2V60NcCKX4M2JUg35UyAlG2")
            .get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
            if (task.isSuccessful) {
                val doc = task.result

                val fields = StringBuilder("Reports for ")
                fields.append(doc!!.get("FirstName"))
                fields.append(" ").append(doc.get("LastName"))




                textView12.setText(fields.toString().toUpperCase())



            }
        })


    }
    fun readReports(){
        val db = FirebaseFirestore.getInstance()
        db.collection("Report")
            .whereEqualTo("UID","RJnwV2V60NcCKX4M2JUg35UyAlG2")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val user = db.collection("Report").document(document.id)
                    user.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        if (task.isSuccessful) {
                            val doc = task.result

                            val fields = StringBuilder("")
                            fields.append("Date: ").append(doc!!.get("DateOfReport"))
                            fields.append("\n Test Date: ").append(doc.get("DateOfTest"))
                            fields.append("\nTest Name: ").append(doc.get("TestName"))
                            fields.append("\nTest Type: ").append(doc.get("TestType"))
                            fields.append("\nFindings: ").append(doc.get("ReportFinding"))



                                listItems.add(0, fields.toString())

                            val adapter = ArrayAdapter(this.context!!, android.R.layout.simple_list_item_1, listItems)
                            lstrep.adapter = adapter

                        }
                    })
                        .addOnFailureListener(OnFailureListener { })
                }
            }
            .addOnFailureListener { exception ->

            }

    }
}
