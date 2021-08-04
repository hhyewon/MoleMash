package com.example.rp_week4_1


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.rp_week4_1.databinding.ActivityMainBinding
import com.example.rp_week4_1.databinding.ActivityResultBinding


class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    var spf: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root) //주소로 알고있는 xml을 눈에 보이는 view로 바꿔줌 ->InfLate
        var spf = getSharedPreferences("spfScore", MODE_PRIVATE) // 키값이 또 있으면 덮어쓰겠다

        val score = intent.getIntExtra("score", -1)
        binding.subResult.setText(score.toString())
        if (spf.getInt("spfscore", 0) < score) { //내점수가 저번 점수보다 크면
            spf.edit().putInt("spfscore", score).commit() //반영의 commit(). 현재상태 저장
            binding.subResult.setText("신기록달성\n$score")
        }


        binding.subRetry.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        })
    }
}