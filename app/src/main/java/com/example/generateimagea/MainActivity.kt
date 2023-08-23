package com.example.generateimagea

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
     val url = "https://api.openai.com/v1/images/generations"
    private lateinit var queryTV: TextView
    private lateinit var imageView: ImageView
    private lateinit var queryEdt: EditText
    private lateinit var loadingPB: ProgressBar
    private lateinit var noDataRL: RelativeLayout
    private lateinit var dataRL: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queryTV = findViewById(R.id.idTvQuery)
        imageView = findViewById(R.id.idIVImage)
        queryEdt = findViewById(R.id.idEdtQuery)
        loadingPB = findViewById(R.id.idPBLoading)
        noDataRL = findViewById(R.id.idNoDataLayout)
        dataRL = findViewById(R.id.idRLData)

        queryEdt.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_SEND){
                if (queryEdt.text.toString().isNotEmpty()){
                    queryTV.text= queryEdt.text.toString()
                    getResponse(queryEdt.text.toString())
                }else{
                    Toast.makeText(applicationContext,"Please enter your query",Toast.LENGTH_SHORT).show()
                }
            }
            false
        }
    }

    private fun getResponse(query: String) {
        queryEdt.setText("")
        loadingPB.visibility = View.VISIBLE

        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val jsonObject = JSONObject()
        jsonObject.put("prompt", query)
        jsonObject.put("n", 1)
        jsonObject.put("size", "256x256")

        val postRequest: JsonObjectRequest= object : JsonObjectRequest(Method.POST,url,jsonObject,Response.Listener { response ->
            noDataRL.visibility = View.GONE
            dataRL.visibility = View.VISIBLE
        }, Response.ErrorListener { error ->
            Toast.makeText(applicationContext,"Fail to generate image..",Toast.LENGTH_SHORT).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer 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"
                return params
            }
        }

        postRequest.retryPolicy = object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            override fun retry(error: VolleyError?) {

            }
        }
        queue.add(postRequest)
    }
}
