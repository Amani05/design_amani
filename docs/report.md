# L3 design pattern report

- **Firstname**: [Amani]
- **Lastname**: [YAHIA BEY]
- 
# TP FINAL : ( Les changement dans le dossier "design_amani" )

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
...
