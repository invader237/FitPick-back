package com.example.project.repository.clothingLib;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.project.model.clothingLib.Clothing;
import com.example.project.model.clothingLib.Tag;

import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface ClothingRepository extends JpaRepository<Clothing, Long> {

    /**
     * Finds all clothing items associated with a specific user.
     *
     * @param userId the ID of the user whose clothing items are being retrieved
     * @return a list of {@link Clothing} objects belonging to the user
     */
    List<Clothing> findByUserId(Long userId);

    /**
     * Finds a specific clothing item by user ID and clothing ID.
     *
     * @param userId the ID of the user who owns the clothing item
     * @param cloId the ID of the clothing item to retrieve
     * @return a {@link Clothing} object representing the clothing item, or null if not found
     */
    @Query("SELECT c FROM Clothing c WHERE c.userId = :userId AND c.clo_id = :cloId")
    Clothing findByUserIdAndCloId(@Param("userId") Long userId, @Param("cloId") Long cloId);

    /**
     * Retrieves all tags associated with a specific clothing item for a given user.
     *
     * @param userId the ID of the user who owns the clothing item
     * @param cloId the ID of the clothing item whose tags are being retrieved
     * @return a list of {@link Tag} objects associated with the clothing item
     */
    @Query("SELECT c.tags FROM Clothing c WHERE c.userId = :userId AND c.clo_id = :cloId")
    List<Tag> findTagsByCloId(@Param("userId") Long userId, @Param("cloId") Long cloId);
}
