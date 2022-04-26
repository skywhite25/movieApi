package com.th.movieapi_kt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.th.movieapi_kt.model.MovieItem
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.w3c.dom.Element
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 크롤링 시작
        btnStart.setOnClickListener {
            doTask("https://movie.naver.com/movie/running/current.nhn?order?reverse")
        }

        }
        // 크롤링 하기
        fun doTask(url : String) {
            var documentTitle : String = ""
            var itemList : ArrayList<MovieItem> = arrayListOf()

            Single.fromCallable {
                try {
                    // 사이트 접속 후 HTML 가져오기
                    val doc = Jsoup.connect(url).get()

                    // HTML 파싱 후 데이터 추출
                    val elements : Elements = doc.select("ul.lst_detail_t1 li")
                    // elements 처리
                    run elemLoops@{
                        elements.forEachIndexed { index, elem ->
                            var title = elem.select("dt.tit a").text()
                            var num = elem.select("d1.info_star div.star_t1 span.num").text()
                            var num2 = elem.select("span.num2").text()
                            var reserve = elem.select("d1.info_exp div.star_t1 span.num").text()

                            // MovieItem 생성 후 추가
                            var item = MovieItem(title,num,num2,reserve)
                            itemList.add(item)

                            // 10개
                            if(index == 9) return@elemLoops
                        }
                    }
                    documentTitle = doc.title()
                } catch (e:Exception) {e.printStackTrace()}

                return@fromCallable documentTitle
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    // 응답 성공
                    { text ->
                        showData(text.toString())
                        showData(itemList.joinToString())
                    },
                    // 오류
                    { it.printStackTrace()})
            }

    // textView 출력
    fun showData(msg : String){
        output.append(msg + "\n")
    }
}