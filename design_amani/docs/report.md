# L3 design pattern report

- **Firstname**: [Amani]
- **Lastname**: [YAHIA BEY]

- 
# TP FINAL : ( Les changements dans le dossier "design_amani" )

1/ j'ai dévisé mon projet sur des fichier : chaque classe a leur propre code 
*App.java 
*CsvFileManger
*jsonFileManager
*ToDoFileManager
*Todoitem

2/ j'ai essayé les teste du cours 
./exec.sh insert -s toto.json "I am not done and stored in JSON"
 Status code: 0

./exec.sh insert -d "I am done and stored in CSV" -s toto.csv
 Status code: 0

./exec.sh --source toto.csv insert -d "I am not done and stored in CSV"
 Status code: 0

./exec.sh insert "I am done" --done -s file.json
./exec.sh insert "I am not done" -s file.json

 List all todos
./exec.sh list -s file.json
 Stdout:
 - Done: I am done
 - I am not done

 List done todos
./exec.sh list -s file.json --done
 Stdout:
 - Done: I am done

# il m'a affiché les memes résultat 
---

3/creer un fichier pom1.xml situé à la racine de mon projet.
 On Ajoute les balises <dependencies> et <dependency> pour inclure les dépendances Spring Boot.
4/ On Configurer les points de terminaison de l'API REST :
5/On Ajoute des méthodes pour gérer les requêtes POST et GET pour créer et lister des tâches.
6/On Traiter les arguments de ligne de commande :
7/On Récupérer l'argument --source pour déterminer la source de données à utiliser.
8/On Gérer les erreurs et les messages de retour :
9/On Renvoyer les réponses appropriées pour les requêtes POST et GET.


import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController
public class App {

    // Autres méthodes de votre classe App (ex: exec, getFileManager, etc.)
    
    @PostMapping("/todos")
    public ResponseEntity<String> createTodo(@RequestBody Todoitem todo) {
        // Logique pour créer une nouvelle tâche
        try {
            // Ajouter la logique pour insérer la tâche dans la source de données
            return ResponseEntity.ok("{\"message\": \"inserted\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create todo: " + e.getMessage());
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todoitem>> listTodos() {
        // Logique pour lister les tâches à partir de la source de données
        try {
            List<Todoitem> todos = // Récupérer les tâches depuis la source de données
            return ResponseEntity.ok(todos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to list todos: " + e.getMessage());
        }
    }
}
