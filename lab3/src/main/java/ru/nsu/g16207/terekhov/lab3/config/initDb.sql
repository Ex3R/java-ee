DROP TABLE IF EXISTS nodes, tags;

/*CREATE EXTENSION cube;
CREATE EXTENSION earthdistance;*/

CREATE TABLE IF NOT EXISTS nodes
(
    node_id   serial PRIMARY KEY,
    id        INTEGER,
    version   INTEGER,
    timestamp date,
    uid       INTEGER,
    "user"    VARCHAR(100),
    changeset INTEGER,
    lat       double precision,
    lon       double precision


);

CREATE TABLE IF NOT EXISTS tags
(
    tag_id  serial PRIMARY KEY,
    key     VARCHAR(100),
    value   VARCHAR(400),
    node_id INTEGER not null references nodes (node_id)
);
