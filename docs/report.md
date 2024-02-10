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
>J'ai instauré une séparation des tâches en instiguant des interfaces distinctes pour superviser les Todos (TodoRepository) et pour exécuter les commandes (TodoManager). Chacune de ces classes est assignée à une responsabilité bien définie, accomplissant une seule fonction.
>L'intégration d'interfaces (TodoRepository et TodoManager) a rendu possible leur extension pour de nouveaux formats de fichier ou de nouvelles fonctionnalités sans nécessiter de modification du code existant. Les implémentations spécifiques, telles que JsonTodoRepository et CsvTodoRepository, peuvent être ajoutées sans altérer la logique fondamentale.
>Les classes JsonTodoRepository et CsvTodoRepository sont des sous-classes de l'interface TodoRepository, assurant ainsi l'interopérabilité de ces classes avec TodoRepository.
>Les interfaces TodoRepository et TodoManager ont été conçues spécifiquement pour leurs tâches respectives, évitant ainsi la création d'interfaces trop étendues.
>La classe TodoManagerImpl dépend désormais de l'abstraction TodoRepository, permettant la définition de comportements spécifiques dans les implémentations concrètes (JsonTodoRepository et CsvTodoRepository). La dépendance a été inversée par rapport à la version originale.
>J'ai également inclus des méthodes privées afin de rendre les fonctions de la classe TodoManagerImpl plus compréhensibles et mieux structurées.

//  Pour 2éme partie : la partie de Ilia wants you to add something new!

 1/On Ajouter une option pour le paramètre --done dans les options de la ligne de commande :
on modifier la méthode parseCommandLine dans la classe TodoManagerImpl pour prendre en charge le nouveau paramètre. ob on ajoutez une option pour le paramètre --done avec un indicateur court -d.
2/on  fait la Mise à jour de la méthode insert dans la classe TodoManagerImpl pour prendre en charge le nouveau paramètre :
Si le paramètre --done est spécifié, marquez le todo comme "done" avant de l'ajouter.
3/   on fait la faite à jour la méthode list pour afficher les todos "done" si le paramètre --done est spécifié 
 Si le paramètre --done est spécifié, n'affichez que les todos marqués comme "done".
 
// Aprés l'amlioration 
1/ j'ai essayé de refaire les principes solid. 
2/ ajouter l'option --donnée à mon interface de ligne de commande dans la méthode process de la classe CommandLineProcessor comme suit : 
cliOptions.addOption(Option.builder("d")
                    .longOpt("done")
                    .desc("Flag to mark a task as done")
                    .build());
3/ Ensuite, dans la méthode execute de la classe TodoManager, j'ai essayé de   vérifier si l'option --done a été spécifiée et, le cas échéant, marquer la tâche comme terminée. 

> Remember: it is ok to make mistakes, you will have time to spot them later.
> 
> Fill free to contact me if needed.

---
...
