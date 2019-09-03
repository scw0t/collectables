CREATE TABLE "artist" (
  "artist_id" bigint PRIMARY KEY,
  "mbid" varchar UNIQUE,
  "name" varchar NOT NULL,
  "sort_name" varchar NOT NULL,
  "disambiguation" varchar,
  "type" varchar,
  "gender" varchar,
  "begin_date" varchar,
  "end_date" varchar,
  "area" varchar,
  "image" bytea,
  "thumb_image" bytea,
  "created_at" timestamp,
  "modified_at" timestamp
);

CREATE TABLE "artist_members" (
  "artist_id" bigint,
  "member_id" bigint
);

CREATE TABLE "artist_release_group" (
  "artist_id" bigint,
  "release_group_id" bigint
);

CREATE TABLE "release_group" (
  "release_group_id" bigint PRIMARY KEY,
  "mbid" varchar UNIQUE,
  "name" varchar NOT NULL,
  "sort_name" varchar NOT NULL,
  "album_artist_title" varchar,
  "va" boolean,
  "primary_type" varchar,
  "secondary_type" varchar,
  "image" bytea,
  "thumb_image" bytea,
  "created_at" timestamp,
  "modified_at" timestamp
);

CREATE TABLE "release" (
  "release_id" bigint PRIMARY KEY,
  "release_group_id" bigint,
  "label_id" bigint,
  "mbid" varchar UNIQUE,
  "name" varchar NOT NULL,
  "sort_name" varchar NOT NULL,
  "barcode" varchar,
  "catalog_number" varchar,
  "year_recorded" varchar,
  "year_released" varchar,
  "country" varchar,
  "status" varchar,
  "quality" varchar,
  "language" varchar,
  "image" bytea,
  "thumb_image" bytea,
  "path" varchar,
  "created_at" timestamp,
  "modified_at" timestamp
);

CREATE TABLE "medium" (
  "medium_id" bigint PRIMARY KEY,
  "release_id" bigint,
  "name" varchar,
  "format" varchar,
  "created_at" timestamp,
  "modified_at" timestamp
);

CREATE TABLE "track" (
  "track_id" bigint PRIMARY KEY,
  "artist_id" bigint,
  "medium_id" bigint,
  "name" varchar NOT NULL,
  "position" int,
  "length" bigint,
  "path" varchar,
  "created_at" timestamp,
  "modified_at" timestamp
);

CREATE TABLE "catalogue" (
  "catalogue_id" bigint PRIMARY KEY,
  "release_id" bigint,
  "label_id" bigint,
  "cat_num" varchar
);

CREATE TABLE "track_artist_credit" (
  "track_id" bigint,
  "artist_id" bigint
);

CREATE TABLE "link" (
  "link_id" bigint PRIMARY KEY,
  "type" varchar,
  "url" varchar
);

CREATE TABLE "link_artist" (
  "link_id" bigint,
  "artist_id" bigint
);

CREATE TABLE "link_release" (
  "link_id" bigint,
  "release_id" bigint
);

CREATE TABLE "link_release_group" (
  "link_id" bigint,
  "release_group_id" bigint
);

CREATE TABLE "label" (
  "label_id" bigint PRIMARY KEY,
  "catalogue_id" bigint,
  "mbid" varchar UNIQUE,
  "name" varchar NOT NULL,
  "sort_name" varchar NOT NULL,
  "created_at" timestamp,
  "modified_at" timestamp
);

CREATE TABLE "genre" (
  "genre_id" bigint PRIMARY KEY,
  "name" varchar,
  "description" varchar
);

CREATE TABLE "genre_release_group" (
  "genre_id" bigint,
  "release_group_id" bigint
);

CREATE TABLE "genre_artist" (
  "genre_id" bigint,
  "artist_id" bigint
);

CREATE TABLE "release_group_secondary_types" (
  "release_group_id" bigint,
  "secondary_types" varchar
);

ALTER TABLE "artist_release_group" ADD FOREIGN KEY ("artist_id") REFERENCES "artist" ("artist_id");

ALTER TABLE "artist_release_group" ADD FOREIGN KEY ("artist_id") REFERENCES "artist" ("artist_id");

ALTER TABLE "artist_release_group" ADD FOREIGN KEY ("release_group_id") REFERENCES "release_group" ("release_group_id");

ALTER TABLE "release" ADD FOREIGN KEY ("release_group_id") REFERENCES "release_group" ("release_group_id");

ALTER TABLE "medium" ADD FOREIGN KEY ("release_id") REFERENCES "release" ("release_id");

ALTER TABLE "track" ADD FOREIGN KEY ("medium_id") REFERENCES "medium" ("medium_id");

ALTER TABLE "link_artist" ADD FOREIGN KEY ("artist_id") REFERENCES "artist" ("artist_id");

ALTER TABLE "link_artist" ADD FOREIGN KEY ("link_id") REFERENCES "link" ("link_id");

ALTER TABLE "link_release" ADD FOREIGN KEY ("release_id") REFERENCES "release" ("release_id");

ALTER TABLE "link_release" ADD FOREIGN KEY ("link_id") REFERENCES "link" ("link_id");

ALTER TABLE "link_release_group" ADD FOREIGN KEY ("release_group_id") REFERENCES "release_group" ("release_group_id");

ALTER TABLE "link_release_group" ADD FOREIGN KEY ("link_id") REFERENCES "link" ("link_id");

ALTER TABLE "genre_release_group" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("genre_id");

ALTER TABLE "genre_release_group" ADD FOREIGN KEY ("release_group_id") REFERENCES "release_group" ("release_group_id");

ALTER TABLE "genre_artist" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("genre_id");

ALTER TABLE "genre_artist" ADD FOREIGN KEY ("artist_id") REFERENCES "artist" ("artist_id");

ALTER TABLE "artist_members" ADD FOREIGN KEY ("artist_id") REFERENCES "artist" ("artist_id");

ALTER TABLE "artist_members" ADD FOREIGN KEY ("member_id") REFERENCES "artist" ("artist_id");

ALTER TABLE "track_artist_credit" ADD FOREIGN KEY ("artist_id") REFERENCES "artist" ("artist_id");

ALTER TABLE "track_artist_credit" ADD FOREIGN KEY ("track_id") REFERENCES "track" ("track_id");

ALTER TABLE "catalogue" ADD FOREIGN KEY ("release_id") REFERENCES "release" ("release_id");

ALTER TABLE "label" ADD FOREIGN KEY ("catalogue_id") REFERENCES "catalogue" ("catalogue_id");

ALTER TABLE "release_group_secondary_types" ADD FOREIGN KEY ("release_group_id") REFERENCES "release_group" ("release_group_id");