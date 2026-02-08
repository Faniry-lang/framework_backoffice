# 🏨 Backoffice - Projet Framework S5

## 📋 Description

Application de gestion de réservations d'hôtel construite avec un framework MVC personnalisé et un ORM Legacy.

---

## 📁 Structure du Projet

```
backoffice/
├── 📄 pom.xml                    # Configuration Maven
├── 📄 build.bat                  # Script de build Windows
├── 📄 deploy.bat                 # Script de déploiement
├── 📄 todo.txt                   # Liste des tâches
│
├── 📂 documentations/            # Documentation des frameworks
│   ├── 📄 framework-documentation.md   # Doc du framework web MVC
│   └── 📄 legacy-documentation.md      # Doc de l'ORM Legacy
│
├── 📂 libs/                      # Librairies JAR externes
│   ├── 📦 framework.jar               # Framework web MVC
│   └── 📦 legacy-orm-1.0-SNAPSHOT.jar # ORM Legacy
│
└── 📂 src/
    └── main/
        ├── 📂 java/itu/framework/backoffice/
        │   ├── 📄 Main.java              # Point d'entrée
        │   ├── 📂 controllers/           # Contrôleurs MVC
        │   │   ├── ErrorController.java
        │   │   ├── HelloWorldController.java
        │   │   └── ReservationController.java
        │   ├── 📂 dtos/                  # Data Transfer Objects
        │   │   ├── CreateReservation.java
        │   │   └── ReservationDTO.java
        │   ├── 📂 entities/              # Entités ORM
        │   │   ├── Hotel.java
        │   │   └── Reservation.java
        │   ├── 📂 helpers/               # Classes utilitaires
        │   └── 📂 code_generator/        # Générateur de code
        │
        ├── 📂 resources/
        │   ├── 📄 application.properties # Configuration
        │   └── 📂 db/migration/          # Scripts SQL Flyway
        │       └── V1__06022026.sql
        │
        └── 📂 webapp/WEB-INF/
            ├── 📄 web.xml               # Configuration Servlet
            ├── 📄 security-config.xml   # Configuration sécurité
            └── 📂 pages/                # Vues JSP
                ├── 📄 error.jsp         # Page d'erreur
                ├── 📄 home.jsp          # Page d'accueil
                ├── 📂 reservation/
                │   └── reservation-form.jsp
                └── 📂 templates/        # Templates HTML de référence
```

---

## 📚 Documentation

### Framework Web MVC
📖 [framework-documentation.md](./documentations/framework-documentation.md)

Le framework web personnalisé fournit :
- Annotations `@Controller`, `@GetMapping`, `@PostMapping`
- Gestion des paramètres avec `@RequestParam`
- Support JSON avec `@Json`
- Système de vues avec `ModelView`
- Redirections

### ORM Legacy
📖 [legacy-documentation.md](./documentations/legacy-documentation.md)

L'ORM Legacy fournit :
- Annotations `@Entity`, `@Column`, `@Id`, `@ForeignKey`
- Génération automatique d'ID avec `@Generated`
- Méthodes `save()`, `findAll()`, `filter()`
- Support des requêtes avec `FilterSet` et `Comparator`

---

## 🎨 Templates UI

Le dossier `src/main/webapp/WEB-INF/pages/templates/` contient les templates HTML de référence basés sur **CryptoVault** (TemplateMo 609) :

| Fichier | Description |
|---------|-------------|
| `index.html` | Dashboard principal |
| `login.html` | Page de connexion |
| `markets.html` | Liste avec tableau de données |
| `settings.html` | Formulaires et paramètres |
| `wallet.html` | Affichage de cartes |
| `templatemo.html` | Template de base |

### Ressources CSS/JS
```
src/main/webapp/assets/
├── css/
│   ├── templatemo-crypto-style.css      # Styles de base
│   ├── templatemo-crypto-dashboard.css  # Styles dashboard
│   ├── templatemo-crypto-pages.css      # Styles pages
│   └── templatemo-crypto-login.css      # Styles login
└── js/
    └── templatemo-crypto-script.js      # Scripts JS
```

### Caractéristiques du template
- 🌙 Thème sombre/clair automatique
- 📱 Design responsive
- 🎨 Palette cuivrée (accent color: `#b87333`)
- 📊 Composants : sidebar, cards, tables, formulaires

---

## 🚀 Démarrage Rapide

### Prérequis
- Java 17+
- Maven 3.8+
- PostgreSQL

### Build
```bash
.\build.bat puis
mvn clean package
```

---

## 📍 Routes

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/` | Page d'accueil |
| GET | `/api/reservations/form` | Formulaire de réservation |
| POST | `/api/reservations/save` | Sauvegarder une réservation |
| GET | `/api/reservations?date_reservation=YYYY-MM-DD` | API JSON des réservations |
| GET | `/error` | Page d'erreur |

---

*Template UI: [TemplateMo 609 - Crypto Vault](https://templatemo.com/tm-609-crypto-vault)*
