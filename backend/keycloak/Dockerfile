FROM quay.io/keycloak/keycloak:26.7.3

# Render's entry-level instances have limited memory. Keep the JVM heap below
# that limit so native memory, caches, and database connections have headroom.
ENV JAVA_OPTS_KC_HEAP="-XX:InitialRAMPercentage=20 -XX:MaxRAMPercentage=55"

# Build the production Keycloak image during Docker build rather than on every
# container start. This substantially reduces startup time and its memory peak.
ENV KC_DB=postgres
RUN /opt/keycloak/bin/kc.sh build

COPY keycloak/import/commerce-realm.json /opt/keycloak/data/import/commerce-realm.json
ENTRYPOINT ["/bin/sh", "-c", "/opt/keycloak/bin/kc.sh start --optimized --http-enabled=true --http-port=${PORT:-8080} --proxy-headers=xforwarded --hostname-strict=false --import-realm"]
