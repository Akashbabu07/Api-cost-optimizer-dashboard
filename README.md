# Dashboard Service

> **Aggregates data from Analytics into a single, cached summary endpoint — the data source that powers the frontend dashboard UI.**

---

## Overview

The Dashboard Service runs on port `8085` and is a lightweight read-through layer over the Analytics Service. It uses Feign to call Analytics, caches the results in Redis, and exposes clean summary endpoints the frontend can hit without needing to know about the underlying service topology. Redis caching means the dashboard stays snappy even under high traffic, and the circuit breaker ensures it degrades gracefully if Analytics is temporarily unavailable.

---

## Port

`8085`

---

## Responsibilities

- Aggregate and surface summary data from the Analytics Service via Feign
- Cache dashboard responses in Redis to minimize upstream load
- Expose a simplified API consumed directly by the frontend
- Circuit break gracefully if Analytics is unreachable

---

## API Endpoints

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| GET | `/dashboard/summary` | Yes (JWT via Gateway) | Full dashboard summary — total calls, cost, top APIs (Redis cached) |
| GET | `/dashboard/api-stats?apiId=xyz` | Yes (JWT via Gateway) | Detailed stats for a specific API (Redis cached) |

### Sample Summary Response

```json
{
  "totalApiCalls": 48320,
  "totalCostUsd": 214.75,
  "avgResponseTimeMs": 840,
  "topApis": [
    { "apiId": "openai-gpt4", "calls": 18000, "costUsd": 135.20 },
    { "apiId": "anthropic-claude", "calls": 12000, "costUsd": 48.00 }
  ]
}
```

---

## Dependencies

- **Analytics Service** — source of all data, called via Feign
- **Redis** — response caching

---

## Configuration

```yaml
server:
  port: 8085

analytics:
  service:
    url: ${ANALYTICS_SERVICE_URL:http://localhost:8083}

resilience4j:
  circuitbreaker:
    instances:
      analytics-service:
        slidingWindowSize: 10
        failureRateThreshold: 50%
        waitDurationInOpenState: 10s
```

---

## Environment Variables

| Variable | Description |
|----------|-------------|
| `ANALYTICS_SERVICE_URL` | URL of Analytics service (default: `http://localhost:8083`) |
| `REDIS_HOST` | Redis host |
| `REDIS_PORT` | Redis port |
| `REDIS_PASSWORD` | Redis password |

---

## Feign Client Timeouts

| Setting | Value |
|---------|-------|
| Connect timeout | 10,000ms |
| Read timeout | 15,000ms |

---

## Swagger UI

http://localhost:8085/swagger-ui.html

---

## Running

```bash
cd services/dashboard
./mvnw spring-boot:run
```
