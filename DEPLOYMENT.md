# Deploy EventFlow Commerce to Render

This repository includes `render.yaml`, a Render Blueprint that creates the React site, API gateway, four private Spring services, Keycloak, and five isolated PostgreSQL databases.

## Before you begin

Create a Render account connected to the GitHub repository, plus a Confluent Cloud Kafka cluster. Keep all credentials in the provider dashboards; never add them to Git.

## Deploy the Blueprint

1. In Render, select **New → Blueprint** and choose this GitHub repository.
2. Render detects the root `render.yaml`. Review the services and click **Apply**.
3. During the initial setup, provide values for every field marked as a secret:
   - `KAFKA_BOOTSTRAP_SERVERS`
   - `KAFKA_SASL_JAAS_CONFIG`
   - `RAZORPAY_KEY_ID` and `RAZORPAY_KEY_SECRET`
   - `KC_BOOTSTRAP_ADMIN_USERNAME` and `KC_BOOTSTRAP_ADMIN_PASSWORD`
4. Wait until Keycloak, the four private services, the gateway, and the frontend are healthy. Open the public frontend URL from Render.

## Confluent Cloud Kafka values

For each service that asks for Kafka configuration, use the same values:

```text
KAFKA_BOOTSTRAP_SERVERS=<Confluent bootstrap server>
KAFKA_SECURITY_PROTOCOL=SASL_SSL
KAFKA_SASL_MECHANISM=PLAIN
KAFKA_SASL_JAAS_CONFIG=org.apache.kafka.common.security.plain.PlainLoginModule required username="<API_KEY>" password="<API_SECRET>";
```

## Finish Keycloak and Google login

After Keycloak receives its Render URL:

1. Open `<Keycloak Render URL>/admin` and sign in with the bootstrap administrator account.
2. In the `commerce` realm, open the `commerce-frontend` client.
3. Add the frontend Render URL to **Valid redirect URIs** as `https://your-frontend.onrender.com/*` and to **Web origins** as `https://your-frontend.onrender.com`.
4. In Google Cloud Console, change the Google OAuth authorised redirect URI to:

```text
https://your-keycloak.onrender.com/realms/commerce/broker/google/endpoint
```

5. Add the Google client ID and secret to Keycloak’s Google identity-provider configuration. Do not put the Google secret in `render.yaml`.

## Verify

1. Sign in from the live frontend with Google.
2. Create an order, wait for inventory reservation, and complete a Razorpay test payment.
3. Open `https://your-gateway.onrender.com/swagger-ui.html` to check API documentation.
4. Confirm the gateway health endpoint returns `UP` at `/actuator/health`.

## Notes

- The Blueprint intentionally deploys only the core product. Prometheus, Grafana, Loki, and Promtail remain part of the local monitoring stack. Use Grafana Cloud after the core application is live.
- The current gateway throttle is in-memory. Keep the gateway at one instance, or replace it with a shared Redis limiter before scaling out.
