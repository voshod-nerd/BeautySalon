insert into users (name,username,email,password,active,role) values ('Пользователь1','user','user@mail.ru','$2a$10$zGr1pgTv8wofH4U8Y10soOVSAO1eFrii9k6fffStSt3iZpOGltKde',true,'ROLE_USER');
insert into users (name,username,email,password,active,role) values ('Админ','admin','admin@mail.ru','$2a$10$zGr1pgTv8wofH4U8Y10soOVSAO1eFrii9k6fffStSt3iZpOGltKde',true,'ROLE_ADMIN');
insert into users (name,username,email,password,active,role) values ('Ниточкина Ирина','master1','master1@mail.ru','$2a$10$zGr1pgTv8wofH4U8Y10soOVSAO1eFrii9k6fffStSt3iZpOGltKde',true,'ROLE_MASTER');
insert into users (name,username,email,password,active,role) values ('Ниточкина Алена','master2','master2@mail.ru','$2a$10$zGr1pgTv8wofH4U8Y10soOVSAO1eFrii9k6fffStSt3iZpOGltKde',true,'ROLE_MASTER');
insert into service_item (duration_in_minute,name,price,active) values (45,'Депиляция',800,true);
insert into service_item (duration_in_minute,name,price,active) values (60,'Стрижка женская',1500,true);
insert into service_item (duration_in_minute,name,price,active) values (45,'Манюкур',800,true);
insert into discount (start_date,end_date,type,name,value) values ('2021-01-01','2021-12-31','PROMOCOD','BRONZE',3);
insert into discount (start_date,end_date,type,name,value) values ('2021-01-01','2021-12-31','PROMOCOD','SILVER',5);
insert into discount (start_date,end_date,type,name,value) values ('2021-01-01','2021-12-31','PROMOCOD','GOLD',7);
insert into material (name,unit_measure,value) values ('Гель лак','мл',500);
insert into material (name,unit_measure,value) values ('Гель основа','мл',1000);
insert into material (name,unit_measure,value) values ('Ватные палочки','шт',500);
insert into material (name,unit_measure,value) values ('Пакеты для стерилизации','шт',100);
insert into consume_material(service_item_id,material_id,quantity) values (1,1,4);
insert into consume_material(service_item_id,material_id,quantity) values (1,2,6);
insert into consume_material(service_item_id,material_id,quantity) values (2,1,5);
insert into consume_material(service_item_id,material_id,quantity) values (2,2,7);
insert into skills (master_id,skill_id) values (3,1);
insert into skills (master_id,skill_id) values (3,2);
insert into skills (master_id,skill_id) values (4,1);
insert into cashier (id,total_sum) values ('81c2ca82-a086-11eb-bcbc-0242ac130002',0);


