package com.svvar.lab3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etAveragePower = findViewById<EditText>(R.id.etAveragePower)
        val etSigma1 = findViewById<EditText>(R.id.etSigma1)
        val etElectricityCost = findViewById<EditText>(R.id.etElectricityCost)
        val etDelta = findViewById<EditText>(R.id.etDelta)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        btnCalculate.setOnClickListener {
            val averagePower = etAveragePower.text.toString().toDouble()
            val sigma1 = etSigma1.text.toString().toDouble()
            val electricityCost = etElectricityCost.text.toString().toDouble()
            val delta = etDelta.text.toString().toDouble() / 100

            val errorRange = delta * averagePower
            val lowerBound = averagePower - errorRange
            val upperBound = averagePower + errorRange

            val deltaW1 = calculateProbability(sigma1, lowerBound, upperBound, averagePower)

            val w1 = averagePower * 24 * deltaW1
            val w2 = averagePower * 24 * (1 - deltaW1)

            val profit = w1 * electricityCost * 1000
            val penalty = w2 * electricityCost * 1000

            tvResult.text = this.getString(
                R.string.resultString,
                String.format("%.1f", w1),
                String.format("%.1f", w2),
                String.format("%.0f", profit),
                String.format("%.0f", penalty)).replace("\\n", "\n")

        }
    }

    private fun calculateProbability(sigma: Double, lowerBound: Double, upperBound: Double, mean: Double): Double {
        val norm = org.apache.commons.math3.distribution.NormalDistribution(mean, sigma)
        return norm.cumulativeProbability(upperBound) - norm.cumulativeProbability(lowerBound)
    }
}
