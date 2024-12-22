package com.example.project.repository.clothingLib;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.model.clothingLib.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
