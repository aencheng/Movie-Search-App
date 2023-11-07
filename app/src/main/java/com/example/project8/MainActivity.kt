package com.example.project8

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://www.omdbapi.com/"
private const val API_KEY = "7a22a494"

class MainActivity : AppCompatActivity() {
    val tag = "MainActivity"
    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val adapter = MoviesItemAdapter(this)
        val rvMovies = this.findViewById<RecyclerView>(R.id.rvMovies)
        rvMovies.layoutManager = LinearLayoutManager(this)
        rvMovies.adapter = adapter

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

        val service = retrofit.create(OmdbService::class.java)

        val searchInput = this.findViewById<EditText>(R.id.searchInput)
        val searchButton = this.findViewById<Button>(R.id.searchButton)
        val feedbackButton = this.findViewById<Button>(R.id.feedbackButton)

        var call: Call<MovieSearchResult>?
        var body: MovieSearchResult? = null


        // on click for search button that uses retrofit to request and search movie based on link, api_key, and movie name
        searchButton.setOnClickListener{
            if(searchInput.text.isNotEmpty()) {
                call = service.searchMovies(searchInput.text.toString(), API_KEY)

                call!!.enqueue(object : Callback<MovieSearchResult> {
                    override fun onResponse(
                        call: Call<MovieSearchResult>,
                        response: Response<MovieSearchResult>
                    ) {
                        Log.i(tag, "onResponse $response")
                        Log.i(tag, "onResponse ${response.body()?.toString()}")
                        body = response.body()
                        if (body == null) {
                            Log.w(
                                tag,
                                "Did not receive valid response body from Yelp API... exiting"
                            )
                            return
                        }
                        adapter.setMovie(body!!)
                    }

                    override fun onFailure(call: Call<MovieSearchResult>, t: Throwable) {
                        Log.i(tag, "onFailure $t")
                    }
                })
            }
        }

        // intent for emailing feedback
        feedbackButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:cheng15@iu.edu")
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            startActivity(emailIntent)
        }
    }
}