insert into users(username, password, enabled) values ('akraxx', '123456', 1);
insert into users(username, password, enabled) values ('robin', '123456', 1);
insert into users(username, password, enabled) values ('maxime', '123456', 1);
insert into users(username, password, enabled) values ('amaury', '123456', 1);

insert into user_roles(username, ROLE) values ('akraxx', 'ROLE_ADMIN');
insert into user_roles(username, ROLE) values ('akraxx', 'ROLE_USER');
insert into user_roles(username, ROLE) values ('robin', 'ROLE_USER');
insert into user_roles(username, ROLE) values ('maxime', 'ROLE_USER');
insert into user_roles(username, ROLE) values ('amaury', 'ROLE_USER');

insert into journeys(user_id) values('akraxx');
insert into journeys(user_id) values('akraxx');
insert into journeys(user_id) values('akraxx');

insert into stopoff_point(latitude, longitude, address, description, type, date) values (3.04, 50.6, '36 Rue Paul Doumer, 59120 Loos, France', 'Pas de bagages', 'DEPARTURE', '2015-03-07 14:15:00');
insert into stopoff_point(latitude, longitude, address, description, type, date) values (3.0452, 50.6, '125 Rue du Capitaine Michel, 59120 Loos, France', '', 'ARRIVAL', '2015-03-07 14:30:00');

insert into stopoff_point(latitude, longitude, address, description, type, date) values (3.1620699999999715, 50.724993, 'Tourcoing, France', 'Simple description du point de rendez vous', 'DEPARTURE', '2015-05-04 15:15:00');
insert into stopoff_point(latitude, longitude, address, description, type, date) values (3.151798099999951, 50.6656741, '52 Rue Marechal de Lattre de Tassigny, Croix, France', 'Simple description du point de rendez vous à Croix', 'ARRIVAL', '2015-05-04 15:30:00');

insert into stopoff_point(latitude, longitude, address, description, type, date) values (2.307314099999985, 48.8657844, 'Paris, France', 'Depart de roissy', 'DEPARTURE', '2015-05-05 08:00:00');
insert into stopoff_point(latitude, longitude, address, description, type, date) values (-3.7037901999999576, 40.4167754, 'Madrid, Espagne', 'Arrivée à bernabeu', 'ARRIVAL', '2015-05-06 00:00:00');

insert into stopoff(journey_id, departure_point_id, arrival_point_id, distance, available_seats, price, status) values (0, 0, 1, 6520, 4, 3, 'ACTIVATED');
insert into stopoff(journey_id, departure_point_id, arrival_point_id, distance, available_seats, price, status) values (1, 2, 3, 9222, 2, 5, 'ACTIVATED');
insert into stopoff(journey_id, departure_point_id, arrival_point_id, distance, available_seats, price, status) values (2, 4, 5, 1270879, 2, 100, 'ACTIVATED');

insert into stopoff_reservations (username, stopoff_id, payed, status, seats) VALUES('robin', 0, false, 'WAITING', 1);
insert into stopoff_reservations (username, stopoff_id, payed, status, seats) VALUES('amaury', 0, false, 'VALIDATED', 3);

insert into stopoff_reservations (username, stopoff_id, payed, status, seats) VALUES('robin', 2, false, 'WAITING', 2);
insert into stopoff_reservations (username, stopoff_id, payed, status, seats) VALUES('robin', 1, false, 'WAITING', 2);

