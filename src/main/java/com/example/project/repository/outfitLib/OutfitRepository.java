package com.example.project.repository.outfitLib;

import com.example.project.model.outfitLib.Outfit;
import com.example.project.model.clothingLib.Clothing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Long> {

    /**
     * Récupère toutes les tenues d’un utilisateur.
     *
     * @param userId l'ID de l'utilisateur
     * @return une liste de tenues appartenant à l'utilisateur
     */
    @Query("SELECT o FROM Outfit o WHERE o.userId = :userId")
    List<Outfit> findAllByUserId(@Param("userId") Long userId);

    /**
     * Récupère le détail d’une tenue, y compris la composition des vêtements.
     *
     * @param outfitId l'ID de la tenue
     * @return une tenue avec la liste des vêtements associés
     */
    @Query("SELECT o FROM Outfit o LEFT JOIN FETCH o.clothes WHERE o.fit_id = :outfitId")
    Outfit findDetailedOutfitById(@Param("outfitId") Long outfitId);

    /**
     * Vérifie si les vêtements appartiennent à un utilisateur spécifique.
     *
     * @param userId l'ID de l'utilisateur
     * @param clothingIds une liste d'IDs des vêtements
     * @return une liste de vêtements appartenant à l'utilisateur
     */
    @Query("SELECT c FROM Clothing c WHERE c.userId = :userId AND c.clo_id IN :cloIds")
    List<Clothing> findByUserIdAndCloIdIn(@Param("userId") Long userId, @Param("cloIds") List<Long> cloIds);

    /**
     * Supprime une tenue par son ID.
     *
     * @param outfitId l'ID de la tenue à supprimer
     */
    void deleteById(Long outfitId);
}
