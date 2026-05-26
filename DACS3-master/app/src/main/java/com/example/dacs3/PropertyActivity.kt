package com.example.dacs3

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dacs3.databinding.ActivityPropertyBinding
import com.google.firebase.firestore.FirebaseFirestore

class PropertyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyBinding

    private lateinit var adapter: PropertyAdapter

    // List gốc từ Firebase
    private val propertyList = mutableListOf<Property>()

    // List dùng để search
    private val filteredList = mutableListOf<Property>()

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityPropertyBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        setupRecycler()

        setupSearch()

        loadProperties()

        // Open Add Property
        binding.btnAdd.setOnClickListener {

            val intent =
                Intent(this, AddPropertyActivity::class.java)

            startActivity(intent)
        }
    }

    private fun setupRecycler() {

        adapter = PropertyAdapter(filteredList)

        binding.recyclerProperty.layoutManager =
            GridLayoutManager(this, 2)

        binding.recyclerProperty.adapter = adapter
    }

    private fun loadProperties() {

        firestore.collection("properties")
            .get()

            .addOnSuccessListener { documents ->

                propertyList.clear()

                filteredList.clear()

                for (document in documents) {

                    val property =
                        document.toObject(Property::class.java)

                    propertyList.add(property)

                    filteredList.add(property)
                }

                adapter.notifyDataSetChanged()
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Failed to load properties",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun setupSearch() {

        binding.edtSearch.addTextChangedListener(

            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    val searchText =
                        s.toString().lowercase()

                    filteredList.clear()

                    for (property in propertyList) {

                        if (
                            property.title.lowercase()
                                .contains(searchText)
                        ) {

                            filteredList.add(property)
                        }
                    }

                    adapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()

        // Reload data khi quay lại
        loadProperties()
    }
}