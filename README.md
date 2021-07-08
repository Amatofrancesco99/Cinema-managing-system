# Progetto-F21

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/IngSW-unipv/Progetto-F21/blob/main/LICENSE)&emsp;
![Java](https://img.shields.io/badge/Backend-java-red)
![Rhythm](https://img.shields.io/badge/Templating-rhythm-green)&emsp;
![HTML](https://img.shields.io/badge/Frontend-html%20-orange)
![Bootstrap](https://img.shields.io/badge/Frontend-bootstrap-blueviolet)
![JavaScript](https://img.shields.io/badge/Frontend-javascript%20-yellow)&emsp;
![SQLite](https://img.shields.io/badge/Database-sqlite-blue)

***Sistema per la gestione delle prenotazioni dei biglietti di un cinema.***

[Presentazione](https://docs.google.com/presentation/d/1DnBT-Xm7wlHo8Mx5TOKYY6t92_84Xn-ywwDrSrER7wE/edit?usp=sharing)

<!-- ![image](https://github.com/IngSW-unipv/Progetto-F21/blob/main/images/popcorn.png) -->
![image](https://github.com/IngSW-unipv/Progetto-F21/blob/main/images/mockup.png)

***

## 0. SETUP DEL REPOSITORY

### 0.1 - Download di Git
Se Git non è già presente sul sistema in uso, scaricarlo e installarlo da [git-scm.com](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git). 

### 0.2 - Clone del repository
È possibile effettuare il clone del repository contenente tutto il necessario alla compilazione ed esecuzione dell'applicazione tramite il comando seguente:

```
git clone https://github.com/IngSW-unipv/Progetto-F21.git
```

### 0.3 - Download di Gradle
Per poter eseguire l'applicazione è necessario avere un'installazione locale di [Gradle](https://it.wikipedia.org/wiki/Gradle).

Su [gradle.org](https://gradle.org/install/) è possibile reperire tutte le istruzioni per poter effettuare il setup di Gradle sul dispositivo in uso, qualora non sia già installato.
Il procedimento per l'installazione a seconda del sistema operativo utilizzato può essere trovato [qui](https://gradle.org/install/).

***
## 1. ESECUZIONE DELL'APPLICAZIONE WEB - VISTA SPETTATORE

Per accedere all'interfaccia grafica web riguardante la vista spettatore, è necessario (dopo aver compilato il progetto) eseguire tramite una JVM la classe [WebGUIMain.java](https://github.com/Amatofrancesco99/Progetto-F21/blob/main/src/main/java/cinema/view/webgui/WebGUIMain.java) (è necessaria una versione di Java 1.8 o superiore).

È possibile farlo con l'aiuto di Gradle per la gestione automatica delle dipendenze tramite i comandi riportati di seguito.

### 1.1 - Apertura del terminale o del prompt dei comandi
Per poter compilare ed eseguire l'applicazione web (server HTTP) è necessario innanzitutto posizionarsi nella directory root del progetto all'interno di un terminale o prompt dei comandi:

```
cd Progetto-F21
```

### 1.2 - Compilazione e setup dell'applicazione
Per aggiornare le dipendenze di Gradle e compilare i file necessari eseguire il seguente comando:

```
gradle clean build
```

### 1.3 - Esecuzione dell'applicazione
Sul prompt dei comandi (Windows):

```
gradlew run
```

Su un terminale (Linux/MacOS): 

```
./gradlew run
```

Qualora uno dei due comandi riportasse un errore dovuto alla mancanza della classe wrapper, esegui il seguente comando e riprovare:

```
gradle wrapper
```

### 1.4 - Accesso al sito web
Tramite un browser aggiornato (l'applicazione è stata testata sulle versioni recenti di Chrome) inserire nella barra di ricerca il seguente link:

[http://localhost:8080/](http://localhost:8080/)

***
## 2. ESECUZIONE DA CLI - VISTA SPETTATORE

Per accedere all'interfaccia a riga di comando riguardante la vista spettatore, è necessario eseguire tramite una JVM la classe [CLIUserMain.java](https://github.com/Amatofrancesco99/Progetto-F21/blob/main/src/main/java/cinema/view/cli/user/CLIUserMain.java).

***
## 3. ESECUZIONE DA CLI - VISTA ADMIN

Per accedere all'interfaccia a riga di comando riguardante la vista amministratore, è necessario eseguire tramite una JVM la classe [CLIAdminMain.java](https://github.com/Amatofrancesco99/Progetto-F21/blob/main/src/main/java/cinema/view/cli/admin/CLIAdminMain.java).

***
## 4. WIKI DEL PROGETTO

È possibile consultare ulteriori informazioni dettagliate riguardo alla documentazione di progetto consultando la [wiki del repository](https://github.com/Amatofrancesco99/Progetto-F21/wiki).

È possibile anche trovare la documentazione JavaDoc generata automaticamente dai commenti presenti nel codice sorgente nella cartella [doc](https://github.com/Amatofrancesco99/Progetto-F21/blob/main/doc).
