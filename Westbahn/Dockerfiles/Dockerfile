FROM mysql:latest
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATA_DIR=/var/lib/mysql \
MYSQL_RUN_DIR=/run/mysqld \
MYSQL_LOG_DIR=/var/log/mysql
ADD ["db_dump.sql", "/tmp/dump.sql"]
COPY ./db_dump.sql /docker-entrypoint-initdb.d/
RUN /etc/init.d/mysql start && mysql -u root -p$\{MYSQL_ROOT_PASSWORD\} < /tmp/dump.sql
EXPOSE 3306
