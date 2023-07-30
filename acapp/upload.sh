scp dist/js/*.js tserver_doc:kob/acapp/
scp dist/css/*.css tserver_doc:kob/acapp/

ssh tserver_doc 'cd kob/acapp && ./rename.sh'
