package com.wajahatkarim3.firestore_usersonline

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.github.thunder413.datetimeutils.DateTimeUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.thetechnocafe.gurleensethi.liteutils.RecyclerAdapterUtil
import com.wajahatkarim3.firestore_usersonline.databinding.ActivityMainBinding
import com.wajahatkarim3.firestore_usersonline.databinding.UserItemLayoutBinding
import morxander.zaman.ZamanUtil
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var bi: ActivityMainBinding

    var usersList: ArrayList<UserModel> = ArrayList<UserModel>()
    var recyclerAdapter: RecyclerAdapterUtil<UserModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bi = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupViews()
        loadUsers()
    }

    fun setupViews()
    {
        // Current User Selector

        // RecyclerView
        recyclerAdapter = RecyclerAdapterUtil(this, usersList, R.layout.user_item_layout)
        recyclerAdapter?.addOnDataBindListener { itemView, item, position, innerViews ->
            var bbb = DataBindingUtil.bind<UserItemLayoutBinding>(itemView)

            bbb.txtUserName.setText(item.name)
            when(item.online)
            {
                true -> bbb.txtOnlineStatus.setText("Online")
                false -> bbb.txtOnlineStatus.setText(DateTimeUtils.getTimeAgo(this@MainActivity, Date(item.last_active.toLong() * 1000L)))
            }
        }
        bi.recyclerUsers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bi.recyclerUsers.adapter = recyclerAdapter
    }

    fun loadUsers()
    {
        var query = FirebaseFirestore.getInstance().collection("users")
        query.get().addOnSuccessListener { docs ->
            usersList.clear()
            usersList.addAll(docs.toObjects(UserModel::class.java))
            recyclerAdapter?.notifyDataSetChanged()

            var names = Array<String>(usersList.size){ "" }
            var i = 0
            for (user in usersList)
            {
                names[i++] = (user.name ?: "")
            }
            bi.txtCurrentUser.setItems(names)
            bi.txtCurrentUser.setText(names[0])
        }
    }
}
