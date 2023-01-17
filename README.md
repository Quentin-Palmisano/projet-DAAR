# Projet PC3R
## Membres
Paul Boursin
Quentin Palmisano
Robin Louis
## Description
L’application web que nous avons décidé de créer est un jeu en ligne dans lequel on peut investir dans la production de ressources que l’on peut vendre sur le marché ou à d’autres joueurs. Le but est de s’enrichir un maximum.

L’application permet au joueur de :
-   produire des ressources, parfois en utilisant d’autres ressources
-   investir dans la production
-   échanger des ressources avec d’autres joueurs, manuellement ou automatiquement selon des critères, créant donc un marché fluctuant

## Prérequis
- Apache Tomcat
- MariaDB
- Java

## Création de la base de données
`cd /PC3R_Projet/sql/configure_db.sql`
`sudo mysql -u root -p < configure_db.sql`

## Tutoriel
On peut accéder (normalement) au serveur d’un de nos ordinateurs :
78.192.140.11:8080
Si le serveur n’est pas ouvert, veuillez contacter paul.boursin@gmail.com

Sur la page de jeu, on retrouve en haut dans la barre de navigation le pseudo du joueur et le total d'argent qu'il cumule. Initialement un joueur possède **1000$**.
Un joueur peut produire des ressources grâce au bouton `add production` de chaque étiquette qu'on retrouve dans le menu déroulant à gauche. Cela augmente donc le stock pour la ressource choisie. Certaines ressources doivent être produites à partir d'autres ressources, cela est indiqué dans la catégorie **craft** le cas échéant. On peut vendre ou acheter des ressources à l’aide des onglets `Buy` et `Sell`. Ces derniers permettent également de voir les offres de vente/achat en cliquant sur le bouton `search`. Pour chacune d’entre elles, il faut indiquer le prix de vente/achat. Le champ `Target Quantity` permet de définir la quantité minimum de ressource à garder en stock dans le cas d’une offre de vente et la quantité maximale de ressource à acheter en cas d’offre d’achat. Ces offres sont visibles par les autres joueurs. En ce qui concerne les voitures, les téléphones et le pain, ces ressources ne sont pas achetables par les joueurs. Les achats se feront automatiquement par le système.

