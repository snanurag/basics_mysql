use test;
drop table if exists transaction;

create table transaction
    (
        id INT(7) NOT NULL AUTO_INCREMENT,
        CONSTRAINT id_pk PRIMARY KEY (id),
        name varchar(20),
        quantity INT
    );
insert into transaction (name,quantity) values('redmi 6A', 100000);
insert into transaction (name,quantity) values('samsung m10', 100000);