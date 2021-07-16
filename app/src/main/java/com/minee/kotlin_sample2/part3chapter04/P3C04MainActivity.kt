package com.minee.kotlin_sample2.part3chapter04

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.minee.kotlin_sample2.BuildConfig
import com.minee.kotlin_sample2.R
import com.minee.kotlin_sample2.databinding.ActivityP3C04MainBinding
import com.minee.kotlin_sample2.part3chapter04.adapter.BookAdapter
import com.minee.kotlin_sample2.part3chapter04.adapter.HistoryAdapter
import com.minee.kotlin_sample2.part3chapter04.api.BookService
import com.minee.kotlin_sample2.part3chapter04.model.BestSellerDto
import com.minee.kotlin_sample2.part3chapter04.model.History
import com.minee.kotlin_sample2.part3chapter04.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class P3C04MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityP3C04MainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService

    private lateinit var db: P3C04Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityP3C04MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()
        initHistoryRecyclerView()

        db = getDatabase(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks("${BuildConfig.INTERPARK_API_KEY}")
            .enqueue(object : Callback<BestSellerDto> {

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "NOT SUCCESS !!!")
                        return
                    }

                    response.body()?.let {
                        Log.d(TAG, it.toString())

                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }

                        adapter.submitList(it.books)
                    }
                }

            })

        initSearchEditText()

    }

    private fun search(keyWord: String) {
        bookService.getBooksByName("${BuildConfig.INTERPARK_API_KEY}", keyWord)
            .enqueue(object : Callback<SearchBookDto> {
                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    hideHistoryView()
                    Log.e(TAG, t.toString())
                }

                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {
                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "NOT SUCCESS !!!")
                        return
                    }

                    hideHistoryView()
                    saveSearchKeyword(keyWord)

                    adapter.submitList(response.body()?.books.orEmpty())
                }

            })
    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter(itemClickedListener = {
            val intent = Intent(this, P3C04DetailActivity::class.java)
            intent.putExtra("bookModel", it)
            startActivity(intent)
        })

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickedListener = {
            deleteSearchKeyword(it)
        })

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }

        binding.searchEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }

    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed()

            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyWord: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyWord))
        }.start()
    }

    private fun deleteSearchKeyword(keyWord: String) {
        Thread {
            db.historyDao().delete(keyWord)
            showHistoryView()
        }.start()
    }

    companion object {
        private const val TAG = "minee"
    }
}