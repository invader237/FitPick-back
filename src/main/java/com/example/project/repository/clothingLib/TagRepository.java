package com.example.project.repository.clothingLib;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.example.project.model.clothingLib.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t WHERE t.tag_temperature_score >= ?1 AND t.tag_temperature_score <= ?2")
    List<Tag> findByTemperature(int min, int max);
}
