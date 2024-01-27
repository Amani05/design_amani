# L3 design pattern report

- **Firstname**: [Amani]
- **Lastname**: [YAHIA BEY]


> Add your thoughts on every TP bellow, everything is interresting but no need to right a book.
> 
> Keep it short simple and efficient:
> - What you did and why:
> - j'ai effectué quelque changement premièrement dans l'importation du bibliothèque au lieu d'ecrire:
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
on peut écrire directement : import java.nio.file.*; import toutes les classes du package une seul fois.
>Réorganisation en approche orientée objet : j'ai organisé le code et utiliser TodoApp qui encapsule les fonctionnalités de l'application.
>Utilisation d'une seule instance ObjectMapper : J'ai créé une seule instance d'ObjectMapper en tant que variable statique pour améliorer l'efficacité, plutôt que de créer une nouvelle instance à chaque fois que nécessaire.
>Gestion des erreurs : J'ai modifié la gestion des erreurs pour utiliser des exceptions plutôt que d'imprimer directement sur System.err et de terminer le programme avec System.exit(1)
>séparation ; j'ai utiliser des méthode plus petit pour faciliter la maintenance
>Utilisation d'une méthode run : J'ai introduit une méthode run pour encapsuler la logique principale du programme
>Suppression des dépendances Apache Commons CLI : J'ai retiré la dépendance à Apache Commons CLI pour simplifier le code

> Add a link to schemas describing your architecture (UML or not but add a legend)
> 
> Remember: it is ok to make mistakes, you will have time to spot them later.
> 
> Fill free to contact me if needed.

---
...
