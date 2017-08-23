create table user (
    id number(10),
    username varchar(100) not null,
    password varchar(100) not null,
    primary key(id)
);

create table role (
    id number(10) primary key,
    name varchar(100) not null
);

create table user_role (
    id number(10) primary key,
    user_id number not null,
    role_id number not null,
    constraint fk_userid FOREIGN KEY (user_id) references user(id),
    constraint fk_roleId FOREIGN KEY (role_id) references role(id)
);

create sequence order_seq start with 100000 increment by 1;

insert into user values(1, 'testuser', 'testuser');
insert into user values(2, 'testuser1', 'testuser1');

insert into role values(1, 'USER');

insert into user_role values(1, 1, 1);
insert into user_role values(2, 2, 1);