insert into users (id,name,username,email,password,active,role) values (1,'Пользователь1','user','user@mail.ru','1980',true,'ROLE_USER');
insert into users (id,name,username,email,password,active,role) values (2,'Админ','admin','admin@mail.ru','1980',true,'ROLE_ADMIN');
insert into users (id,name,username,email,password,active,role) values (3,'Ниточкина Ирина','master1','master1@mail.ru','1980',true,'ROLE_MASTER');
insert into users (id,name,username,email,password,active,role) values (4,'Ниточкина Алена','master2','master2@mail.ru','1980',true,'ROLE_MASTER');
insert into service_item (duration_in_minute,name,price,active) values (45,'Депиляция',800,true);
insert into service_item (duration_in_minute,name,price,active) values (60,'Стрижка женская',1500,true);
insert into service_item (duration_in_minute,name,price,active) values (45,'Манюкур',800,true);
insert into discount (start_date,end_date,type,name,value) values ('2021-01-01','2021-12-31','PROMOCOD','BRONZE',3);
insert into discount (start_date,end_date,type,name,value) values ('2021-01-01','2021-12-31','PROMOCOD','SILVER',5);
insert into discount (start_date,end_date,type,name,value) values ('2021-01-01','2021-12-31','PROMOCOD','GOLD',7);


