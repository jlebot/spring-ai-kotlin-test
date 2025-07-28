# Spring AI Kotlin Test

Ce projet est une application Spring Boot utilisant Kotlin et Spring AI avec le modèle OpenAI.

## Prérequis
- Java 21
- Gradle (wrapper inclus)
- Une clé API OpenAI (variable d'environnement `OPENAI_API_KEY`)

## Construction du projet

Utilisez le wrapper Gradle pour compiler le projet :

```bash
./gradlew build
```

Sous Windows :
```bat
./gradlew.bat build
```

## Lancement de l'application

Toujours avec le wrapper Gradle :

```bash
./gradlew bootRun
```

Sous Windows :
```bat
./gradlew.bat bootRun
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

Généré automatiquement.

