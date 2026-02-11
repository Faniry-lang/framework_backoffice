# ===================================
# Stage 1: Build
# ===================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY libs/ libs/

# Installer les dépendances locales (framework.jar et legacy-orm)
RUN mvn install:install-file \
    -Dfile=libs/framework.jar \
    -DgroupId=com.itu.framework \
    -DartifactId=framework \
    -Dversion=1.0-SNAPSHOT \
    -Dpackaging=jar && \
    mvn install:install-file \
    -Dfile=libs/legacy-orm-1.0-SNAPSHOT.jar \
    -DgroupId=com.itu.legacy \
    -DartifactId=legacy-orm \
    -Dversion=1.0-SNAPSHOT \
    -Dpackaging=jar

# Télécharger les dépendances (utilise le cache Docker)
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src/ src/

# Compiler et packager l'application
RUN mvn clean package -DskipTests

# ===================================
# Stage 2: Runtime
# ===================================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/backoffice.jar app.jar

# Port exposé (Render utilise la variable PORT, défaut 8080)
EXPOSE 8080

# Variables d'environnement par défaut (surchargées par Render)
# Sur Render, configurer ces variables dans le dashboard :
# - DATABASE_URL (fourni automatiquement si vous utilisez Render PostgreSQL)
# - ou DB_URL, DB_USERNAME, DB_PASSWORD, DB_DRIVER manuellement
ENV PORT=8080
ENV DB_DRIVER=org.postgresql.Driver

# Commande de démarrage - utilise la variable PORT de Render
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
