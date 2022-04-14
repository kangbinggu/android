package com.kang.mysolelife.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kang.mysolelife.R
import com.kang.mysolelife.comment.CommentLVAdapter
import com.kang.mysolelife.comment.CommentModel
import com.kang.mysolelife.databinding.ActivityBoardInsideBinding
import com.kang.mysolelife.databinding.ActivityBoardWriteBinding
import com.kang.mysolelife.utills.FBAuth
import com.kang.mysolelife.utills.FBRef
import java.lang.Exception

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private lateinit var binding : ActivityBoardInsideBinding

    private lateinit var key : String

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter : CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_inside)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        binding.boardSettingIcon.setOnClickListener {

            showDialog()

        }

        // 첫번째 방법
//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()
//
//        binding.titleArea.text = title
//        binding.textArea.text = content
//        binding.timeArea.text = time

        // 두번째 방법
        key = intent.getStringExtra("key").toString()
        getBoardData(key.toString())
        getImageData(key.toString())

        binding.commentBtn.setOnClickListener {

            insertComment(key)

        }

        getCommentData(key)

        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentAdapter


    }

    fun getCommentData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()
//
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)

                }

                commentAdapter.notifyDataSetChanged()
//
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)

    }

    fun insertComment(key : String){
        // comment
        //  - BoardKey
        //      - CommentKey
        //           - CommentData
        //           - CommentData
        //           - CommentData
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(
                    binding.commentArea.text.toString(),
                    FBAuth.getTime())
            )

        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")



    }

    private fun showDialog(){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()

        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {


            Toast.makeText(this, "수정 버튼을 눌렀습니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)

        }

        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {

            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()
            finish()

        }

    }


    private  fun getImageData(key : String){

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful){

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            }else{

                binding.getImageArea.isVisible = false

            }

        })

    }


    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try{

                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(TAG, dataModel!!.title)

                    binding.titleArea.text = dataModel!!.title
                    binding.textArea.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time

                    val myUid = FBAuth.getUid()
                    val writerUid = dataModel.uid

                    if(myUid.equals(writerUid)){
                        Toast.makeText(baseContext, "내가 글쓴이임", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "aaa")

                        binding.boardSettingIcon.isVisible = true

                    }else{
                        Toast.makeText(baseContext, "내가 글쓴이 아님", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "bbb")
                    }

                }catch (e: Exception){

                    Log.d(TAG, "삭제완료")

                }



            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)

    }
}