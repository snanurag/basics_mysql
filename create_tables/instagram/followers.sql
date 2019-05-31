create database if not exists instagram;
use instagram;
drop table if exists followers;

create table followers
    (
        user varchar(20),
        follower varchar(20),
        index(user)
    );
insert into followers (user,follower) values('Monu', 'bill');
insert into followers (user,follower) values('anurag', 'jobs');
insert into followers (user,follower) values('anurag', 'mark');