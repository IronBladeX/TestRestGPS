# TestREST GPS

Instalation:
  - Installer et lancer un serveur [MongoDB](https://www.mongodb.com/download-center#community)
  - Paramétrer si les configurations de MongoDB sont différentes dans [application.properties](https://github.com/IronBladeX/TestRestGPS/blob/master/src/main/resources/application.properties)
  - Lancer le Projet pour essayer

# Services REST:

Ajouter un point GPS :

```sh
    Url POST: http://localhost:8080/positions
    JSON:
                {"longitude":194.67,"latitude":690.6,"timeEntry":"2017-09-09 13:36:03"}
    Retourne:
        Retourne le point ajouté
```

Récupérer la liste des points entre deux dates:

```sh
    Url: http://localhost:8080/positions?begin=2017-08-20 10:00&end=2017-08-30 15:00
    Paramétres:
                Date de Début
                    nom: begin
                    type: String
                    format: yyyy-MM-dd HH:mm
                Date de Fin
                    nom: end
                    type: String
                    format: yyyy-MM-dd HH:mm
    Retourne:
        Retourne la liste des points entre les deux dates
```

Récupérer la distance des points entre deux dates:

```sh
    Url: http://localhost:8080/positions/distance?begin=2017-08-20 10:00&end=2017-08-30 15:00
    Paramétres:
                Date de Début
                    nom: begin
                    type: String
                    format: yyyy-MM-dd HH:mm
                Date de Fin
                    nom: end
                    type: String
                    format: yyyy-MM-dd HH:mm
    Retourne:
        Retourne la distance entre les points entre les deux dates
```

Supprimer un point GPS

```sh
    Url DELETE: http://localhost:8080/positions/{id}
        ID: Id de la position
```

Récupérer un point GPS

```sh
    Url GET: http://localhost:8080/positions/{id}
        ID: Id de la position
    Retourne:
        Retourne le point GPS si trouvé sinon not Found
```

# Les Choix

MongoDB:
> J'ai Choisi MongoDB d'une part car je ne connaissais que de nom et je voulais essayer, et d'autre part que quand j'ai commencer a m'interesser au fonctionnement de MongoDB avec SPRING j'ai remarqué que SPRING le gére trés bien et cela m'a permis de l'implémenter trés rapidement et au niveau des performances même si sur ce projet il n'y à pas besoin de performances, cela a été quand même trés intéressant par rapport à MYSQL.
