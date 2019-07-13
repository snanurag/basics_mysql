## mysql -u root -pwelcome@123 instagram;
## Use indexed table for these operations. Since on normal table it searches all rows for match.
## Default transaction level is Repeatable Read which uses row level locking

start transaction;

update followers set follower='habermas' where user='Monu';

## Run second screen commands

update followers set follower='Rubino' where user='anurag';

## Run second screen commands now