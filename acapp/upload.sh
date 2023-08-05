scp dist/js/*.js spring:kob/acapp/
scp dist/css/*.css spring:kob/acapp/

ssh spring 'cd kob/acapp && ./rename.sh'
