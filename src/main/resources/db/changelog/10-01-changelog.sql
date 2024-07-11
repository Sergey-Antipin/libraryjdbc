
CREATE TABLE IF NOT EXISTS books
(
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(255),
    author           VARCHAR(255),
    publication_year INTEGER
);

INSERT INTO books(id, title, author, publication_year)
values (100, 'Title 1', 'Author 1', 2002),
       (101, 'Title 2', 'Author 2', 2024);
