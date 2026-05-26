package com.example.dacs3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacs3.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    // VIEW BINDING
    private lateinit var binding:
            ActivityMainBinding

    // FIREBASE
    private lateinit var auth:
            FirebaseAuth

    private lateinit var firestore:
            FirebaseFirestore

    // LIST
    private val propertyList =
        mutableListOf<Property>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // BINDING
        binding =
            ActivityMainBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        // FIREBASE
        auth =
            FirebaseAuth.getInstance()

        firestore =
            FirebaseFirestore.getInstance()

        // SETUP
        setupBottomNavigation()


        setupFeatureRecycler()

        setupPropertyRecycler()

        setupButtons()

        loadProperties()
    }

    // ===================================
    // BUTTONS
    // ===================================

    private fun setupButtons() {

        // MENU
        binding.btnMenu
            .setOnClickListener {

                Toast.makeText(
                    this,
                    "Menu",
                    Toast.LENGTH_SHORT
                ).show()
            }

        // CHAT
        binding.btnChat
            .setOnClickListener {

                startActivity(

                    Intent(
                        this,
                        ChatActivity::class.java
                    )
                )
            }
    }

    // ===================================
    // BOTTOM NAVIGATION
    // ===================================

    private fun setupBottomNavigation() {

        binding.bottomNav.selectedItemId =
            R.id.nav_home

        binding.bottomNav
            .setOnItemSelectedListener { item ->

                when (item.itemId) {

                    // HOME
                    R.id.nav_home -> {

                        true
                    }

                    // PROPERTY
                    R.id.nav_rooms -> {

                        startActivity(

                            Intent(
                                this,
                                PropertyActivity::class.java
                            )
                        )

                        overridePendingTransition(0, 0)

                        true
                    }

                    // BLOG
                    R.id.nav_blog -> {

                        startActivity(

                            Intent(
                                this,
                                NewsActivity::class.java
                            )
                        )

                        overridePendingTransition(0, 0)

                        true
                    }

                    // PROFILE
                    R.id.nav_profile -> {

                        startActivity(

                            Intent(
                                this,
                                ProfileActivity::class.java
                            )
                        )

                        overridePendingTransition(0, 0)

                        true
                    }

                    else -> false
                }
            }
    }

    // ===================================
    // FEATURE RECYCLER
    // ===================================

    private fun setupFeatureRecycler() {

        val featureList = listOf(

            Feature(
                1,
                "Nhà / Phòng",
                android.R.drawable.ic_menu_myplaces
            ),

            Feature(
                2,
                "Blogs",
                android.R.drawable.ic_menu_info_details
            ),

            Feature(
                3,
                "Tin nhắn",
                android.R.drawable.ic_dialog_email
            ),

            Feature(
                4,
                "Bản đồ",
                android.R.drawable.ic_dialog_map
            )
        )

        val adapter =
            FeatureAdapter(featureList) {

                when (it.id) {

                    // PROPERTY
                    1 -> {

                        startActivity(

                            Intent(
                                this,
                                PropertyActivity::class.java
                            )
                        )
                    }

                    // BLOG
                    2 -> {

                        startActivity(

                            Intent(
                                this,
                                NewsActivity::class.java
                            )
                        )
                    }

                    // CHAT
                    3 -> {

                        startActivity(

                            Intent(
                                this,
                                ChatActivity::class.java
                            )
                        )
                    }

                    // MAP
                    4 -> {

                        Toast.makeText(
                            this,
                            "Google Maps",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        binding.rvFeatures.layoutManager =
            GridLayoutManager(this, 2)

        binding.rvFeatures.adapter =
            adapter
    }

    // ===================================
    // PROPERTY RECYCLER
    // ===================================

    private fun setupPropertyRecycler() {

        val adapter =
            PropertyAdapter(propertyList)

        binding.rvProperty.layoutManager =
            LinearLayoutManager(this)

        binding.rvProperty.adapter =
            adapter
    }

    // ===================================
    // LOAD FIREBASE
    // ===================================

    private fun loadProperties() {

        firestore.collection("properties")
            .get()

            .addOnSuccessListener { documents ->

                propertyList.clear()

                for (document in documents) {

                    val property =
                        document.toObject(
                            Property::class.java
                        )

                    propertyList.add(property)
                }

                binding.rvProperty.adapter
                    ?.notifyDataSetChanged()
            }

            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Không tải được dữ liệu",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
