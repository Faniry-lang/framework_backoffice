[ASSIGN] reservations count=16
[ASSIGN] res id=117 client=T112 nb=12 arrivee=2036-03-13T10:10
[ASSIGN] res id=116 client=T111 nb=10 arrivee=2036-03-13T10:00
[ASSIGN] res id=107 client=T102 nb=8 arrivee=2036-03-13T08:10
[ASSIGN] res id=120 client=T115 nb=8 arrivee=2036-03-13T13:00
[ASSIGN] res id=121 client=T116 nb=8 arrivee=2036-03-13T15:00
[ASSIGN] res id=113 client=T108 nb=7 arrivee=2036-03-13T09:00
[ASSIGN] res id=111 client=T106 nb=6 arrivee=2036-03-13T08:35
[ASSIGN] res id=108 client=T103 nb=5 arrivee=2036-03-13T08:20
[ASSIGN] res id=115 client=T110 nb=5 arrivee=2036-03-13T09:25
[ASSIGN] res id=118 client=T113 nb=5 arrivee=2036-03-13T11:30
[ASSIGN] res id=119 client=T114 nb=5 arrivee=2036-03-13T11:45
[ASSIGN] res id=110 client=T105 nb=4 arrivee=2036-03-13T08:28
[ASSIGN] res id=109 client=T104 nb=3 arrivee=2036-03-13T08:25
[ASSIGN] res id=106 client=T101 nb=2 arrivee=2036-03-13T08:00
[ASSIGN] res id=112 client=T107 nb=2 arrivee=2036-03-13T08:50
[ASSIGN] res id=114 client=T109 nb=1 arrivee=2036-03-13T09:05
[ASSIGN] remainingReservations=117:T112,116:T111,107:T102,120:T115,121:T116,113:T108,111:T106,108:T103,115:T110,118:T113,119:T114,110:T105,109:T104,106:T101,112:T107,114:T109
[ASSIGN] premiereReservation id=117 client=T112 arrivee=2036-03-13T10:10
[ASSIGN] vehiculesCandidates size=6
[ASSIGN] findBestVehicle returned=MV-004(id=4)
[GROUP] Reservation disponibles: T112 T111 T102 T115 T116 T108 T106 T103 T110 T113 T114 T105 T104 T101 T107 T109
[CALENDAR] getClosestAvailableDateForVehicle vehicle=4 requested=2036-03-13T10:10 intervals=[]
[CALENDAR] no intervals for vehicle=4 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-004 premiere=117 heurePremiere=2036-03-13T10:10 heureArriveeVehicule=2036-03-13T10:10 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T10:10]
[GROUP] departCandidat=2036-03-13T10:10 groupeSize=1 ids=[117] nbPassagers=12
[GROUP] meilleur updated depart=2036-03-13T10:10 nbPassagers=12 nbReservations=1
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[117]
[ASSIGN] candidat vehicule=MV-004 start=2036-03-13T10:10 end=2036-03-13T11:22 distance=36.00
[CALENDAR] occupied for veh=4 = []
[CALENDAR] tentative for veh=4 = []
[ASSIGN] vehicule free overall id=4
[ASSIGN] marque comme assignee id=117 client=T112
[ASSIGN] avant groupVehicles candidats count=1
[ASSIGN] apres groupVehicles candidats count=1
[ASSIGN] remainingReservations=116:T111,107:T102,120:T115,121:T116,113:T108,111:T106,108:T103,115:T110,118:T113,119:T114,110:T105,109:T104,106:T101,112:T107,114:T109
[ASSIGN] premiereReservation id=116 client=T111 arrivee=2036-03-13T10:00
[ASSIGN] vehiculesCandidates size=6
[ASSIGN] findBestVehicle returned=MV-001(id=1)
[GROUP] Reservation disponibles: T111 T102 T115 T116 T108 T106 T103 T110 T113 T114 T105 T104 T101 T107 T109
[CALENDAR] getClosestAvailableDateForVehicle vehicle=1 requested=2036-03-13T10:00 intervals=[]
[CALENDAR] no intervals for vehicle=1 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-001 premiere=116 heurePremiere=2036-03-13T10:00 heureArriveeVehicule=2036-03-13T10:00 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T10:00]
[GROUP] departCandidat=2036-03-13T10:00 groupeSize=1 ids=[116] nbPassagers=10
[GROUP] meilleur updated depart=2036-03-13T10:00 nbPassagers=10 nbReservations=1
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[116]
[ASSIGN] candidat vehicule=MV-001 start=2036-03-13T10:00 end=2036-03-13T11:22 distance=41.00
[CALENDAR] occupied for veh=1 = []
[CALENDAR] tentative for veh=1 = []
[ASSIGN] vehicule free overall id=1
[ASSIGN] marque comme assignee id=116 client=T111
[ASSIGN] avant groupVehicles candidats count=2
[ASSIGN] apres groupVehicles candidats count=2
[ASSIGN] remainingReservations=107:T102,120:T115,121:T116,113:T108,111:T106,108:T103,115:T110,118:T113,119:T114,110:T105,109:T104,106:T101,112:T107,114:T109
[ASSIGN] premiereReservation id=107 client=T102 arrivee=2036-03-13T08:10
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:5, ref: MV-005 }, {id:6, ref: MV-006 }]
VEHICULE: 5, CANDIDATS: 0
VEHICULE: 6, CANDIDATS: 0
MIN NBR CANDIDATS: 0
VEHICULE: 5, COUNT: 0, CANDIDATS: 0
VEHICULE: 6, COUNT: 0, CANDIDATS: 0
[ASSIGN] findBestVehicle returned=MV-006(id=6)
[GROUP] Reservation disponibles: T102 T115 T116 T108 T106 T103 T110 T113 T114 T105 T104 T101 T107 T109
[CALENDAR] getClosestAvailableDateForVehicle vehicle=6 requested=2036-03-13T08:10 intervals=[]
[CALENDAR] no intervals for vehicle=6 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-006 premiere=107 heurePremiere=2036-03-13T08:10 heureArriveeVehicule=2036-03-13T08:10 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:10, 2036-03-13T08:20, 2036-03-13T08:25, 2036-03-13T08:28, 2036-03-13T08:35]
[GROUP] departCandidat=2036-03-13T08:10 groupeSize=1 ids=[107] nbPassagers=8
[GROUP] meilleur updated depart=2036-03-13T08:10 nbPassagers=8 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:20 groupeSize=1 ids=[107] nbPassagers=8
[GROUP] departCandidat=2036-03-13T08:25 groupeSize=1 ids=[107] nbPassagers=8
[GROUP] departCandidat=2036-03-13T08:28 groupeSize=1 ids=[107] nbPassagers=8
[GROUP] departCandidat=2036-03-13T08:35 groupeSize=1 ids=[107] nbPassagers=8
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[107]
[ASSIGN] candidat vehicule=MV-006 start=2036-03-13T08:10 end=2036-03-13T09:34 distance=42.00
[CALENDAR] occupied for veh=6 = []
[CALENDAR] tentative for veh=6 = []
[ASSIGN] vehicule free overall id=6
[ASSIGN] marque comme assignee id=107 client=T102
[ASSIGN] avant groupVehicles candidats count=3
[ASSIGN] apres groupVehicles candidats count=3
[ASSIGN] remainingReservations=120:T115,121:T116,113:T108,111:T106,108:T103,115:T110,118:T113,119:T114,110:T105,109:T104,106:T101,112:T107,114:T109
[ASSIGN] premiereReservation id=120 client=T115 arrivee=2036-03-13T13:00
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:5, ref: MV-005 }, {id:6, ref: MV-006 }]
VEHICULE: 5, CANDIDATS: 0
VEHICULE: 6, CANDIDATS: 1
MIN NBR CANDIDATS: 0
VEHICULE: 5, COUNT: 0, CANDIDATS: 0
VEHICULE: 6, COUNT: 1, CANDIDATS: 0
Choisi: {id:5, ref: MV-005 } over [{id:5, ref: MV-005 }]
====FIN====
[ASSIGN] findBestVehicle returned=MV-005(id=5)
[GROUP] Reservation disponibles: T115 T116 T108 T106 T103 T110 T113 T114 T105 T104 T101 T107 T109
[CALENDAR] getClosestAvailableDateForVehicle vehicle=5 requested=2036-03-13T13:00 intervals=[]
[CALENDAR] no intervals for vehicle=5 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-005 premiere=120 heurePremiere=2036-03-13T13:00 heureArriveeVehicule=2036-03-13T13:00 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T13:00]
[GROUP] departCandidat=2036-03-13T13:00 groupeSize=1 ids=[120] nbPassagers=8
[GROUP] meilleur updated depart=2036-03-13T13:00 nbPassagers=8 nbReservations=1
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[120]
[ASSIGN] candidat vehicule=MV-005 start=2036-03-13T13:00 end=2036-03-13T14:32 distance=46.00
[CALENDAR] occupied for veh=5 = []
[CALENDAR] tentative for veh=5 = []
[ASSIGN] vehicule free overall id=5
[ASSIGN] marque comme assignee id=120 client=T115
[ASSIGN] avant groupVehicles candidats count=4
[ASSIGN] apres groupVehicles candidats count=4
[ASSIGN] remainingReservations=121:T116,113:T108,111:T106,108:T103,115:T110,118:T113,119:T114,110:T105,109:T104,106:T101,112:T107,114:T109
[ASSIGN] premiereReservation id=121 client=T116 arrivee=2036-03-13T15:00
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:5, ref: MV-005 }, {id:6, ref: MV-006 }]
VEHICULE: 5, CANDIDATS: 1
VEHICULE: 6, CANDIDATS: 1
MIN NBR CANDIDATS: 1
VEHICULE: 5, COUNT: 1, CANDIDATS: 1
VEHICULE: 6, COUNT: 1, CANDIDATS: 1
[ASSIGN] findBestVehicle returned=MV-006(id=6)
[GROUP] Reservation disponibles: T116 T108 T106 T103 T110 T113 T114 T105 T104 T101 T107 T109
[CALENDAR] getClosestAvailableDateForVehicle vehicle=6 requested=2036-03-13T15:00 intervals=[2036-03-13T08:10->2036-03-13T09:34]
[CALENDAR] requested after all intervals -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-006 premiere=121 heurePremiere=2036-03-13T15:00 heureArriveeVehicule=2036-03-13T15:00 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T15:00]
[GROUP] departCandidat=2036-03-13T15:00 groupeSize=1 ids=[121] nbPassagers=8
[GROUP] meilleur updated depart=2036-03-13T15:00 nbPassagers=8 nbReservations=1
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[121]
[ASSIGN] candidat vehicule=MV-006 start=2036-03-13T15:00 end=2036-03-13T16:24 distance=42.00
[CALENDAR] occupied for veh=6 = []
[CALENDAR] tentative for veh=6 = [2036-03-13T08:10->2036-03-13T09:34]
[ASSIGN] vehicule free overall id=6
[ASSIGN] marque comme assignee id=121 client=T116
[ASSIGN] avant groupVehicles candidats count=5
[ASSIGN] apres groupVehicles candidats count=5
[ASSIGN] remainingReservations=113:T108,111:T106,108:T103,115:T110,118:T113,119:T114,110:T105,109:T104,106:T101,112:T107,114:T109
[ASSIGN] premiereReservation id=113 client=T108 arrivee=2036-03-13T09:00
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:5, ref: MV-005 }, {id:6, ref: MV-006 }]
VEHICULE: 5, CANDIDATS: 1
VEHICULE: 6, CANDIDATS: 2
MIN NBR CANDIDATS: 1
VEHICULE: 5, COUNT: 1, CANDIDATS: 1
VEHICULE: 6, COUNT: 2, CANDIDATS: 1
Choisi: {id:5, ref: MV-005 } over [{id:5, ref: MV-005 }]
====FIN====
[ASSIGN] findBestVehicle returned=MV-005(id=5)
[GROUP] Reservation disponibles: T108 T106 T103 T110 T113 T114 T105 T104 T101 T107 T109
[CALENDAR] getClosestAvailableDateForVehicle vehicle=5 requested=2036-03-13T09:00 intervals=[2036-03-13T13:00->2036-03-13T14:32]
[CALENDAR] requested before next interval 2036-03-13T13:00 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-005 premiere=113 heurePremiere=2036-03-13T09:00 heureArriveeVehicule=2036-03-13T09:00 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T09:00, 2036-03-13T09:05, 2036-03-13T09:25]
[GROUP] departCandidat=2036-03-13T09:00 groupeSize=1 ids=[113] nbPassagers=7
[GROUP] meilleur updated depart=2036-03-13T09:00 nbPassagers=7 nbReservations=1
[GROUP] departCandidat=2036-03-13T09:05 groupeSize=2 ids=[113,114] nbPassagers=8
[GROUP] meilleur updated depart=2036-03-13T09:05 nbPassagers=8 nbReservations=2
[GROUP] departCandidat=2036-03-13T09:25 groupeSize=2 ids=[113,114] nbPassagers=8
[ASSIGN] groupeReservation size=2
[ASSIGN] groupe reservations ids=[113,114]
[ASSIGN] candidat vehicule=MV-005 start=2036-03-13T09:05 end=2036-03-13T10:39 distance=47.00
[CALENDAR] occupied for veh=5 = []
[CALENDAR] tentative for veh=5 = [2036-03-13T13:00->2036-03-13T14:32]
[ASSIGN] vehicule free overall id=5
[ASSIGN] marque comme assignee id=113 client=T108
[ASSIGN] marque comme assignee id=114 client=T109
[ASSIGN] avant groupVehicles candidats count=6
[ASSIGN] apres groupVehicles candidats count=6
[ASSIGN] remainingReservations=111:T106,108:T103,115:T110,118:T113,119:T114,110:T105,109:T104,106:T101,112:T107
[ASSIGN] premiereReservation id=111 client=T106 arrivee=2036-03-13T08:35
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:5, ref: MV-005 }, {id:6, ref: MV-006 }]
VEHICULE: 5, CANDIDATS: 2
VEHICULE: 6, CANDIDATS: 2
MIN NBR CANDIDATS: 2
VEHICULE: 5, COUNT: 2, CANDIDATS: 2
VEHICULE: 6, COUNT: 2, CANDIDATS: 2
[ASSIGN] findBestVehicle returned=MV-006(id=6)
[GROUP] Reservation disponibles: T106 T103 T110 T113 T114 T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=6 requested=2036-03-13T08:35 intervals=[2036-03-13T08:10->2036-03-13T09:34,2036-03-13T15:00->2036-03-13T16:24]
[CALENDAR] requested inside interval 2036-03-13T08:10->2036-03-13T09:34 -> return 2036-03-13T09:34
[GROUP] findOptimalWaitingGroup vehicule=MV-006 premiere=111 heurePremiere=2036-03-13T08:35 heureArriveeVehicule=2036-03-13T09:34 tempsAttenteRestant=-29
[ASSIGN] groupeReservation size=0
[ASSIGN] vehiculesCandidates size=5
[ASSIGN] findBestVehicle returned=MV-005(id=5)
[GROUP] Reservation disponibles: T106 T103 T110 T113 T114 T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=5 requested=2036-03-13T08:35 intervals=[2036-03-13T09:05->2036-03-13T10:39,2036-03-13T13:00->2036-03-13T14:32]
[CALENDAR] requested before next interval 2036-03-13T09:05 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-005 premiere=111 heurePremiere=2036-03-13T08:35 heureArriveeVehicule=2036-03-13T08:35 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:35, 2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:35 groupeSize=1 ids=[111] nbPassagers=6
[GROUP] meilleur updated depart=2036-03-13T08:35 nbPassagers=6 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=2 ids=[111,112] nbPassagers=8
[GROUP] meilleur updated depart=2036-03-13T08:50 nbPassagers=8 nbReservations=2
[ASSIGN] groupeReservation size=2
[ASSIGN] groupe reservations ids=[111,112]
[ASSIGN] candidat vehicule=MV-005 start=2036-03-13T08:50 end=2036-03-13T10:14 distance=42.00
[CALENDAR] occupied for veh=5 = []
[CALENDAR] tentative for veh=5 = [2036-03-13T09:05->2036-03-13T10:39,2036-03-13T13:00->2036-03-13T14:32]
[ASSIGN] vehicule NOT free overall id=5 start=2036-03-13T08:50 end=2036-03-13T10:14
[CALENDAR] occupiedCalendar empty for veh=5
[CALENDAR] overlap with tentativeCalendar veh=5 interval=2036-03-13T09:05->2036-03-13T10:39
[ASSIGN] vehiculesCandidates size=4
[ASSIGN] findBestVehicle returned=MV-001(id=1)
[GROUP] Reservation disponibles: T106 T103 T110 T113 T114 T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=1 requested=2036-03-13T08:35 intervals=[2036-03-13T10:10->2036-03-13T11:32]
[CALENDAR] requested before next interval 2036-03-13T10:10 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-001 premiere=111 heurePremiere=2036-03-13T08:35 heureArriveeVehicule=2036-03-13T08:35 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:35, 2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:35 groupeSize=1 ids=[111] nbPassagers=6
[GROUP] meilleur updated depart=2036-03-13T08:35 nbPassagers=6 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=2 ids=[111,112] nbPassagers=8
[GROUP] meilleur updated depart=2036-03-13T08:50 nbPassagers=8 nbReservations=2
[ASSIGN] groupeReservation size=2
[ASSIGN] groupe reservations ids=[111,112]
[ASSIGN] candidat vehicule=MV-001 start=2036-03-13T08:50 end=2036-03-13T10:14 distance=42.00
[CALENDAR] occupied for veh=1 = []
[CALENDAR] tentative for veh=1 = [2036-03-13T10:10->2036-03-13T11:32]
[ASSIGN] vehicule NOT free overall id=1 start=2036-03-13T08:50 end=2036-03-13T10:14
[CALENDAR] occupiedCalendar empty for veh=1
[CALENDAR] overlap with tentativeCalendar veh=1 interval=2036-03-13T10:10->2036-03-13T11:32
[ASSIGN] vehiculesCandidates size=3
[ASSIGN] findBestVehicle returned=MV-004(id=4)
[GROUP] Reservation disponibles: T106 T103 T110 T113 T114 T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=4 requested=2036-03-13T08:35 intervals=[2036-03-13T10:10->2036-03-13T11:22]
[CALENDAR] requested before next interval 2036-03-13T10:10 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-004 premiere=111 heurePremiere=2036-03-13T08:35 heureArriveeVehicule=2036-03-13T08:35 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:35, 2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:35 groupeSize=1 ids=[111] nbPassagers=6
[GROUP] meilleur updated depart=2036-03-13T08:35 nbPassagers=6 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=2 ids=[111,112] nbPassagers=8
[GROUP] meilleur updated depart=2036-03-13T08:50 nbPassagers=8 nbReservations=2
[ASSIGN] groupeReservation size=2
[ASSIGN] groupe reservations ids=[111,112]
[ASSIGN] candidat vehicule=MV-004 start=2036-03-13T08:50 end=2036-03-13T10:14 distance=42.00
[CALENDAR] occupied for veh=4 = []
[CALENDAR] tentative for veh=4 = [2036-03-13T10:10->2036-03-13T11:22]
[ASSIGN] vehicule NOT free overall id=4 start=2036-03-13T08:50 end=2036-03-13T10:14
[CALENDAR] occupiedCalendar empty for veh=4
[CALENDAR] overlap with tentativeCalendar veh=4 interval=2036-03-13T10:10->2036-03-13T11:22
[ASSIGN] vehiculesCandidates size=2
[ASSIGN] findBestVehicle returned=null
[ASSIGN] marque comme processed (no vehicle) id=111 client=T106
[ASSIGN] marque comme processed (exhausted vehicles) id=111 client=T106
[ASSIGN] avant groupVehicles candidats count=6
[ASSIGN] apres groupVehicles candidats count=6
[ASSIGN] remainingReservations=108:T103,115:T110,118:T113,119:T114,110:T105,109:T104,106:T101,112:T107
[ASSIGN] premiereReservation id=108 client=T103 arrivee=2036-03-13T08:20
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:2, ref: MV-002 }, {id:3, ref: MV-003 }]
VEHICULE: 2, CANDIDATS: 0
VEHICULE: 3, CANDIDATS: 0
MIN NBR CANDIDATS: 0
VEHICULE: 2, COUNT: 0, CANDIDATS: 0
VEHICULE: 3, COUNT: 0, CANDIDATS: 0
SIZE FINAL: 2
[ASSIGN] findBestVehicle returned=MV-003(id=3)
[GROUP] Reservation disponibles: T103 T110 T113 T114 T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=3 requested=2036-03-13T08:20 intervals=[]
[CALENDAR] no intervals for vehicle=3 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-003 premiere=108 heurePremiere=2036-03-13T08:20 heureArriveeVehicule=2036-03-13T08:20 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:20, 2036-03-13T08:25, 2036-03-13T08:28, 2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:20 groupeSize=1 ids=[108] nbPassagers=5
[GROUP] meilleur updated depart=2036-03-13T08:20 nbPassagers=5 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:25 groupeSize=1 ids=[108] nbPassagers=5
[GROUP] departCandidat=2036-03-13T08:28 groupeSize=1 ids=[108] nbPassagers=5
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=1 ids=[108] nbPassagers=5
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[108]
[ASSIGN] candidat vehicule=MV-003 start=2036-03-13T08:20 end=2036-03-13T09:32 distance=36.00
[CALENDAR] occupied for veh=3 = []
[CALENDAR] tentative for veh=3 = []
[ASSIGN] vehicule free overall id=3
[ASSIGN] marque comme assignee id=108 client=T103
[ASSIGN] avant groupVehicles candidats count=7
[ASSIGN] apres groupVehicles candidats count=7
[ASSIGN] remainingReservations=115:T110,118:T113,119:T114,110:T105,109:T104,106:T101,112:T107
[ASSIGN] premiereReservation id=115 client=T110 arrivee=2036-03-13T09:25
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:2, ref: MV-002 }, {id:3, ref: MV-003 }]
VEHICULE: 2, CANDIDATS: 0
VEHICULE: 3, CANDIDATS: 1
MIN NBR CANDIDATS: 0
VEHICULE: 2, COUNT: 0, CANDIDATS: 0
VEHICULE: 3, COUNT: 1, CANDIDATS: 0
Choisi: {id:2, ref: MV-002 } over [{id:2, ref: MV-002 }]
====FIN====
[ASSIGN] findBestVehicle returned=MV-002(id=2)
[GROUP] Reservation disponibles: T110 T113 T114 T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=2 requested=2036-03-13T09:25 intervals=[]
[CALENDAR] no intervals for vehicle=2 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-002 premiere=115 heurePremiere=2036-03-13T09:25 heureArriveeVehicule=2036-03-13T09:25 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T09:25]
[GROUP] departCandidat=2036-03-13T09:25 groupeSize=1 ids=[115] nbPassagers=5
[GROUP] meilleur updated depart=2036-03-13T09:25 nbPassagers=5 nbReservations=1
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[115]
[ASSIGN] candidat vehicule=MV-002 start=2036-03-13T09:25 end=2036-03-13T10:49 distance=42.00
[CALENDAR] occupied for veh=2 = []
[CALENDAR] tentative for veh=2 = []
[ASSIGN] vehicule free overall id=2
[ASSIGN] marque comme assignee id=115 client=T110
[ASSIGN] avant groupVehicles candidats count=8
[ASSIGN] apres groupVehicles candidats count=8
[ASSIGN] remainingReservations=118:T113,119:T114,110:T105,109:T104,106:T101,112:T107
[ASSIGN] premiereReservation id=118 client=T113 arrivee=2036-03-13T11:30
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:2, ref: MV-002 }, {id:3, ref: MV-003 }]
VEHICULE: 2, CANDIDATS: 1
VEHICULE: 3, CANDIDATS: 1
MIN NBR CANDIDATS: 1
VEHICULE: 2, COUNT: 1, CANDIDATS: 1
VEHICULE: 3, COUNT: 1, CANDIDATS: 1
SIZE FINAL: 2
[ASSIGN] findBestVehicle returned=MV-003(id=3)
[GROUP] Reservation disponibles: T113 T114 T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=3 requested=2036-03-13T11:30 intervals=[2036-03-13T08:20->2036-03-13T09:32]
[CALENDAR] requested after all intervals -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-003 premiere=118 heurePremiere=2036-03-13T11:30 heureArriveeVehicule=2036-03-13T11:30 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T11:30, 2036-03-13T11:45]
[GROUP] departCandidat=2036-03-13T11:30 groupeSize=1 ids=[118] nbPassagers=5
[GROUP] meilleur updated depart=2036-03-13T11:30 nbPassagers=5 nbReservations=1
[GROUP] departCandidat=2036-03-13T11:45 groupeSize=1 ids=[118] nbPassagers=5
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[118]
[ASSIGN] candidat vehicule=MV-003 start=2036-03-13T11:30 end=2036-03-13T12:42 distance=36.00
[CALENDAR] occupied for veh=3 = []
[CALENDAR] tentative for veh=3 = [2036-03-13T08:20->2036-03-13T09:32]
[ASSIGN] vehicule free overall id=3
[ASSIGN] marque comme assignee id=118 client=T113
[ASSIGN] avant groupVehicles candidats count=9
[ASSIGN] apres groupVehicles candidats count=9
[ASSIGN] remainingReservations=119:T114,110:T105,109:T104,106:T101,112:T107
[ASSIGN] premiereReservation id=119 client=T114 arrivee=2036-03-13T11:45
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:2, ref: MV-002 }, {id:3, ref: MV-003 }]
VEHICULE: 2, CANDIDATS: 1
VEHICULE: 3, CANDIDATS: 2
MIN NBR CANDIDATS: 1
VEHICULE: 2, COUNT: 1, CANDIDATS: 1
VEHICULE: 3, COUNT: 2, CANDIDATS: 1
Choisi: {id:2, ref: MV-002 } over [{id:2, ref: MV-002 }]
====FIN====
[ASSIGN] findBestVehicle returned=MV-002(id=2)
[GROUP] Reservation disponibles: T114 T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=2 requested=2036-03-13T11:45 intervals=[2036-03-13T09:25->2036-03-13T10:49]
[CALENDAR] requested after all intervals -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-002 premiere=119 heurePremiere=2036-03-13T11:45 heureArriveeVehicule=2036-03-13T11:45 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T11:45]
[GROUP] departCandidat=2036-03-13T11:45 groupeSize=1 ids=[119] nbPassagers=5
[GROUP] meilleur updated depart=2036-03-13T11:45 nbPassagers=5 nbReservations=1
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[119]
[ASSIGN] candidat vehicule=MV-002 start=2036-03-13T11:45 end=2036-03-13T13:07 distance=41.00
[CALENDAR] occupied for veh=2 = []
[CALENDAR] tentative for veh=2 = [2036-03-13T09:25->2036-03-13T10:49]
[ASSIGN] vehicule free overall id=2
[ASSIGN] marque comme assignee id=119 client=T114
[ASSIGN] avant groupVehicles candidats count=10
[ASSIGN] apres groupVehicles candidats count=10
[ASSIGN] remainingReservations=110:T105,109:T104,106:T101,112:T107
[ASSIGN] premiereReservation id=110 client=T105 arrivee=2036-03-13T08:28
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:2, ref: MV-002 }, {id:3, ref: MV-003 }]
VEHICULE: 2, CANDIDATS: 2
VEHICULE: 3, CANDIDATS: 2
MIN NBR CANDIDATS: 2
VEHICULE: 2, COUNT: 2, CANDIDATS: 2
VEHICULE: 3, COUNT: 2, CANDIDATS: 2
SIZE FINAL: 2
[ASSIGN] findBestVehicle returned=MV-003(id=3)
[GROUP] Reservation disponibles: T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=3 requested=2036-03-13T08:28 intervals=[2036-03-13T08:20->2036-03-13T09:32,2036-03-13T11:45->2036-03-13T12:57]
[CALENDAR] requested inside interval 2036-03-13T08:20->2036-03-13T09:32 -> return 2036-03-13T09:32
[GROUP] findOptimalWaitingGroup vehicule=MV-003 premiere=110 heurePremiere=2036-03-13T08:28 heureArriveeVehicule=2036-03-13T09:32 tempsAttenteRestant=-34
[ASSIGN] groupeReservation size=0
[ASSIGN] vehiculesCandidates size=5
[ASSIGN] findBestVehicle returned=MV-002(id=2)
[GROUP] Reservation disponibles: T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=2 requested=2036-03-13T08:28 intervals=[2036-03-13T09:25->2036-03-13T10:49,2036-03-13T11:45->2036-03-13T13:07]
[CALENDAR] requested before next interval 2036-03-13T09:25 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-002 premiere=110 heurePremiere=2036-03-13T08:28 heureArriveeVehicule=2036-03-13T08:28 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:28, 2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:28 groupeSize=1 ids=[110] nbPassagers=4
[GROUP] meilleur updated depart=2036-03-13T08:28 nbPassagers=4 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=1 ids=[110] nbPassagers=4
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[110]
[ASSIGN] candidat vehicule=MV-002 start=2036-03-13T08:28 end=2036-03-13T09:50 distance=41.00
[CALENDAR] occupied for veh=2 = []
[CALENDAR] tentative for veh=2 = [2036-03-13T09:25->2036-03-13T10:49,2036-03-13T11:45->2036-03-13T13:07]
[ASSIGN] vehicule NOT free overall id=2 start=2036-03-13T08:28 end=2036-03-13T09:50
[CALENDAR] occupiedCalendar empty for veh=2
[CALENDAR] overlap with tentativeCalendar veh=2 interval=2036-03-13T09:25->2036-03-13T10:49
[ASSIGN] vehiculesCandidates size=4
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:5, ref: MV-005 }, {id:6, ref: MV-006 }]
VEHICULE: 5, CANDIDATS: 2
VEHICULE: 6, CANDIDATS: 2
MIN NBR CANDIDATS: 2
VEHICULE: 5, COUNT: 2, CANDIDATS: 2
VEHICULE: 6, COUNT: 2, CANDIDATS: 2
[ASSIGN] findBestVehicle returned=MV-006(id=6)
[GROUP] Reservation disponibles: T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=6 requested=2036-03-13T08:28 intervals=[2036-03-13T08:20->2036-03-13T09:44,2036-03-13T15:00->2036-03-13T16:24]
[CALENDAR] requested inside interval 2036-03-13T08:20->2036-03-13T09:44 -> return 2036-03-13T09:44
[GROUP] findOptimalWaitingGroup vehicule=MV-006 premiere=110 heurePremiere=2036-03-13T08:28 heureArriveeVehicule=2036-03-13T09:44 tempsAttenteRestant=-46
[ASSIGN] groupeReservation size=0
[ASSIGN] vehiculesCandidates size=3
[ASSIGN] findBestVehicle returned=MV-005(id=5)
[GROUP] Reservation disponibles: T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=5 requested=2036-03-13T08:28 intervals=[2036-03-13T09:25->2036-03-13T10:59,2036-03-13T13:00->2036-03-13T14:32]
[CALENDAR] requested before next interval 2036-03-13T09:25 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-005 premiere=110 heurePremiere=2036-03-13T08:28 heureArriveeVehicule=2036-03-13T08:28 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:28, 2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:28 groupeSize=1 ids=[110] nbPassagers=4
[GROUP] meilleur updated depart=2036-03-13T08:28 nbPassagers=4 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=2 ids=[110,112] nbPassagers=6
[GROUP] meilleur updated depart=2036-03-13T08:50 nbPassagers=6 nbReservations=2
[ASSIGN] groupeReservation size=2
[ASSIGN] groupe reservations ids=[110,112]
[ASSIGN] candidat vehicule=MV-005 start=2036-03-13T08:50 end=2036-03-13T10:12 distance=41.00
[CALENDAR] occupied for veh=5 = []
[CALENDAR] tentative for veh=5 = [2036-03-13T09:25->2036-03-13T10:59,2036-03-13T13:00->2036-03-13T14:32]
[ASSIGN] vehicule NOT free overall id=5 start=2036-03-13T08:50 end=2036-03-13T10:12
[CALENDAR] occupiedCalendar empty for veh=5
[CALENDAR] overlap with tentativeCalendar veh=5 interval=2036-03-13T09:25->2036-03-13T10:59
[ASSIGN] vehiculesCandidates size=2
[ASSIGN] findBestVehicle returned=MV-001(id=1)
[GROUP] Reservation disponibles: T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=1 requested=2036-03-13T08:28 intervals=[2036-03-13T10:10->2036-03-13T11:32]
[CALENDAR] requested before next interval 2036-03-13T10:10 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-001 premiere=110 heurePremiere=2036-03-13T08:28 heureArriveeVehicule=2036-03-13T08:28 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:28, 2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:28 groupeSize=1 ids=[110] nbPassagers=4
[GROUP] meilleur updated depart=2036-03-13T08:28 nbPassagers=4 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=2 ids=[110,112] nbPassagers=6
[GROUP] meilleur updated depart=2036-03-13T08:50 nbPassagers=6 nbReservations=2
[ASSIGN] groupeReservation size=2
[ASSIGN] groupe reservations ids=[110,112]
[ASSIGN] candidat vehicule=MV-001 start=2036-03-13T08:50 end=2036-03-13T10:12 distance=41.00
[CALENDAR] occupied for veh=1 = []
[CALENDAR] tentative for veh=1 = [2036-03-13T10:10->2036-03-13T11:32]
[ASSIGN] vehicule NOT free overall id=1 start=2036-03-13T08:50 end=2036-03-13T10:12
[CALENDAR] occupiedCalendar empty for veh=1
[CALENDAR] overlap with tentativeCalendar veh=1 interval=2036-03-13T10:10->2036-03-13T11:32
[ASSIGN] vehiculesCandidates size=1
[ASSIGN] findBestVehicle returned=MV-004(id=4)
[GROUP] Reservation disponibles: T105 T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=4 requested=2036-03-13T08:28 intervals=[2036-03-13T10:10->2036-03-13T11:22]
[CALENDAR] requested before next interval 2036-03-13T10:10 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-004 premiere=110 heurePremiere=2036-03-13T08:28 heureArriveeVehicule=2036-03-13T08:28 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:28, 2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:28 groupeSize=1 ids=[110] nbPassagers=4
[GROUP] meilleur updated depart=2036-03-13T08:28 nbPassagers=4 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=2 ids=[110,112] nbPassagers=6
[GROUP] meilleur updated depart=2036-03-13T08:50 nbPassagers=6 nbReservations=2
[ASSIGN] groupeReservation size=2
[ASSIGN] groupe reservations ids=[110,112]
[ASSIGN] candidat vehicule=MV-004 start=2036-03-13T08:50 end=2036-03-13T10:12 distance=41.00
[CALENDAR] occupied for veh=4 = []
[CALENDAR] tentative for veh=4 = [2036-03-13T10:10->2036-03-13T11:22]
[ASSIGN] vehicule NOT free overall id=4 start=2036-03-13T08:50 end=2036-03-13T10:12
[CALENDAR] occupiedCalendar empty for veh=4
[CALENDAR] overlap with tentativeCalendar veh=4 interval=2036-03-13T10:10->2036-03-13T11:22
[ASSIGN] marque comme processed (exhausted vehicles) id=110 client=T105
[ASSIGN] avant groupVehicles candidats count=10
[ASSIGN] apres groupVehicles candidats count=10
[ASSIGN] remainingReservations=109:T104,106:T101,112:T107
[ASSIGN] premiereReservation id=109 client=T104 arrivee=2036-03-13T08:25
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:2, ref: MV-002 }, {id:3, ref: MV-003 }]
VEHICULE: 2, CANDIDATS: 2
VEHICULE: 3, CANDIDATS: 2
MIN NBR CANDIDATS: 2
VEHICULE: 2, COUNT: 2, CANDIDATS: 2
VEHICULE: 3, COUNT: 2, CANDIDATS: 2
SIZE FINAL: 2
[ASSIGN] findBestVehicle returned=MV-002(id=2)
[GROUP] Reservation disponibles: T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=2 requested=2036-03-13T08:25 intervals=[2036-03-13T09:25->2036-03-13T10:49,2036-03-13T11:45->2036-03-13T13:07]
[CALENDAR] requested before next interval 2036-03-13T09:25 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-002 premiere=109 heurePremiere=2036-03-13T08:25 heureArriveeVehicule=2036-03-13T08:25 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:25, 2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:25 groupeSize=1 ids=[109] nbPassagers=3
[GROUP] meilleur updated depart=2036-03-13T08:25 nbPassagers=3 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=2 ids=[109,112] nbPassagers=5
[GROUP] meilleur updated depart=2036-03-13T08:50 nbPassagers=5 nbReservations=2
[ASSIGN] groupeReservation size=2
[ASSIGN] groupe reservations ids=[109,112]
[ASSIGN] candidat vehicule=MV-002 start=2036-03-13T08:50 end=2036-03-13T10:22 distance=46.00
[CALENDAR] occupied for veh=2 = []
[CALENDAR] tentative for veh=2 = [2036-03-13T09:25->2036-03-13T10:49,2036-03-13T11:45->2036-03-13T13:07]
[ASSIGN] vehicule NOT free overall id=2 start=2036-03-13T08:50 end=2036-03-13T10:22
[CALENDAR] occupiedCalendar empty for veh=2
[CALENDAR] overlap with tentativeCalendar veh=2 interval=2036-03-13T09:25->2036-03-13T10:49
[ASSIGN] vehiculesCandidates size=5
[ASSIGN] findBestVehicle returned=MV-003(id=3)
[GROUP] Reservation disponibles: T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=3 requested=2036-03-13T08:25 intervals=[2036-03-13T08:20->2036-03-13T09:32,2036-03-13T11:45->2036-03-13T12:57]
[CALENDAR] requested inside interval 2036-03-13T08:20->2036-03-13T09:32 -> return 2036-03-13T09:32
[GROUP] findOptimalWaitingGroup vehicule=MV-003 premiere=109 heurePremiere=2036-03-13T08:25 heureArriveeVehicule=2036-03-13T09:32 tempsAttenteRestant=-37
[ASSIGN] groupeReservation size=0
[ASSIGN] vehiculesCandidates size=4
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:5, ref: MV-005 }, {id:6, ref: MV-006 }]
VEHICULE: 5, CANDIDATS: 2
VEHICULE: 6, CANDIDATS: 2
MIN NBR CANDIDATS: 2
VEHICULE: 5, COUNT: 2, CANDIDATS: 2
VEHICULE: 6, COUNT: 2, CANDIDATS: 2
[ASSIGN] findBestVehicle returned=MV-006(id=6)
[GROUP] Reservation disponibles: T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=6 requested=2036-03-13T08:25 intervals=[2036-03-13T08:20->2036-03-13T09:44,2036-03-13T15:00->2036-03-13T16:24]
[CALENDAR] requested inside interval 2036-03-13T08:20->2036-03-13T09:44 -> return 2036-03-13T09:44
[GROUP] findOptimalWaitingGroup vehicule=MV-006 premiere=109 heurePremiere=2036-03-13T08:25 heureArriveeVehicule=2036-03-13T09:44 tempsAttenteRestant=-49
[ASSIGN] groupeReservation size=0
[ASSIGN] vehiculesCandidates size=3
[ASSIGN] findBestVehicle returned=MV-005(id=5)
[GROUP] Reservation disponibles: T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=5 requested=2036-03-13T08:25 intervals=[2036-03-13T09:25->2036-03-13T10:59,2036-03-13T13:00->2036-03-13T14:32]
[CALENDAR] requested before next interval 2036-03-13T09:25 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-005 premiere=109 heurePremiere=2036-03-13T08:25 heureArriveeVehicule=2036-03-13T08:25 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:25, 2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:25 groupeSize=1 ids=[109] nbPassagers=3
[GROUP] meilleur updated depart=2036-03-13T08:25 nbPassagers=3 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=2 ids=[109,112] nbPassagers=5
[GROUP] meilleur updated depart=2036-03-13T08:50 nbPassagers=5 nbReservations=2
[ASSIGN] groupeReservation size=2
[ASSIGN] groupe reservations ids=[109,112]
[ASSIGN] candidat vehicule=MV-005 start=2036-03-13T08:50 end=2036-03-13T10:22 distance=46.00
[CALENDAR] occupied for veh=5 = []
[CALENDAR] tentative for veh=5 = [2036-03-13T09:25->2036-03-13T10:59,2036-03-13T13:00->2036-03-13T14:32]
[ASSIGN] vehicule NOT free overall id=5 start=2036-03-13T08:50 end=2036-03-13T10:22
[CALENDAR] occupiedCalendar empty for veh=5
[CALENDAR] overlap with tentativeCalendar veh=5 interval=2036-03-13T09:25->2036-03-13T10:59
[ASSIGN] vehiculesCandidates size=2
[ASSIGN] findBestVehicle returned=MV-001(id=1)
[GROUP] Reservation disponibles: T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=1 requested=2036-03-13T08:25 intervals=[2036-03-13T10:10->2036-03-13T11:32]
[CALENDAR] requested before next interval 2036-03-13T10:10 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-001 premiere=109 heurePremiere=2036-03-13T08:25 heureArriveeVehicule=2036-03-13T08:25 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:25, 2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:25 groupeSize=1 ids=[109] nbPassagers=3
[GROUP] meilleur updated depart=2036-03-13T08:25 nbPassagers=3 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=2 ids=[109,112] nbPassagers=5
[GROUP] meilleur updated depart=2036-03-13T08:50 nbPassagers=5 nbReservations=2
[ASSIGN] groupeReservation size=2
[ASSIGN] groupe reservations ids=[109,112]
[ASSIGN] candidat vehicule=MV-001 start=2036-03-13T08:50 end=2036-03-13T10:22 distance=46.00
[CALENDAR] occupied for veh=1 = []
[CALENDAR] tentative for veh=1 = [2036-03-13T10:10->2036-03-13T11:32]
[ASSIGN] vehicule NOT free overall id=1 start=2036-03-13T08:50 end=2036-03-13T10:22
[CALENDAR] occupiedCalendar empty for veh=1
[CALENDAR] overlap with tentativeCalendar veh=1 interval=2036-03-13T10:10->2036-03-13T11:32
[ASSIGN] vehiculesCandidates size=1
[ASSIGN] findBestVehicle returned=MV-004(id=4)
[GROUP] Reservation disponibles: T104 T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=4 requested=2036-03-13T08:25 intervals=[2036-03-13T10:10->2036-03-13T11:22]
[CALENDAR] requested before next interval 2036-03-13T10:10 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-004 premiere=109 heurePremiere=2036-03-13T08:25 heureArriveeVehicule=2036-03-13T08:25 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:25, 2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:25 groupeSize=1 ids=[109] nbPassagers=3
[GROUP] meilleur updated depart=2036-03-13T08:25 nbPassagers=3 nbReservations=1
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=2 ids=[109,112] nbPassagers=5
[GROUP] meilleur updated depart=2036-03-13T08:50 nbPassagers=5 nbReservations=2
[ASSIGN] groupeReservation size=2
[ASSIGN] groupe reservations ids=[109,112]
[ASSIGN] candidat vehicule=MV-004 start=2036-03-13T08:50 end=2036-03-13T10:22 distance=46.00
[CALENDAR] occupied for veh=4 = []
[CALENDAR] tentative for veh=4 = [2036-03-13T10:10->2036-03-13T11:22]
[ASSIGN] vehicule NOT free overall id=4 start=2036-03-13T08:50 end=2036-03-13T10:22
[CALENDAR] occupiedCalendar empty for veh=4
[CALENDAR] overlap with tentativeCalendar veh=4 interval=2036-03-13T10:10->2036-03-13T11:22
[ASSIGN] marque comme processed (exhausted vehicles) id=109 client=T104
[ASSIGN] avant groupVehicles candidats count=10
[ASSIGN] apres groupVehicles candidats count=10
[ASSIGN] remainingReservations=106:T101,112:T107
[ASSIGN] premiereReservation id=106 client=T101 arrivee=2036-03-13T08:00
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:2, ref: MV-002 }, {id:3, ref: MV-003 }]
VEHICULE: 2, CANDIDATS: 2
VEHICULE: 3, CANDIDATS: 2
MIN NBR CANDIDATS: 2
VEHICULE: 2, COUNT: 2, CANDIDATS: 2
VEHICULE: 3, COUNT: 2, CANDIDATS: 2
SIZE FINAL: 2
[ASSIGN] findBestVehicle returned=MV-003(id=3)
[GROUP] Reservation disponibles: T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=3 requested=2036-03-13T08:00 intervals=[2036-03-13T08:20->2036-03-13T09:32,2036-03-13T11:45->2036-03-13T12:57]
[CALENDAR] requested before next interval 2036-03-13T08:20 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-003 premiere=106 heurePremiere=2036-03-13T08:00 heureArriveeVehicule=2036-03-13T08:00 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:00]
[GROUP] departCandidat=2036-03-13T08:00 groupeSize=1 ids=[106] nbPassagers=2
[GROUP] meilleur updated depart=2036-03-13T08:00 nbPassagers=2 nbReservations=1
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[106]
[ASSIGN] candidat vehicule=MV-003 start=2036-03-13T08:00 end=2036-03-13T09:22 distance=41.00
[CALENDAR] occupied for veh=3 = []
[CALENDAR] tentative for veh=3 = [2036-03-13T08:20->2036-03-13T09:32,2036-03-13T11:45->2036-03-13T12:57]
[ASSIGN] vehicule NOT free overall id=3 start=2036-03-13T08:00 end=2036-03-13T09:22
[CALENDAR] occupiedCalendar empty for veh=3
[CALENDAR] overlap with tentativeCalendar veh=3 interval=2036-03-13T08:20->2036-03-13T09:32
[ASSIGN] vehiculesCandidates size=5
[ASSIGN] findBestVehicle returned=MV-002(id=2)
[GROUP] Reservation disponibles: T101 T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=2 requested=2036-03-13T08:00 intervals=[2036-03-13T09:25->2036-03-13T10:49,2036-03-13T11:45->2036-03-13T13:07]
[CALENDAR] requested before next interval 2036-03-13T09:25 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-002 premiere=106 heurePremiere=2036-03-13T08:00 heureArriveeVehicule=2036-03-13T08:00 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:00]
[GROUP] departCandidat=2036-03-13T08:00 groupeSize=1 ids=[106] nbPassagers=2
[GROUP] meilleur updated depart=2036-03-13T08:00 nbPassagers=2 nbReservations=1
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[106]
[ASSIGN] candidat vehicule=MV-002 start=2036-03-13T08:00 end=2036-03-13T09:22 distance=41.00
[CALENDAR] occupied for veh=2 = []
[CALENDAR] tentative for veh=2 = [2036-03-13T09:25->2036-03-13T10:49,2036-03-13T11:45->2036-03-13T13:07]
[ASSIGN] vehicule free overall id=2
[ASSIGN] marque comme assignee id=106 client=T101
[ASSIGN] avant groupVehicles candidats count=11
[ASSIGN] apres groupVehicles candidats count=11
[ASSIGN] remainingReservations=112:T107
[ASSIGN] premiereReservation id=112 client=T107 arrivee=2036-03-13T08:50
[ASSIGN] vehiculesCandidates size=6
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:2, ref: MV-002 }, {id:3, ref: MV-003 }]
VEHICULE: 2, CANDIDATS: 3
VEHICULE: 3, CANDIDATS: 2
MIN NBR CANDIDATS: 2
VEHICULE: 2, COUNT: 3, CANDIDATS: 2
VEHICULE: 3, COUNT: 2, CANDIDATS: 2
Choisi: {id:3, ref: MV-003 } over [{id:3, ref: MV-003 }]
====FIN====
[ASSIGN] findBestVehicle returned=MV-003(id=3)
[GROUP] Reservation disponibles: T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=3 requested=2036-03-13T08:50 intervals=[2036-03-13T08:20->2036-03-13T09:32,2036-03-13T11:45->2036-03-13T12:57]
[CALENDAR] requested inside interval 2036-03-13T08:20->2036-03-13T09:32 -> return 2036-03-13T09:32
[GROUP] findOptimalWaitingGroup vehicule=MV-003 premiere=112 heurePremiere=2036-03-13T08:50 heureArriveeVehicule=2036-03-13T09:32 tempsAttenteRestant=-12
[ASSIGN] groupeReservation size=0
[ASSIGN] vehiculesCandidates size=5
[ASSIGN] findBestVehicle returned=MV-002(id=2)
[GROUP] Reservation disponibles: T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=2 requested=2036-03-13T08:50 intervals=[2036-03-13T08:20->2036-03-13T10:49,2036-03-13T11:45->2036-03-13T13:07]
[CALENDAR] requested inside interval 2036-03-13T08:20->2036-03-13T10:49 -> return 2036-03-13T10:49
[GROUP] findOptimalWaitingGroup vehicule=MV-002 premiere=112 heurePremiere=2036-03-13T08:50 heureArriveeVehicule=2036-03-13T10:49 tempsAttenteRestant=-89
[ASSIGN] groupeReservation size=0
[ASSIGN] vehiculesCandidates size=4
====DEBUT findBestVehicle====
VEHICULE DISPO: [{id:5, ref: MV-005 }, {id:6, ref: MV-006 }]
VEHICULE: 5, CANDIDATS: 2
VEHICULE: 6, CANDIDATS: 2
MIN NBR CANDIDATS: 2
VEHICULE: 5, COUNT: 2, CANDIDATS: 2
VEHICULE: 6, COUNT: 2, CANDIDATS: 2
[ASSIGN] findBestVehicle returned=MV-006(id=6)
[GROUP] Reservation disponibles: T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=6 requested=2036-03-13T08:50 intervals=[2036-03-13T08:20->2036-03-13T09:44,2036-03-13T15:00->2036-03-13T16:24]
[CALENDAR] requested inside interval 2036-03-13T08:20->2036-03-13T09:44 -> return 2036-03-13T09:44
[GROUP] findOptimalWaitingGroup vehicule=MV-006 premiere=112 heurePremiere=2036-03-13T08:50 heureArriveeVehicule=2036-03-13T09:44 tempsAttenteRestant=-24
[ASSIGN] groupeReservation size=0
[ASSIGN] vehiculesCandidates size=3
[ASSIGN] findBestVehicle returned=MV-005(id=5)
[GROUP] Reservation disponibles: T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=5 requested=2036-03-13T08:50 intervals=[2036-03-13T09:25->2036-03-13T10:59,2036-03-13T13:00->2036-03-13T14:32]
[CALENDAR] requested before next interval 2036-03-13T09:25 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-005 premiere=112 heurePremiere=2036-03-13T08:50 heureArriveeVehicule=2036-03-13T08:50 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=1 ids=[112] nbPassagers=2
[GROUP] meilleur updated depart=2036-03-13T08:50 nbPassagers=2 nbReservations=1
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[112]
[ASSIGN] candidat vehicule=MV-005 start=2036-03-13T08:50 end=2036-03-13T10:02 distance=36.00
[CALENDAR] occupied for veh=5 = []
[CALENDAR] tentative for veh=5 = [2036-03-13T09:25->2036-03-13T10:59,2036-03-13T13:00->2036-03-13T14:32]
[ASSIGN] vehicule NOT free overall id=5 start=2036-03-13T08:50 end=2036-03-13T10:02
[CALENDAR] occupiedCalendar empty for veh=5
[CALENDAR] overlap with tentativeCalendar veh=5 interval=2036-03-13T09:25->2036-03-13T10:59
[ASSIGN] vehiculesCandidates size=2
[ASSIGN] findBestVehicle returned=MV-001(id=1)
[GROUP] Reservation disponibles: T107
[CALENDAR] getClosestAvailableDateForVehicle vehicle=1 requested=2036-03-13T08:50 intervals=[2036-03-13T10:10->2036-03-13T11:32]
[CALENDAR] requested before next interval 2036-03-13T10:10 -> return requested date
[GROUP] findOptimalWaitingGroup vehicule=MV-001 premiere=112 heurePremiere=2036-03-13T08:50 heureArriveeVehicule=2036-03-13T08:50 tempsAttenteRestant=30
[GROUP] pointsDepart=[2036-03-13T08:50]
[GROUP] departCandidat=2036-03-13T08:50 groupeSize=1 ids=[112] nbPassagers=2
[GROUP] meilleur updated depart=2036-03-13T08:50 nbPassagers=2 nbReservations=1
[ASSIGN] groupeReservation size=1
[ASSIGN] groupe reservations ids=[112]
[ASSIGN] candidat vehicule=MV-001 start=2036-03-13T08:50 end=2036-03-13T10:02 distance=36.00
[CALENDAR] occupied for veh=1 = []
[CALENDAR] tentative for veh=1 = [2036-03-13T10:10->2036-03-13T11:32]
[ASSIGN] vehicule free overall id=1
[ASSIGN] marque comme assignee id=112 client=T107
[ASSIGN] avant groupVehicles candidats count=12
[ASSIGN] apres groupVehicles candidats count=12
[SAVE] saved trajet id=37 veh=5 interval=2036-03-13T09:25->2036-03-13T10:59
[SAVE] saved trajet id=38 veh=4 interval=2036-03-13T10:10->2036-03-13T11:22
[SAVE] saved trajet id=39 veh=1 interval=2036-03-13T10:10->2036-03-13T11:32
[SAVE] saved trajet id=40 veh=6 interval=2036-03-13T08:20->2036-03-13T09:44
[SAVE] saved trajet id=41 veh=5 interval=2036-03-13T13:00->2036-03-13T14:32
[SAVE] saved trajet id=42 veh=6 interval=2036-03-13T15:00->2036-03-13T16:24
[SAVE] saved trajet id=43 veh=3 interval=2036-03-13T08:20->2036-03-13T09:32
[SAVE] saved trajet id=44 veh=2 interval=2036-03-13T09:25->2036-03-13T10:49
[SAVE] saved trajet id=45 veh=3 interval=2036-03-13T11:45->2036-03-13T12:57
[SAVE] saved trajet id=46 veh=2 interval=2036-03-13T11:45->2036-03-13T13:07
[SAVE] skip candidat veh=2 start=2036-03-13T08:20 end=2036-03-13T09:42 cause overlap with occupiedToSave=[2036-03-13T09:25->2036-03-13T10:49,2036-03-13T11:45->2036-03-13T13:07]
[SAVE] saved trajet id=47 veh=1 interval=2036-03-13T08:50->2036-03-13T10:02
[ASSIGN] finished assignVehicles, trajetsCreated=11 nonAssigned=0
