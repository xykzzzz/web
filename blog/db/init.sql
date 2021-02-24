drop database if exists servlet_blog1;
create database servlet_blog1 character set utf8mb4;
use servlet_blog1;
create table user(
     id int primary key auto_increment,
     username varchar(20) not null unique comment '账号' ,
     password varchar (20) not null ,
     nickname varchar (20) ,
     sex bit,
     birthday date ,
     head varchar (50)
);
create table article(
  id int primary key auto_increment,
  title varchar (20) not null ,
  content mediumtext not null ,
  create_time timestamp default now(),
  view_count int default 0,
  user_id int,
  foreign key(user_id) references user(id)
);
insert into user(username,password) values ('a','1'),('b','2'),('c','3');

insert into article(title, content, user_id) values ('快速排序','public...',1);
insert into article(title, content, user_id) values ('冒泡排序','public...',1);
insert into article(title, content, user_id) values ('选择排序','public...',1);
insert into article(title, content, user_id) values ('归并排序','public...',2);
insert into article(title, content, user_id) values ('插入排序','public...',2);

select id, username, password, nickname, sex, birthday, head from user where username='a';


select id,title from article where user_id=1;