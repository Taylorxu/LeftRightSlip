package com.xuzhiguang.leftrightslip

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.FOCUSABLE
import android.view.View.FOCUSABLE_AUTO
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Scroller
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var data = mutableListOf<String>()
    var mScroller: Scroller? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setData()
        mScroller = Scroller(baseContext)
        context_list.layoutManager = LinearLayoutManager(baseContext)
        context_list.adapter = MyAdapter(data)


    }


    private fun setData() {
        for (i in 0..50) {
            data.add("这是第${i + 1}数据")
        }

    }


    inner class MyAdapter(var data: MutableList<String>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
        override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
            holder?.bindData(data[position])
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
            var view = LayoutInflater.from(parent?.context).inflate(R.layout.item_list, parent, false)
            view.focusable = FOCUSABLE
            view.setOnTouchListener { v, event -> touchEvent(v, event) }
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return data.size
        }


        inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            fun bindData(itemData: String) {
                var textView = itemView.findViewById<TextView>(R.id.item_text)
                textView.text = itemData
            }
        }

        var smoothMove = false
        var mLastX: Int = 0
        var width: Int = 0
        var downX: Int = 0
        var upX: Int = 0
        fun touchEvent(v: View, event: MotionEvent): Boolean {
            width = v.findViewById<LinearLayout>(R.id.hide_view).width
            var event_x = event.x
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                     var right_layout_x = mLastX - event_x
                     var view_scroll_x = v.scrollX
                     Log.e("ACTION_MOVE", "view_scroll_x--${view_scroll_x}+ ${right_layout_x}=${view_scroll_x + right_layout_x}  ")
                     if (Math.abs(right_layout_x) + 100 < Math.abs(event.y)) {
                         return true
                     }
                     if (!smoothMove) {

                         if (view_scroll_x + right_layout_x <= 80) {
                             v.scrollTo(0, 0)
                             return true
                         } else if (view_scroll_x + right_layout_x >= width) {
                             v.scrollTo(width, 0)
                             return true
                         }
                         v.scrollBy(right_layout_x.toInt(), 0)
                     }
                }
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x.toInt()
                    Log.e("ACTION_DOWN", "ACTION_DOWN------${downX}")
                }
                MotionEvent.ACTION_UP -> {
                    smoothMove = true
                    upX = event.x.toInt()
                    var far = downX - upX
                    Log.e("ACTION_UP", "ACTION_UP------${far}")
                    if (far > 0) {//左方向
                        if (far >= 80) {
                            v.scrollTo(width, 0)
                            return true
                        }
                    } else if (far < 0) {
                        if (Math.abs(far) >= 80) {
                            v.scrollTo(0, 0)
                            return true
                        }
                    }

                }

                else->{
                smoothMove = true
                upX = event.x.toInt()
                var far = downX - upX
                if (far > 0) {//左方向
                    if (far >= 80) {
                        v.scrollTo(width, 0)
                        return true
                    }
                } else if (far < 0) {
                    if (Math.abs(far) >= 80) {
                        v.scrollTo(0, 0)
                        return true
                    }
                }
                return true
            }
            }
            mLastX = event_x.toInt()
            smoothMove = false
            return true
        }


    }


}
