# 🧁 Bakers.in — Multi-Category Food E-Commerce (Spring Boot + Maven)

A full-stack, colourful food e-commerce web app: cakes, pastries & desserts, pizza, burgers,
mocktails, and cookies & snacks — each with product images, price tags with sale badges, a
cart, and checkout — built with Java, Spring Boot, Thymeleaf, and Maven, ready to containerize
and deploy to any cloud.

## Tech stack

- **Java 21**, **Spring Boot 3.3** (Web MVC, Spring Data JPA, Validation, Thymeleaf)
- **H2** in-memory database for local development (auto-seeded with sample data)
- **PostgreSQL** for production (swap in with three environment variables — no code changes)
- Plain HTML/CSS/JS front end rendered server-side via Thymeleaf (no separate JS build step)
- **Maven** build, packaged as a single runnable JAR

## Project structure

```
bakers-ecommerce/
├── pom.xml
├── Dockerfile
├── Procfile                     # for Heroku/Railway-style buildpacks
├── render.yaml                  # Render.com blueprint
└── src/main/
    ├── java/com/bakersin/
    │   ├── BakersInApplication.java
    │   ├── config/DataLoader.java        # seeds categories & products on startup
    │   ├── model/                        # Category, Product, Cart, CartItem, Order, OrderItem, OrderStatus
    │   ├── repository/                   # Spring Data JPA repositories
    │   ├── service/                      # ProductService, CartService, OrderService
    │   └── controller/                   # Home, Product, Cart, Checkout controllers + GlobalModelAdvice
    └── resources/
        ├── application.properties        # dev profile (H2)
        ├── application-prod.properties   # prod profile (PostgreSQL, via env vars)
        ├── templates/                    # Thymeleaf pages (index, products, product-detail, cart, checkout, order-confirmation, about)
        └── static/css, static/js
```

## Features

- **6 categories** (Cakes, Pastries & Desserts, Pizza, Burgers, Mocktails, Cookies & Snacks)
  with **36 products**, each with an image, price, unit (per kg / per piece / per glass / per
  box), star rating, stock count, veg/non-veg indicator, and optional sale discount.
- Category browsing, full menu view, and keyword search.
- Colourful, distinctive "bakery box" visual identity — scalloped product-card edges, a
  rounded Fredoka display face, and per-category accent colours — not a generic template.
- Product detail pages with quantity selector and related products.
- Session-based shopping cart (survives across pages, no login required) with quantity
  update/remove, free-delivery threshold (₹499), and running total.
- Checkout form with server-side-required fields (name, email, phone, address, city, PIN) and
  payment method selection (Cash on Delivery / UPI / Card — captured, not actually processed).
- Order confirmation page with a generated order number, persisted to the database.

> **Note on images:** products currently use auto-generated colour-coded placeholder images
> (via placehold.co, labelled with each product's name) so the app runs immediately with zero
> setup and no licensing risk. Swap the `imageUrl` values — generated in
> `DataLoader.java`'s `img()` helper — for real product photography (your own photos, hosted on
> S3/Cloudinary/your CDN) before going live.

## Run locally

Requires JDK 21+ and Maven.

```bash
mvn clean package
java -jar target/bakers-ecommerce.jar
```

Visit **http://localhost:8080**. The H2 database is in-memory and reseeds every restart —
no setup needed. (H2 console, if you want to inspect data: http://localhost:8080/h2-console,
JDBC URL `jdbc:h2:mem:bakersin`, user `sa`, blank password.)

## Deploy to the cloud

The app is 12-factor style: it reads its port from `PORT` and its database from
`SPRING_DATASOURCE_URL` / `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD`,
activated via the `prod` Spring profile. Pick whichever platform you prefer:

### Option A — Render.com (matches the included `render.yaml`)

1. Push this project to a GitHub repo.
2. On Render: **New → Blueprint**, point it at the repo (`render.yaml` is already included).
3. Create a free PostgreSQL instance on Render, then in the web service's **Environment** tab
   set `SPRING_DATASOURCE_URL` (convert Render's `postgres://user:pass@host:5432/db` to
   `jdbc:postgresql://host:5432/db`), `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`.

### Option B — Docker (works on any cloud: Railway, Fly.io, AWS App Runner, Azure, GCP Cloud Run)

```bash
docker build -t bakers-ecommerce .
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/<db> \
  -e SPRING_DATASOURCE_USERNAME=<user> \
  -e SPRING_DATASOURCE_PASSWORD=<password> \
  bakers-ecommerce
```

Push the same image to any container registry (Docker Hub, ECR, GCR, ACR) and point your
cloud platform's container service at it. **Do not** try to serve this app from S3 static
website hosting — it's a server-rendered Java app, not a static site, so S3 alone cannot run it.

### Option C — Railway / Heroku (buildpack, no Docker needed)

Both detect the `pom.xml` and `Procfile` automatically:

1. Push to GitHub, create a new app, connect the repo.
2. Add a PostgreSQL plugin/add-on.
3. Set `SPRING_PROFILES_ACTIVE=prod` and the three `SPRING_DATASOURCE_*` variables from the
   database credentials the platform gives you.
4. Deploy — the `Procfile` handles the start command.

### Option D — AWS Elastic Beanstalk / EC2 / a traditional VM

```bash
mvn clean package
scp target/bakers-ecommerce.jar user@your-server:/opt/bakersin/
ssh user@your-server
cd /opt/bakersin
SPRING_PROFILES_ACTIVE=prod \
SPRING_DATASOURCE_URL=jdbc:postgresql://<rds-endpoint>:5432/bakersin \
SPRING_DATASOURCE_USERNAME=<user> \
SPRING_DATASOURCE_PASSWORD=<password> \
java -jar bakers-ecommerce.jar
```

Run it behind `systemd` for restarts, and put it behind Nginx / an ALB for TLS termination.
For **AWS specifically with containers**, use App Runner or ECS/Fargate (push the Docker image
to ECR first) rather than S3.

## Before going to production

- [ ] Replace placeholder images with real product photography.
- [ ] Point `SPRING_DATASOURCE_URL` at a managed PostgreSQL instance (Render, RDS, Supabase, Neon, etc.).
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate` once you've reviewed the generated schema, and
      manage schema changes with Flyway/Liquibase instead of `update` for a real production app.
- [ ] Wire the payment method selector to an actual payment gateway (Razorpay/Stripe) if you
      want to process real payments — currently it's captured as order metadata only.
- [ ] Add HTTPS (most platforms above provide this automatically) and set secure cookie flags.
- [ ] Add an admin view or import script for managing products/categories instead of editing `DataLoader.java`.
- [ ] Register a real domain and point it at your deployment if you want it live at bakers.in
      (or your domain of choice).

## A note on this build

This project could not be compiled inside the sandbox that generated it, because that
environment has no outbound network access to reach Maven Central. Every file was written and
manually reviewed for correctness (imports, JPA/Thymeleaf/SpEL expressions, brace balance),
but please run `mvn clean package` as your first step locally or in CI to confirm a clean build
before deploying, and open an issue/ask if anything doesn't compile.
