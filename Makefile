PLUGIN_VERSION := $(shell xmllint --xpath '/*[local-name()="project"]/*[local-name()="version"]/text()' pom.xml)
PLUGIN_JAR=bigcommerce-plugin-$(PLUGIN_VERSION).jar
KILLBILL_DOCKER_CONTAINER=killbill-dev

update:
	make build
	make copy
	make install
	make restart

build:
	mvn clean install -DskipTests

copy:
	docker cp target/$(PLUGIN_JAR) $(KILLBILL_DOCKER_CONTAINER):/var/lib/killbill	


install:
	docker exec $(KILLBILL_DOCKER_CONTAINER) bash -c " \
		cd /var/lib/killbill && \
		kpm install_java_plugin 'dev:bigcommerce' --from-source-file=./$(PLUGIN_JAR) --destination=/var/lib/killbill/bundles"

restart:
	docker restart $(KILLBILL_DOCKER_CONTAINER)
	echo -e "\e[32mFinished installation of plugin\e[0m"



logs:
	docker exec $(KILLBILL_DOCKER_CONTAINER) tail -f logs/killbill.out
