use test;

#SET autocommit=0;

START TRANSACTION;
INSERT INTO USER VALUES ('John');

## Display the results from cache. But does not commit.
SELECT * FROM USER;


UPDATE USER SET name = 'anurag1' where name='anurag';

COMMIT;