# Come avviare l'applicazione in locale

- da terminale avviare con `docker compose up -d` il db

# cURL esempio di inserimento

### Linux
```shell
curl -H "Content-Type: application/json" -X POST http://localhost:8080/users -d '{ "username": "Mattia", "email": "mattia.iaria@email.it" }'
```
### Windows
```shell
curl -Headers @{"Content-Type" = "application/json"} -Method POST -Uri http://localhost:8080/users -Body '{ "username": "Mattia", "email": "mattia.iaria@email.it" }'
```  