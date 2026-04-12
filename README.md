**Projet : Le Désert Interdit Binôme : Sokhna FALL & Song LI**

**1. Parties traitées :**
Nous avons implémenté l'intégralité du moteur de jeu fonctionnel :
       - Plateau de jeu 5x5 avec génération aléatoire des zones (Oasis, Tunnels,Piste de vol, Crash).
       - Système de Tempête : mouvement de l'Oeil, ajout de sable et montée du niveau de tempête.
       - Conditions de victoire/défaite : récupération des 4 pièces, mort par soif, ensevelissement sous le sable ou tempête niveau 7.
       - Interface Graphique (Swing) : affichage de la grille, logs des actions etbarre de statut des joueurs.
       
**2. Choix d'architecture**

   **Organisation des classes (MVC) :**
    Nous avons opté pour une architecture MVC (Modèle-Vue-Contrôleur) :
       - Modèle (Desert.java, Zone.java, Joueur.java) : Contient toute la logique métier et les règles. Nous avons utilisé l'héritage pour les zones (la classe abstraite Zone est déclinée en Oasis, Tunnel, Piste_Vol, etc.), ce          qui permet d'utiliser le polymorphisme pour les actions spéciales.
       - Vue (Vue.java) : Gère l'affichage. Elle utilise un GridBagLayout pour une interface flexible et des classes internes pour chaque section (Grille, Logs, Status).
       - Contrôleur (Controleur.java) : Fait le lien entre les deux, capturant les clics de l'utilisateur pour mettre à jour le modèle et rafraîchir la vue.

       
    **Partage des rôles (Binôme)**
    Le travail a été réparti de manière équilibrée tout au long du projet, avec une communication constante via GitHub et Google Docs :

       **Sokhna Fatou Diarra Fall (Logique & Structure)**
       - Conception de la structure initiale du modèle et de la hiérarchie des classes Zone.
       - Implémentation de la logique complexe : gestion des directions, ramassage des pièces, et vérification des conditions de victoire/défaite.
       **Song Li**
       - Développement du moteur de la tempête (mouvement de l'Oeil, gestion du sable).
       - Conception et réalisation de l'interface graphique (Swing) : mise en place du GridBagLayout, création de la Barre de Log pour l'historique des actions et de la barre de statut des joueurs.
       Et tout les deux, on a fait la classe Desert, les tests et la correction des bugs.

       **Techniques utilisées (Cours/TP)**
        - Héritage et Polymorphisme : Utilisation d'une classe abstraite Zone pour factoriser le code commun, avec redéfinition de actionSpeciale() pour les classes filles (Oasis, Tunnel, etc.).
        - Encapsulation : Beaucoup d'attributs sont privés avec des accesseurs (getters/setters) pour sécuriser les données.
        - Gestion des collections : Utilisation d' ArrayList pour gérer dynamiquement la liste des joueurs et des pièces.
        - Interfaces Graphiques : Utilisation des LayoutManagers et des ActionListeners pour une interface interactive.
    
**3. Problèmes connus**
   L'esthétique de l'interface reste basique mais ça nous permet quand même de jouer à Desert Interdit.
   
**4. Crédit et sources**
    Nous avons utilisé une IA générative de manière ciblée pour :
        - Le visuel (Swing) : Aide à la mise en page complexe avec GridBagLayout et la structuration des panneaux imbriqués.
        - Algorithmique 2D : Optimisation des boucles de parcours du tableau 2D pour le déplacement de l'Oeil de la tempête.
        - Débogage : Identification rapide de fautes d'inattention (comparaisons de Strings, assignations de variables).
    L'architecture globale et l'implémentation des règles de jeu sont issues de notre travail en binôme.
