## mysql -u root -pwelcome@123 instagram;
## Use indexed table for these operations. Since on normal table it searches all rows for match.

start transaction;

select * from followers where user='anurag' for update;

# Run first screen commands now

select * from followers where user='Monu' for update;

## Here you will see the deadlock.