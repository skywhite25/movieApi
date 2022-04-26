package com.th.movieapi_kt.model

// 영화 객체
data class MovieItem(
    val title : String, // 제목
    val num : String, // 평점
    val num2 : String, // 평점 준 인원 수
    val reserve : String // 예매율
)
