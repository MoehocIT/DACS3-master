package com.example.dacs3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.dacs3.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    // VIEW BINDING
    private lateinit var binding:
            ActivityProfileBinding

    // FIREBASE
    private lateinit var auth:
            FirebaseAuth

    private lateinit var firestore:
            FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // BINDING
        binding =
            ActivityProfileBinding.inflate(
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

        loadUserInfo()

        setupButtons()
    }

    // ===================================
    // BOTTOM NAVIGATION
    // ===================================

    private fun setupBottomNavigation() {

        binding.bottomNav.selectedItemId =
            R.id.nav_profile

        binding.bottomNav
            .setOnItemSelectedListener { item ->

                when (item.itemId) {

                    // HOME
                    R.id.nav_home -> {

                        startActivity(

                            Intent(
                                this,
                                MainActivity::class.java
                            )
                        )

                        overridePendingTransition(0, 0)

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

                        true
                    }

                    else -> false
                }
            }
    }

    // ===================================
    // LOAD USER INFO
    // ===================================

    private fun loadUserInfo() {

        val currentUser =
            auth.currentUser

        if (currentUser != null) {

            firestore.collection("Users")
                .document(currentUser.uid)
                .get()

                .addOnSuccessListener {

                    // NAME
                    val fullName =
                        it.getString("fullName")
                            ?: "Người dùng"

                    binding.txtUsername.text =
                        fullName

                    // EMAIL
                    val email =
                        it.getString("email")
                            ?: ""

                    binding.txtEmail.text =
                        email

                    // AVATAR
                    val avatar =
                        it.getString("avatar")

                    if (!avatar.isNullOrEmpty()) {

                        Glide.with(this)
                            .load(avatar)
                            .placeholder(
                                android.R.drawable.sym_def_app_icon
                            )
                            .into(binding.imgAvatar)
                    }
                }

        } else {

            Toast.makeText(
                this,
                "Bạn chưa đăng nhập",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // ===================================
    // BUTTON CLICK
    // ===================================

    private fun setupButtons() {

        // EDIT PROFILE
        binding.layoutEditProfile
            .setOnClickListener {

                Toast.makeText(
                    this,
                    "Chỉnh sửa hồ sơ",
                    Toast.LENGTH_SHORT
                ).show()
            }

        // FAVORITE
        binding.layoutFavorite
            .setOnClickListener {

                Toast.makeText(
                    this,
                    "Danh sách yêu thích",
                    Toast.LENGTH_SHORT
                ).show()
            }

        // CHAT
        binding.layoutMessage
            .setOnClickListener {

                startActivity(

                    Intent(
                        this,
                        ChatActivity::class.java
                    )
                )
            }

        // LOGOUT
        binding.layoutLogout
            .setOnClickListener {

                auth.signOut()

                Toast.makeText(
                    this,
                    "Đăng xuất thành công",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(

                    Intent(
                        this,
                        LoginActivity::class.java
                    )
                )

                finish()
            }

        // CAMERA
        binding.btnCamera
            .setOnClickListener {

                Toast.makeText(
                    this,
                    "Đổi ảnh đại diện",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}