package com.example.rp_week4_1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils.join
import android.transition.Transition
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rp_week4_1.databinding.ActivityMainBinding
import github.hongbeomi.touchmouse.TouchMouseManager
import github.hongbeomi.touchmouse.TouchMouseOption
import java.lang.String.join
import java.util.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), MyCustomDialogInterface {

    var isPlaying = false

    private lateinit var binding: ActivityMainBinding
    var img_array = arrayOfNulls<ImageView>(9)
    var imageID = intArrayOf(
        R.id.mole1,
        R.id.mole2,
        R.id.mole3,
        R.id.mole4,
        R.id.mole5,
        R.id.mole6,
        R.id.mole7,
        R.id.mole8,
        R.id.mole9
    )
    val TAG_ON = "on" //태그용
    val TAG_OFF = "off"
    var score = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isPlaying = false
//        val dialogActivity = DialogActivity(this,this)

        TouchMouseManager.setOptions(
            TouchMouseOption(
                cursorDrawable = R.drawable.mouse
            )
        )

        for (i in img_array.indices) {
/*int img_id = getResources().getIdentifier("imageView"+i+1, "id", "com.example.pc_20.molegame");*/
            img_array[i] = findViewById<View>(imageID[i]) as ImageView
            img_array[i]!!.setImageResource(R.drawable.mole_be)
            img_array[i]!!.tag = TAG_OFF
            img_array[i]!!.setOnClickListener { v ->

                //두더지이미지에 온클릭리스너
                if ((v as ImageView).tag.toString() == TAG_ON) {
//                    Toast.makeText(applicationContext, "good", Toast.LENGTH_LONG).show()
                    score++
                    Thread {
                        handler.post {
                            binding.scoreLi.text = "+1" //0.5초간 표시 되도록 수정하기
                            binding.scoreLi.setTextColor(Color.parseColor("#fee203"))
                        }
                        Thread.sleep(500)
                        handler.post {
                            binding.scoreLi.text = ""
                        }
                    }.start()

                    binding.scoreTv.text = score.toString()
//                    binding.scoreTv.text = score++.toString()
                    v.setImageResource(R.drawable.mole_click2)
                    v.setTag(TAG_OFF)
                } else {
//                    Toast.makeText(applicationContext, "bad", Toast.LENGTH_LONG).show()
                    Thread {
                        handler.post {
                            binding.scoreLi.text = "-1"
                            //0.5초간 표시 되도록 수정하기
                            binding.scoreLi.setTextColor(Color.parseColor("#fa2804"))

                        }
                        Thread.sleep(500)
                        handler.post {
                            binding.scoreLi.text = ""
                        }
                    }.start()


                    if (score == 0) {
                        score = 0
                        binding.scoreTv.text = score.toString()
                    } else {
                        score--
                        binding.scoreTv.text = score.toString()
                    }
//                    v.setImageResource(R.drawable.mole)
//                    v.setTag(TAG_ON)
                }
            }
        }
        binding.timerTv!!.text = "30초"
        binding.scoreTv!!.text = "0"
        binding.startTv!!.setOnClickListener {
            binding.startTv!!.visibility = View.GONE
            binding.scoreTv!!.visibility = View.VISIBLE
            binding.moleLl.visibility = View.VISIBLE
            Thread(timeCheck()).start()
            for (i in img_array.indices) {
                Thread(DThread(i)).start()
            }
        }
    }

    var onHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            img_array[msg.arg1]!!.setImageResource(R.drawable.mole)
            img_array[msg.arg1]!!.tag = TAG_ON //올라오면 ON태그 달아줌
        }
    }
    var offHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            img_array[msg.arg1]!!.setImageResource(R.drawable.mole_be)
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
            binding.timerTv!!.text = msg.arg1.toString() + "초"
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

            intent = Intent(this@MainActivity, ResultActivity::class.java)
            intent.putExtra("score", score)
            startActivity(intent)
            finish()

        }
    }


    private fun onClick() {
        var dialog = Intent(this, DialogActivity::class.java)
        startActivity(dialog)
    }

    override fun onYes() {

    }

    override fun onPause() {
        super.onPause()
        isPlaying = false

            Thread{
                handler.post(){

                    Thread(timeCheck())!!.join()
                }
            }.join()
            for (i in img_array.indices) {
                Thread(DThread(i))!!.join()
            }



    }


    override fun onResume() {
        super.onResume()
        isPlaying = true
        val dialogActivity = DialogActivity(this, this)


        Thread(timeCheck())!!.start()
        for (i in img_array.indices) {
            Thread(DThread(i))!!.start()
        }
        dialogActivity.show()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }

}