package com.wajahatkarim3.firestore_usersonline

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter


/**
 * Created by Wajahat on 8/24/2017.
 */

class MyAutoCompleteTextview : android.support.v7.widget.AppCompatAutoCompleteTextView {

    private var mContext: Context? = null
    lateinit var adapter: ArrayAdapter<String>

    constructor(context: Context) : super(context) {
        mContext = context
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val array = mContext!!.obtainStyledAttributes(attrs, R.styleable.macview)
            val id = array.getResourceId(R.styleable.macview_mac_itemList, 0)

            if (id != 0) {
                val values = resources.getStringArray(id)
                adapter = ArrayAdapter(mContext!!, android.R.layout.simple_list_item_1, values)
                setAdapter(adapter)
            }
            array.recycle()
        }

        isClickable = true
        threshold = 0
        setOnTouchListener { view, motionEvent ->
            showDropDown()
            false
        }
    }

    fun setItems(list: Array<String>) {
        adapter = ArrayAdapter(mContext!!, android.R.layout.simple_list_item_1, list)
        setAdapter(adapter)
    }

    fun onItemClick( listner: (pos:Int, label:String) -> Unit )
    {
        onItemClickListener = object : AdapterView.OnItemClickListener
        {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                listner(p2, adapter.getItem(p2))
            }
        }
    }
}