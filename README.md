# Spring AI Kotlin Test

Ce projet est une application Spring Boot développée en Kotlin, intégrant Spring AI avec le modèle OpenAI. 
Il s'agit d'un projet expérimental dont l'objectif principal est de découvrir et d'explorer l'intégration de Spring AI dans un environnement Kotlin. 
Ce projet n'est pas destiné à un usage en production, la gestion des erreurs et la couverture de tests n'ont d'ailleurs pas encore été mises en place.

## Prérequis
- Java 21
- Gradle (wrapper inclus)
- Une clé API OpenAI (variable d'environnement `OPENAI_API_KEY`)

Afin d'utiliser le RAG (Retrieval-Augmented Generation), il est nécessaire d'installer Milvus localement.
Un fichier `docker-compose.yml` est fourni dans le répertoire `src/main/resources/milvus` pour en faciliter le déploiement.
Il s'exécute via la commande :

```bash
docker compose up -d
```

## Construction du projet

Utilisez le wrapper Gradle pour compiler le projet :

```bash
./gradlew build
```

## Lancement de l'application

Toujours avec le wrapper Gradle :

```bash
./gradlew bootRun
```

L'application sera accessible sur [http://localhost:8080](http://localhost:8080).

## Configuration

La clé API OpenAI doit être définie dans la variable d'environnement `OPENAI_API_KEY` avant de lancer l'application.

## Tests

Pour exécuter les tests :

```bash
./gradlew test
```

## Structure du projet
- `src/main/kotlin` : code source principal
- `src/test/kotlin` : tests
- `src/main/resources/application.yaml` : configuration Spring

---

