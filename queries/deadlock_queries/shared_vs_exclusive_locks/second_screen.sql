## mysql -u root -pwelcome@123 test
# RUN THIS when instructed in first screen.alter

SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

START TRANSACTION;

UPDATE USER SET NAME='ANURAG' WHERE ID=1;