DROP TABLE IF EXISTS Film;
DROP TABLE IF EXISTS Projection;
DROP TABLE IF EXISTS Room;
DROP TABLE IF EXISTS Discount;
DROP TABLE IF EXISTS Coupon;
DROP TABLE IF EXISTS Reservation;


CREATE TABLE Film(
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


CREATE TABLE Projection(
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    datetime INTEGER NOT NULL,
    price REAL NOT NULL CHECK(price >= 0),
    film INTEGER,
    room INTEGER,
    FOREIGN KEY(film) REFERENCES Film(id) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY(room) REFERENCES Room(id) ON UPDATE CASCADE ON DELETE SET NULL
);


CREATE TABLE Room(
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    rows INTEGER NOT NULL CHECK(rows > 0),
    columns INTEGER NOT NULL CHECK(columns > 0)
);


CREATE TABLE Coupon(
	promocode TEXT PRIMARY KEY NOT NULL,
	percentage REAL NOT NULL CHECK(percentage >= 0 AND percentage <= 1),
	used INTEGER NOT NULL DEFAULT(0) CHECK(used == 1 OR used == 0)
);


CREATE TABLE Discount(
	code TEXT PRIMARY KEY NOT NULL,
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
	date INTEGER NOT NULL,
	projection INTEGER,
	name TEXT NOT NULL,
	surname TEXT NOT NULL,
	email TEXT NOT NULL,
	paymentcardowner TEXT NOT NULL,
	paymentcard TEXT NOT NULL,
	coupon TEXT,
	discount TEXT,
	numberpeopleunderage INTEGER DEFAULT(0) CHECK(numberpeopleunderage >= 0),
	numberpeopleoverage INTEGER DEFAULT(0) CHECK(numberpeopleoverage >= 0),

	FOREIGN KEY(projection) REFERENCES Projection(id) ON UPDATE CASCADE ON DELETE SET NULL,
	FOREIGN KEY(coupon) REFERENCES Coupon(promocode) ON UPDATE CASCADE ON DELETE SET NULL,
	FOREIGN KEY(discount) REFERENCES Discount(code) ON UPDATE CASCADE ON DELETE SET NULL
);