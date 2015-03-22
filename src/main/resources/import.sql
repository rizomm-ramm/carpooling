insert into users(username, password, enabled) values ('akraxx', '123456', 1);
insert into users(username, password, enabled) values ('robin', '123456', 1);

insert into user_roles(username, ROLE) values ('akraxx', 'ROLE_ADMIN');
insert into user_roles(username, ROLE) values ('akraxx', 'ROLE_USER');
insert into user_roles(username, ROLE) values ('robin', 'ROLE_USER');

insert into journeys(user_id, status) values('akraxx', 'ACTIVATED');

insert into stopoff_point(latitude, longitude, address, description, type) values (3.04, 50.6, '36 Rue Paul Doumer, 59120 Loos, France', 'Simple description du point de rendez vous', 'DEPARTURE');
insert into stopoff_point(latitude, longitude, address, description, type) values (3.0452, 50.6, '125 Rue du Capitaine Michel, 59120 Loos, France', 'Simple description du point de rendez vous à LOOS', 'ARRIVAL');


insert into stopoff(journey_id, departure_point_id, arrival_point_id, distance, available_seats, price) values (0, 0, 1, 6520, 4, 50);