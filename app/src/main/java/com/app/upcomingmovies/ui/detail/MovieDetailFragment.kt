package com.app.upcomingmovies.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.app.upcomingmovies.R
import com.app.upcomingmovies.ui.base.BaseFragment
import com.app.upcomingmovies.utils.gone
import com.app.upcomingmovies.utils.toast
import com.app.upcomingmovies.utils.visible
import kotlinx.android.synthetic.main.movie_detail_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailFragment : BaseFragment() {
    private val viewModel: MovieDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.run {
            with(MovieDetailFragmentArgs.fromBundle(this)) {
                viewModel.fetchImages(id)
                setTitle(titile)
            }
        }

        viewModel.error.observe(viewLifecycleOwner, Observer {
            requireContext().toast(it)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
            container.visibility = if (it) View.GONE else View.VISIBLE
        })

        viewModel.movieImages.observe(viewLifecycleOwner, Observer {
            imageSlider.adapter = ImageSliderAdapter(it)
            if (it.size > 1) {
                pageIndicator.visible()
                pageIndicator.attachTo(imageSlider)
            } else {
                pageIndicator.gone()
            }
        })

        viewModel.movie.observe(viewLifecycleOwner, Observer {
            it.run {
                tv_title.text = title
                tv_overview.text = overview
            }
        })
    }

    override fun getLayout(): Int = R.id.movieDetailFragment

    override fun getHasOptionsMenu(): Boolean = false

    override fun setTitle(title: String) {
        tvTitle?.text = title
    }
}