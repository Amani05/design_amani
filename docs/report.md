# L3 design pattern report

- **Firstname**: [Amani]
- **Lastname**: [YAHIA BEY]



TP NUM 02
Composants principaux
TodoItem : Une classe représentant une tâche individuelle avec deux propriétés principales : task (le libellé de la tâche) et done (l'état de la tâche, accomplie ou non).
TodoFileManager : Une interface définissant les opérations essentielles (insert, list, readAll) que doivent implémenter les gestionnaires de fichiers.
JsonFileManager et CsvFileManager : Des classes concrètes implémentant TodoFileManager pour gérer respectivement les fichiers au format JSON et CSV.
App : La classe principale qui orchestre l'exécution des commandes utilisateur basées sur les arguments de la ligne de commande.

---
...
