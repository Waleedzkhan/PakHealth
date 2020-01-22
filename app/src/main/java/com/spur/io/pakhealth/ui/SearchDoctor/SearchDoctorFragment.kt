package com.spur.io.pakhealth.ui.SearchDoctor


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

import com.spur.io.pakhealth.R
import com.spur.io.pakhealth.ui.BookAppointment.BookAppointmentFragment
import com.spur.io.pakhealth.ui.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_book_appointment.*

import kotlinx.android.synthetic.main.fragment_search_doctor.*
class SearchDoctorFragment : Fragment() {
     var listItems: ArrayList<String> = ArrayList<String>()
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_search_doctor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BtnSearch.setOnClickListener {
            ReadSingleContact()
        }
        (activity as AppCompatActivity).supportActionBar!!.setTitle("Search Doctors")
        lstsearch.setOnItemClickListener { parent, view, position, id ->

            var name: String = lstsearch.getItemAtPosition(position).toString()

            val frag : Fragment = BookAppointmentFragment()
             val name2 = name.split('\n')
            val data = name2[0]
            val email = name2[5]
           val args:Bundle = Bundle()
            args.putString("DocName",data)
            args.putString("DocEmail",email)
            frag.arguments = args
            SwitchFragment(frag)
        }

    }
    private fun ReadSingleContact() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Doctor")
            .whereEqualTo("Role", "Doctor")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val user = db.collection("Doctor").document(document.id)
                    user.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        if (task.isSuccessful) {
                            val doc = task.result
                            var SearchCriteria = SpnCriteria.selectedItem
                            var crt: String = ""
                            var NameSearch : Boolean = false
                            var SpecSearch : Boolean = false
                            var CitySearch : Boolean = false
                            val SearchQuery = txtQuery.text
                            val fields = StringBuilder("")
                            fields.append(doc!!.get("FirstName"))
                            fields.append(" ").append(doc.get("LastName"))
                            fields.append("\nGender: ").append(doc.get("Gender"))
                            fields.append("\n").append(doc.get("Specialization"))
                            fields.append("\nPhone Number: ").append(doc.get("PhoneNo"))
                            fields.append("\nAddress: ").append(doc.get("Address"))
                            fields.append("\nEmail:").append(doc.get("Email"))
                            if(SearchCriteria.equals("Name")){
                                crt = doc!!.get(("FirstName")).toString()
                                crt+= " "
                                crt+= doc!!.get("LastName").toString()
                                NameSearch = true
                            }
                            if(SearchCriteria.equals("Specialization")) {
                                SpecSearch = true

                            }
                            if(NameSearch) {
                                if(crt.contains(SearchQuery,ignoreCase = true))
                                    listItems.add(0, fields.toString())
                            }
                            else{
                                listItems.add(0, fields.toString())
                            }
                            val adapter = ArrayAdapter(this.context!!, android.R.layout.simple_list_item_1, listItems)
                            lstsearch.adapter = adapter

                        }
                    })
                        .addOnFailureListener(OnFailureListener { })
                }
            }
            .addOnFailureListener { exception ->

            }

    }
    fun SwitchFragment(fragment: Fragment){
        val transaction = fragmentManager!!.beginTransaction()

        transaction.replace(
            R.id.nav_host_fragment,fragment
        ) // give your fragment container id in first parameter
        transaction.addToBackStack(null)  // if written, this transaction will be added to backstack
        transaction.commit()
    }
}
