package com.kang.mysolelife.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kang.mysolelife.R
import com.kang.mysolelife.board.BoardInsideActivity
import com.kang.mysolelife.board.BoardListLVAdapter
import com.kang.mysolelife.board.BoardModel
import com.kang.mysolelife.board.BoardWriteActivity
import com.kang.mysolelife.contentsList.BookmarkRVAdapter
import com.kang.mysolelife.contentsList.ContentModel
import com.kang.mysolelife.databinding.FragmentTalkBinding
import com.kang.mysolelife.utills.FBRef


class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding

    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()

    private val TAG = TalkFragment::class.java.simpleName

    private lateinit var boardRVAdapter: BoardListLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)


        boardRVAdapter = BoardListLVAdapter(boardDataList)
        binding.boardListView.adapter = boardRVAdapter

        binding.boardListView.setOnItemClickListener{ parent, view, position, id ->

            // 첫번째 방법으로는 listview에 있는 데이터 title content time 다 다른 액티비티로 전달해줘서 만들기
//            val intent = Intent(context, BoardInsideActivity::class.java)
//            intent.putExtra("title", boardDataList[position].title)
//            intent.putExtra("content", boardDataList[position].content)
//            intent.putExtra("time", boardDataList[position].time)
//            startActivity(intent)

            // 두번째 방법으로는 Firebase에 있는 board에 대한 데이터의 id를 기반으로 다시 데이터를 받아오는 방법
            val intent = Intent(context, BoardInsideActivity::class.java)
            intent.putExtra("key", boardKeyList[position])
            startActivity(intent)

        }




        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        binding.homeTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)

        }

        binding.tipTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)

        }

        binding.bookmarkTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)

        }

        binding.storeTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)

        }

        getFBBoardData()

        return binding.root
    }

    private fun getFBBoardData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear()

                for(dataModel in dataSnapshot.children){

                    Log.d(TAG, dataModel.toString())
//                    dataModel.key

                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())

                }

                // 글 순서를 뒤집어서 최신순으로 출력
                boardDataList.reverse()
                boardKeyList.reverse()

                boardRVAdapter.notifyDataSetChanged()

                Log.d(TAG, boardDataList.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)

    }


}