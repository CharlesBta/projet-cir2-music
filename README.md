![Menu de l'application](./View/src/main/resources/image_menu.jpg)
# Projet d'Application Musicale

Ce dépôt contient une application musicale basée sur Java qui permet aux utilisateurs de jouer différents instruments de musique, d'enregistrer des performances et de les sauvegarder. L'application suit le modèle d'architecture Modèle-Vue-Contrôleur (MVC).

## Prérequis

- Kit de Développement Java (JDK) 21 ou supérieur
- Apache Maven 3.6.0 ou supérieur
- Git

## Structure du Projet

Le projet est organisé comme un projet Maven multi-modules avec les modules suivants :

- **Main** : Contient le point d'entrée de l'application
- **Model** : Contient la logique métier et les modèles de données
- **View** : Contient les composants de l'interface utilisateur
- **Controller** : Contient les contrôleurs qui connectent le modèle et la vue
- **Share** : Contient les ressources partagées et les utilitaires utilisés dans tous les modules

## Installation

1. Clonez le dépôt :
   ```bash
   git clone https://github.com/yourusername/projet-cir2-music.git
   cd projet-cir2-music
   ```

2. Construisez le projet avec Maven :
   ```bash
   mvn clean install
   ```

## Exécution de l'Application

### Utilisation de Maven

Vous pouvez exécuter l'application en utilisant le plugin exec de Maven :

```bash
mvn exec:java -Dexec.mainClass="com.music.Main"
```

### Utilisation du fichier JAR

Après avoir construit le projet, vous pouvez exécuter le fichier JAR généré :

```bash
java -jar Main/target/Main-*-jar-with-dependencies.jar
```

## Développement

### Configuration du Projet dans un IDE

1. Importez le projet en tant que projet Maven dans votre IDE préféré (IntelliJ IDEA, Eclipse, etc.)
2. Assurez-vous que votre IDE est configuré pour utiliser JDK 21
3. Construisez le projet en utilisant Maven dans votre IDE

### Apporter des Modifications

1. L'application suit le modèle MVC :
   - Apportez des modifications aux classes du modèle dans le module `Model` pour la logique métier
   - Apportez des modifications aux classes de vue dans le module `View` pour les composants de l'interface utilisateur
   - Apportez des modifications aux classes de contrôleur dans le module `Controller` pour le flux de l'application

2. Après avoir apporté des modifications, reconstruisez le projet :
   ```bash
   mvn clean install
   ```

### Ajouter de Nouveaux Instruments

De nouveaux instruments peuvent être ajoutés en implémentant l'interface `IInstrument` et en les enregistrant dans la classe `InstrumentFactory`.

## Fonctionnalités

- Jouer différents instruments de musique (Piano, Synthétiseur, Batterie, etc.)
- Ajuster les paramètres d'octave et de vélocité
- Enregistrer et sauvegarder les performances
- Interface conviviale pour jouer des notes

## Dépendances

- Lombok : Utilisé pour réduire le code répétitif
- Java Sound API : Utilisé pour la lecture audio

