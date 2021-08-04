package com.example.rp_week4_1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    var time: TextView? = null
    var count: TextView? = null
    var start: Button? = null
    var img_array = arrayOfNulls<ImageView>(9)
    var imageID = intArrayOf(
        R.id.imageView1,
        R.id.imageView2,
        R.id.imageView3,
        R.id.imageView4,
        R.id.imageView5,
        R.id.imageView6,
        R.id.imageView7,
        R.id.imageView8,
        R.id.imageView9
    )
    val TAG_ON = "on" //태그용
    val TAG_OFF = "off"
    var score = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        time = findViewById<View>(R.id.time) as TextView
        count = findViewById<View>(R.id.count) as TextView
        start = findViewById<View>(R.id.start) as Button
        for (i in img_array.indices) {
/*int img_id = getResources().getIdentifier("imageView"+i+1, "id", "com.example.pc_20.molegame");*/
            img_array[i] = findViewById<View>(imageID[i]) as ImageView
            img_array[i]!!.setImageResource(R.drawable.moledown)
            img_array[i]!!.tag = TAG_OFF
            img_array[i]!!.setOnClickListener { v ->

                //두더지이미지에 온클릭리스너
                if ((v as ImageView).tag.toString() == TAG_ON) {
                    Toast.makeText(applicationContext, "good", Toast.LENGTH_LONG).show()
                    count!!.text = score++.toString()
                    v.setImageResource(R.drawable.moledown)
                    v.setTag(TAG_OFF)
                } else {
                    Toast.makeText(applicationContext, "bad", Toast.LENGTH_LONG).show()
                    if (score <= 0) {
                        score = 0
                        count!!.text = score.toString()
                    } else {
                        count!!.text = score--.toString()
                    }
                    v.setImageResource(R.drawable.moleup)
                    v.setTag(TAG_ON)
                }
            }
        }
        time!!.text = "30초"
        count!!.text = "0마리"
        start!!.setOnClickListener {
            start!!.visibility = View.GONE
            count!!.visibility = View.VISIBLE
            Thread(timeCheck()).start()
            for (i in img_array.indices) {
                Thread(DThread(i)).start()
            }
        }
    }

    var onHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            img_array[msg.arg1]!!.setImageResource(R.drawable.moleup)
            img_array[msg.arg1]!!.tag = TAG_ON //올라오면 ON태그 달아줌
        }
    }
    var offHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            img_array[msg.arg1]!!.setImageResource(R.drawable.moledown)
            img_array[msg.arg1]!!.tag = TAG_OFF //내려오면 OFF태그 달아줌
        }
    }

    inner class DThread internal constructor(index: Int) : Runnable {
        //두더지를 올라갔다 내려갔다 해줌
        var index = 0 //두더지 번호
        override fun run() {
            while (true) {
                try {
                    val msg1 = Message()
                    val offtime = Random().nextInt(5000) + 500
                    Thread.sleep(offtime.toLong()) //두더지가 내려가있는 시간
                    msg1.arg1 = index
                    onHandler.sendMessage(msg1)
                    val ontime = Random().nextInt(1000) + 500
                    Thread.sleep(ontime.toLong()) //두더지가 올라가있는 시간
                    val msg2 = Message()
                    msg2.arg1 = index
                    offHandler.sendMessage(msg2)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        init {
            this.index = index
        }
    }

    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            time!!.text = msg.arg1.toString() + "초"
        }
    }

    inner class timeCheck : Runnable {
        val MAXTIME = 30
        override fun run() {
            for (i in MAXTIME downTo 0) {
                val msg = Message()
                msg.arg1 = i
                handler.sendMessage(msg)
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            val intent = Intent(this@MainActivity, ResultActivity::class.java)
            intent.putExtra("score", score)
            startActivity(intent)
            finish()
        }
    }
}