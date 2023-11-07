package com.example.project8

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.project8.databinding.MovieItemBinding

class MoviesItemAdapter(val context: Context) :
    RecyclerView.Adapter<MoviesItemAdapter.ItemViewHolder>() {

    private var movie: MovieSearchResult? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setMovie(movie: MovieSearchResult) {
        this.movie = movie
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        movie?.let { holder.bind(it, context) }
    }

    override fun getItemCount(): Int = if (movie != null) 1 else 0

    class ItemViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieItemBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }

        @SuppressLint("SetTextI18n", "QueryPermissionsNeeded")
        fun bind(movie: MovieSearchResult, context: Context) {

            // setting all needed values to their expected items
            binding.movieName.text = movie.title
            binding.runTime.text = "Runtime: ${movie.runtime}"
            binding.releaseYear.text = "Released in ${movie.year}"
            if(movie.ratings[0] != null){
                binding.rating.text = "Rating: ${(movie.ratings[0]).toString().substring(
                    (movie.ratings[0]).toString().indexOf("value=") + 6,
                    (movie.ratings[0]).toString().length - 1)
                }"
            }
            binding.imdbRating.text = "IMDB Rating: ${movie.imdbRating}"
            binding.genre.text = "Genre: ${movie.genre}"
            binding.link.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.link.text = "https://www.imdb.com/title/${movie.id}/"
            // Display other movie details
            // You can access movie.year and movie.poster here
            // Example: binding.releaseYear.text = "Year: ${movie.year}"

            // Load the movie poster using Glide
            Glide.with(context).load(movie.poster)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(), RoundedCorners(20)
                    )
                )
                .into(binding.imageView)

            // link on click listener which takes you to link page
            binding.link.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(binding.link.text.toString()))
                context.startActivity(intent)
            }

            // share button onclick for sharing intent
            binding.shareButton.setOnClickListener{
                if(movie.title != null){
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "${movie.title} - https://www.imdb.com/title/${movie.id}/")

                    if (shareIntent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(Intent.createChooser(shareIntent, "Share URL"))
                    }
                }
            }
        }
    }
}
