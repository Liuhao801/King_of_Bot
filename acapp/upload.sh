scp dist/js/*.js ts_d:kob/acapp/
scp dist/css/*.css ts_d:kob/acapp/

ssh ts_d 'cd kob/acapp && ./rename.sh'
