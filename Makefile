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

update:
	make build
	make copy
	make install
	make restart


build:
	mvn clean install -DskipTests


copy:
	docker cp target/stripe-plugin-8.0.3-SNAPSHOT.jar killbill-dev:/var/lib/killbill	


install:
	docker exec -it killbill-dev bash && \
	cd /var/lib/killbill && \
	kpm install_java_plugin 'dev:stripe' --from-source-file=./stripe-plugin-8.0.3-SNAPSHOT.jar --destination=/var/lib/killbill/bundles


restart:
	docker restart killbill-dev


