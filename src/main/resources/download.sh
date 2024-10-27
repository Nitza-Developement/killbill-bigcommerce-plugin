# Download the required jars

MVN_DOWNLOAD="mvn org.apache.maven.plugins:maven-dependency-plugin:3.1.2:get -DremoteRepositories=https://repo.maven.apache.org/maven2"
JOOQ_VERSION=3.14.12
REACTIVE_STREAM_VERSION=1.0.3
MYSQL_VERSION=8.0.30
JAXB_API_VERSION=2.3.1
JAXB_IMPL_VERSION=2.3.1

$MVN_DOWNLOAD -Dartifact=org.jooq:jooq:$JOOQ_VERSION:jar
$MVN_DOWNLOAD -Dartifact=org.jooq:jooq-meta:$JOOQ_VERSION:jar
$MVN_DOWNLOAD -Dartifact=org.jooq:jooq-codegen:$JOOQ_VERSION:jar
$MVN_DOWNLOAD -Dartifact=org.reactivestreams:reactive-streams:$REACTIVE_STREAM_VERSION:jar
$MVN_DOWNLOAD -Dartifact=mysql:mysql-connector-java:$MYSQL_VERSION:jar
$MVN_DOWNLOAD -Dartifact=javax.xml.bind:jaxb-api:$JAXB_API_VERSION:jar
$MVN_DOWNLOAD -Dartifact=org.glassfish.jaxb:jaxb-runtime:$JAXB_IMPL_VERSION:jar

M2_REPOS=~/.m2/repository
JOOQ="$M2_REPOS/org/jooq"

MYSQL="$M2_REPOS/mysql/mysql-connector-java/$MYSQL_VERSION/mysql-connector-java-$MYSQL_VERSION.jar"
REACTIVE_STREAMS="$M2_REPOS/org/reactivestreams/reactive-streams/$REACTIVE_STREAM_VERSION/reactive-streams-$REACTIVE_STREAM_VERSION.jar"
JAXB_API="$M2_REPOS/javax/xml/bind/jaxb-api/$JAXB_API_VERSION/jaxb-api-$JAXB_API_VERSION.jar"
JAXB_IMPL="$M2_REPOS/org/glassfish/jaxb/jaxb-runtime/$JAXB_IMPL_VERSION/jaxb-runtime-$JAXB_IMPL_VERSION.jar"

JARS="$JOOQ/jooq/$JOOQ_VERSION/jooq-$JOOQ_VERSION.jar:$JOOQ/jooq-meta/$JOOQ_VERSION/jooq-meta-$JOOQ_VERSION.jar:$JOOQ/jooq-codegen/$JOOQ_VERSION/jooq-codegen-$JOOQ_VERSION.jar:$REACTIVE_STREAMS:$JAXB_API:$JAXB_IMPL:$MYSQL:."

# Run jOOQ's generation tool
java -cp $JARS org.jooq.codegen.GenerationTool ./src/main/resources/gen.xml
