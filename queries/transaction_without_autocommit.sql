use test;

SET autocommit=0;
INSERT INTO USER VALUES ('John');

## Display the results from cache. But does not commit.
SELECT * FROM USER;
