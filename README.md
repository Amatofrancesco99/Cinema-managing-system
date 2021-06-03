## Progetto-F21

Sistema per la gestione delle prenotazioni di biglietti di un cinema.

![image](https://user-images.githubusercontent.com/80333091/113708434-81895080-96e1-11eb-85db-60251d9deaf9.png)

***
### 0. Install lombok annotations support for Eclipse

Questo passo è richiesto solo per poter modificare i file .java del progetto tramite Eclipse senza avere feedback di warning e/o errori dall'editor.

Per installare il supporto alla sintassi delle annotazioni di lombok su Eclipse, scaricare il seguente file (non è necessario inserirlo all'interno dei file del progetto): [lombok 1.18.20 (.jar)](https://projectlombok.org/downloads/lombok.jar)

Una volta scaricato, eseguirlo come amministratore e seguire le istruzioni per installare il plugin per Eclipse.

Al termine dell'installazione riavviare Eclipse e aggiornare la struttura del progetto per abilitare l'interprete delle annotazioni di lombok.

***
### 1. Build application
```
gradle clean build
```

***
### 2. Run application

Con dispositivi Windows:

```
gradlew run
```

Con dispositivi Linux/MacOS: 

```
./gradlew run
```

Qualora uno dei due comandi sopracitati dicesse che manchi la classe wrapper, esegui il seguente comando:

```
gradle wrapper
```

***
### 3. Open browser
Nella barra degli indirizzi di un browser (preferibilmente Google Chrome), scrivi: 

 `http://localhost:8080/`
