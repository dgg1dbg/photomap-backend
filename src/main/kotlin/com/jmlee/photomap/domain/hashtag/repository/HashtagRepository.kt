package com.jmlee.photomap.domain.hashtag.repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import com.jmlee.photomap.domain.hashtag.model.Hashtag
import com.jmlee.photomap.domain.post.model.Post

interface HashtagRepository: JpaRepository<Hashtag, Long> {
    @Query("SELECT h FROM Hashtag h WHERE h.name LIKE :name%")
    fun getHashtag(name: String): List<Hashtag>?
    @Query("SELECT h FROM Hashtag h WHERE h.name = :name")
    fun getExactHashtag(name: String): Hashtag?
    @Query(
        """
        SELECT h.* 
        FROM hashtag h
        JOIN post_hashtag ph ON h.id = ph.hashtag_id
        GROUP BY h.id
        ORDER BY COUNT(ph.post_id) DESC
        LIMIT :count
        """,
        nativeQuery = true
    )
    fun getTrending(@Param("count") count: Int): List<Hashtag>?
    
}