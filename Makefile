#   Copyright 2020-2020 Equinix, Inc
#   Copyright 2014-2020 The Billing Project, LLC
#  
#   The Billing Project licenses this file to you under the Apache License, version 2.0
#   (the "License"); you may not use this file except in compliance with the
#   License.  You may obtain a copy of the License at:
#  
#      http://www.apache.org/licenses/LICENSE-2.0
#  
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
#   WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
#   License for the specific language governing permissions and limitations
#   under the License.

# Variables
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
