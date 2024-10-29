# killbill-bigcommerce-plugin

### Build plugin with Maven or download from release section

```console
mvn clean install
```

### Install plugin inside docker container

1. Add volumen to kill bill service in compose file
```console
...
  killbill:
    container_name: killbill-container
    image: killbill/killbill:0.24.10
    volumes:
      - kb-var:/var/
    ....

volumes:
  kb-var:
  ...
```

2. Copy jar file to docker container

```console
docker cp target/bigcommerce-plugin-1.0.55-SNAPSHOT.jar killbill-container:/var/lib/killbill
```

3. Install plugin 

```console
docker exec killbill-container bash -c " \
		cd /var/lib/killbill && \
		kpm install_java_plugin 'dev:bigcommerce' --from-source-file=./bigcommerce-plugin-1.0.55-SNAPSHOT.jar--destination=/var/lib/killbill/bundles"
```



4. Restart killbill container
```console
docker restart killbill-container
```


### Configure plugin 

1. Create table in killbill database with the following script
[ddl.sql](src/main/resources/ddl.sql)

2. Set plugin configuration

```console
curl --request GET \
  --url 'http://localhost:8080/plugins/bigcommerce-plugin?url=api_url' \
  --header 'X-Killbill-ApiKey: Bob' \
  --header 'X-Killbill-ApiSecret: Lazar'
```