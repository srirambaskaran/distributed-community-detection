--Create GraphDB
CREATE DATABASE GraphDB;

USE GraphDB;

-- Create tables
CREATE TABLE Community (
    community_id INT,
    community_weight INT,
    community_degree INT,
    PRIMARY KEY (community_id)
);

CREATE TABLE Node (
    vertex_id INT,
    community_id INT,
    PRIMARY KEY (vertex_id),
    FOREIGN KEY (community_id)
        REFERENCES Community(community_id)
);

-- Create custom databases for each graph,
-- change the database name and create similar tables.
