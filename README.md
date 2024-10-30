# killbill-bigcommerce-plugin


### Installation with Kaui from github release section

1. Open kaui and got to [plugins section](http://127.0.0.1:9090/kpm/plugins)
2. Scroll down to the plugin upload section (internet connection required)
3. Use:
    * Plugin key: `dev:bigcommerce`
    * Version: `1.0.55-SNAPSHOT`
    * URI (copy url): [bigcommerce-plugin-1.0.55-SNAPSHOT.jar](https://github.com/Nitza-Developement/killbill-bigcommerce-plugin/releases/download/v1.0.55/bigcommerce-plugin-1.0.55-SNAPSHOT.jar)

4. Click on the `Upload` button
5. Go to [kpm section](http://127.0.0.1:9090/kpm/) and click in play icon
6. [Configure plugin](#configure-plugin)
 

### Build plugin with Maven

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