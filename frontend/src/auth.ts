import Keycloak from 'keycloak-js'

const keycloak = new Keycloak({
  url: import.meta.env.VITE_KEYCLOAK_URL ?? 'http://127.0.0.1:8081',
  realm: import.meta.env.VITE_KEYCLOAK_REALM ?? 'commerce',
  clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID ?? 'commerce-frontend'
})

// React Strict Mode deliberately mounts effects twice in development. Reuse the
// same initialization request so Keycloak is created exactly once.
let initialization: Promise<boolean> | undefined

export function initializeAuth() {
  initialization ??= keycloak.init({ onLoad: 'check-sso', pkceMethod: 'S256' })
  return initialization
}
export function signIn() { return keycloak.login() }
export function signUp() { return keycloak.register() }
export function signOut() { return keycloak.logout({ redirectUri: window.location.origin }) }
export async function authorizationHeader() {
  if (!keycloak.authenticated) throw new Error('Please sign in before calling the secured Order Service.')
  await keycloak.updateToken(30)
  return { Authorization: `Bearer ${keycloak.token}` }
}
