# baze2_projekat

I built a layered Java application that demonstrates a clean JDBC data‑access architecture over a relational schema (DDL/DML), with a service layer for transactional use cases and a thin UI layer for running operations and reports. The repo also includes database artifacts (conceptual/logical/relational model and data scripts) used to initialize and exercise the database.

The focus is on:
- clear separation of concerns (model → dao → service → ui),
- safe SQL access (prepared statements, typed mapping),
- explicit transactions where business flows span multiple tables,
- repeatable database bootstrapping (DDL/DML scripts).

---

## Tech stack

- Language: Java (Maven project)
- Build: Maven (`pom.xml`)
- Persistence: JDBC (plain) with a hand‑rolled DAO layer
- Database artifacts: `dml,dmd,rel/` (DDL/DML, model → relational mapping)
- Packaging/Run: Maven compile/package; entrypoint `src/Main.java`

I intentionally kept dependencies to a minimum to highlight core JDBC/SQL mechanics and service transactions.

---

## Repository layout

```
.
├─ pom.xml
├─ .gitignore
├─ README.md
├─ dml,dmd,rel/                # DDL/DML scripts and model-to-relational artifacts
└─ src/
   ├─ Main.java                # entry point (menu / demo driver)
   ├─ dao/                     # Data Access Objects (CRUD + queries)
   ├─ database/                # connection utilities (JDBC), bootstrapping helpers
   ├─ model/                   # domain entities (POJOs)
   ├─ service/                 # business logic + transactions
   └─ ui/                      # simple console UI / orchestrators
```

- `dml,dmd,rel`: holds schema definitions and/or seed data (DDL/DML), plus mapping artifacts (conceptual/logical/relational). I use these scripts to create tables, constraints, and optionally load sample data for demos/benchmarks.

---

## What I implemented

### 1) Model (src/model)
- Plain POJOs for the domain (e.g., entities mapped one‑to‑one with relational tables).
- Only data and minimal invariants; no DB logic here.
- Equals/hashCode/toString where useful for identity/equality in collections and debug output.

Typical entity shape:
```java
public class EntityX {
  private Long id;
  private String code;
  private String name;
  // getters/setters
}
```

### 2) Database utilities (src/database)
- Centralized JDBC connection acquisition (DriverManager/DataSource).
- A small helper for closing resources (ResultSet/Statement/Connection) safely.
- Optional bootstrap hooks that can run DDL/DML from `dml,dmd,rel` if needed (handy in dev).

Connection utility pattern:
```java
public final class Database {
  // read connection string/credentials (env, system properties, or static for demo)
  public static Connection getConnection() throws SQLException {
    // Example: return DriverManager.getConnection(DB_URL, USER, PASS);
    // Autocommit default true; services control tx boundaries.
  }
}
```

### 3) DAO layer (src/dao)
- One DAO per aggregate/table group (fine‑grained for clarity).
- CRUD with prepared statements and strict column mapping.
- Optional batch operations (e.g., mass inserts/updates) where it makes sense.
- Conversion methods (ResultSet → model) and parameter binders (model → PreparedStatement).

DAO pattern:
```java
public class EntityXDao {
  public Optional<EntityX> findById(Connection con, long id) throws SQLException {
    try (PreparedStatement ps = con.prepareStatement(
         "SELECT id, code, name FROM entity_x WHERE id = ?")) {
      ps.setLong(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return Optional.of(map(rs));
        return Optional.empty();
      }
    }
  }

  public long insert(Connection con, EntityX e) throws SQLException {
    try (PreparedStatement ps = con.prepareStatement(
         "INSERT INTO entity_x(code, name) VALUES(?, ?)",
         Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, e.getCode());
      ps.setString(2, e.getName());
      ps.executeUpdate();
      try (ResultSet keys = ps.getGeneratedKeys()) {
        if (keys.next()) return keys.getLong(1);
        throw new SQLException("No key generated");
      }
    }
  }

  private EntityX map(ResultSet rs) throws SQLException {
    EntityX e = new EntityX();
    e.setId(rs.getLong("id"));
    e.setCode(rs.getString("code"));
    e.setName(rs.getString("name"));
    return e;
  }
}
```

Design notes:
- I pass an existing `Connection` into DAO methods so the service layer controls transaction scope across multiple DAOs.
- I never string‑concatenate user inputs into SQL; everything uses bind parameters to avoid injection and leverage plan caching.
- When reading, I always name columns explicitly to avoid ambiguity and protect against future schema changes.

### 4) Service layer (src/service)
- Coordinates DAOs to implement business flows that may touch multiple tables.
- Opens a connection with `setAutoCommit(false)`, calls DAOs, then `commit()` or `rollback()` on failure.
- Encapsulates domain rules (pre‑checks, uniqueness constraints beyond DB, cross‑entity validations).
- Provides higher‑level methods used by the UI (e.g., “enroll”, “transfer”, “close period”, “generate report”).

Service pattern:
```java
public class EntityXService {
  private final EntityXDao xDao;
  private final RelatedYDao yDao;

  public void complexOperation(EntityX x, Long relatedYId) throws SQLException {
    try (Connection con = Database.getConnection()) {
      con.setAutoCommit(false);
      try {
        // validations
        Optional<RelatedY> y = yDao.findById(con, relatedYId);
        if (y.isEmpty()) throw new IllegalArgumentException("Related Y not found");

        // write operations
        long newId = xDao.insert(con, x);
        yDao.link(con, newId, relatedYId);

        con.commit();
      } catch (Exception ex) {
        con.rollback();
        throw ex;
      }
    }
  }
}
```

Transactions:
- I keep transactions as short as possible (only the statements that must be atomic).
- I ensure ordering avoids deadlocks (consistent table touch order).
- Checked exceptions are transparently propagated; UI catches them and prints user‑friendly messages.

### 5) UI (src/ui)
- A simple console‑driven runner (menus/prompts) that calls service methods.
- Parses user input, displays results in tables (aligned columns) or lists, and prints counts/summaries.
- Converts domain/technical errors into clear messages (e.g., constraint violations, not found).

Console scaffold:
```java
public class ConsoleUi {
  private final EntityXService service;

  public void start() {
    // menu loop
    // 1) List X  2) Create X  3) Update X  4) Delete X  0) Exit
    // call service accordingly; render results
  }
}
```

### 6) Main (src/Main.java)
- Entry point that wires services/DAOs and hands off to the UI.

Example outline:
```java
public class Main {
  public static void main(String[] args) {
    // construct DAOs
    EntityXDao xDao = new EntityXDao();
    RelatedYDao yDao = new RelatedYDao();

    // construct services
    EntityXService xService = new EntityXService(xDao, yDao);

    // UI
    new ConsoleUi(xService).start();
  }
}
```

---

## Database artifacts (dml,dmd,rel)

- DDL scripts: create tables, primary/foreign keys, unique constraints, and indexes (covering common filters/joins).
- DML scripts: seed data for demo scenarios (enough rows to test queries, joins, pagination).
- Model mapping: conceptual → logical → relational (naming conventions and normalization decisions are documented here).

Conventions I followed:
- snake_case for table/column names (or a consistent variant),
- surrogate numeric PKs (BIGINT/IDENTITY/SERIAL) unless a natural key is clearly superior,
- foreign keys named `fk_<child>_<parent>`,
- unique constraints for business keys where applicable (`uk_entity_x_code`),
- indexes to support frequent lookups (`idx_entity_x_code`, composite indexes for join/filter paths).

---

## Typical query patterns

- Listing with pagination:
```sql
SELECT id, code, name
FROM entity_x
ORDER BY id
OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;
```
- Searching by code or name (prefix/ILIKE depending on vendor):
```sql
SELECT id, code, name
FROM entity_x
WHERE code LIKE ? OR name LIKE ?
ORDER BY name;
```
- Join across entities:
```sql
SELECT x.id, x.name, y.title
FROM entity_x x
JOIN related_y y ON y.x_id = x.id
WHERE y.status = ?;
```

All these are invoked via prepared statements in DAOs.

---

## Build & run

Compile:
```bash
mvn clean compile
```

Package:
```bash
mvn clean package
```

Run (option A – direct):
```bash
java -cp target/classes Main
```

Run (option B – if exec plugin is configured):
```bash
mvn -q exec:java -Dexec.mainClass=Main
```

Database connection
- Configure the connection parameters in the `database/` utility (environment variables / system properties / constants, depending on how you wire it).
- Ensure the target database is up and the schema is applied (see `dml,dmd,rel`).

Apply schema (example):
```bash
# Example with psql or mysql; adapt to your RDBMS and script set
psql -d <db> -f dml,dmd,rel/schema.sql
psql -d <db> -f dml,dmd,rel/seed.sql
```

---

## Error handling & robustness

- All JDBC resource handling uses try‑with‑resources.
- I centralize error messages for common failure modes (not found, unique violation, FK violation).
- Service methods are atomic per business unit of work; partial changes are rolled back.

---

## Notes on performance

- Use of targeted indexes for frequent access paths.
- Batching for insert/update where high volume is expected.
- Simple pagination on read-heavy lists.
- Read methods favor `SELECT` column lists (no `SELECT *`) to minimize IO and mapping overhead.

---

## Why plain JDBC?

This is a Databases 2 project designed to practice:
- SQL schema design & normalization,
- writing and reasoning about SQL (joins, constraints, indexes),
- understanding transaction semantics and isolation,
- implementing DAOs cleanly without an ORM.

It keeps the mechanics transparent and under my control.

---

## Extensibility

- To add a new entity:
  1) extend the DDL/DML in `dml,dmd,rel/`,
  2) add a POJO in `model/`,
  3) create a DAO in `dao/`,
  4) expose operations via a service in `service/`,
  5) add menu actions in `ui/`.

- To add a new use case spanning multiple entities:
  - implement it in `service/` with proper transaction handling,
  - wire it in `Main` and a UI command.

---
