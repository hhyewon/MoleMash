package com.example.rp_week4_1


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.rp_week4_1.databinding.ActivityResultBinding


class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root) //주소로 알고있는 xml을 눈에 보이는 view로 바꿔줌 ->InfLate
        val pref = getSharedPreferences("pref_score", MODE_PRIVATE) // 키값이 또 있으면 덮어쓰겠다

        val score = intent.getIntExtra("score", -1)
        binding.subResult.text = score.toString()

        if (pref.getInt("pref_score", 0) < score) { //내점수가 저번 점수보다 크면
            pref.edit().putInt("prefscore", score).commit() //반영의 commit(). 현재상태 저장
//            binding.newScore.visibility=View.VISIBLE
            binding.newScore.text = "🔻 최고점수 달성 ! 🔻"
            binding.subResult.text = score.toString()
        }
        if(score == -3){
            binding.newScore.text= "다시 도전해 보세요😥"
            binding.retultTxt.text=" 점 💦"
        }


        binding.subRetry.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onPause() {
        super.onPause()

    }

//    protected fun saveState() {
//        val pref= getSharedPreferences("prefscore",MODE_PRIVATE)
//        val editor=pref.edit()
//        editor.putString("prefscore", binding.subResult.text).commit()
//
//    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }


}