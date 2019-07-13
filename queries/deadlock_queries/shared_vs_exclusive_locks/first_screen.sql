## mysql -u root -pwelcome@123 test
## You need to run these queries from different screens.

SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

START TRANSACTION;

SELECT * FROM USER;

# NOW RUN SECOND SCREEN TRANSACTION.


UPDATE USER SET NAME='ANURAG' WHERE ID=1;

# Observe deadlock on 1st second screen.
