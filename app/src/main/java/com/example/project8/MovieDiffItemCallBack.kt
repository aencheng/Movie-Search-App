package com.example.project8

import androidx.recyclerview.widget.DiffUtil

class MovieDiffItemCallBack : DiffUtil.ItemCallback<OmdbMovie>(){
    override fun areItemsTheSame(oldItem: OmdbMovie, newItem: OmdbMovie)
            = (oldItem.title == newItem.title)
    override fun areContentsTheSame(oldItem: OmdbMovie, newItem: OmdbMovie) = (oldItem == newItem)

}