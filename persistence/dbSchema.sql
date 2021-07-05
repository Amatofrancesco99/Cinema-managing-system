# Creazione dello schema del database relazionale utilizzato nella persistenza
# e inserimento dati (instassi standard SQLite).
#
# Screaming Hairy Armadillo Team

# Eliminazione tabelle eventualmente esistenti

DROP TABLE IF EXISTS Movie;
DROP TABLE IF EXISTS Projection;
DROP TABLE IF EXISTS Room;
DROP TABLE IF EXISTS Discount;
DROP TABLE IF EXISTS Coupon;
DROP TABLE IF EXISTS Reservation;
DROP TABLE IF EXISTS OccupiedSeat;
DROP TABLE IF EXISTS Cinema;

# Creazione tabelle

CREATE TABLE Movie(
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    title TEXT NOT NULL,
    duration INTEGER NOT NULL CHECK(duration >= 0),
    rating INTEGER NOT NULL CHECK(rating <= 5 AND rating >= 0),
    imageurl TEXT NOT NULL,
    trailerurl TEXT NOT NULL,
    description TEXT NOT NULL,
    genres TEXT NOT NULL,
    directors TEXT NOT NULL,
    cast TEXT NOT NULL
);

CREATE TABLE Room(
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    rows INTEGER NOT NULL CHECK(rows > 0),
    columns INTEGER NOT NULL CHECK(columns > 0)
);

CREATE TABLE Projection(
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    datetime INTEGER NOT NULL,
    price REAL NOT NULL CHECK(price >= 0),
    movie INTEGER,
    room INTEGER,
    FOREIGN KEY (Movie) REFERENCES Movie(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY(room) REFERENCES Room(id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE Coupon(
	promocode TEXT PRIMARY KEY NOT NULL,
	amount REAL NOT NULL CHECK(amount > 0),
	used INTEGER NOT NULL DEFAULT(0) CHECK(used == 1 OR used == 0)
);

CREATE TABLE Discount(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	type TEXT NOT NULL,
	percentage REAL NOT NULL CHECK(percentage >= 0 AND percentage <= 1),
	minage INTEGER CHECK(minage > 0),
	maxage INTEGER CHECK(maxage > 0),
	date INTEGER,
	numberpeople INTEGER CHECK(numberpeople > 0),
	CHECK((date IS NOT NULL) OR ((minage IS NOT NULL) AND (maxage IS NOT NULL)) OR (numberpeople IS NOT NULL))
);

CREATE TABLE Reservation(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	date INTEGER,
	projection INTEGER,
	name TEXT,
	surname TEXT,
	email TEXT,
	paymentcardowner TEXT,
	paymentcard TEXT,
	coupon TEXT,
	discount INTEGER,
	numberpeopleunderage INTEGER DEFAULT(0) CHECK(numberpeopleunderage >= 0),
	numberpeopleoverage INTEGER DEFAULT(0) CHECK(numberpeopleoverage >= 0),
	FOREIGN KEY(projection) REFERENCES Projection(id) ON UPDATE CASCADE ON DELETE SET NULL,
	FOREIGN KEY(coupon) REFERENCES Coupon(promocode) ON UPDATE CASCADE ON DELETE SET NULL,
	FOREIGN KEY(discount) REFERENCES Discount(id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE OccupiedSeat(
	projection INTEGER,
	row INTEGER,
	column INTEGER,
	reservation INTEGER,
	PRIMARY KEY(projection, row, column),
	FOREIGN KEY(projection) REFERENCES Projection(id) ON UPDATE CASCADE ON DELETE SET NULL,
	FOREIGN KEY(reservation) REFERENCES Reservation(id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE Cinema(
	id INTEGER PRIMARY KEY NOT NULL,
	name TEXT NOT NULL,
	city TEXT NOT NULL,
	country TEXT NOT NULL,
	zipCode TEXT NOT NULL,
	address TEXT NOT NULL,
	email TEXT NOT NULL,
	mailPassword TEXT NOT NULL,
	adminPassword TEXT NOT NULL,
	logoURL TEXT NOT NULL,
	discountstrategy TEXT NOT NULL,
	FOREIGN KEY (discountstrategy) REFERENCES Discount(type)
);



# Inserimento dati all'interno delle tabelle create in precedenza

INSERT INTO Movie (id, title, duration, rating, imageurl, trailerurl, description, genres, directors, cast)
    VALUES (1, "Druk - Un altro giro", 117, 4, "druk-un-altro-giro.jpg", "https://www.youtube.com/watch?v=hFbDh58QHzw", "C'è una teoria secondo la quale tutti noi siamo nati con una piccola quantità di alcool già presente nel sangue e che, pertanto, una piccola ebbrezza possa aprire le nostre menti al mondo che ci circonda, diminuendo la nostra percezione dei problemi e aumentando la nostra creatività. Rincuorati da questa teoria, Martin e tre suoi amici, tutti annoiati insegnanti delle superiori, intraprendono un esperimento per mantenere un livello costante di ubriachezza durante tutta la giornata lavorativa. Se Churchill vinse la seconda guerra mondiale in preda a un pesante stordimento da alcool, chissà cosa potrebbero fare pochi bicchieri per loro e per i loro studenti?", "Drammatico,Commedia", "Thomas Vinterberg", "Mads Mikkelsen,Thomas Bo Larsen,Lars Ranthe,Magnus Millang");
INSERT INTO Movie (id, title, duration, rating, imageurl, trailerurl, description, genres, directors, cast)
    VALUES (2, "Pulp Fiction", 154, 5, "pulp-fiction.jpg", "https://www.youtube.com/watch?v=s7EdQ4FqbhY", "Un killer si innamora della moglie del suo capo, un pugile rinnega la sua promessa e una coppia tenta una rapina che va rapidamente fuori controllo.", "Drammatico,Thriller", "Quentin Tarantino", "Uma Thurman,John Travolta,Samuel L. Jackson,Bruce Willis,Steve Buscemi");
INSERT INTO Movie (id, title, duration, rating, imageurl, trailerurl, description, genres, directors, cast)
    VALUES (3, "Non è un paese per vecchi", 122, 4, "non-e-un-paese-per-vecchi.jpg", "https://www.youtube.com/watch?v=38A__WT3-o0", "Texas, 1980. Mentre è a caccia nei territori selvaggi al confine con il Messico, Llewelyn Moss, un saldatore texano reduce dalla guerra del Vietnam, si imbatte in quel che resta di un regolamento di conti tra bande locali per una partita di droga. In mezzo ai numerosi cadaveri, Moss trova un'ingente somma di denaro che si porta a casa, con l'intento di assicurarsi un futuro migliore per sé e la giovane moglie, Carla Jean.", "Thriller,Crime,Neo-Western", "Ethan Coen,Joel Coen", "Javier Bardem,Tommy Lee Jones,Josh Brolin,Kelly Mcdonald");
INSERT INTO Movie (id, title, duration, rating, imageurl, trailerurl, description, genres, directors, cast)
    VALUES (4, "Avengers: Endgame", 182, 3, "avengers-endgame.jpg", "https://www.youtube.com/watch?v=vqWz0ZCpYBs", "Tutto riparte dalla terra devastata da Thanos e le sue gemme dell'infinito, dopo aver visto svanire in particelle infinitesimali di polvere i Guardiani della Galassia e personaggi come Spider-Man, Black Panther e Dr. Strange. Questa seconda parte evidenzia una situazione altamente drammatica, con i superstiti che non sanno darsi pace, dilaniati dal senso di colpa e dal vuoto ed incapaci di ripartire. I legami rimasti continuano a rafforzarsi, seppur in sordina, quelli che avevano portato a scontri mutano in fiducia, le amicizie si cementano, i rapporti di vecchia data si consolidano ed alcuni cercano di costruirsi una famiglia fino a cinque anni dopo, quando una microscopica particella ritrovata rimette in moto lo spirito di gruppo. Un paese in ginocchio che prova a cambiare le cose e ripristinare l'ordine, quantomeno apparente e in mezzo al prevedibile e oramai noto affiorano sorprese inaspettate. Il finale non si tinge di dark, ma in alcuni tratti è caratterizzato da toni distesi, dialoghi divertenti, personaggi quasi caricaturali senza i muscoli di Avengers: Infinity War, ma tanto cervello e costruzione dell'azione come del pensiero.", "Azione,Fantascienza,Avventura", "Anthony Russo,Joe Russo", "Robert Downey Jr.,Chris Evans,Scarlett Johansson,Chris Hemsworth,Mark Ruffalo,Jeremy Renner");
INSERT INTO Movie (id, title, duration, rating, imageurl, trailerurl, description, genres, directors, cast)
    VALUES (5, "Skyfall", 143, 4, "skyfall.jpg", "https://www.youtube.com/watch?v=OnlSRBTG5Tw", "In seguito al fallimento di una missione recente, Il celebre agente segreto britannico è costretto ad essere testimone di una serie terribile di eventi: la MI6 deve trasferirsi al più presto mentre i dipendenti sotto copertura vedono le proprie identità venire rivelate. M è disperata e si rivolge a James Bond in cerca di un aiuto immediato.", "Drammatico,Spy,Action","Sam Mendes", "Daniel Craig,Judi Dench,Javier Bardem,Ben Whishaw");

INSERT INTO Room(id, rows, columns) VALUES (1, 7, 15);
INSERT INTO Room(id, rows, columns) VALUES (2, 8, 14);
INSERT INTO Room(id, rows, columns) VALUES (3, 5, 10);
INSERT INTO Room(id, rows, columns) VALUES (4, 6, 12);
INSERT INTO Room(id, rows, columns) VALUES (5, 7, 11);

INSERT INTO Projection(id, datetime, price, movie, room) VALUES (1, "2021-08-01 19:30:00", 10.5, 1, 1);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (2, "2021-08-01 22:30:00", 10.5, 1, 1);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (3, "2021-08-02 20:00:00", 10.5, 1, 2);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (4, "2021-08-02 22:20:00", 10.5, 1, 2);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (5, "2021-08-03 19:30:00", 10.5, 1, 3);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (6, "2021-08-03 21:15:00", 10.5, 1, 3);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (7, "2021-08-04 18:30:00", 8.5, 1, 5);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (8, "2021-08-04 21:45:00", 8.5, 1, 5);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (9, "2021-08-01 16:30:00", 10.5, 2, 2);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (10, "2021-08-01 19:00:00", 9.5, 3, 3);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (11, "2021-08-02 20:00:00", 9.5, 3, 1);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (12, "2021-08-02 22:20:00", 9.5, 3, 1);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (13, "2021-08-03 19:30:00", 7.5, 3, 1);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (14, "2021-08-03 21:15:00", 7.5, 3, 2);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (15, "2021-08-03 18:30:00", 8.5, 3, 2);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (16, "2021-08-04 21:45:00", 8.5, 3, 4);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (17, "2021-08-01 16:30:00", 10.5, 4, 4);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (18, "2021-08-01 19:00:00", 10.5, 4, 4);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (19, "2021-08-02 20:10:00", 11.5, 4, 4);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (20, "2021-08-03 21:15:00", 9.5, 4, 4);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (21, "2021-08-03 21:10:00", 10.5, 4, 3);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (22, "2021-08-01 16:30:00", 9.5, 5, 5);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (23, "2021-08-01 19:00:00", 9.5, 5, 5);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (24, "2021-08-02 20:00:00", 9.5, 5, 5);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (25, "2021-08-02 22:20:00", 9.5, 5, 5);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (26, "2021-08-03 19:30:00", 7.5, 5, 5);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (27, "2021-08-04 21:45:00", 8.5, 5, 1);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (28, "2021-08-02 20:00:00", 9.5, 2, 3);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (29, "2021-08-02 22:20:00", 9.5, 2, 3);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (30, "2021-08-02 16:45:00", 10.5, 2, 3);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (31, "2021-08-03 21:15:00", 10.5, 2, 1);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (32, "2021-08-04 21:45:00", 8.5, 2, 2);
INSERT INTO Projection(id, datetime, price, movie, room) VALUES (33, "2021-08-01 16:30:00", 9.5, 3, 3);

INSERT INTO Coupon (promocode, amount) VALUES ("SCONTO10", 10.0);
INSERT INTO Coupon (promocode, amount) VALUES ("PLUTO123", 4.0);
INSERT INTO Coupon (promocode, amount) VALUES ("PAPERINO123", 7.0);
INSERT INTO Coupon (promocode, amount) VALUES ("SCONTO50", 50.0);

INSERT INTO Discount (type, percentage, minage, maxage) VALUES ("AGE", 0.15, 5, 80);
INSERT INTO Discount (type, percentage, date)           VALUES ("DAY", 0.2, "2021-08-01");
INSERT INTO Discount (type, percentage, numberpeople)   VALUES ("NUMBER", 0.15, 5);

INSERT INTO Reservation(id, date, projection, name, surname, email, paymentcardowner, paymentcard) VALUES (1, "2021-07-01", 1, "Fake", "Person", "fakeemail@fake.fa", "Fake Person", "FAKECREDITCARD");

INSERT INTO OccupiedSeat (projection, row, column, reservation) VALUES (1, 1, 1, 1);
INSERT INTO OccupiedSeat (projection, row, column, reservation) VALUES (1, 1, 2, 1);
INSERT INTO OccupiedSeat (projection, row, column, reservation) VALUES (1, 1, 3, 1);
INSERT INTO OccupiedSeat (projection, row, column, reservation) VALUES (1, 1, 4, 1);

INSERT INTO Cinema (id, name, city, country, zipCode, address, email, mailPassword, adminPassword, logoURL, discountstrategy)
    VALUES (1, "Cinema Armadillo", "Pavia (PV)", "Italia", "27100", "Via A. Ferrata, 5", "cinemaarmadillo@gmail.com", "CinemaArmadillo@1999", "admin", "https://cdn1.iconfinder.com/data/icons/luchesa-2/128/Movie-512.png", "AGE");
