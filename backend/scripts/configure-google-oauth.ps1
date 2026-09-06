<#!
.SYNOPSIS
Configures Google sign-in in the local Keycloak commerce realm from backend/.env.
#>

$ErrorActionPreference = 'Stop'
$envFile = Join-Path $PSScriptRoot '..\\.env'

if (Test-Path $envFile) {
    Get-Content $envFile | ForEach-Object {
        $line = $_.Trim()
        if ($line -and -not $line.StartsWith('#') -and $line.Contains('=')) {
            $name, $value = $line.Split('=', 2)
            if ($name -in @('GOOGLE_CLIENT_ID', 'GOOGLE_CLIENT_SECRET') -and -not [string]::IsNullOrWhiteSpace($value)) {
                Set-Item -Path "Env:$name" -Value $value.Trim()
            }
        }
    }
}

if ([string]::IsNullOrWhiteSpace($env:GOOGLE_CLIENT_ID) -or [string]::IsNullOrWhiteSpace($env:GOOGLE_CLIENT_SECRET)) {
    throw 'Create backend/.env from .env.example, then fill in GOOGLE_CLIENT_ID and GOOGLE_CLIENT_SECRET.'
}

$keycloakUrl = 'http://127.0.0.1:8081'
$token = Invoke-RestMethod -Method Post -Uri "$keycloakUrl/realms/master/protocol/openid-connect/token" -ContentType 'application/x-www-form-urlencoded' -Body 'client_id=admin-cli&username=admin&password=admin&grant_type=password'
$headers = @{ Authorization = "Bearer $($token.access_token)" }
$provider = @{
    alias = 'google'; displayName = 'Google'; providerId = 'google'; enabled = $true; trustEmail = $true
    firstBrokerLoginFlowAlias = 'first broker login'
    config = @{ clientId = $env:GOOGLE_CLIENT_ID; clientSecret = $env:GOOGLE_CLIENT_SECRET; defaultScope = 'openid profile email'; useJwksUrl = 'true' }
}
$providerJson = $provider | ConvertTo-Json -Depth 5
$providerUrl = "$keycloakUrl/admin/realms/commerce/identity-provider/instances/google"
try {
    Invoke-RestMethod -Method Get -Uri $providerUrl -Headers $headers | Out-Null
    Invoke-RestMethod -Method Put -Uri $providerUrl -Headers $headers -ContentType 'application/json' -Body $providerJson
} catch {
    if ($_.Exception.Response.StatusCode.value__ -ne 404) { throw }
    Invoke-RestMethod -Method Post -Uri "$keycloakUrl/admin/realms/commerce/identity-provider/instances" -Headers $headers -ContentType 'application/json' -Body $providerJson
}
Write-Host 'Google sign-in is enabled for the commerce realm.'
