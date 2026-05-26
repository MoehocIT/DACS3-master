package com.example.dacs3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dacs3.databinding.ActivityNewsDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityNewsDetailBinding

    private lateinit var adapter:
            CommentAdapter

    private val commentList =
        mutableListOf<Comment>()

    private lateinit var firestore:
            FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityNewsDetailBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        firestore =
            FirebaseFirestore.getInstance()

        // Get Data
        val title =
            intent.getStringExtra("title")

        val description =
            intent.getStringExtra("description")

        val image =
            intent.getStringExtra("image")

        val category =
            intent.getStringExtra("category")

        // Set Data
        binding.txtTitle.text =
            title

        binding.txtDescription.text =
            description

        binding.txtCategory.text =
            category

        // Load Image
        Glide.with(this)
            .load(image)

            .placeholder(
                android.R.drawable.ic_menu_gallery
            )

            .error(
                R.drawable.ic_delete
            )

            .into(binding.imgNews)

        // Back
        binding.btnBack.setOnClickListener {

            finish()
        }

        // Setup Recycler
        setupCommentRecycler()

        // Load Comment
        loadComments()

        // Send Comment
        binding.btnSendComment.setOnClickListener {

            sendComment()
        }
    }

    private fun setupCommentRecycler() {

        adapter =
            CommentAdapter(commentList)

        binding.recyclerComment.layoutManager =
            LinearLayoutManager(this)

        binding.recyclerComment.adapter =
            adapter
    }

    private fun sendComment() {

        val text =
            binding.edtComment.text
                .toString()

        if (text.isEmpty()) {

            return
        }

        val comment = hashMapOf(

            "username" to "Guest",

            "content" to text
        )

        firestore.collection("comments")
            .add(comment)

        binding.edtComment.text.clear()
    }

    private fun loadComments() {

        firestore.collection("comments")

            .addSnapshotListener { value, _ ->

                commentList.clear()

                value?.documents?.forEach {

                    val comment =
                        it.toObject(
                            Comment::class.java
                        )

                    if (comment != null) {

                        commentList.add(comment)
                    }
                }

                adapter.notifyDataSetChanged()
            }
    }
}