## mysql -u root -pwelcome@123 instagram;
## Use indexed table for these operations. Since on normal table it searches all rows for match.

start transaction;

update followers set follower='Rubino' where user='anurag';

# Run first screen commands now

update followers set follower='habermas' where user='Monu';

## Here you will see the deadlock.