insert into users(username, password, enabled) values ('akraxx', '123456', 1);
insert into users(username, password, enabled) values ('robin', '123456', 1);

insert into user_roles(username, ROLE) values ('akraxx', 'ROLE_ADMIN');
insert into user_roles(username, ROLE) values ('akraxx', 'ROLE_USER');
insert into user_roles(username, ROLE) values ('robin', 'ROLE_USER');