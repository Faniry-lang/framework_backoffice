# Plan d'implémentation — Feature « vehicule redisponible : inclure réservations mêmes heures »

Objectif court : lorsqu'un véhicule devient redisponible (heure de retour), il doit pouvoir embarquer :
- en priorité, les réservations non assignées des créneaux antérieurs ("expired / pending") ;
- ensuite, les réservations dont la date d'arrivée est exactement égale à l'heure de redisponibilité du véhicule ;
- si aucun client n'est pris, créer quand même un intervalle d'attente [heureRedispo, heureRedispo + waitTime]
  (paramétrable, défaut 30 minutes). Si plusieurs intervalles se chevauchent, l'intervalle du véhicule est prioritaire.

Checklist (à valider avant implémentation) :
- [ ] Ajouter/mettre à jour le document de design (ce fichier) — done
- [ ] Localiser la fonction qui gère l'affectation pour véhicules de retour (`assignRemainingReservationsToReturnVehicles`) et y ajouter la logique décrite
- [ ] Mettre à jour l'ordre de tri des candidats pour un véhicule redisponible (priorité : réservations antérieures non assignées desc nb_passager, puis réservations exact match heureRetour desc nb_passager)
- [ ] S'assurer que la génération de groupe (`buildReturnVehicleRegroupement` / `findOptimalWaitingGroup`) accepte une liste mixte (expired + same-time)
- [ ] Après création d'un `TrajetCandidat`, vérifier disponibilité via `occupiedCalendar` / `tentativeCalendar`, et respecter la priorité d'intervalle en cas de chevauchement
- [ ] Créer intervalle d'attente si aucun candidat trouvé
- [ ] Tests unitaires / jeux de données SQL + script d'exécution manuelle
- [ ] Ajouter logs (System.out.println) pour tous les points décisionnels

Contrainte/Principe important
- Ne pas modifier la base de données des réservations pour marquer des fragments avant qu'un trajet ne soit sauvegardé : travailler avec des DTO/structures temporaires puis décrémenter/mettre à jour uniquement au moment du `saveTrajet`.
- Respecter les calendriers `occupiedCalendar` (trajets existants) et `tentativeCalendar` (trajets candidats en cours) pour déterminer la disponibilité.
- Utiliser `Constants.Config.getDefaultWaitTime()` pour la valeur par défaut du temps d'attente.

Détail d'implémentation (étapes techniques)

1) Point d'entrée
- Localiser `assignRemainingReservationsToReturnVehicles(LocalDate date, ReturnAssignmentPreparation preparation, Map<Integer,List<Interval>> occupiedCalendar, List<TrajetCandidat> existingCandidats)` dans `AssignmentService`.
- C'est ici qu'on traite les véhicules de retour (voituresRedispo). Nous allons modifier la logique qui construit `groupeReservation` pour un `returnVehicle`.

2) Récupérer deux listes candidates pour chaque `returnVehicle`
- `remainingDtos` déjà construite (réservations non-assignees restants) — ces réservations sont *antérieures* (expired/pending).
- Construire `sameTimeDtos` : filtrer les réservations initiales (ou la source appropriée) pour lesquelles `dateHeureArrivee.equals(returnVehicle.heureRetour)`.
  - Important : diese source doit être bâtie à partir des `remainingDtos` et/ou de la liste globale des réservations non assignées de la journée ; ne pas recharger toute la DB (à moins que nécessaire).

3) Tri et priorisation
- Construire une liste candidate ordonnée comme suit :
  1. toutes les `remainingDtos` (réservations d'intervalles antérieurs) triées par `nb_passager` décroissant puis par dateHeureArrivee ascendante (ou autre tie-breaker existant)
  2. ensuite les `sameTimeDtos` triées par `nb_passager` décroissant
- Cette liste mixte sera fournie à `buildReturnVehicleRegroupement` ou à une nouvelle méthode `buildReturnGroupForVehicle` qui applique le remplissage/fragmentation sur ce flux ordonné.

4) Construction du groupe pour la voiture (nouvelle fonction / adaptation)
- Implémenter / adapter `buildReturnVehicleRegroupement` (ou `buildReturnGroupForVehicle`) pour accepter la liste ordonnée ci-dessus :
  - Remplir la capacité du véhicule en priorisant la première sous-liste (expired) ; si arrive un élément trop grand, fragmenter en simulé (DTO fragment) — sans écrire en DB.
  - Si la capacité n'est pas atteinte après les `remainingDtos`, essayer d'ajouter des `sameTimeDtos` (exact match heureRetour).
  - Retourner `GroupeReservation` avec la liste de fragments (DTO) et `heureArriveeVehicule` calculée (maximum des datesHeureArrivee des éléments du groupe ou heureRetour si force)

5) Comportement si aucun candidat
- Si le groupe est vide après tentative, créer un intervalle d'attente pour ce véhicule : [heureRetour, heureRetour + waitTime]
- Si un intervalle préexistant chevauche cet intervalle et provient d'un autre véhicule/algorithme, garantir la priorité en fusionnant/triant :
  - Représenter la priorité en ajoutant l'intervalle du véhicule *avant* l'autre au `occupiedCalendar` ou en marquant un flag explicite pour que la vérification d'overlap favorise l'intervalle du véhicule courant.

6) Calcul des timings et vérification de disponibilité
- Après construction du `TrajetCandidat` via `optimizeRoute` / `calculateTripTiming`, vérifier disponibilité avec `isVehicleFree` / `isVehicleFreeOverall` en utilisant `occupiedCalendar` et `tentativeCalendar`.
- Si disponible, ajouter le candidat à la liste `candidats` (et ajouter intervalle au `tentativeCalendar`).

7) Sauvegarde et décrémentation
- Lors du `saveTrajet(candidat, date)` :
  - créer l'entité `Trajet` et `TrajetReservation` avec `nbr_passager` correspondant aux fragments
  - décrémenter les passagers restants en mémoire (map) et, à la fin, déterminer les réservations encore non-allouées

8) Priorité d'intervalle (chevauchement)
- Si deux intervalles se chevauchent, le premier créé aura priorité :
  - Lorsqu'on ajoute l'intervalle du véhicule au `occupiedCalendar`, insérer en tête ou si vous utilisez la logique de `addIntervalToCalendar` existante, vérifier les chevauchements et, si conflit, écraser/étendre l'intervalle concurrent si ce dernier n'a pas de flag `priority`.
  - Implémenter un drapeau local temporaire (ex : Map<Integer, List<IntervalWithPriority>>) si nécessaire ; sinon, la règle simple : quand on ajoute l'intervalle du véhicule, vérifier l'existence d'un intervalle concurrent et, si oui, remplacer l'autre par la fusion qui respecte la priorité.

9) Logs et visibilité
- Ajouter System.out.println systématiques :
  - entrée de la boucle pour chaque `returnVehicle` (id, heureRetour, capaciteDisponible)
  - candidats trouvés (count expired, count sameTime)
  - résultat du groupement (liste ids, passengersAssigned)
  - décision : saved / deferred / wait-interval
  - raisons de skip (overlap, manque de données, distance introuvable)

10) Tests et jeux de données
- Créer un fichier SQL `data-test-redispo.sql` qui couvre 4 scénarios minimaux :
  1. véhicule redispo et il existe des réservations expired -> affectation OK
  2. véhicule redispo et il existe des réservations exact match heureRetour -> affectation OK
  3. mélange expired + sameTime ; verify priority and filling
  4. aucun candidat -> intervalle créé
- Ecrire un test manuel : lancer `Main` (ou runner) pour la date correspondante, lister `Trajet` et `TrajetReservation` et valider counts

11) Quality gates
- Compiler le projet (mvn clean install -DskipTests)
- Exécuter un run local en mode console (Main modifié pour invoquer `assignVehicles` ou via test harness)
- Vérifier DB tables (via psql) pour s'assurer des enregistrements corrects

12) Risques / points d'attention
- Fragmentation multiple d'une même réservation : s'assurer que le mécanisme de decrement ne double pas les fragments
- Ordre des opérations sur `tentativeCalendar` : ajouter les intervalles dès qu'un candidat est accepté pour empêcher chevauchements
- Performance : tri et scans ; si dataset grand, penser à limiter scans ou pré-calculer index

Livrable attendu
- Nouveau ou modifié : `AssignmentService.assignRemainingReservationsToReturnVehicles` (logique principale)
- Nouvelle fonction utilitaire optionnelle : `buildReturnGroupForVehicle(Vehicule, LocalDateTime, Integer, List<ReservationDTO> expired, List<ReservationDTO> sameTime)`
- SQL test file : `sprint7/data-test-redispo.sql`
- Logs ajoutés

---

À présent j'attends votre validation sur ce plan; une fois validé je ferai l'implémentation étape par étape et je compilerai après chaque modification.
