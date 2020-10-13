SCOUTER_COLLECTOR_ADDR=127.0.0.1 \
SCOUTER_COLLECTOR_PORT=6100 \
SCOUTER_SERVICE_MAPS_OJB_TYPE= \
STORAGE_TYPE=scouter \
java -Dloader.path='zipkin-storage-scouter.jar,zipkin-storage-scouter.jar!lib' -Dspring.profiles.active=scouter -cp zipkin.jar org.springframework.boot.loader.PropertiesLauncher
