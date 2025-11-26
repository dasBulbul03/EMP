#!/bin/bash
# Simple smoke test used by the pipeline after deployment.
# Usage: ./scripts/smoke_test.sh [TARGET_URL]
# If TARGET_URL is not provided, the script will try to read the external IP
# from the service `ems-service` in namespace $NAMESPACE (defaults to "default").

set -euo pipefail
NAMESPACE=${NAMESPACE:-default}
SVC_NAME=ems-service
TARGET_URL=${1:-}

if [ -z "$TARGET_URL" ]; then
  echo "Attempting to discover external IP for service $SVC_NAME in namespace $NAMESPACE..."
  for i in {1..30}; do
    EXTERNAL_IP=$(kubectl get svc $SVC_NAME -n "$NAMESPACE" -o jsonpath='{.status.loadBalancer.ingress[0].ip}' 2>/dev/null || true)
    if [ -n "$EXTERNAL_IP" ]; then
      TARGET_URL="http://$EXTERNAL_IP"
      break
    fi
    echo "Waiting for external IP... ($i/30)"
    sleep 5
  done
  if [ -z "$TARGET_URL" ]; then
    echo "No external IP found. Trying NodePort or ClusterIP fallback..."
    # Try NodePort
    NODE_PORT=$(kubectl get svc $SVC_NAME -n "$NAMESPACE" -o jsonpath='{.spec.ports[0].nodePort}' 2>/dev/null || true)
    if [ -n "$NODE_PORT" ]; then
      # pick first node IP
      NODE_IP=$(kubectl get nodes -o jsonpath='{.items[0].status.addresses[?(@.type=="InternalIP")].address}' 2>/dev/null || true)
      if [ -n "$NODE_IP" ]; then
        TARGET_URL="http://$NODE_IP:$NODE_PORT"
      fi
    fi
  fi
fi

if [ -z "$TARGET_URL" ]; then
  echo "Unable to determine target URL for smoke test. Exiting with failure."
  exit 2
fi

echo "Running smoke test against $TARGET_URL"

# Try GET /api/employees up to 12 times (about 60s)
for i in {1..12}; do
  HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$TARGET_URL/api/employees" || true)
  if [ "$HTTP_STATUS" = "200" ]; then
    echo "Smoke test succeeded: /api/employees returned 200"
    exit 0
  fi
  echo "Attempt $i: returned $HTTP_STATUS. Retrying..."
  sleep 5
done

echo "Smoke test failed: /api/employees did not return 200 after retries"
exit 1
