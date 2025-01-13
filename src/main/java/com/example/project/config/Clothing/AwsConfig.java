package com.example.project.config.Clothing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Configuration pour Amazon S3.
 * Ce fichier configure le client S3 avec les informations nécessaires
 * telles que les clés d'accès et la région.
 */
@Configuration
public class AwsConfig {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    /**
     * Configure et retourne une instance de S3Client pour interagir avec AWS S3.
     *
     * @return une instance configurée de S3Client
     */
    @Bean
    public S3Client s3Client() {
        validateAwsCredentials();

        return S3Client.builder()
                .region(Region.of(region)) // Région configurée
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    /**
     * Valide que les clés AWS et la région sont correctement configurées.
     */
    private void validateAwsCredentials() {
        if (accessKey == null || accessKey.isEmpty()) {
            throw new IllegalArgumentException("La clé d'accès AWS (accessKey) est manquante ou invalide.");
        }

        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("La clé secrète AWS (secretKey) est manquante ou invalide.");
        }

        if (region == null || region.isEmpty()) {
            throw new IllegalArgumentException("La région AWS (region) est manquante ou invalide.");
        }
    }
}
