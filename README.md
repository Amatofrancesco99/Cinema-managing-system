## Progetto-F21

Sistema per la gestione delle prenotazioni di biglietti di un cinema.

![image](https://user-images.githubusercontent.com/80333091/113708434-81895080-96e1-11eb-85db-60251d9deaf9.png)


***

### 0. Download Gradle

Per poter eseguire l'applicazione c'è bisogno di aver installato sul proprio dispositivo [Gradle](https://it.wikipedia.org/wiki/Gradle).

Qui sotto è riportato un link contenente tutte le istruzioni per poter fare il setup di Gradle sul vostro dispositivo, qualora non lo aveste già precedentemente installato.
Trovate inoltre i procedimenti per l'installazione a seconda del sistema operativo che state utilizzando.

[Gradle installation](https://gradle.org/install/)


***

### 1 Open terminal/command prompt

Aprite il vostro terminale/prompt dei comandi, per poter eseguire i comandi che saranno riportati nei passaggi successivi.


***

### 2 Build application

```
gradle clean build
```


***

### 3 Run application

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
### 4. Open browser
Nella barra degli indirizzi di un browser (preferibilmente Google Chrome), scrivi: 

 `http://localhost:8080/`

