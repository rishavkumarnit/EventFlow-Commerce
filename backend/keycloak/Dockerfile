FROM quay.io/keycloak/keycloak:26.7.3
COPY keycloak/import/commerce-realm.json /opt/keycloak/data/import/commerce-realm.json
ENTRYPOINT ["/bin/sh", "-c", "/opt/keycloak/bin/kc.sh start --http-enabled=true --http-port=${PORT:-8080} --proxy-headers=xforwarded --hostname-strict=false --import-realm"]
