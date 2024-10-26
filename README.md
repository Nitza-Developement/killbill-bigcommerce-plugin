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
docker cp target/bigcommerce-plugin-x.x.x-SNAPSHOT.jar killbill-container:/var/lib/killbill
```

3. Install plugin 

```console
docker exec killbill-container bash -c " \
		cd /var/lib/killbill && \
		kpm install_java_plugin 'dev:bigcommerce' --from-source-file=./$(PLUGIN_JAR) --destination=/var/lib/killbill/bundles"
```



4. Restart killbill container
```console
docker restart killbill-container
```


### Configure plugin 
<!-- TODO -->