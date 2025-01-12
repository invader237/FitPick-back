package com.example.project.service.AwsS3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.UUID;

/**
 * Service pour gérer les opérations liées à Amazon S3, notamment le téléchargement de fichiers.
 * Ce service utilise le SDK AWS pour interagir avec S3.
 */
@Service
public class AwsS3Service {

    private final S3Client s3Client;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.bucket:mon-projet-bucket}")
    private String bucketName;

    /**
     * Constructeur pour injecter les dépendances nécessaires.
     *
     * @param s3Client Instance du client S3 configurée pour interagir avec AWS.
     */
    public AwsS3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Télécharge un fichier vers un bucket S3 et retourne l'URL publique du fichier téléchargé.
     *
     * @param file Le fichier à télécharger.
     * @return L'URL publique du fichier téléchargé.
     * @throws IOException Si une erreur survient lors de la lecture des données du fichier.
     */
    public String uploadFile(MultipartFile file) throws IOException {
        validateFileInput(file);

        // Génération d'un nom unique pour le fichier à l'aide d'un UUID
        String fileName = generateUniqueFileName(file.getOriginalFilename());

        // Construction de la requête pour télécharger le fichier dans le bucket S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType()) // Ajout du type MIME pour améliorer la compatibilité
                .build();

        // Téléchargement du fichier vers S3
        try {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes())
            );
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi du fichier vers S3 : " + e.getMessage(), e);
        }

        // Génération de l'URL publique du fichier
        return generateFileUrl(fileName);
    }

    /**
     * Valide les entrées du fichier.
     *
     * @param file Le fichier à valider.
     */
    private void validateFileInput(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est vide ou non valide.");
        }
    }

    /**
     * Génère un nom unique pour le fichier en préfixant le nom d'origine avec un UUID.
     *
     * @param originalFileName Le nom d'origine du fichier.
     * @return Un nom unique pour le fichier.
     */
    private String generateUniqueFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    /**
     * Génère l'URL publique du fichier basé sur le nom du bucket, la région et le nom du fichier.
     *
     * @param fileName Le nom du fichier stocké dans le bucket.
     * @return L'URL publique complète permettant d'accéder au fichier.
     */
    private String generateFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }
}
