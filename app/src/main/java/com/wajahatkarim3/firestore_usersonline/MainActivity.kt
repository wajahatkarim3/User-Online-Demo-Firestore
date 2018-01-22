package com.wajahatkarim3.firestore_usersonline

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.github.thunder413.datetimeutils.DateTimeUtils
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
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
    var selected = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bi = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupViews()
        loadUsers()
    }

    override fun onStop() {
        super.onStop()
        if (selected != -1)
            makeUserOffline(usersList.get(selected))
    }

    fun setupViews()
    {
        // Current User Selector
        bi.txtCurrentUser.onItemClick { pos, label ->
            if (selected != -1)
                makeUserOffline(usersList.get(selected))
            selected = pos
            makeUserOnline(usersList.get(selected))
        }

        // RecyclerView
        recyclerAdapter = RecyclerAdapterUtil(this, usersList, R.layout.user_item_layout)
        recyclerAdapter?.addOnDataBindListener { itemView, item, position, innerViews ->
            var bbb = DataBindingUtil.bind<UserItemLayoutBinding>(itemView)

            bbb.txtUserName.setText(item.name)
            when(item.online)
            {
                true -> {
                    bbb.txtOnlineStatus.setText("Online")
                    bbb.txtOnlineStatus.setTextColor(Color.parseColor("#1BAB16"))
                }
                false -> {
                    bbb.txtOnlineStatus.setText(DateTimeUtils.getTimeAgo(this@MainActivity, Date(item.last_active)))
                    bbb.txtOnlineStatus.setTextColor(Color.parseColor("#9E9E9E"))
                }
            }
        }
        bi.recyclerUsers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bi.recyclerUsers.adapter = recyclerAdapter
    }

    fun loadUsers()
    {
        var query:Query = FirebaseFirestore.getInstance().collection("users")

        query.addSnapshotListener { snapshots, ex ->
            usersList.clear()
            for (doc in snapshots?.documents ?: emptyList()) {
                usersList.add(doc.toObject(UserModel::class.java))
            }
            recyclerAdapter?.notifyDataSetChanged()

            var names = arrayOfNulls<String>(usersList.size)
            var i = 0
            for (user in usersList) {
                names[i] = user.name ?: ""
                i++
            }
            bi.txtCurrentUser.setItems(names.requireNoNulls())
        }
    }

    fun makeUserOnline(user: UserModel)
    {
        // Firestore
        var query = FirebaseFirestore.getInstance().collection("users").document(user.userId ?: "")
        user.apply {
            online = true
            last_active = 0
        }
        query.set(user)
        /*
                .addOnCompleteListener { task ->
            if (task.isSuccessful)
                loadUsers()
        }
        */

        // Firebase Status
        var fbquery = FirebaseDatabase.getInstance().getReference("status/" + user.userId).setValue("online")


        // Adding on disconnect hook
        FirebaseDatabase.getInstance().getReference("/status/" + user.userId)
                .onDisconnect()     // Set up the disconnect hook
                .setValue("offline");

    }

    fun makeUserOffline(user: UserModel)
    {

        // Firestore
        var query = FirebaseFirestore.getInstance().collection("users").document(user.userId ?: "")
        user.apply {
        //    online = false
            last_active = System.currentTimeMillis()
        }
        query.set(user)

        // Firebase
        var fbquery = FirebaseDatabase.getInstance().getReference("status/" + user.userId).setValue("offline")
    }
}
