#set STORAGE_TYPE=scouter 
STORAGE_TYPE=scouter java -Dloader.path='zipkin-storage-scouter.jar' -Dspring.profiles.active=scouter -cp zipkin.jar org.springframework.boot.loader.PropertiesLauncher
#java -Dloader.path='zipkin-storage-scouter.jar' -Dspring.profiles.active=scouter -cp zipkin.jar org.springframework.boot.loader.PropertiesLauncher
